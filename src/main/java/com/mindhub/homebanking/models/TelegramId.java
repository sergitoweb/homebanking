package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class TelegramId {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;

    private long chatId;

    private boolean isAsociated = false;
    private boolean questionAsked = false;

    @OneToOne(mappedBy = "telegramId")
    private Client cliente;

    public TelegramId() {
    }

    public TelegramId(long chatId) {
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public boolean isAsociated() {
        return isAsociated;
    }

    public void setAsociated(boolean asociated) {
        isAsociated = asociated;
    }

    public boolean isQuestionAsked() {
        return questionAsked;
    }

    public void setQuestionAsked(boolean questionAsked) {
        this.questionAsked = questionAsked;
    }

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }
}
