package com.stedi.asyncdialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public abstract class AsyncDialogCore<Result> extends DialogFragment implements Runnable {
    private static final String LOG_TAG = "AsyncDialog";
    private static final String DEFAULT_TAG = AsyncDialogCore.class.getName();

    private Thread backgroundThread;
    private Handler uiHandler;
    private Runnable pendingOnAfterExecute;
    private Bundle args;

    private boolean isFromFragment;
    private boolean isAllowStateLoss;
    private boolean isDismissed;

    protected abstract Result doInBackground() throws Exception;

    public interface OnResult<Result> {
        void onResult(Exception exception, Result result, Bundle args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        uiHandler = new Handler();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pendingOnAfterExecute != null) {
            uiHandler.post(pendingOnAfterExecute);
            pendingOnAfterExecute = null;
        }
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isDismissed = true;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        throw new RuntimeException("Use execute() to show dialog");
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        throw new RuntimeException("Use execute() to show dialog");
    }

    public Thread getBackgroundThread() {
        return backgroundThread;
    }

    public void setAllowStateLoss(boolean isAllowStateLoss) {
        this.isAllowStateLoss = isAllowStateLoss;
    }

    public void execute(Fragment fragment) {
        execute(fragment, null, DEFAULT_TAG);
    }

    public void execute(Fragment fragment, Bundle args) {
        execute(fragment, args, DEFAULT_TAG);
    }

    public void execute(Fragment fragment, String tag) {
        execute(fragment, null, tag);
    }

    public void execute(Fragment fragment, Bundle args, String tag) {
        isFromFragment = true;
        setTargetFragment(fragment, 0);
        executeWith(fragment.getFragmentManager(), args, tag);
    }

    public void execute(Activity activity) {
        execute(activity, null, DEFAULT_TAG);
    }

    public void execute(Activity activity, Bundle args) {
        execute(activity, args, DEFAULT_TAG);
    }

    public void execute(Activity activity, String tag) {
        execute(activity, null, tag);
    }

    public void execute(Activity activity, Bundle args, String tag) {
        isFromFragment = false;
        executeWith(activity.getFragmentManager(), args, tag);
    }

    private void executeWith(FragmentManager manager, Bundle args, String tag) {
        this.args = args;
        super.show(manager, tag);
        backgroundThread = new Thread(this);
        backgroundThread.start();
    }

    @Override
    public void run() {
        Result result = null;
        Exception exception = null;
        try {
            result = doInBackground();
        } catch (Exception e) {
            exception = e;
        }
        onAfterExecute(exception, result, args);
    }

    private void onAfterExecute(final Exception exception, final Result result, final Bundle args) {
        postOnAfterExecute(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                if (isDismissed)
                    return;
                if (isFromFragment) {
                    Fragment fragment = getTargetFragment();
                    if (fragment != null && fragment instanceof OnResult)
                        ((OnResult<Result>) fragment).onResult(exception, result, args);
                    else
                        Log.e(LOG_TAG, "onResult failed: Target fragment not found");
                } else {
                    Activity activity = getActivity();
                    if (activity != null && activity instanceof OnResult)
                        ((OnResult<Result>) activity).onResult(exception, result, args);
                    else
                        Log.e(LOG_TAG, "onResult failed: Target activity not found");
                }
                if (isAllowStateLoss)
                    dismissAllowingStateLoss();
                else
                    dismiss();
            }
        });
    }

    private void postOnAfterExecute(Runnable runnable) {
        if (isResumed() || isAllowStateLoss)
            uiHandler.post(runnable);
        else
            pendingOnAfterExecute = runnable;
    }
}
