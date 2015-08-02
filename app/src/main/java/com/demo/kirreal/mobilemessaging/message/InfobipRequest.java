package com.demo.kirreal.mobilemessaging.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kirreal on 02.08.2015.
 */
public class InfobipRequest {
    private String from;
    private int to;
    private String text;

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("from", getFrom());
        json.put("to", getTo());
        json.put("text", getText());

        return json.toString();
    }

}