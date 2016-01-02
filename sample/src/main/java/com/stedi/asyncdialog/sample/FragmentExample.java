package com.stedi.asyncdialog.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        new ProgressAsyncDialog<String>() {
            @Override
            protected String doInBackground() throws Exception {
                Thread.sleep(5000);
                return "work done !";
            }
        }.execute(this);
    }

    @Override
    public void onResult(Exception exception, String s, Bundle args) {
        textView.setText(s);
    }
}
