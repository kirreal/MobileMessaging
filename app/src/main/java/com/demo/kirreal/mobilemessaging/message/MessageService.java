package com.demo.kirreal.mobilemessaging.message;

/**
 * Created by kirreal on 02.08.2015.
 */
public class MessageService {

    void sendMessage(final InfobipRequest message) {
        SendMessageTask sendMessageTask = new SendMessageTask();

        sendMessageTask.execute(message);
    }
}
