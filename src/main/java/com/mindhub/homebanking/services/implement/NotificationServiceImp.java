package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.NotificationService;
import com.mindhub.homebanking.services.TelegramIdService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class NotificationServiceImp extends TelegramLongPollingBot implements NotificationService {


    private TelegramIdService telegramIdService;


    private ClientService clientService;

    public NotificationServiceImp(TelegramIdService telegramIdService, ClientService clientService) {
        this.telegramIdService = telegramIdService;
        this.clientService = clientService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Se obtiene el mensaje escrito por el usuario
        final String messageTextReceived = update.getMessage().getText();

        // Se obtiene el id de chat del usuario
        final long chatId = update.getMessage().getChatId();

        int resultCheck = telegramIdService.checkId(chatId);

        switch (resultCheck){
            case -1:
                telegramIdService.setQuestionAsked(chatId,true);
                sendMessage("Parece que no estas registrado, envia el mail de tu cliente en el banco.", chatId);
                break;
            case -2:
                if(telegramIdService.setUser(chatId,messageTextReceived)){
                    telegramIdService.setAsociated(chatId,true);
                    sendMessage("Vinculacion exitosa",chatId);
                }else{
                    telegramIdService.setQuestionAsked(chatId,false);
                    sendMessage("Algo salio mal, vuelve a iniciar la comunicacion.",chatId);
                }
                break;
            case 0:
                String mensajePredeterminado = "Hola " + telegramIdService.getUserTelegram(chatId).getFirstName() +"!";
                sendMessage(mensajePredeterminado,chatId);
                break;
            default:

        }



    }

    @Override
    public String getBotUsername() {
        return "Santandy_bot";
    }

    @Override
    public String getBotToken() {
        return "5743088965:AAEVfl6nZYCgQl0DMaU3TL8TgqHZgHeVknU";
    }

    @Override
    public void sendMessage(String message, long chatId){
        // Se crea un objeto mensaje
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            // Se envía el mensaje
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNotification(String mail, String mensaje){
        Client client = clientService.findByEmail(mail).orElse(null);
        if(client != null){
            sendMessage(mensaje,client.getTelegramId().getChatId());
        }
        return;
    }


}
