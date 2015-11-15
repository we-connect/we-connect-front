package berlin.weconnect.weconnect.model.webservices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

import berlin.weconnect.weconnect.App;
import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.EFacebookPictureType;

public class FacebookGetProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "FacebookGetProfilePictu";

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(String... params) {
        String facebookId = params[0];
        String type = params[1];
        try {
            return getProfilePicture(facebookId, type);
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

    private static Bitmap getProfilePicture(final String facebookId, final String type) throws Exception {
        String host = App.getContext().getResources().getString(R.string.facebook_graph_host);
        String resource = App.getContext().getResources().getString(R.string.facebook_graph_resource_picture);
        String parameters =  type != null ? "?type=" + type : "?type=" + EFacebookPictureType.NORMAL.getValue();
        URL url = new URL(host + facebookId + resource + parameters);

        Log.d(TAG, "Call " + url.toString());

        InputStream is = url.openConnection().getInputStream();
        return BitmapFactory.decodeStream(is);
    }
}
