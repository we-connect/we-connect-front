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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import berlin.weconnect.weconnect.App;
import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.Interest;

public class GetInterestsTask extends AsyncTask<Void, Void, List<Interest>> {
    private static final String TAG = "GetInterests";

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
    protected List<Interest> doInBackground(Void... params) {
        try {
            return getInterests();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(@Nullable List<Interest> result) {
        super.onPostExecute(result);

        if (result != null) {
            for (Interest interest : result) {
                Log.d(TAG, interest.toString());
            }
        }
    }

    // --------------------
    // Methods
    // --------------------

    private static List<Interest> getInterestsMockup() {
        List<Interest> interests = new ArrayList<>();

        interests.add(new Interest("1", "Sports", "Football", 0));
        interests.add(new Interest("2", "Sports", "Basketball", 0));
        interests.add(new Interest("3", "Sports", "Swimming", 0));
        interests.add(new Interest("4", "Sports", "Table Tennis", 0));
        interests.add(new Interest("5", "Sports", "Yoga", 0));
        interests.add(new Interest("6", "Sports", "Dance", 0));
        interests.add(new Interest("7", "Sports", "Martial Arts", 0));
        interests.add(new Interest("8", "Sports", "Volleyball", 0));
        interests.add(new Interest("9", "Sports", "Hiking", 0));

        interests.add(new Interest("10", "Arts", "Painting", 0));
        interests.add(new Interest("11", "Arts", "Street Art", 0));
        interests.add(new Interest("12", "Arts", "Handcraft", 0));
        interests.add(new Interest("13", "Arts", "Knitting", 0));
        interests.add(new Interest("14", "Arts", "Gardening", 0));
        interests.add(new Interest("15", "Arts", "Poetry", 0));
        interests.add(new Interest("16", "Arts", "Photgraphy", 0));
        interests.add(new Interest("17", "Arts", "Theater", 0));
        interests.add(new Interest("18", "Arts", "Cinema", 0));
        interests.add(new Interest("19", "Arts", "Museums", 0));
        interests.add(new Interest("20", "Arts", "Galleries", 0));

        interests.add(new Interest("21", "Music", "Make Music", 0));
        interests.add(new Interest("22", "Music", "Concerts", 0));

        interests.add(new Interest("23", "Food", "Cooking", 0));
        interests.add(new Interest("24", "Food", "Baking", 0));

        interests.add(new Interest("25", "Education", "Languages", 0));
        interests.add(new Interest("26", "Education", "Business", 0));
        interests.add(new Interest("27", "Education", "Computer Science", 0));
        interests.add(new Interest("28", "Education", "Intercultural Studies", 0));
        interests.add(new Interest("29", "Education", "Architecture", 0));
        interests.add(new Interest("30", "Education", "Engineering", 0));

        interests.add(new Interest("31", "Others", "Coding", 0));
        interests.add(new Interest("32", "Others", "Fishing", 0));

        return interests;
    }

    /**
     * Gets all interests
     *
     * @return list of interests
     * @throws Exception
     */
    private static List<Interest> getInterests() throws Exception {
        // Connection
        final String host = App.getContext().getResources().getString(R.string.backend_host);
        final String api = App.getContext().getResources().getString(R.string.backend_api);
        final String resources = App.getContext().getResources().getString(R.string.backend_resource_interests);
        final URL url = new URL(host + api + resources);
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
                Log.d(TAG, response.toString());
                return null;
            } else {
                Type listType = new TypeToken<List<Interest>>() {
                }.getType();
                return new Gson().fromJson(response.toString(), listType);
            }
        } finally {
            con.disconnect();
        }
    }
}