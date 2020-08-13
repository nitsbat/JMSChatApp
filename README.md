# JMSChatApp
Simple Chat Application using JMS 2.x artemis client. 

## Project still In progress

* Step 1 - clone the repository https://github.com/nitsbat/JMSChatApp.git
* Step 2 - build it : mvn clean install
* Step 3 - change your jndi properties from connectionFactory.ConectionFactory=tcp://192.168.137.1:61616 to whatever port your broker is running on
* Step 4 - Run Consumer1.java and then run MainPublisher.java
* Step 5 - Start chatting in the console of Consumer1.java and see the chat in MainPublisher.java class console.
