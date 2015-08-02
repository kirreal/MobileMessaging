package com.demo.kirreal.mobilemessaging.ui;

import android.app.Fragment;
import android.content.DialogInterface.OnClickListener;

public class OkAlertDialog extends AlertDialogFragment {

    public OkAlertDialog(Fragment fragment, int titleId, int messageId) {
        super(fragment);
        mTitleId = titleId;
        mMessageId = messageId;
    }

    public OkAlertDialog(Fragment fragment, String title, String message) {
        super(fragment);
        mTitle = title;
        mMessage = message;
    }

    public OkAlertDialog(Fragment fragment, String title, int messageId) {
        super(fragment);
        mTitle = title;
        mMessageId = messageId;
    }

    public OkAlertDialog(Fragment fragment, int titleId, String message) {
        super(fragment);
        mTitleId = titleId;
        mMessage = message;
    }

    public void show(final OnClickListener positiveListener) {
        this.setPositiveButtonListener(positiveListener);
        show();
    }

}
