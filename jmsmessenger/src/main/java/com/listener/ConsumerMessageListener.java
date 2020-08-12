package com.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerMessageListener implements MessageListener {

  @Override
  public void onMessage(Message message) {
    try {

      TextMessage receiveText = (TextMessage) message;
      if (receiveText.getBooleanProperty("Active")) {
        System.out.println("\nConnected\n" + receiveText.getText() + "\n");
        Thread.sleep(1000);
      }

    } catch (Exception ex) {
      System.out.println(ex);
    }
  }
}
