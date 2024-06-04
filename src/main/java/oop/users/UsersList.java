package oop.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import oop.files.CsvParser;

/**
 * The UsersList class represents a list of users and provides methods for managing and printing them.
 */
public class UsersList { 
    /**
     * Fields representing user list information.   
     */
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<Integer, String> userIds = new HashMap<>();
    private final LocalDate date;
    private final UserGenerator userConstructor;
    private final UserClearMethod userClearDataMethod;

    public UsersList(String userData, String results) {
        this(LocalDate.now(), userData.contains("noemail") ? UserTypes.USER : UserTypes.USER_EMAIL, new CsvParser(userData, ",").fileData);
        
        loadResults(new CsvParser(results, ",").fileData);
    }

    /**
     * Constructs a UsersList object with the given date, user type, and user data.
     *
     * @param date      The date associated with the user list.
     * @param type      The type of users in the list (USER or USER_EMAIL).
     * @param userData  The list of user data, where each entry is represented by a LinkedHashMap containing user attributes.
     */
    public UsersList(LocalDate date, UserTypes type, ArrayList<LinkedHashMap<String, String>> userData) {
        this.date = date;

        if (type == UserTypes.USER) {
            userConstructor = User::new;
            userClearDataMethod = User::clearUserInfo;
        } else {
            userConstructor = UserWithEmail::new;
            userClearDataMethod = UserWithEmail::clearUserInfo;
        }

        loadUsers(userData);
    }

    /**
     * Generates user information files for all users in the collection.
     * Invokes the generateUserInfo() method for each user to create their respective files.
     */
    public void generateUserFiles() {
        for (User user : users.values())
            user.generateUserInfo();
    }

    /**
     * Loads users from the provided user data.
     *
     * @param userData  The list of user data to load, where each entry is represented by a LinkedHashMap containing user attributes.
     */
    private void loadUsers(ArrayList<LinkedHashMap<String, String>> userData) {
        for (LinkedHashMap<String, String> data : userData) {
            userClearDataMethod.getUserClearMethod(data);
    
            int code = Optional.ofNullable(data.get("code"))
                               .map(String::trim)
                               .map(ageStr -> {
                                    try {
                                        return Math.max(Integer.parseInt(ageStr), 0);
                                    } catch (NumberFormatException e) {
                                        return -1;
                                    }
                                })
                               .orElse(-1);
            String uniqueUserStr = User.uniqueUserStr(data);
            
            if (!users.containsKey(uniqueUserStr)) {
                User user = userConstructor.getUser(data);
                users.put(uniqueUserStr, user);
            }   
    
            if (!userIds.containsKey(code))
                userIds.put(code, uniqueUserStr);
        }
    }

    /**
     * Loads results data into the users result maps.
     * Iterates through the provided result data and processes each entry.
     * For each entry, extracts code, type, and value information.
     * 
     * @param resultData    The ArrayList containing result data represented as LinkedHashMaps, where keys represent column names.
     */
    private void loadResults(ArrayList<LinkedHashMap<String, String>> resultData) {
        for (LinkedHashMap<String, String> data : resultData) {
            userClearDataMethod.getUserClearMethod(data);

            int code = Optional.ofNullable(data.get("code"))
                               .map(String::trim)
                               .map(ageStr -> {
                                   try {
                                       return Math.max(Integer.parseInt(ageStr), 0);
                                   } catch (NumberFormatException e) {
                                       return -1;
                                   }
                               })
                               .orElse(-1);
            String type = Optional.ofNullable(data.get("type"))
                                  .map(String::strip)
                                  .map(String::toUpperCase)
                                  .orElse("UNKNOWN");
            float value = Optional.ofNullable(data.get("value"))
                                  .map(String::strip)
                                  .map(str -> {
                                      try {
                                          return Float.parseFloat(str);
                                      } catch (NumberFormatException e) {
                                          return 0.0f;
                                      }
                                  })
                                  .orElse(0.0f);
            
            User user = users.get(userIds.get(code));
            HashMap<String, HashMap<String, String>> result = user.setResults(type, value);

            if (result == null)
                continue;

            if (user.results.containsKey(userIds.get(code)))
                user.results.get(userIds.get(code)).putAll(user.setResults(type, value));
            else 
                user.results.put(userIds.get(code), users.get(userIds.get(code)).setResults(type, value));
        }
    }

    /**
     * Prints all users in the list.
     */
    public void print() {
        System.out.println("Users:");
        for (Map.Entry<String, User> user : users.entrySet())
            System.out.println("Key: " + user.getKey() + ", Value: " + user.getValue());
    
        System.out.println("User IDs:");
        for (Map.Entry<Integer, String> id : userIds.entrySet())
            System.out.println("Key: " + id.getKey() + ", Value: " + id.getValue());
    }
}