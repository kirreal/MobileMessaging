package com.demo.kirreal.mobilemessaging.message;

import org.json.JSONException;
import org.json.JSONObject;

public class InfobipRequest {
    private String from;
    private String to;
    private String text;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
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

    public String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("from", getFrom());
        json.put("to", getTo());
        json.put("text", getText());

        return json.toString();
    }

}
