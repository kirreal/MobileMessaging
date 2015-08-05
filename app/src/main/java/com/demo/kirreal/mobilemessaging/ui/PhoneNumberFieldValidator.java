package com.demo.kirreal.mobilemessaging.ui;

import android.content.Context;

import com.demo.kirreal.mobilemessaging.R;

public class PhoneNumberFieldValidator {
    private String mValue;
    private Context mCtx;
    private String mErrorMessage;

    public PhoneNumberFieldValidator(final Context ctx, String value) {
        this.mValue = value;
        mCtx = ctx;
    }

    public boolean validate() {
        boolean isValid = mValue.length() == 11;

        if (!isValid) {
            mErrorMessage = mCtx.getResources().getString(R.string.phone_validation_error);
        }

        return isValid;
    }

    public String getError() {
        return mErrorMessage;
    }

}
