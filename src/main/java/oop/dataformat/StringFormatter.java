package oop.dataformat;

/**
 * Utility class for formatting strings.
 */
public class StringFormatter {
    /**
     * Wraps a string with another string template.
     *
     * @param str                           The string to be wrapped.
     * @param wrapperStr                    The wrapper string template.
     * @return                              The wrapped string.
     * @throws IllegalArgumentException     If the wrapper string is null or doesn't contain exactly one "%s".
     */
    public static String wrapString(String str, String wrapperStr) {
        if (wrapperStr == null || !wrapperStr.contains("%s") || wrapperStr.indexOf("%s") != wrapperStr.lastIndexOf("%s"))
            throw new IllegalArgumentException("Wrapper string must contain only one '%s'!");

        return String.format(wrapperStr, str);
    }
}