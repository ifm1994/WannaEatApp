package com.example.ifmfo.wannaeatapp.Model;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private int tipoMensaje; //1 para mensajes que envío, y 2 para los que recibo

    public ChatMessage(String messageText, String messageUser, int tipoMensaje) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.tipoMensaje = tipoMensaje;
    }

    public ChatMessage() {}

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

}
