package berlin.weconnect.weconnect.model.webservices;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
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
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.util.StringUtil;

public class GetSuggestedUsersTask extends AsyncTask<User, Void, List<User>> {
    private static final String TAG = "GetSuggestedUsersTask";

    private static final String ENCODING = "UTF-8";
    private static final int RESPONSE_CODE_OKAY = 200;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Nullable
    @Override
    protected List<User> doInBackground(User... params) {
        User user = params[0];
        try {
            return getSuggestedUsers(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(@Nullable List<User> result) {
        super.onPostExecute(result);

        if (result != null) {
            for (User user : result) {
                Log.d(TAG, user.toString());
            }
        }
    }

    // --------------------
    // Methods
    // --------------------

    /**
     * Gets all users filtered
     *
     * @return list of users
     * @throws Exception
     */
    private static List<User> getSuggestedUsers(@Nullable User user) throws Exception {
        // Connection
        final String host = App.getContext().getResources().getString(R.string.backend_host);
        final String api = App.getContext().getResources().getString(R.string.backend_api);
        final String resources = App.getContext().getResources().getString(R.string.backend_resource_users);

        StringBuilder filter = new StringBuilder();
        if (user.getInterests() != null && !user.getInterests().isEmpty()) {
            filter.append("?");
            for (Interest i : user.getInterests()) {
                filter.append("filters[interests][]=").append(i.getId()).append("&");
            }
        }

        if(user.getMeetingPref() != null) {
            filter.append("filters[gender][]=").append(user.getMeetingPref()).append("&");
        }

        final URL url = new URL(host + api + resources + filter);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Request header
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + ENCODING);
        con.setRequestProperty("Accept-Charset", ENCODING);

        try {
            Log.d(TAG, "Call " + url.toString());

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

            Log.d(TAG, response.toString());

            if (response.toString().startsWith("ArgumentException")) {
                Log.e(TAG, response.toString());
                return null;
            } else {
                Type listType = new TypeToken<List<User>>() {
                }.getType();
                return new Gson().fromJson(StringUtil.jsonToCamelCase(response.toString()), listType);
            }
        } finally {
            con.disconnect();
        }
    }
}