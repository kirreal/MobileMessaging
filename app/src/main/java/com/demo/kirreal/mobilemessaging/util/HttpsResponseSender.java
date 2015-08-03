package com.demo.kirreal.mobilemessaging.util;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kmoshias on 03.08.2015.
 */
public class HttpsResponseSender {

    public String sendPost(String targetURL, String jsonString) throws IOException {
        HttpsURLConnection connection = null;
        URL url = new URL(targetURL);

        try {
            connection = (HttpsURLConnection) url.openConnection();
            sendRequest(jsonString, connection);

            return getResponse(connection);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();

        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        return response.toString();
    }

    private void sendRequest(String jsonString, HttpURLConnection connection) throws IOException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(URLEncoder.encode(jsonString, "UTF-8"));
        wr.flush ();
        wr.close ();
    }
}
