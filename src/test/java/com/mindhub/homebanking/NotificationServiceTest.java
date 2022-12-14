package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.NotificationService;
import com.mindhub.homebanking.services.TelegramIdService;
import com.mindhub.homebanking.services.implement.NotificationServiceImp;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NotificationServiceTest {

    TelegramIdService telegramIdService = mock(TelegramIdService.class);

    ClientService clientService = mock(ClientService.class);

    NotificationService notificationService = new NotificationServiceImp(telegramIdService,clientService);


    @Test
    public void testOnUpdateReceived1() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Mensaje test");
        when(message.getChatId()).thenReturn(1l);
        when(telegramIdService.checkId(1l)).thenReturn(-1);

        notificationService.onUpdateReceived(update);
    }

    @Test
    public void testOnUpdateReceived2() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Mensaje test");
        when(message.getChatId()).thenReturn(1l);
        when(telegramIdService.checkId(1l)).thenReturn(1);

        notificationService.onUpdateReceived(update);
    }

    @Test
    public void testOnUpdateReceived3() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Mensaje test");
        when(message.getChatId()).thenReturn(1l);
        when(telegramIdService.checkId(1l)).thenReturn(0);
        Client client = mock(Client.class);
        when(client.getFirstName()).thenReturn("Prueba");
        when(telegramIdService.getUserTelegram(1l)).thenReturn(client);

        notificationService.onUpdateReceived(update);
    }
}
