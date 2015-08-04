package com.demo.kirreal.mobilemessaging.message;

import android.os.AsyncTask;

import com.demo.kirreal.mobilemessaging.ui.MessageFragment;
import com.demo.kirreal.mobilemessaging.util.HttpsResponseSender;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

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
            response = responseSender.sendPostJSONString(SINGLE_MESSAGE_URL, message.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
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
