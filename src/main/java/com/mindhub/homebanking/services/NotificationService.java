package com.mindhub.homebanking.services;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface NotificationService {

    public void onUpdateReceived(Update update);

    public String getBotUsername();

    public String getBotToken();

    public void sendMessage(String message, long chatId);

    public void sendNotification(String mail, String mensaje);
}
