package com.clients;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer1 {
  public static void main(String[] args)
      throws NamingException, JMSException, InterruptedException {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    InitialContext initialContext = new InitialContext();

    Topic sentTopic = (Topic) initialContext.lookup("topic/msgSentTopic");
    Topic receivedTopic = (Topic) initialContext.lookup("topic/msgReceivedTopic");

    while (true) {
      JMSContext jmsContext = connectionFactory.createContext();

      JMSConsumer consumer = jmsContext.createConsumer(sentTopic);
      TextMessage receiveText = (TextMessage) consumer.receive();
      jmsContext.close();
      Thread.sleep(500);
      JMSContext jmsContextCon = connectionFactory.createContext();
      try {
        if (receiveText.getBooleanProperty("Active")) {
          System.out.println(receiveText.getText());
          JMSProducer chatProducer = jmsContextCon.createProducer();
          TextMessage textMessage = jmsContextCon.createTextMessage(" Hi everyone..!!");
          chatProducer.send(receivedTopic, textMessage);
          System.out.println("Message Sent");
        }
      } catch (Exception e) {
        System.out.println(e);
      }
      jmsContextCon.close();
    }
  }
}
