package berlin.weconnect.weconnect;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;

public class App extends Application {
    // Context
    private static Context context;

    private static App instance;

    // --------------------
    // Constructors
    // --------------------

    public App() {
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }

        return instance;
    }

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    // --------------------
    // Methods
    // --------------------

    public static Context getContext() {
        return context;
    }
}
