package com.servers;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MainPublisher {
  public static void main(String[] args)
      throws NamingException, JMSException, InterruptedException {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    InitialContext initialContext = new InitialContext();
    Topic sentTopic = (Topic) initialContext.lookup("topic/msgSentTopic");
    Topic receivedTopic = (Topic) initialContext.lookup("topic/msgReceivedTopic");

    JMSContext jmsContext = connectionFactory.createContext();
    System.out.println("Connection established");
    TextMessage textMessage = jmsContext.createTextMessage("Chat Begins : ");
    textMessage.setBooleanProperty("Active", true);
    jmsContext.createProducer().send(sentTopic, textMessage);
    jmsContext.close();

    JMSContext jmsContextCon = connectionFactory.createContext();
    try {
      JMSConsumer chatConsumer = jmsContextCon.createConsumer(receivedTopic);
      TextMessage chatReceive = (TextMessage) chatConsumer.receive();
      System.out.println(chatReceive.getText());

      JMSConsumer chatConsumer1 = jmsContextCon.createConsumer(receivedTopic);
      TextMessage chatReceive1 = (TextMessage) chatConsumer1.receive();
      System.out.println(chatReceive1.getText());
    } catch (Exception ex) {
      System.out.println(ex);
    }
    jmsContext.close();
  }
}
