package com.demo.kirreal.mobilemessaging.ui;

import android.app.Fragment;
import android.content.DialogInterface.OnClickListener;

public class OkCancelAlertDialog extends AlertDialogFragment {

    public OkCancelAlertDialog() {
        super();
    }

    public OkCancelAlertDialog(Fragment fragment, int titleId, int messageId) {
        super(fragment);
        mTitleId = titleId;
        mMessageId = messageId;
    }

    public OkCancelAlertDialog(Fragment fragment, String title, String message) {
        super(fragment);
        mTitle = title;
        mMessage = message;
    }

    public OkCancelAlertDialog(Fragment fragment, String title, int messageId) {
        super(fragment);
        mTitle = title;
        mMessageId = messageId;
    }

    public OkCancelAlertDialog(Fragment fragment, int titleId, String message) {
        super(fragment);
        mTitleId = titleId;
        mMessage = message;
    }

    public void show(final OnClickListener positiveListener, final OnClickListener negativeListener) {
        this.setPositiveButtonListener(positiveListener);
        this.setNegativeButtonListener(negativeListener);

        show();
    }

}
