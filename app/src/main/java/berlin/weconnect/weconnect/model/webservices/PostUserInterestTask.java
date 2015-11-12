package berlin.weconnect.weconnect.model.webservices;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import berlin.weconnect.weconnect.App;
import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.UserInterest;

public class PostUserInterestTask extends AsyncTask<UserInterest, Void, Void> {
    private static final String TAG = "PostUserInterestTask";

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
    protected Void doInBackground(UserInterest... params) {
        UserInterest userInterest = params[0];
        try {
            postUserInterest(userInterest);
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
    private static void postUserInterest(@NonNull UserInterest userInterest) throws Exception {
        // Connection
        final String host = App.getContext().getResources().getString(R.string.backend_host);
        final String api = App.getContext().getResources().getString(R.string.backend_api);
        final String resources = App.getContext().getResources().getString(R.string.backend_resource_userinterests);
        final URL url = new URL(host + api + resources);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=" + ENCODING);
        con.setRequestProperty("Accept-Charset", ENCODING);

        // Add JSON
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("user", userInterest.getUser().getId());
        jsonParam.put("interest", userInterest.getInterest().getId());
        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
        out.write(jsonParam.toString());
        out.close();

        try {
            Log.d(TAG, "Call " + url.toString() + " with " + jsonParam.toString());

            if (con.getResponseCode() != RESPONSE_CODE_OKAY) {
                Log.e(TAG, "Error from Web API");
                Log.e(TAG, "ResponseCode : " + con.getResponseCode());
                Log.e(TAG, "ResponseMethod : " + con.getRequestMethod());

                for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
                    Log.e("PostInterestsTask", entry.getKey() + " : " + entry.getValue());
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
                Log.d("PostInterestsTask", response.toString());
            }
        } finally {
            con.disconnect();
        }
    }
}