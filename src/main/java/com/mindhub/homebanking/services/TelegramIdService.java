package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TelegramIdRepository;

public interface TelegramIdService {


    public int checkId(long chatId);

    public boolean setUser(long chatId, String mail);

    public void setQuestionAsked(long chatId, boolean value);

    public void setAsociated(long chatId, boolean value);

    public Client getUserTelegram(long chatId);
}
