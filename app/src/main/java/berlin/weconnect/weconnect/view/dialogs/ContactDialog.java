package berlin.weconnect.weconnect.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.model.entities.EFacebookPictureType;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.FacebookGetProfilePictureTask;
import berlin.weconnect.weconnect.view.adapters.InterestsDisplayAdapter;

public class ContactDialog extends DialogFragment {
    public static final String TAG = "contact";

    private OnCompleteListener ocListener;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Resources res = getActivity().getResources();

        UsersController usersController = UsersController.getInstance();

        // Load layout
        final View v = View.inflate(getActivity(), R.layout.dialog_contact, null);

        final ImageView ivProfilePicture = (ImageView) v.findViewById(R.id.ivProfilePicture);
        final TextView tvName = (TextView) v.findViewById(R.id.tvName);
        final TextView tvSharedInterests = (TextView) v.findViewById(R.id.tvSharedInterests);
        final ListView lvInterests = (ListView) v.findViewById(R.id.lvInterests);

        // Get arguments
        Bundle bundle = this.getArguments();
        // final String dialogTitle = bundle.getString(res.getString(R.string.bundle_dialog_title));
        final String contactFacebookId = bundle.getString(res.getString(R.string.bundle_contact_facebook_id));

        User user = usersController.getUserByFacebookId(contactFacebookId);

        // Close dialog if user does not exist
        if (user == null)
            dismiss();

        // Fill views with arguments
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        // builder.setTitle(dialogTitle);

        if (user != null) {
            Bitmap bmp = null;
            try {
                bmp = new FacebookGetProfilePictureTask().execute(user.getFacebookId(), EFacebookPictureType.LARGE.getValue()).get();
            } catch (@NonNull InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (bmp != null)
                ivProfilePicture.setImageBitmap(bmp);

            tvName.setText(user.getFirstName());
            int sharedInterests = user.getSharedInterestsWith(user).size();
            tvSharedInterests.setText(String.format(res.getQuantityString(R.plurals.shared_interests, sharedInterests), sharedInterests));

            final InterestsDisplayAdapter interestsSelectionAdapter = new InterestsDisplayAdapter(getActivity(), R.layout.list_item_interest_display, user.getInterests());
            lvInterests.setAdapter(interestsSelectionAdapter);
        }

        // Add positive button
        builder.setPositiveButton(R.string.contact_on_facebook, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get arguments
        Bundle bundle = this.getArguments();
        final String facebookId = bundle.getString(getActivity().getResources().getString(R.string.bundle_contact_facebook_id));

        AlertDialog dialog = (AlertDialog) getDialog();

        Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ocListener.onGoToFacebookPage(facebookId);
                dismiss();
            }
        });
    }

    // --------------------
    // Callback interfaces
    // --------------------

    public interface OnCompleteListener {
        void onGoToFacebookPage(String page);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.ocListener = (OnCompleteListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}