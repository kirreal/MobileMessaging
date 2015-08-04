package com.demo.kirreal.mobilemessaging.util;

import android.util.Log;

import com.demo.kirreal.mobilemessaging.message.SendMessageException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.KeyStore;

public class HttpsResponseSender {
    private static final String USER_NAME = "kirreal";
    private static final String PASSWORD = "kirreal";


    public String sendPostJSONString(String targetURL, String jsonString) throws Exception {
        String responseString = "";
        DefaultHttpClient httpClient = getNewHttpClient();
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(targetURL);
        HttpPost postMethod = new HttpPost(newURI);
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(targetURL, 443),
                new UsernamePasswordCredentials(USER_NAME, PASSWORD));

        postMethod.setEntity(new StringEntity(jsonString));
        postMethod.setHeader("Content-Type", "application/json");
        response = httpClient.execute(postMethod);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
            Log.d("", EntityUtils.toString(response.getEntity()));
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } else {
            String msg = statusLine.getStatusCode() + " " + statusLine.getReasonPhrase();

            throw new SendMessageException(msg);
        }

        return responseString;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();

        return sb.toString();
    }

    public DefaultHttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
