package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) throws JMSException {
		
		ApplicationContext ctx =  SpringApplication.run(DemoApplication.class, args);
		
		QueueMessageConsumer queueMsgListener = new QueueMessageConsumer(Constants.TEST_BROKER_URL, Constants.ADMIN,
                Constants.ADMIN);

	    queueMsgListener.setDestinationName(Constants.TEST_QUEUE);
		    
	        try {
	            queueMsgListener.run();
	 
	        } catch (JMSException e) {
	            e.printStackTrace();
	        }
		
		}
        
 
//		ApplicationContext ctx =  SpringApplication.run(DemoApplication.class, args);
//		
//		JmsTemplate jms = ctx.getBean(JmsTemplate.class);
//		
//		HashMap<String, Object> allMessages =   new GetAllMessages().getMessages();
//		
//		
//		for ( Map.Entry<String, Object> entry : allMessages.entrySet()) {
//		    String key = entry.getKey();
//		    Object message = entry.getValue();
//		    
//		    jms.convertAndSend(key, message);
//		
//		}
//		
		
//		jms.convertAndSend("James", allMessages.get("James"));
//		jms.convertAndSend("New Year", allMessages.get("New Year"));
//		jms.convertAndSend("Singh ji", allMessages.get("Singh ji"));
//		jms.convertAndSend("Joseph", allMessages.get("Joseph"));
//		jms.convertAndSend("Katrina", allMessages.get("Katrina"));
//		jms.convertAndSend("Jorden", allMessages.get("Jorden"));
//		jms.convertAndSend("Gagan", allMessages.get("Gagan"));
//		jms.convertAndSend("Kamal", allMessages.get("Kamal"));
//		jms.convertAndSend("Suresh", allMessages.get("Suresh"));
//		jms.convertAndSend("kajal", allMessages.get("kajal"));
//		jms.convertAndSend("Farhan", allMessages.get("Farhan"));
//		jms.convertAndSend("Google", allMessages.get("Google"));
//		jms.convertAndSend("Facebook", allMessages.get("Facebook"));
//		jms.convertAndSend("Havana", allMessages.get("Havana"));
//		
		

}
