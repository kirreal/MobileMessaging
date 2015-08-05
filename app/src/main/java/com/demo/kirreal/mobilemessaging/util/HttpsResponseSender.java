package com.demo.kirreal.mobilemessaging.util;

import android.util.Base64;
import android.util.Log;

import com.demo.kirreal.mobilemessaging.message.SendMessageException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;

public class HttpsResponseSender {
    private static final String USER_NAME = "kirreal";
    private static final String PASSWORD = "kirreal";


    public String sendPostJSONString(String targetURL, String jsonString) throws Exception {
        String responseString = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(targetURL);
        HttpPost postMethod = new HttpPost(newURI);

        setHeaders(postMethod);
        postMethod.setEntity(new StringEntity(jsonString, "UTF-8"));
        response = httpClient.execute(postMethod);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
            responseString = EntityUtils.toString(response.getEntity());
            Log.d("", responseString);
        } else {
            String msg = statusLine.getStatusCode() + " " + statusLine.getReasonPhrase();

            throw new SendMessageException(msg);
        }

        return responseString;
    }

    private void setHeaders(HttpPost postMethod) throws UnsupportedEncodingException {
        byte[] data = (USER_NAME + ":" + PASSWORD).getBytes("UTF-8");
        String base64 = Base64.encodeToString(data, Base64.URL_SAFE|Base64.NO_WRAP);

        postMethod.setHeader("Authorization", "Basic " + base64);
        postMethod.setHeader("Content-Type", "application/json");
        postMethod.setHeader("Accept", "application/json");
    }
}
