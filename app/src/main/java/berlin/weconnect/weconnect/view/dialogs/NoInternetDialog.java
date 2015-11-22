package berlin.weconnect.weconnect.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import berlin.weconnect.weconnect.R;

public class NoInternetDialog extends DialogFragment {
    public static final String TAG = "no_internet";

    private OnCompleteListener ocListener;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fill views with arguments
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.no_internet_connection);

        // Add positive button
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(R.string.close_app, new DialogInterface.OnClickListener() {
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
                ocListener.onRetry();
                dismiss();
            }
        });

        Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ocListener.onCloseApp();
                dismiss();
            }
        });
    }

    // --------------------
    // Callback interfaces
    // --------------------

    public interface OnCompleteListener {
        void onRetry();
        void onCloseApp();
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