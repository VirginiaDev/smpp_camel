package com.example.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.pool.PooledConnectionFactory;

import com.example.demo.dto.Clients;

public class QueueMessageConsumer implements MessageListener{

	private String activeMqBrokerUri;
	    private String username;
	    private String password;
	    private String destinationName;
	    private ActiveMQConnection connection;
	 
	    public QueueMessageConsumer(String activeMqBrokerUri, String username, String password) {
	        super();
	        this.activeMqBrokerUri = activeMqBrokerUri;
	        this.username = username;
	        this.password = password;
	    }
	    
	    
	    class Sender implements Runnable {

	        private final Connection connection;
	        Destination destination;

	        Sender(Connection connection) {
	        	System.out.println("1111111==========================");
	            this.connection = connection;
	        }

	        public void run() {
	            try {
	            	connection.start();
	                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	                System.out.println("22222222===========================================");
	                for (int i = 0; i < 20; i++) {
	                	Clients client = new ClientManager().getPendingMessages();
	                	if(client.getSender_details()==null) {
	                		System.out.println("=======================No New Message Found======================");
	                	} else {
                				Destination d = session.createQueue(client.getSender_details());
	                			
			                	MessageConsumer consumer =  session.createConsumer(d);
			                	 Message message = consumer.receive(2000);
			     	        	    if (message == null){
			     	        	    	//new GetPendingQueues().getAllPendingQueues();
			     	        	    }

			     	        	    if (message instanceof TextMessage) {
			     	        	        TextMessage textMessage = (TextMessage) message;
			     	        	        System.out.println("Incomming Message: '" + textMessage.getText() + "'");  
			     	        	       new sendMessageOnRoute().sendMessage(textMessage.getText(), client);
			     	        	    }
	                			
	                		}
	                		
	                	
//	                	 DestinationSource ds = connection.getDestinationSource();
//	         	        Set<ActiveMQQueue> queues = ds.getQueues();
//	         	       for(ActiveMQQueue queue : queues){
//	     	        	  try {
//	     	        	    System.out.println(queue.getQueueName());
//	     	        	    Destination destination = session.createQueue(queue.getQueueName());
//	     	        	    MessageConsumer consumer = session.createConsumer(destination);
//	     	        	    
//	     	        	    Message message = consumer.receive(2000);
//	     	        	    if (message == null){
//	     	        	    	continue;
//	     	        	    }
//
//	     	        	    if (message instanceof TextMessage) {
//	     	        	        TextMessage textMessage = (TextMessage) message;
//	     	        	        System.out.println("Incomming Message: '" + textMessage.getText() + "'");
//	     	        	    }
//	     	        	  } catch (JMSException e) {
//	     	        	    e.printStackTrace();
//	     	        	  }
//	     	        	}
	                }

	                session.close();
	                connection.close();
	            } catch(Exception ex) {
	                System.out.println("Run caught exception: " + ex.getMessage());
	            } finally {
	            }
	        }
	    }
	 
	    public void run() throws JMSException {
	    	
	        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(username, password, activeMqBrokerUri);
    	    PooledConnectionFactory pooledFactory = new PooledConnectionFactory(factory);
	    	ExecutorService service = Executors.newFixedThreadPool(10);

	    	for (int i = 0; i < 100; ++i) {
	    		
	    	    service.execute(new Sender(pooledFactory.createConnection()));
	    	    
    	    }
	    	    
//	        System.out.println("Sessio=============================================="+session);
//	        Destination destination = session.createQueue(destinationName);
//	        System.out.println("Destination=============================================="+destination);
//	        MessageConsumer consumer = session.createConsumer(destination);
//	        System.out.println("consumer=============================================="+consumer);
//	        consumer.setMessageListener(this);
//	 
//	        System.out.println(String.format("QueueMessageConsumer Waiting for messages at queue='%s' broker='%s'",
//	                destinationName, this.activeMqBrokerUri));
	        
//	        DestinationSource ds = connection.getDestinationSource();
//	        Set<ActiveMQQueue> queues = ds.getQueues();
//	        for(ActiveMQQueue queue : queues){
//	        	  try {
//	        	    System.out.println(queue.getQueueName());
//	        	    Destination destination = session.createQueue(queue.getQueueName());
//	        	    MessageConsumer consumer = session.createConsumer(destination);
//	        	    
//	        	    Message message = consumer.receive(2000);
//	        	    if (message == null){
//	        	    	continue;
//	        	    }
//
//	        	    if (message instanceof TextMessage) {
//	        	        TextMessage textMessage = (TextMessage) message;
//	        	        System.out.println("Incomming Message: '" + textMessage.getText() + "'");
//	        	    }
//	        	  } catch (JMSException e) {
//	        	    e.printStackTrace();
//	        	  }
//	        	}
	        
	    }
	 

	@Override
	public void onMessage(Message message) {
		 try {
	            String filename = message.getStringProperty(Constants.FILE_NAME);
	 
	            Instant start = Instant.now();
	 
	            if (message instanceof ActiveMQTextMessage) {
	                handleTextMessage((ActiveMQTextMessage) message);
	                
	            } else if (message instanceof ActiveMQBlobMessage) {
	                handleBlobMessage((ActiveMQBlobMessage) message, filename);
	            } else if (message instanceof ActiveMQBytesMessage) {
	                handleBytesMessage((ActiveMQBytesMessage) message, filename);
	            } else {
	                System.out.println("test");
	            }
	 
	            Instant end = Instant.now();
	            System.out.println("Consumed message with filename [" + filename + "], took " + Duration.between(start, end));
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	private void handleTextMessage(ActiveMQTextMessage txtMessage) throws JMSException {
        String msg = String.format(txtMessage.getText());
        System.out.println(msg);
        //new sendMessageOnRoute().sendMessage(msg);
    }
	
	 private void handleBytesMessage(ActiveMQBytesMessage bytesMessage, String filename)
	            throws IOException, JMSException {
	        String outputfileName = Constants.FILE_OUTPUT_BYTE_DIRECTORY + filename;
//	        fileManager.writeFile(bytesMessage.getContent().getData(), outputfileName);
	        System.out.println("Received ActiveMQBytesMessage message");
	    }
	 
	    private void handleBlobMessage(ActiveMQBlobMessage blobMessage, String filename)
	            throws FileNotFoundException, IOException, JMSException {
	        // for 1mb or bigger message
	        String outputfileName = Constants.FILE_OUTPUT_BLOB_DIRECTORY + filename;
	        InputStream in = blobMessage.getInputStream();
//	        fileManager.writeFile(IOUtils.toByteArray(in), outputfileName);
	        System.out.println("Received ActiveMQBlobMessage message");
	    }
	    
	    public String getDestinationName() {
	        return destinationName;
	    }
	 
	    public void setDestinationName(String destinationName) {
	        this.destinationName = destinationName;
	    }

}
