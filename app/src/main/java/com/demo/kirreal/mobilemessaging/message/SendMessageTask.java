package com.demo.kirreal.mobilemessaging.message;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kirreal on 02.08.2015.
 */
public class SendMessageTask extends AsyncTask<InfobipRequest, Void, String> {
    private final String SINGLE_MESSAGE_URL = "https://api.infobip.com/sms/1/text/single";

    @Override
    protected String doInBackground(InfobipRequest... params) {
        URL url = null;
        String response = "";
        InfobipRequest message = params[0];

        try {
            url = new URL(SINGLE_MESSAGE_URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(message.toJSONString());
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
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
    }
}
