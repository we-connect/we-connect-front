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
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.model.entities.EFacebookPictureType;
import berlin.weconnect.weconnect.model.entities.EType;
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.FacebookGetProfilePictureTask;
import berlin.weconnect.weconnect.view.adapters.InterestCategoriesShowAdapter;

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
        final ImageView ivType = (ImageView) v.findViewById(R.id.ivType);
        final ListView lvInterestCategories = (ListView) v.findViewById(R.id.lvInterestCategories);

        // Get arguments
        Bundle bundle = this.getArguments();
        final String contactFacebookId = bundle.getString(res.getString(R.string.bundle_contact_facebook_id));

        User currentUser = usersController.getCurrentUser();
        User user = usersController.getUserByFacebookId(contactFacebookId);

        // Close dialog if user does not exist
        if (user == null)
            dismiss();

        // Set values
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);

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
            List<Interest> sharedInterests = user.getSharedInterestsWith(currentUser);
            tvSharedInterests.setText(String.format(res.getQuantityString(R.plurals.shared_interests, sharedInterests.size()), sharedInterests.size()));


            if (user.getType().equals(EType.NEWCOMER.getValue()))
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_web_site));
            else if (user.getType().equals(EType.LOCAL.getValue()))
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_berlin));

            final InterestCategoriesShowAdapter interestCategoriesShowAdapter = new InterestCategoriesShowAdapter(getActivity(), R.layout.list_item_interest_category_show, user.getInterestCategories());
            lvInterestCategories.setAdapter(interestCategoriesShowAdapter);
            interestCategoriesShowAdapter.filter(currentUser);
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