package com.stedi.asyncdialog.sample;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.stedi.asyncdialog.AsyncDialogCore;

public abstract class MyProgressDialog<T> extends AsyncDialogCore<T> {
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
