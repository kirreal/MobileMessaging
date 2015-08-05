package com.demo.kirreal.mobilemessaging.message;

public class InfobipResponse {

    private String to;
    private String messageId;
    private String status;

    public static String getMockString() {
        return  "{\"messages\":[{\"to\":\"41793026727\",\"status\":{\"groupId\":0,\"groupName\":\"ACCEPTED\",\"id\":0,\"name\":\"MESSAGE_ACCEPTED\",\"description\":\"Message accepted\"},\"smsCount\":1,\"messageId\":\"2250be2d4219-3af1-78856-aabe-1362af1edfd2\"}]}";
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
