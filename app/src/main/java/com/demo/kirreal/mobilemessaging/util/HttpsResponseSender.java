package com.demo.kirreal.mobilemessaging.util;

import android.app.Application;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;

import com.demo.kirreal.mobilemessaging.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import static com.demo.kirreal.mobilemessaging.R.*;

/**
 * Created by kmoshias on 03.08.2015.
 */
public class HttpsResponseSender {
    private static final int DEFAULT_TIMEOUT = 15000;

    public String sendPostJSONString(String targetURL, String jsonString) throws IOException,
            KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException,
            CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream certificateFileInput = Resources.getSystem().openRawResource(raw.infobip);
        Certificate certificate;

        try {
            certificate = cf.generateCertificate(certificateFileInput);
        } finally {
            certificateFileInput.close();
        }
        HttpsURLConnection connection = null;
        URL url = new URL(targetURL);
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        trustStore.setCertificateEntry("ca", certificate);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(trustStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(context.getSocketFactory());
            sendRequest(jsonString, connection);

            return getResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void sendRequest(String jsonString, HttpsURLConnection connection) throws IOException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setConnectTimeout(DEFAULT_TIMEOUT);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(URLEncoder.encode(jsonString, "UTF-8"));
        wr.flush();
        wr.close();
    }

    private String getResponse(HttpsURLConnection connection) throws IOException {
        String responseMessage = "";

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream in = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            responseMessage = response.toString();
        } else {
            responseMessage = "Server error " + connection.getResponseCode() + " " + connection.getResponseMessage();
        }

        return responseMessage;
    }

}
