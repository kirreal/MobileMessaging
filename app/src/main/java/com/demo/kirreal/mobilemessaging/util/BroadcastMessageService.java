package com.demo.kirreal.mobilemessaging.util;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.demo.kirreal.mobilemessaging.location.LocationService;

public class BroadcastMessageService {

    public static void sendBroadcastMessage(Context ctx, Message msg) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(LocationService.ADRESS_RESOLVED);

        intent.putExtra(LocationService.BROADCAST_MESSAGE_KEY, msg);
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
    }
}
