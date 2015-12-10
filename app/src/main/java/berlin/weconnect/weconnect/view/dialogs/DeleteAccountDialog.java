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
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.model.entities.EFacebookPictureType;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.FacebookGetProfilePictureTask;
import berlin.weconnect.weconnect.view.activities.BaseActivity;

public class DeleteAccountDialog extends DialogFragment {
    public static final String TAG = "delete_account";

    private OnCompleteListener ocListener;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Resources res = getActivity().getResources();

        UsersController usersController = UsersController.getInstance((BaseActivity) getActivity());
        User currentUser = usersController.getCurrentUser();

        // Load layout
        final View v = View.inflate(getActivity(), R.layout.dialog_delete_account, null);
        final ImageView ivProfilePicture = (ImageView) v.findViewById(R.id.ivProfilePicture);
        final TextView tvName = (TextView) v.findViewById(R.id.tvName);
        final TextView tvMessage = (TextView) v.findViewById(R.id.tvMessage);

        // Get arguments
        Bundle bundle = this.getArguments();
        final String dialogTitle = bundle.getString(res.getString(R.string.bundle_dialog_title));
        final String message = bundle.getString(res.getString(R.string.bundle_message));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogTitle);

        // Set values
        if (currentUser != null) {
            Bitmap bmp = null;
            try {
                bmp = new FacebookGetProfilePictureTask().execute(currentUser.getFacebookId(), EFacebookPictureType.LARGE.getValue()).get();
            } catch (@NonNull InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (bmp != null)
                ivProfilePicture.setImageBitmap(bmp);

            tvName.setText(currentUser.getFirstName());
        }

        tvMessage.setText(message);

        // Add buttons
        builder.setPositiveButton(R.string.delete_account, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
        AlertDialog dialog = (AlertDialog) getDialog();

        Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ocListener.onDeleteAccount();
                dismiss();
            }
        });

        Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    // --------------------
    // Callback interfaces
    // --------------------

    public interface OnCompleteListener {
        void onDeleteAccount();
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