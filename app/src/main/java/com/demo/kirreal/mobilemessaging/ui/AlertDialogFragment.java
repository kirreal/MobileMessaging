package com.demo.kirreal.mobilemessaging.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.demo.kirreal.mobilemessaging.R;

public abstract class AlertDialogFragment extends DialogFragment {
    protected AlertDialog.Builder mDialogBuilder;
    protected Fragment mFragment;
    protected int mTitleId;
    protected String mTitle;
    protected int mMessageId;
    protected String mMessage;

    public AlertDialogFragment() {
        super();
    }

    public AlertDialogFragment(Fragment fragment) {
        mDialogBuilder = new AlertDialog.Builder(fragment.getActivity());
        mFragment = fragment;
    }

    protected void build() {
        setTitle();
        setMessage();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        build();
        return mDialogBuilder.create();
    }

    protected void show() {
        setRetainInstance(true);
        super.show(mFragment.getFragmentManager(), "AlertDialog");
    }

    @Override
    public void onDestroyView() {
      if (getDialog() != null && getRetainInstance()) {
          getDialog().setDismissMessage(null);
      }

      super.onDestroyView();
    }

    protected void setTitle() {
        if (mTitleId > 0) {
            mDialogBuilder.setTitle(mTitleId);
        } else if (mTitle != null && !mTitle.equals("")) {
            mDialogBuilder.setTitle(mTitle);
        }
    }

    protected void setMessage() {
        if (mMessageId > 0) {
            mDialogBuilder.setMessage(mMessageId);
        } else if (mMessage != null && !mMessage.equals("")) {
            mDialogBuilder.setMessage(mMessage);
        }
    }


    public void setPositiveButtonListener(OnClickListener listener) {
        mDialogBuilder.setPositiveButton(R.string.ok_label, listener);
    }

    public void setNegativeButtonListener(final OnClickListener listener) {
        mDialogBuilder.setNegativeButton(R.string.cancel_label, listener);
    }
}
