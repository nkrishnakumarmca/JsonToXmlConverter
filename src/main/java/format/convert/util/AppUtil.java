package format.convert.util;

import java.util.regex.Pattern;

public class AppUtil {
    private AppUtil() {

    }

    /**
     * @param s
     * @return
     */
    public static String validate(String s) {
        //TODO: String with -
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (String.valueOf(s).equalsIgnoreCase("null")) {
            return "null";
        }
        if (pattern.matcher(s).matches()) {
            return "number";
        } else if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
            return "boolean";
        } else
            return "string";
    }
}
