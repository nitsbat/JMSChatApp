package com.clients;

import com.model.User;
import com.util.ChatUtilities;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Consumer1 {
  public static void main(String[] args)
      throws NamingException, JMSException, InterruptedException {
    System.out.println("Connecting.....");

    /* Created the context with pointing the two topics to jndi.properties */

    InitialContext initialContext = new InitialContext();
    Topic sentTopic = (Topic) initialContext.lookup("topic/msgSentTopic");
    Topic receivedTopic = (Topic) initialContext.lookup("topic/msgReceivedTopic");

    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    JMSContext jmsContext = connectionFactory.createContext();

    JMSConsumer consumer = jmsContext.createConsumer(sentTopic);
    TextMessage receiveText = (TextMessage) consumer.receive();
    if (receiveText.getBooleanProperty("Active")) {
      System.out.println("\nConnected\n" + receiveText.getText() + "\n");
      Thread.sleep(1000);
    }
    jmsContext.close();
    while (true) {
      JMSContext jmsContextCon = connectionFactory.createContext();
      try {
        JMSProducer chatProducer = jmsContextCon.createProducer();
        User user = new User("Nitin");
        user.setColorCode(ChatUtilities.ANSI_CYAN);
        System.out.print("Comment please - ");
        Scanner sc = new Scanner(System.in);
        String chatMessage = sc.nextLine();
        TextMessage textMessage;
        if (chatMessage.equalsIgnoreCase("clear")) {
          ChatUtilities.clearScreen();
          textMessage =
              jmsContextCon.createTextMessage(
                  user.getColorCode()
                      + user.getName()
                      + " : "
                      + "cleared my console. Its a mess"
                      + ChatUtilities.ANSI_RESET);
        } else {
          textMessage =
              jmsContextCon.createTextMessage(
                  user.getColorCode()
                      + user.getName()
                      + " : "
                      + chatMessage
                      + ChatUtilities.ANSI_RESET);
        }
        chatProducer.send(receivedTopic, textMessage);
        //          System.out.println("Message Sent");
      } catch (Exception e) {
        System.out.println(e);
      }
      jmsContextCon.close();
    }
  }
}
