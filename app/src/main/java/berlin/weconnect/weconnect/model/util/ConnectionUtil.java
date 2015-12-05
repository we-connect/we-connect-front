package berlin.weconnect.weconnect.model.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import berlin.weconnect.weconnect.App;

public class ConnectionUtil {
    /**
     * Tests if there is an internet connection
     *
     * @return whether there is an internet connection or not
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
