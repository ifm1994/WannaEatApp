package com.example.ifmfo.wannaeatapp.Model;

import java.util.Map;

public class SendMessage extends ChatMessage {

    private Map time;

    public SendMessage(){

    }

    public SendMessage(Map time){
        this.time = time;
    }

    public SendMessage(String messageText, String messageUser, Map time) {
        super(messageText, messageUser);
        this.time = time;
    }

    public Map getTime() {
        return time;
    }

    public void setTime(Map time) {
        this.time = time;
    }
}
