package com.movetothebit.newholland.android.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.ui.HomeActivity;

public class LogoutDialogFragment extends SherlockDialogFragment {

    public static LogoutDialogFragment newInstance() {
        LogoutDialogFragment frag = new LogoutDialogFragment();
      
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.logout_dialog_title)
                .setMessage(R.string.logout_dialog_message)
                .setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((HomeActivity)getActivity()).doLogoutClick();
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