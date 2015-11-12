package berlin.weconnect.weconnect.model.util;

import android.support.annotation.NonNull;

/**
 * Created by Florian on 12.11.2015.
 */
public class StringUtil {
    public static String jsonToCamelCase(@NonNull String in) {
        StringBuilder sb = new StringBuilder();
        boolean indicator = false;
        for (char c : in.toCharArray()) {
            if (c == '_') {
                indicator = true;
            } else {
                if (indicator) {
                    sb.append(Character.toUpperCase(c));
                    indicator = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
