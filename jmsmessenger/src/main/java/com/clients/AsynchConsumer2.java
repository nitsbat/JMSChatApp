package com.clients;

import com.listener.ConsumerMessageListener;
import com.model.User;
import com.util.ChatUtilities;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class AsynchConsumer2 {
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
    int count = 0;
    while (true) {
      consumer.setMessageListener(new ConsumerMessageListener());
      if (count == 0) {
        Thread.sleep(10000);
      }
      count++;

      JMSProducer chatProducer = jmsContext.createProducer();

      System.out.print("Comment please - ");
      Scanner sc = new Scanner(System.in);
      String chatMessage = sc.nextLine();
      User user = new User("Akash");
      user.setColorCode(ChatUtilities.ANSI_YELLOW);
      user.setChatText(chatMessage);
      ObjectMessage objMessage;

      if (chatMessage.equalsIgnoreCase("bye")
          || chatMessage.equalsIgnoreCase("quit")
          || chatMessage.equalsIgnoreCase("exit")) {
        objMessage = jmsContext.createObjectMessage(user);
        chatProducer.send(receivedTopic, objMessage);
        jmsContext.close();
        break;
      } else {
        objMessage = jmsContext.createObjectMessage(user);
        chatProducer.send(receivedTopic, objMessage);
      }
    }
  }
}
