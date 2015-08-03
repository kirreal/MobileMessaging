package com.demo.kirreal.mobilemessaging.message;

import android.os.AsyncTask;

import com.demo.kirreal.mobilemessaging.ui.MessageFragment;
import com.demo.kirreal.mobilemessaging.util.HttpsResponseSender;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by kirreal on 02.08.2015.
 */
public class SendMessageTask extends AsyncTask<Object, Void, String> {
    private final String SINGLE_MESSAGE_URL = "https://api.infobip.com/sms/1/text/single";
    private MessageFragment mFragment;

    @Override
    protected String doInBackground(Object... params) {
        String response = "";
        mFragment = (MessageFragment) params[0];
        InfobipRequest message = (InfobipRequest) params[1];
        HttpsResponseSender responseSender = new HttpsResponseSender();

        try {
            response = responseSender.sendPost(SINGLE_MESSAGE_URL, message.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        mFragment.onSendMessageSuccess(response);
    }
}
