package com.stedi.asyncdialog.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stedi.asyncdialog.AsyncDialogCore;

public class FragmentExample extends Fragment implements View.OnClickListener, AsyncDialogCore.OnResult<Integer> {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_example, container, false);
        root.findViewById(R.id.fragment_btn_close_fragment).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        new MyProgressDialog<Integer>() {
            @Override
            protected Integer doInBackground() throws Exception {
                Thread.sleep(2000); // some work here
                return 1337;
            }
        }.execute(this);
    }

    @Override
    public void onResult(Exception exception, Integer i, Bundle args) {
        Toast.makeText(getActivity(), "Result: " + i, Toast.LENGTH_LONG).show();

        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
