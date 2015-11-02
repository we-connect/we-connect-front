package berlin.weconnect.weconnect.model.webservices;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import berlin.weconnect.weconnect.App;
import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;

public class PostInterestsTask extends AsyncTask<Object, Void, Void> {
    private static final String ENCODING = "UTF-8";
    private static final int RESPONSE_CODE_OKAY = 200;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Object... params) {
        User user = (User) params[0];
        List<Interest> interests = (List<Interest>) params[1];
        try {
            postInterests(user, interests);
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
    private static void postInterests(User user, List<Interest> interests) throws Exception {
        // Connection
        final String URL = App.getContext().getResources().getString(R.string.url_userinterests);
        HttpURLConnection con = (HttpURLConnection) new URL(URL).openConnection();

        // Request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + ENCODING);
        con.setRequestProperty("Accept-Charset", ENCODING);

        // TODO : fill body

        try {
            if (con.getResponseCode() != RESPONSE_CODE_OKAY) {
                Log.d("PostInterestsTask", "Error from Web API Download");
                Log.d("PostInterestsTask", "ResponseCode : " + con.getResponseCode());
                Log.d("PostInterestsTask", "ResponseMethod : " + con.getRequestMethod());

                for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
                    Log.d("PostInterestsTask", entry.getKey() + " : " + entry.getValue());
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