package com.stedi.asyncdialog.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stedi.asyncdialog.AsyncDialogCore;
import com.stedi.asyncdialog.ProgressAsyncDialog;

public class ActivityExample extends AppCompatActivity implements View.OnClickListener, AsyncDialogCore.OnResult<String> {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        findViewById(R.id.activity_example_button).setOnClickListener(this);
        textView = (TextView) findViewById(R.id.activity_example_text_view);
    }

    @Override
    public void onClick(View v) {
        textView.setText("");
        ProgressAsyncDialog dlg = new ProgressAsyncDialog<String>() {
            @Override
            protected String doInBackground() throws Exception {
                Thread.sleep(5000);
                return "work done !";
            }
        };
        dlg.setAllowStateLoss(true);
        dlg.execute(this);
    }

    @Override
    public void onResult(Exception exception, String s, Bundle args) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        textView.setText(s);
    }
}
