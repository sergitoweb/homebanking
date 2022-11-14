package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.TelegramId;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TelegramIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TelegramIdService {

    @Autowired
    private TelegramIdRepository telegramIdRepository;

    @Autowired
    private ClientRepository clientRepository;

    public int checkId(long chatId){
        TelegramId telegramId = telegramIdRepository.findByChatId(chatId).orElse(null);

        if(telegramId == null) {
            telegramIdRepository.save(new TelegramId(chatId));
            return -1;
        } else if (!telegramId.isQuestionAsked() && !telegramId.isAsociated()) {
            return -1;
        }else if (telegramId.isQuestionAsked() && !telegramId.isAsociated()){
            return -2;
        }else{
            return 0;
        }
    }

    public boolean setUser(long chatId, String mail){
        Client client = clientRepository.findByEmail(mail).orElse(null);
        if(client != null){
            if(client.isHasTelegram()){
                return false;
            }
            TelegramId telegramId = telegramIdRepository.findByChatId(chatId).orElse(null);
            client.setTelegramId(telegramId);
            client.setHasTelegram(true);
            telegramId.setCliente(client);
            telegramIdRepository.save(telegramId);
            clientRepository.save(client);
            return true;
        }else {
            return false;
        }
    }

    public void setQuestionAsked(long chatId, boolean value){
        TelegramId telegramId = telegramIdRepository.findByChatId(chatId).orElse(null);
        telegramId.setQuestionAsked(value);
        telegramIdRepository.save(telegramId);
        return;
    }

    public void setAsociated(long chatId, boolean value){
        TelegramId telegramId = telegramIdRepository.findByChatId(chatId).orElse(null);
        telegramId.setAsociated(value);
        telegramIdRepository.save(telegramId);
        return;
    }

    public Client getUserTelegram(long chatId){
        TelegramId telegramId = telegramIdRepository.findByChatId(chatId).orElse(null);
        if(telegramId != null ){
            return telegramId.getCliente();
        }else{
            return null;
        }
    }

}
