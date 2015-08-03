package com.demo.kirreal.mobilemessaging.message;

import com.demo.kirreal.mobilemessaging.ui.MessageFragment;

import java.util.concurrent.ExecutionException;

/**
 * Created by kirreal on 02.08.2015.
 */
public class MessageService {
    private MessageFragment mFragment;

    public void sendMessage(final MessageFragment mFragment, final InfobipRequest message)
            throws ExecutionException, InterruptedException {
        SendMessageTask sendMessageTask = new SendMessageTask();

        sendMessageTask.execute(mFragment, message);
    }
}
