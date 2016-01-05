package com.stedi.asyncdialog.sample;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stedi.asyncdialog.AsyncDialogCore;
import com.stedi.asyncdialog.ProgressAsyncDialog;

public class FragmentExample extends Fragment implements View.OnClickListener, AsyncDialogCore.OnResult<String> {
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_example, container, false);
        root.findViewById(R.id.fragment_example_button).setOnClickListener(this);
        textView = (TextView) root.findViewById(R.id.fragment_example_text_view);
        return root;
    }

    @Override
    public void onClick(View v) {
        textView.setText("");
        ProgressAsyncDialog dlg = new ProgressAsyncDialog<String>() {
            private volatile boolean isDismissed;

            @Override
            protected String doInBackground() throws Exception {
                for (int i = 0; i < 50; i++) {
                    Thread.sleep(100);
                    if (isDismissed)
                        return null;
                }
                return "work done !";
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                super.onDismiss(dialog);
                isDismissed = true;
            }
        };
        dlg.setAllowStateLoss(true);
        dlg.execute(this);
    }

    @Override
    public void onResult(Exception exception, String s, Bundle args) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        textView.setText(s);
    }
}
