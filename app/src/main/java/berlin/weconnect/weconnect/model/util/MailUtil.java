package berlin.weconnect.weconnect.model.util;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import berlin.weconnect.weconnect.R;

public class MailUtil {
    /**
     * Opens a mail client to send a feedback mail
     *
     * @param activity activity
     */
    public static void sendFeedback(Activity activity) {
        Resources res = activity.getResources();

        String versionMajor = Configuration.getGradleProperty(activity, res.getString(R.string.gradle_version_major));
        String versionMinor = Configuration.getGradleProperty(activity, res.getString(R.string.gradle_version_minor));
        String versionPatch = Configuration.getGradleProperty(activity, res.getString(R.string.gradle_version_patch));

        String version = versionMajor + "." + versionMinor + "." + versionPatch;

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {res.getString(R.string.feedback_mail_address)});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.feedback_mail_subject) + " " + version);
        emailIntent.putExtra(Intent.EXTRA_TEXT, res.getString(R.string.feedback_mail_text));
        activity.startActivity(Intent.createChooser(emailIntent, res.getString(R.string.feedback_mail_send)));
    }
}
