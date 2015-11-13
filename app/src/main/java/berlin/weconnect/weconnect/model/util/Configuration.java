package berlin.weconnect.weconnect.model.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final String TAG = "Configuration";
    private static final String GRADLE_PROPERTIES_FILE = "gradle.properties";

    public static String getGradleProperty(Context c, String property) {
        try {
            InputStream inputStream = c.getAssets().open(GRADLE_PROPERTIES_FILE);
            Properties props = new Properties();
            props.load(inputStream);
            return props.getProperty(property);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return null;
    }
}
