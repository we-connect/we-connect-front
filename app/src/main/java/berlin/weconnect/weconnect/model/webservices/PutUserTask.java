package berlin.weconnect.weconnect.model.webservices;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import berlin.weconnect.weconnect.App;
import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.User;

public class PutUserTask extends AsyncTask<User, Void, Void> {
    private static final String TAG = "PutUserTask";

    private static final String ENCODING = "UTF-8";
    private static final int RESPONSE_CODE_OKAY = 201;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Nullable
    @Override
    protected Void doInBackground(User... params) {
        User user = params[0];
        try {
            putUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // --------------------
    // Methods
    // --------------------

    /**
     * Updates a user's interests
     *
     * @throws Exception
     */
    private static void putUser(User user) throws Exception {
        // Connection
        final String host = App.getContext().getResources().getString(R.string.backend_host);
        final String api = App.getContext().getResources().getString(R.string.backend_api);
        final String resources = App.getContext().getResources().getString(R.string.backend_resource_users);
        final URL url = new URL(host + api + resources);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Request header
        con.setRequestMethod("PUT");
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=" + ENCODING);
        con.setRequestProperty("Accept-Charset", ENCODING);

        // Add JSON
        String json = new Gson().toJson(user, User.class);
        OutputStreamWriter out = new   OutputStreamWriter(con.getOutputStream());
        out.write(json);
        out.close();

        try {
            Log.d(TAG, "Call " + url.toString() + " with " + json);

            if (con.getResponseCode() != RESPONSE_CODE_OKAY) {
                Log.e(TAG, "Error from Web API");
                Log.e(TAG, "ResponseCode : " + con.getResponseCode());
                Log.e(TAG, "ResponseMethod : " + con.getRequestMethod());

                for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
                    Log.e(TAG, entry.getKey() + " : " + entry.getValue());
                }
                throw new Exception("Error from Web API");
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            // Evaluate response
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            if (response.toString().startsWith("ArgumentException")) {
                Log.d(TAG, response.toString());
            }
        } finally {
            con.disconnect();
        }
    }
}