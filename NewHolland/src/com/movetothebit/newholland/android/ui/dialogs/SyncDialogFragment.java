package com.movetothebit.newholland.android.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.ui.HomeActivity;

public class SyncDialogFragment extends SherlockDialogFragment {

    public static SyncDialogFragment newInstance(int title) {
        SyncDialogFragment frag = new SyncDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.alert_dialog_title)
                .setMessage(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((HomeActivity)getActivity()).doSyncClick();
                        }
                    }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dismiss();
                        }
                    }
                )
                .create();
    }
}