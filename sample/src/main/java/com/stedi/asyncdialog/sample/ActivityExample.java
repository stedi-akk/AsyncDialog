package com.stedi.asyncdialog.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.stedi.asyncdialog.AsyncDialogCore;

public class ActivityExample extends AppCompatActivity implements View.OnClickListener, AsyncDialogCore.OnResult<String> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        findViewById(R.id.activity_btn_show_fragment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new MyProgressDialog<String>() {
            @Override
            protected String doInBackground() throws Exception {
                Thread.sleep(2000); // some work here
                return "qwerty";
            }
        }.execute(this);
    }

    @Override
    public void onResult(Exception exception, String s, Bundle args) {
        Toast.makeText(this, "Result: " + s, Toast.LENGTH_LONG).show();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.activity_fragment_container, new FragmentExample(), FragmentExample.class.getName())
                .commit();
    }
}
