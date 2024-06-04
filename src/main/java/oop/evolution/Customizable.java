package oop.evolution;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *  A interface that provides a method to load properties from a specified file path.
 */
public interface Customizable {
     /**
     * Loads properties from the specified file path and returns them as a {@code HashMap<String, Integer>}.
     * The properties file should contain key-value pairs where the values are integers.
     *
     * @param propertiesPath    The path to the properties file.
     * @return                  The hashmap containing the properties loaded from the file.
     */
    static HashMap<String, Integer> loadProperties(String propertiesPath) {
        HashMap<String, Integer> result = new HashMap<>();
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(propertiesPath)) {
            properties.load(inputStream);
            
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                result.put(key, Integer.parseInt(value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}