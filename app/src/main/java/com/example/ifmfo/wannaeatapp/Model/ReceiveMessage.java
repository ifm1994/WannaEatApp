package com.example.ifmfo.wannaeatapp.Model;

public class ReceiveMessage extends ChatMessage {

    private Long time;

    public ReceiveMessage (){

    }

    public ReceiveMessage(Long time) {
        this.time = time;
    }

    public ReceiveMessage(String messageText, String messageUser, Long time) {
        super(messageText, messageUser, 2);
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
