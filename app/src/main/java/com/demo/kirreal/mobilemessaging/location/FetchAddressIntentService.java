package com.demo.kirreal.mobilemessaging.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.demo.kirreal.mobilemessaging.R;
import com.demo.kirreal.mobilemessaging.ui.MessageFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kmoshias on 31.07.2015.
 */
public class FetchAddressIntentService extends IntentService {
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        Location location = intent.getParcelableExtra(LocationService.LOCATION_DATA_EXTRA);

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
                    LocationService.GEOCODING_ADDREESS_LIMIT);
        } catch (IOException ioException) {
            errorMessage = getString(R.string.location_service_not_available);
            Log.e("onHandleIntent", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e("onHandleIntent", errorMessage + ". " + "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }

        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e("onHandleIntent", errorMessage);
            }
            Message msg = new Message();

            msg.what = MessageFragment.SERVICE_FAIL;
            msg.getData().putString(LocationService.ADDRESS_KEY, errorMessage);
            LocationService.sendBroadcastMessage(this, msg);
        } else {
            Message msg = new Message();

            msg.what = MessageFragment.LOCATION_SERVICE_SUCCESS;
            msg.getData().putString(LocationService.ADDRESS_KEY, getAddressString(addresses.get(0)));
            LocationService.sendBroadcastMessage(this, msg);
        }
    }

    private String getAddressString(Address address) {
        StringBuilder sb = new StringBuilder();

        sb.append(address.getLocality());
        sb.append(" ");
        sb.append(address.getThoroughfare());
        sb.append(" ");
        sb.append(address.getSubThoroughfare());

        return sb.toString();
    }

}
