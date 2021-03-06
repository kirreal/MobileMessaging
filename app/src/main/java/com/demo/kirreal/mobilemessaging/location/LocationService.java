package com.demo.kirreal.mobilemessaging.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.demo.kirreal.mobilemessaging.ui.MessageFragment;
import com.demo.kirreal.mobilemessaging.util.BroadcastMessageService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final String BROADCAST_MESSAGE_KEY = "location_service_message";
    public static final String ADRESS_RESOLVED = "ADRESS_RESOLVED_SUCCESS";
    public static final String LOCATION_DATA_EXTRA = "location_data_extra";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Context mContext;
    public static final String ADDRESS_KEY = "adress";
    public static final String ERROR_CODE_KEY = "erorr_code";
    public static final String ERROR_MESSAGE_KEY = "erorr_message";
    public static final int GEOCODING_ADDREESS_LIMIT = 1;

    public LocationService(final Context ctx) {
        mContext = ctx;
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            Log.d("onConnected", mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
            disconnect();
            getAddressByLocation();
        } else {
            Message msg = Message.obtain();

            msg.what = MessageFragment.SERVICE_FAIL;
            msg.getData().putString(ERROR_MESSAGE_KEY, "Check your gps is turned on.");

            BroadcastMessageService.sendBroadcastMessage(mContext, msg);
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting())) {
            mGoogleApiClient.disconnect();
        }
    }

    public void getAddressByLocation() {
        Intent intent = new Intent(mContext, FetchAddressIntentService.class);

        intent.putExtra(LOCATION_DATA_EXTRA, mLastLocation);
        mContext.startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("onConnectionSuspended", "Hangs...");
        Message msg = Message.obtain();

        msg.what = MessageFragment.GOOGLE_SERVICE_FAIL;
        msg.getData().putInt(ERROR_CODE_KEY, i);

        BroadcastMessageService.sendBroadcastMessage(mContext, msg);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", "Failed...");
        Message msg = Message.obtain();

        msg.what = MessageFragment.GOOGLE_SERVICE_FAIL;
        msg.getData().putInt(ERROR_CODE_KEY, connectionResult.getErrorCode());

        BroadcastMessageService.sendBroadcastMessage(mContext, msg);
    }

    public void getLastLocation() {
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        disconnect();
        mGoogleApiClient.connect();
    }


}
