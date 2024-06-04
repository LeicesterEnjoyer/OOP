package oop.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * CsvParser class parses a CSV file, storing its data in a list of LinkedHashMaps.
 * Each LinkedHashMap represents a row in the CSV file, with column names as keys and corresponding values.
 */
public class CsvParser {
    /**
     * List to store the data from the CSV file. Each element represents a row in the file.
     * The LinkedHashMap stores column names as keys and corresponding values.
     */
    public final ArrayList<LinkedHashMap<String, String>> fileData = new ArrayList<>();
    /**
     * Set to store the column names from the CSV file.
     */
    public final LinkedHashSet<String> columnNames = new LinkedHashSet<>();

    /**
     * Constructs a CsvParser object to parse a CSV file.
     * 
     * @param filePath   The path to the CSV file to parse.
     * @param separator  The delimiter used to separate columns in the CSV file.
     */
    public CsvParser(String filePath, String separator) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            
            if (lines.isEmpty())
                return; 
                
            String[] tokens = lines.get(0).split(separator);
            for (String token : tokens)
                columnNames.add(token);

            for (int i = 1; i < lines.size(); ++i) {
                String[] values = lines.get(i).split(separator);
                LinkedHashMap<String, String> row = new LinkedHashMap<>();

                for (int j = 0; j < Math.min(tokens.length, values.length); ++j)
                    row.put(tokens[j], values[j]);

                fileData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}