package oop.sitegenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.dataformat.*;
import oop.files.*;
import oop.dates.SummerHighQuery;
import oop.dates.WinterHighQuery;

/**
 * The class provides methods for generating websites based on templates and customer data.
 */
public class Generator {
    /**
     * Generates websites for multiple customers based on a template.
     *
     * @param template   The directory path of the template.
     * @param customers  An array of customers that provide directory paths of specific info.
     * @param output     The output directory path for the generated websites.
     */
    public static void generateSites(String template, String[] customers, String output) {
        for (String customer : customers) {
            String[] tokens = customer.split("/");
            generateSite(template, customer, output.isEmpty() ? output : output + "/" + tokens[tokens.length - 1]);
        }
    }

    /**
     * Generates a website for a specific customer based on a template.
     *
     * @param template   The directory path of the template.
     * @param customer   The directory path of a specific customer.
     * @param output     The output directory path for the generated website.
     */
    private static void generateSite(String template, String customer, String output) {
        final Map<String, String> wrappers = readValuesFromFile(template + "/wrappers.txt");
        final Map<String, String> common = readValuesFromFile(customer + "/common.txt");
        final CsvParser pricesCSV = new CsvParser(customer + "/prices.csv", ",");

        formatPrices(wrappers, common.get("high_season"), pricesCSV);
        final String prices = generatePriceTable(wrappers, pricesCSV);

        final String[] pages = common.get("menu_order").split(",");
        final Map<String, Map<String, String>> extractedInfo = new HashMap<>();
        final String menu = generateMenuAndExtractInfo(wrappers, customer, pages, extractedInfo);

        final SmartCopy smartCopy = new SmartCopy(new ArrayList<>(Arrays.asList("HTML")));
        try {
            smartCopy.copyDirectory(template, output.isEmpty() ? customer + "/website" : output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String page : pages) 
            generateHTML((output.isEmpty() ? customer + "/website" : output) + "/page.html", common, extractedInfo, prices, menu, output.isEmpty() ? customer + "/website" : output, page);
        generateHTML((output.isEmpty() ? customer + "/website" : output) + "/prices.html", common, extractedInfo, prices, menu, output.isEmpty() ? customer + "/website" : output, "prices.html");
        generateHTML((output.isEmpty() ? customer + "/website" : output) + "/css/style.css", common, extractedInfo, prices, menu, (output.isEmpty() ? customer + "/website" : output) + "/css", "style.css");

        removeUnnecessaryFile((output.isEmpty() ? customer + "/website" : output) + "/page.html");
        removeUnnecessaryFile((output.isEmpty() ? customer + "/website" : output) + "/wrappers.txt");
    }

    /**
     * Removes an unnecessary file.
     *
     * @param page The file path of the file to be removed.
     */
    private static void removeUnnecessaryFile(String page) {
        Path path = Paths.get(page);

        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println("Failed to delete the file: " + e.getMessage());
        }
    }

