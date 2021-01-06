package com.example.demo.dto;

public class Message {

    private int id;
    private long sender;
    private String messageBody;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setSender(long sender){
        this.sender = sender;
    }
    public long getSender(){
        return sender;
    }

    public void setMessageBody(String messageBody){
        this.messageBody = messageBody;
    }
    public String getMessageBody(){
        return messageBody;
    }

    public String toString(){
        return "sender "+ sender + "messageBody " + messageBody;
    }
}