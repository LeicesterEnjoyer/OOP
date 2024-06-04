package oop.files;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for copying files from a source directory to a destination directory.
 */
public class SmartCopy {
    /** 
     * Hash map containing regular expression patterns for identifying file types based on their extensions.
     */
    private static final HashMap<String, Pattern> FILE_TYPES_PATTERNS = new HashMap<>();
    /** 
     * A list containing file types to be retrieved after copying. 
     */
    private final ArrayList<String> fileTypes = new ArrayList<String>();

    static {
        FILE_TYPES_PATTERNS.put("TXT", Pattern.compile("\\.txt$"));
        FILE_TYPES_PATTERNS.put("HTML", Pattern.compile("\\.html$"));
        FILE_TYPES_PATTERNS.put("CSS", Pattern.compile("\\.css$"));
        FILE_TYPES_PATTERNS.put("CSV", Pattern.compile("\\.csv$"));
    }

    /**
     * Constructs a SmartCopy object with the specified list of file types.
     * 
     * @param fileTypes     The list of file types to be retrieved after copying.
     */
    public SmartCopy(ArrayList<String> fileTypes) {
        this.fileTypes.addAll(fileTypes);
    }

    /**
     * Copies files from a source directory to a destination directory.
     * 
     * @param sourceDirectory           The path to the source directory.
     * @param destinationDirectory      The path to the destination directory.
     * @throws IOException              If an I/O error occurs during the copying process.
     * @return                          A hash map containing copied file paths with the extensions listed in {@code fileTypes}.
     */
    public HashMap<String, ArrayList<Path>> copyDirectory(String sourceDirectory, String destinationDirectory) throws IOException {
        HashMap<String, ArrayList<Path>> filePaths = new HashMap<>();

        Files.walk(Paths.get(sourceDirectory)).forEach(source -> {
            Path destination = Paths.get(destinationDirectory, source.toString().substring(sourceDirectory.length()));
            try {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                
                for (String fileType : fileTypes) {
                    Matcher matcher = FILE_TYPES_PATTERNS.get(fileType).matcher(source.toString());
                    if (matcher.find() == false)
                        continue; 
                        
                    ArrayList<Path> fileList = filePaths.getOrDefault(fileType, new ArrayList<>());
                    fileList.add(destination);
                    filePaths.put(fileType, fileList);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return filePaths;
    }
}