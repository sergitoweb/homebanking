package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.*;
import com.mindhub.homebanking.services.implement.CardServiceImp;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardServiceTest {

    CardRepository cardRepository = mock(CardRepository.class);
    ClientRepository clientRepository = mock(ClientRepository.class);

    CardService cardService = new CardServiceImp(cardRepository, clientRepository);

    List<Card> cards = Arrays.asList(
            new Card("Tomas Quinteros", "3325-6745-7876-4445", 990, LocalDateTime.parse("2023-09-08T00:00:00"), LocalDateTime.parse("2022-09-08T00:00:00"), CardType.CREDIT, CardColor.GOLD),
            new Card("Admin", "1925-6745-7876-4445", 222, LocalDateTime.parse("2023-09-08T00:00:00"), LocalDateTime.parse("2022-09-08T00:00:00"), CardType.CREDIT, CardColor.SILVER),
            new Card("Admin", "8889-6745-7876-4445", 350, LocalDateTime.parse("2023-09-08T00:00:00"), LocalDateTime.parse("2022-09-08T00:00:00"), CardType.DEBIT, CardColor.TITANIUM)
    );

    @Test
    public void testAgregarCard(){
        when(cardRepository.save(cards.get(0))).thenReturn(cards.get(0));
        Client client = mock(Client.class);
        String result = cardService.agregarCard(client,CardColor.GOLD, CardType.CREDIT);
        assertNotNull(result);
        assertEquals("mensaje.exito",result);
    }

    @Test
    public void testAgregarCardFail(){
        //Ver de agregar las tres tarjetas para que a la cuarta falle
        when(cardRepository.save(cards.get(0))).thenReturn(cards.get(0));
        Client client = mock(Client.class);
        System.out.println(client.getCards());
        String result = cardService.agregarCard(client,CardColor.GOLD, CardType.CREDIT);
        String result2 = cardService.agregarCard(client,CardColor.GOLD, CardType.CREDIT);
        String result3 = cardService.agregarCard(client,CardColor.GOLD, CardType.CREDIT);
        String result4 = cardService.agregarCard(client,CardColor.GOLD, CardType.CREDIT);
        assertNotNull(result4);
        assertEquals("mensaje.limit.card.number",result4);
    }

}
