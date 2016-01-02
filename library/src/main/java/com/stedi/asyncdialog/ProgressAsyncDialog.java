package com.stedi.asyncdialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

public abstract class ProgressAsyncDialog<T> extends AsyncDialogCore<T> {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        return dialog;
    }
}