    /**
     * Generates an HTML file based on a template, common data, and additional information.
     *
     * @param input    The input file path.
     * @param common   Common data shared among pages.
     * @param other    Additional information specific to each page.
     * @param prices   Price information in html format.
     * @param menu     Menu data in html format.
     * @param output   The output directory path for the generated HTML file.
     * @param page     The page name.
     */
    public static void generateHTML(String input, Map<String, String> common, Map<String, Map<String, String>> other, String prices, String menu, String output, String page) {
        Pattern pattern = Pattern.compile("\\{([^\\s]+)\\}");
        
        output = output + "/" + (page.endsWith(".txt") ? page.replace(".txt", ".html") : page);
        if (input.equals(output))
        output += ".tmp";
        
        Path inputPath = Paths.get(input);
        Path outputPath = Paths.get(output);

        try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                
                while (matcher.find()) {
                    String placeholder = matcher.group(1);
                    
                    if (common.containsKey(placeholder))
                        line = line.replace("{" + placeholder + "}", common.get(placeholder));
                    else if (other.containsKey(page) && other.get(page).containsKey(placeholder)) 
                        line = line.replace("{" + placeholder + "}", other.get(page).get(placeholder));
                    else if (placeholder.equals("menu"))
                        line = line.replace("{" + placeholder + "}", menu);
                    else if (placeholder.equals("prices"))
                        line = line.replace("{" + placeholder + "}", prices);
                }

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (output.endsWith(".tmp"))
            try {
                Files.move(outputPath, inputPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Generates menu items and extracts information from text files for each page.
     *
     * @param wrappers  The map containing wrappers for HTML elements.
     * @param customer  The directory path of a specific customer.
     * @param pages     An array of page names.
     * @param info      A map to store extracted information for each page.
     * @return          A string containing generated menu items as html format.
     */
    private static String generateMenuAndExtractInfo(Map<String, String> wrappers, String customer, String[] pages, Map<String, Map<String, String>> info) {
        StringBuilder result = new StringBuilder();
        
        for (String page : pages) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(customer + "/" + page));
                
                if (lines.isEmpty())
                    continue;

                    
                String title = lines.get(0);
                StringBuilder textBuilder = new StringBuilder();
                for (int i = 1; i < lines.size(); i++)
                    textBuilder.append(lines.get(i)).append("\n");
                    
                result.append(String.format(wrappers.get("menu_item_start"), page.replace(".txt", ".html")));
                result.append(String.format(wrappers.get("menu_item_end"), title));
                
                Map<String, String> nestedMap = new HashMap<>();
                nestedMap.put("title", title);
                nestedMap.put("text", textBuilder.toString().trim());
                info.put(page, nestedMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    /**
     * Generates a price table based on wrapper elements and CSV data.
     *
     * @param wrappers      The map containing wrappers for HTML elements.
     * @param pricesCSV     The CSV parser object containing price data.
     * @return              A string representing the generated price table as html format.
     */
    private static String generatePriceTable(Map<String, String> wrappers, CsvParser pricesCSV) {
        StringBuilder table = new StringBuilder();

        pricesCSV.fileData.forEach(line -> {
            StringBuilder row = new StringBuilder();
            row.append(String.format(wrappers.get("table_cell"), line.get("Стая")));
            row.append(String.format(wrappers.get("table_cell"), line.get("Период")));
            row.append(String.format(wrappers.get("table_cell"), line.get("Цена")));
            
            table.append(String.format(wrappers.get("table_row"), row));
        });

        return String.format(wrappers.get("table"), table);
    }

    /**
     * Formats prices and periods based on high season status.
     *
     * @param wrappers   The map containing wrappers for HTML elements.
     * @param highSeason The high season indicator.
     * @param pricesCSV     The CSV parser object containing price data.
     */
    private static void formatPrices(Map<String, String> wrappers, String highSeason, CsvParser pricesCSV) {
        pricesCSV.fileData.forEach(line -> {
            formatPeriod(line, wrappers, highSeason, pricesCSV);
            formatPrice(line, wrappers, highSeason, pricesCSV);
        });
    }

    /**
     * Formats the period of stay based on high season status and updates the provided line accordingly.
     *
     * @param line        The line containing period information.
     * @param wrappers    The map containing wrappers for HTML elements.
     * @param highSeason  The high season indicator.
     * @param pricesCSV   The CSV parser object containing price data.
     */
    private static void formatPeriod(LinkedHashMap<String, String> line, Map<String, String> wrappers, String highSeason, CsvParser pricesCSV) {
        String period = line.get("Период");
        String[] dates = period.split("-");
        String formattedPeriod = formatPeriod(dates);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        TemporalAccessor temporalAccessor = formatter.parse(dates[0]);

        String queryResult;
        if (highSeason.equals("summer"))
            queryResult = (new SummerHighQuery().queryFrom(temporalAccessor)) ? wrappers.get("high_season") : wrappers.get("not_high_season");
        else
            queryResult = (new WinterHighQuery().queryFrom(temporalAccessor)) ? wrappers.get("high_season") : wrappers.get("not_high_season");

        line.put("Период", String.format(queryResult, formattedPeriod));
    }

    /**
     * Formats the price based on high season status and updates the provided line accordingly.
     *
     * @param line        The line containing price information.
     * @param wrappers    The map containing wrappers for HTML elements.
     * @param highSeason  The high season indicator.
     * @param pricesCSV   The CSV parser object containing price data.
     */
    private static void formatPrice(LinkedHashMap<String, String> line, Map<String, String> wrappers, String highSeason, CsvParser pricesCSV) {
        String price = line.get("Цена");
        line.put("Цена", NumberFormatter.formatMoney(Float.parseFloat(price)));
    }

    /**
     * Formats the period from an array of dates.
     *
     * @param dates  An array containing start and end dates of the period.
     * @return       A formatted string representing the period.
     */
    private static String formatPeriod(String[] dates) {
        StringBuilder result = new StringBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        result.append(DateFormatter.formatDate(LocalDate.parse(dates[0], formatter), DateFormats.MONTH_WITH_WORD) + "-");
        result.append(DateFormatter.formatDate(LocalDate.parse(dates[1], formatter), DateFormats.MONTH_WITH_WORD));

        return result.toString();
    }

    /**
     * Reads key-value pairs from a file and returns them as a map.
     *
     * @param filePath  The path to the file containing key-value pairs.
     * @return          A map containing the read key-value pairs.
     */
    private static Map<String, String> readValuesFromFile(String filePath) {
        Map<String, String> result = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            lines.forEach(line -> {
                String[] tokens = line.split("\\{", 2);
                
                if (tokens.length == 2) {
                    String key = tokens[0];
                    String value = tokens[1].substring(0, tokens[1].length() - 1);
                    result.put(key, value);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}