package com.demo.kirreal.mobilemessaging.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.demo.kirreal.mobilemessaging.R;
import com.demo.kirreal.mobilemessaging.location.LocationService;
import com.demo.kirreal.mobilemessaging.message.InfobipRequest;
import com.demo.kirreal.mobilemessaging.message.MessageService;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.concurrent.ExecutionException;

/**
 * Created by kmoshias on 29.07.2015.
 */
public class MessageFragment extends Fragment {
    public static final String TAG = "MessageFragment";
    public static final String PHONE_NUMBER_KEY = "phone_number";
    public static final String MESSAGE_KEY = "message";
    public static final java.lang.String AUTHOR_KEY = "author";
    private LocationService mLocationService;
    private ProgressDialog mProgress;
    private EditText mAuthor;
    private EditText mMessage;
    private EditText mPhoneNumber;
    private Button mSendButton;
    public static final int LOCATION_SERVICE_SUCCESS = 1;

    public static final int GOOGLE_SERVICE_FAIL = 2;
    public static final int SERVICE_FAIL = 3;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleReceiverResult(intent);
        }
    };

    private void handleReceiverResult(Intent intent) {
        Message message = (Message) intent.getExtras().get(LocationService.BROADCAST_MESSAGE_KEY);
        Bundle messageData = message.getData();

        switch (message.what) {
            case LOCATION_SERVICE_SUCCESS:
                String address = messageData.getString(LocationService.ADDRESS_KEY);

                onLocationRetrieveSuccess(address);
                break;
            case GOOGLE_SERVICE_FAIL:
                int errorCode = messageData.getInt(LocationService.ERROR_CODE_KEY);

                onGoogleServiceFail(errorCode);
                break;
            case SERVICE_FAIL:
                String errorMessage = messageData.getString(LocationService.ERROR_MESSAGE_KEY);

                onServiceFail(errorMessage);
                break;
       }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationService = new LocationService(getActivity());
        IntentFilter intentFilter = new IntentFilter(LocationService.ADRESS_RESOLVED);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        broadcastManager.registerReceiver(mMessageReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_layout, container, false);

        mAuthor = (EditText) view.findViewById(R.id.author);
        mPhoneNumber = (EditText) view.findViewById(R.id.phoneNumber);
        mMessage = (EditText) view.findViewById(R.id.message);
        mSendButton = (Button) view.findViewById(R.id.send_button);

        mSendButton.setOnClickListener(getSendButtonCallBack());
        recoverLastState(savedInstanceState);

        return view;
    }

    private View.OnClickListener getSendButtonCallBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();
                mLocationService.getLastLocation();
            }
        };
    }

    public void onLocationRetrieveSuccess(String location) {
        String messageBody = buildMessage(location);

        mMessage.setText(messageBody);
        MessageService messageService = new MessageService();
        InfobipRequest infobipMessage = new InfobipRequest();

        infobipMessage.setFrom(mAuthor.getText().toString());
        infobipMessage.setTo(Long.parseLong(mPhoneNumber.getText().toString()));
        infobipMessage.setText(messageBody);

        try {
            messageService.sendMessage(this, infobipMessage);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String buildMessage(String location) {
        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.message_prefix));
        sb.append(mAuthor.getText());
        sb.append(".");
        sb.append(getString(R.string.location_message_prefix));
        sb.append(" ");
        sb.append(location);

        return sb.toString();
    }

    public void onSendMessageSuccess(String response) {
        OkAlertDialog dialog = new OkAlertDialog(this, R.string.send_message_fail, response);

        dialog.show(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        reset();
        stopProgress();
    }

    public void onGoogleServiceFail(int errorCode) {
        stopProgress();
        GooglePlayServicesUtil.getErrorDialog(errorCode, getActivity(), R.string.get_location_fail).show();
    }

    public void onServiceFail(String errorMessage) {
        OkAlertDialog dialog = new OkAlertDialog(this, R.string.send_message_fail, errorMessage);
        stopProgress();

        dialog.show(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void recoverLastState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String message = savedInstanceState.getString(MESSAGE_KEY);
            String phoneNumber = savedInstanceState.getString(PHONE_NUMBER_KEY);
            String author = savedInstanceState.getString(AUTHOR_KEY);

            mAuthor.setText(author);
            mPhoneNumber.setText(phoneNumber);
            mMessage.setText(message);
        }
    }

    private void reset() {
        mMessage.setText("");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(AUTHOR_KEY, mAuthor.getText().toString());
        savedInstanceState.putString(PHONE_NUMBER_KEY, mPhoneNumber.getText().toString());
        savedInstanceState.putString(MESSAGE_KEY, mMessage.getText().toString());
    }

    private void startProgress() {
        MainActivity activity = (MainActivity) getActivity();

        activity.startProgress();
        mSendButton.setEnabled(false);
        mAuthor.setEnabled(false);
        mPhoneNumber.setEnabled(false);
        mMessage.setEnabled(false);
    }

    private void stopProgress() {
        MainActivity activity = (MainActivity) getActivity();

        activity.stopProgress();
        mSendButton.setEnabled(true);
        mAuthor.setEnabled(true);
        mPhoneNumber.setEnabled(true);
        mMessage.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationService.disconnect();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }
}
