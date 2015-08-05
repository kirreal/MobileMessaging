package com.demo.kirreal.mobilemessaging.ui;

import android.content.Context;

import com.demo.kirreal.mobilemessaging.R;
import com.demo.kirreal.mobilemessaging.util.Util;

public class AuthorFieldValidator {
    private static final int ALPHANUMERIC_HIGHT_LIMIT = 11;
    private static final int ALPHANUMERIC_LOW_LIMIT = 3;
    private static final int NUMERIC_HIGHT_LIMIT = 14;
    private static final int NUMERIC_LOW_LIMIT = 3;
    private String mValue;
    private Context mCtx;
    private String mErrorMessage;

    public AuthorFieldValidator(final Context ctx, String value) {
        this.mValue = value;
        mCtx = ctx;
    }

    public boolean validate() {
        boolean isValid = true;

        if (Util.isNumeric(mValue)) {
            isValid = validateNumeric();

            if (!isValid) {
                mErrorMessage = mCtx.getResources().getString(R.string.numeric_validation_error);
            }

            return isValid;
        } else {
            isValid = validateAlphanumeric();

            if (!isValid) {
                mErrorMessage = mCtx.getResources().getString(R.string.alphanumeric_validation_error);
            }

            return isValid;
        }
    }

    private boolean validateAlphanumeric() {
        return (ALPHANUMERIC_LOW_LIMIT <= mValue.length() && mValue.length() <= ALPHANUMERIC_HIGHT_LIMIT);
    }

    private boolean validateNumeric() {
        return (NUMERIC_LOW_LIMIT <= mValue.length() && mValue.length() <= NUMERIC_HIGHT_LIMIT);
    }

    public String getError() {
        return mErrorMessage;
    }
}
