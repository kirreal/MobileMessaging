package com.demo.kirreal.mobilemessaging.message;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InfobipResponseMessageParser {
    public static List<InfobipResponse> parse(String jsonString) {
        List<InfobipResponse> responseList = new ArrayList<InfobipResponse>();

        try {
            JSONObject messages = new JSONObject(jsonString);

            JSONArray responses = messages.getJSONArray("messages");

            for (int i = 0; i < responses.length(); i++) {
                JSONObject json = responses.getJSONObject(i);
                responseList.add(parseJSON(json));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseList;
    }

    private static InfobipResponse parseJSON(final JSONObject json) {
        InfobipResponse response = new InfobipResponse();

        try {
            response.setMessageId(json.getString("messageId"));
            response.setTo(json.getString("to"));
            response.setStatus(json.getJSONObject("status").getString("groupName"));
        } catch (JSONException e) {
            Log.e("Parsing InfobipResponse", e.getMessage());
        }
        return response;
    }
}
