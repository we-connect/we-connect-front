package berlin.weconnect.weconnect.model.webservices;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import berlin.weconnect.weconnect.App;
import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.User;

public class GetUsersTask extends AsyncTask<Void, Void, List<User>> {
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
    protected List<User> doInBackground(Void... params) {
        try {
            return getUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<User> result) {
        super.onPostExecute(result);
    }

    // --------------------
    // Methods
    // --------------------

    /**
     * Gets all users
     *
     * @return list of users
     * @throws Exception
     */
    private static List<User> getUsers() throws Exception {
        // Connection
        final String URL = App.getContext().getResources().getString(R.string.url_users);
        String info = "?infos[interests]";
        HttpURLConnection con = (HttpURLConnection) new URL(URL + info).openConnection();

        // Request header
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + ENCODING);
        con.setRequestProperty("Accept-Charset", ENCODING);

        try {
            if (con.getResponseCode() != RESPONSE_CODE_OKAY) {
                Log.d("GetUsersTask", "Error from Web API Download");
                Log.d("GetUsersTask", "ResponseCode : " + con.getResponseCode());
                Log.d("GetUsersTask", "ResponseMethod : " + con.getRequestMethod());

                for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
                    Log.d("GetUsersTask", entry.getKey() + " : " + entry.getValue());
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

            Log.d("GetUsersTask", response.toString());

            if (response.toString().startsWith("ArgumentException")) {
                Log.d("GetUsersTask", response.toString());
                return null;
            } else {
                Type listType = new TypeToken<List<User>>() {}.getType();
                return new Gson().fromJson(response.toString(), listType);
            }
        } finally {
            con.disconnect();
        }
    }
}