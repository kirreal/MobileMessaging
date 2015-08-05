package com.demo.kirreal.mobilemessaging.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import com.demo.kirreal.mobilemessaging.message.InfobipResponse;
import com.demo.kirreal.mobilemessaging.message.InfobipResponseMessageParser;
import com.demo.kirreal.mobilemessaging.util.BroadcastMessageService;
import com.demo.kirreal.mobilemessaging.util.HttpsResponseSender;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.List;

public class MessageFragment extends Fragment {
    public static final String TAG = "MessageFragment";
    public static final String PHONE_NUMBER_KEY = "phone_number";
    public static final String MESSAGE_KEY = "message";
    public static final String AUTHOR_KEY = "author";
    private LocationService mLocationService;
    private EditText mAuthor;
    private EditText mMessage;
    private EditText mPhoneNumber;
    private Button mSendButton;
    public static final int LOCATION_SERVICE_SUCCESS = 1;
    public static final int GOOGLE_SERVICE_FAIL = 2;
    public static final int SERVICE_FAIL = 3;
    public static final int TRANSMIT_FAIL = 4;
    public static final int TRANSMIT_SUCCESS = 5;

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
            case TRANSMIT_FAIL:
                String error = messageData.getString(LocationService.ERROR_MESSAGE_KEY);

                onSendMessageFail(error);
                break;
            case TRANSMIT_SUCCESS:
                onSendMessageSuccess((InfobipResponse) message.obj);
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
                AuthorFieldValidator authorValidator =
                        new AuthorFieldValidator(getActivity(), mAuthor.getText().toString());
                boolean isAuthorValid = authorValidator.validate();
                PhoneNumberFieldValidator phoneValidator =
                        new PhoneNumberFieldValidator(getActivity(), mPhoneNumber.getText().toString());
                boolean isPhoneValid = phoneValidator.validate();

                if (isAuthorValid && isPhoneValid) {
                    startProgress();
                    mLocationService.getLastLocation();
                } else {
                    if (!isAuthorValid) {
                        mAuthor.setError(authorValidator.getError());
                    }

                    if (!isPhoneValid) {
                        mPhoneNumber.setError(phoneValidator.getError());
                    }
                }
            }
        };
    }

    public void onLocationRetrieveSuccess(String location) {
        String messageBody = buildMessage(location);

        mMessage.setText(messageBody);
        InfobipRequest infobipMessage = new InfobipRequest();

        infobipMessage.setFrom(mAuthor.getText().toString());
        infobipMessage.setTo(mPhoneNumber.getText().toString());
        infobipMessage.setText(messageBody);

        new SendMessageTask().execute(infobipMessage);
    }

    private String buildMessage(String location) {
        String message = new String();

        message += getString(R.string.message_prefix) + " " + mAuthor.getText() + ". ";
        message += getString(R.string.location_message_prefix);
        message += " " + location;

        return message;
    }

    public void onSendMessageSuccess(InfobipResponse response) {
        OkAlertDialog dialog = new OkAlertDialog(this, R.string.send_message_success, response.getStatus());

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


    private void onSendMessageFail(String errorMessage) {
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

    public class SendMessageTask extends AsyncTask<Object, Void, String> {
        private final String SINGLE_MESSAGE_URL = "https://api.infobip.com/sms/1/text/single";

        @Override
        protected String doInBackground(Object... params) {
            String response = "";
            InfobipRequest message = (InfobipRequest) params[0];
            HttpsResponseSender responseSender = new HttpsResponseSender();

            try {
                response = responseSender.sendPostJSONString(SINGLE_MESSAGE_URL, message.toJSONString());
            } catch (Exception e) {
                transmitFailed(e);
            }

            return response;
        }

        private void transmitFailed(Exception e) {
            Message msg = Message.obtain();

            msg.what = TRANSMIT_FAIL;
            msg.getData().putString(LocationService.ERROR_MESSAGE_KEY, e.getMessage());

            BroadcastMessageService.sendBroadcastMessage(getActivity(), msg);
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (!response.equals("")) {
                List<InfobipResponse> infobipResponses = InfobipResponseMessageParser.parse(response);

                if (infobipResponses.size() > 0) {
                    InfobipResponse infobipMessage = infobipResponses.get(0);
                    Message msg = Message.obtain();

                    msg.what = TRANSMIT_SUCCESS;
                    msg.obj = infobipMessage;

                    BroadcastMessageService.sendBroadcastMessage(getActivity(), msg);
                    return;
                }
            }
            transmitFailed(new RuntimeException("Server response is empty"));
        }
    }
}
