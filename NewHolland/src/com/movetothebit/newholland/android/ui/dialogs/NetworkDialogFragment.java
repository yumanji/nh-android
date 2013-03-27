package com.movetothebit.newholland.android.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.ui.HomeActivity;

public class NetworkDialogFragment extends SherlockDialogFragment {

    public static NetworkDialogFragment newInstance() {
        NetworkDialogFragment frag = new NetworkDialogFragment();
       
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.no_connection)
                .setMessage(R.string.no_connection_msg)
                .setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                           dismiss();
                        }
                    }
                )
               .setNegativeButton(R.string.config,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                           ((BaseActivity)getActivity()).showNetworkOptions();
                        }
                    }
                )
                .create();
    }
}