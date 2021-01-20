package com.example.demo;

import java.io.IOException;
import java.util.Date;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import com.example.demo.dto.Clients;

public class sendMessageOnRoute {
	 private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
	
	public void sendMessage(Clients c) {
		  SMPPSession session = new SMPPSession();
	        try {
	            System.out.println("Connecting");
	            System.out.println("HostName==================================="+c.getIpAddress());
	            System.out.println("systemId==================================="+c.getSystemId());
	            System.out.println("Sender details============================="+c.getSender_details());
	            System.out.println("Destination address========================"+c.getContacts());
	            String systemId = session.connectAndBind(c.getIpAddress(), Integer.parseInt(c.getTxPort()), new BindParameter(BindType.BIND_TX, c.getSystemId(), c.getPassword(), c.getSystemType(), TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
	            System.out.println("Connected with SMSC with system id {}"+systemId);

	            try {
	                String messageId = session.submitShortMessage("CMT",
	                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, c.getSender_details(),
	                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, c.getContacts(),
	                    new ESMClass(), (byte)0, (byte)1,  TIME_FORMATTER.format(new Date()), null,
	                    new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte)0,
	                    c.getMessage().getBytes());
	                System.out.println("Message submitted, message_id is {}"+messageId);
	                System.out.println("Message details................."+c.getMessage());
	                //Update Client Status
	                new ClientManager().updateStatusById(c);
	            } catch (PDUException e) {
	                // Invalid PDU parameter
	                System.out.println("Invalid PDU parameter"+e);
	            } catch (ResponseTimeoutException e) {
	                // Response timeout
	                System.out.println("Response timeout"+e);
	            } catch (InvalidResponseException e) {
	                // Invalid response
	                System.out.println("Receive invalid response"+e);
	            } catch (NegativeResponseException e) {
	                // Receiving negative response (non-zero command_status)
	                System.out.println("Receive negative response"+e);
	            } catch (IOException e) {
	                System.out.println("IO error occurred"+e);
	            }

	            session.unbindAndClose();

	        } catch (IOException e) {
	            System.out.println("Failed connect and bind to host"+e);
	        }
		
		
	}

}
