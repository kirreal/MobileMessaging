package com.demo.kirreal.mobilemessaging.location;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.demo.kirreal.mobilemessaging.ui.MessageFragment;

/**
 * Created by kmoshias on 31.07.2015.
 */
public class LocationServiceHandler extends Handler {
    private final MessageFragment mMessageFragment;

    public LocationServiceHandler(MessageFragment fragment) {
        mMessageFragment = fragment;
    }

    @Override
    public void handleMessage(Message message) {

    }
}
