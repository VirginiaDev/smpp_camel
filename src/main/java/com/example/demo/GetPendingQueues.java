package com.example.demo;

import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;

public class GetPendingQueues {
	 	private String activeMqBrokerUri;
	    private String username;
	    private String password;
	 
	    private ActiveMQSession session;
	    private MessageProducer msgProducer;
	    private ConnectionFactory connFactory;
	    private ActiveMQConnection connection;
	
	public void getAllPendingQueues() throws JMSException {
		  connFactory = new ActiveMQConnectionFactory(Constants.ADMIN, Constants.ADMIN,
				  Constants.TEST_BROKER_URL);
	        connection = (ActiveMQConnection)connFactory.createConnection();
	        connection.start();
	        session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        connection.start();
	        
	   	 DestinationSource ds = connection.getDestinationSource();
	        Set<ActiveMQQueue> queues = ds.getQueues();
	       for(ActiveMQQueue queue : queues){
      	  try {
      	    Destination destination = session.createQueue(queue.getQueueName());
      	    MessageConsumer consumer = session.createConsumer(destination);
      	    
      	    Message message = consumer.receive(2000);
      	    if (message == null){
      	    	continue;
      	    }

      	    if (message instanceof TextMessage) {
      	        TextMessage textMessage = (TextMessage) message;
      	        System.out.println("Incomming Message: '" + textMessage.getText() + "'");
      	    }
      	  } catch (JMSException e) {
      	    e.printStackTrace();
      	  }
	}

	}
}
