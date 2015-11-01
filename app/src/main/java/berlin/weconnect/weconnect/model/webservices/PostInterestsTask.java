package berlin.weconnect.weconnect.model.webservices;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import berlin.weconnect.weconnect.App;
import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.User;

public class PostInterestsTask extends AsyncTask<User, Void, List<User>> {
    private static final String ENCODING = "UTF-8";
    private static final String contentType = "text/plain";

    private static final int RESPONSE_CODE_OKAY = 200;

    private OnCompleteListener ocListener;

    // --------------------
    // Constructors
    // --------------------

    public PostInterestsTask() {
    }

    public PostInterestsTask(OnCompleteListener ocListener) {
        this.ocListener = ocListener;
    }

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<User> doInBackground(User... params) {
        User user = params[0];
        try {
            postInterests(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<User> result) {
        super.onPostExecute(result);

        if (result != null) {
            if (ocListener != null)
                ocListener.onGetUsers(result);
        }
    }

    // --------------------
    // Methods
    // --------------------

    /**
     * Gets all users
     *
     * @return
     * @throws Exception
     */
    private static void postInterests(User user) throws Exception {
        // Connection
        final String URL = App.getContext().getResources().getString(R.string.url_userinterests);
        String info = "?infos[interests]";
        HttpURLConnection con = (HttpURLConnection) new URL(URL + info).openConnection();

        // Request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + ENCODING);
        con.setRequestProperty("Accept-Charset", ENCODING);

        try {
            if (con.getResponseCode() != RESPONSE_CODE_OKAY) {
                System.out.println("Error from Web API Download");
                System.out.println("ResponseCode : " + con.getResponseCode());
                System.out.println("ResponseMethod : " + con.getRequestMethod());

                for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
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

            System.out.println(response.toString());

            if (response.toString().startsWith("ArgumentException")) {
                System.out.println(response.toString());
            } else {
                Type listType = new TypeToken<List<User>>() {}.getType();
            }
        } finally {
            con.disconnect();
        }
    }

    /**
     * Converts an input stream to string
     *
     * @param inputStream input stream to be converted
     * @return string value
     */
    private static String inputStreamToString(final InputStream inputStream) throws IOException {
        final StringBuilder outputBuilder = new StringBuilder();

        String string;
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
            while (null != (string = reader.readLine())) {
                outputBuilder.append(string.replaceAll("\uFEFF", ""));
            }
        }

        return outputBuilder.toString();
    }

    // --------------------
    // Callback interfaces
    // --------------------

    public interface OnCompleteListener {
        void onGetUsers(List<User> response);
    }
}