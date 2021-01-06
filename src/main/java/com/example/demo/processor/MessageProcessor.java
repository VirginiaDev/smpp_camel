package com.example.demo.processor;

import com.example.demo.dto.Message;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("messageProcessor")
public class MessageProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception{
        Message msg = exchange.getIn().getBody(Message.class);
        // do something with the msg and/or exchange here
        System.out.println(msg);

    }
}
