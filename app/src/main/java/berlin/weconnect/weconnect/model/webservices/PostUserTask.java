package berlin.weconnect.weconnect.model.webservices;

import android.os.AsyncTask;
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

public class PostUserTask extends AsyncTask<User, Void, Void> {
    private static final String ENCODING = "UTF-8";
    private static final int RESPONSE_CODE_OKAY = 201;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(User... params) {
        User user = params[0];
        try {
            postUser(user);
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
    private static void postUser(User user) throws Exception {
        // Connection
        final String URL = App.getContext().getResources().getString(R.string.url_users);
        HttpURLConnection con = (HttpURLConnection) new URL(URL).openConnection();

        // Request header
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=" + ENCODING);
        con.setRequestProperty("Accept-Charset", ENCODING);

        // Add JSON
        String json = new Gson().toJson(user, User.class);
        OutputStreamWriter out = new   OutputStreamWriter(con.getOutputStream());
        out.write(json);
        out.close();

        try {
            if (con.getResponseCode() != RESPONSE_CODE_OKAY) {
                Log.d("PostUserTask", "Error from Web API Download");
                Log.d("PostUserTask", json);
                Log.d("PostUserTask", "ResponseCode : " + con.getResponseCode());
                Log.d("PostUserTask", "ResponseMethod : " + con.getRequestMethod());

                for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
                    Log.d("PostUserTask", entry.getKey() + " : " + entry.getValue());
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
                Log.d("PostUserTask", response.toString());
            }
        } finally {
            con.disconnect();
        }
    }
}