package com.servers;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Optional;

public class MainPublisher {
  public static void main(String[] args)
      throws NamingException, JMSException, InterruptedException {

    System.out.println("Waiting for the users....\n");
    Thread.sleep(1000);

    /* Created the context with pointing the two topics to jndi.properties */
    InitialContext initialContext = new InitialContext();
    Topic sentTopic = (Topic) initialContext.lookup("topic/msgSentTopic");
    Topic receivedTopic = (Topic) initialContext.lookup("topic/msgReceivedTopic");

    /* created a connection , session and all in jmsContext */
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    JMSContext jmsContext = connectionFactory.createContext();
    System.out.println("Connection established");

    /* created a Text Message, can made an object message too */
    TextMessage textMessage = createMessage(jmsContext);

    /* created a producer to send messages to all subscribers for confirming the connection and starting the chat */
    jmsContext.createProducer().send(sentTopic, textMessage);
    jmsContext.close();
    System.out.println("Chat Room Created\n");

    /*
        Next loop is to take continuous messages from the users which means that the the user side must be creating some
        producer to send message here, therefore we have to make the consumer here which can show all the caht messages.
    */

    while (true) {
      JMSContext jmsContextCon = connectionFactory.createContext();
      try {
        JMSConsumer chatConsumer = jmsContextCon.createConsumer(receivedTopic);
        TextMessage chatReceive = (TextMessage) chatConsumer.receive(2000);
        Optional.ofNullable(chatReceive)
            .ifPresent(
                msg -> {
                  try {
                    System.out.println(msg.getText());
                  } catch (JMSException e) {
                    System.out.println(e);
                  }
                });
        /*
            Can create an extra consumer for load balancing. just uncomment the code from next line.

                JMSConsumer chatConsumer1 = jmsContextCon.createConsumer(receivedTopic);
                TextMessage chatReceive1 = (TextMessage) chatConsumer1.receive(2000);
                System.out.println(chatReceive1.getText());
        */
      } catch (Exception ex) {
        System.out.println(ex);
      }
      jmsContext.close();
    }
  }

  private static TextMessage createMessage(JMSContext jmsContext) throws JMSException {
    TextMessage textMessage = jmsContext.createTextMessage("Let the Chat Begins : ");
    textMessage.setBooleanProperty("Active", true);
    return textMessage;
  }
}
