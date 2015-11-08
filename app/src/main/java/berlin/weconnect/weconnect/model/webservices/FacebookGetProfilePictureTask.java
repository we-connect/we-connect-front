package berlin.weconnect.weconnect.model.webservices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class FacebookGetProfilePictureTask extends AsyncTask<String, Void, Bitmap> {

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String facebookId = params[0];
        try {
            return getProfilePicture(facebookId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
    }

    // --------------------
    // Methods
    // --------------------

    private static Bitmap getProfilePicture(final String facebookId) throws Exception {
        URL url = new URL("https://graph.facebook.com/" + facebookId + "/picture?type=normal");
        InputStream is = url.openConnection().getInputStream();
        Bitmap bmp = BitmapFactory.decodeStream(is);
        return bmp;
    }
}
