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

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {res.getString(R.string.feedback_mail_address)});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.feedback_mail_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, res.getString(R.string.feedback_mail_text));
        activity.startActivity(Intent.createChooser(emailIntent, res.getString(R.string.feedback_mail_send)));
    }
}
