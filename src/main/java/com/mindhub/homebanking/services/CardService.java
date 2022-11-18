package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.CardUtils.getCardCvv;
import static com.mindhub.homebanking.utils.CardUtils.getCardNumber;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private ClientRepository clientRepository;

    public String agregarCard(Client client, CardColor color, CardType type) {

        //genero aleatoriamente el numero de cvv y numero de tarjeta

        //crea una lista de tarjetas del tipo que pasa por parametro...
        List<Card> cardFiltradas = client.getCards().stream().filter(card -> card.getType()==type).collect(Collectors.toList());

        if(cardFiltradas.size()<3) {

            String number = getCardNumber();
            int cvv = getCardCvv();

            //calculo mas 5 anio
            LocalDateTime fechahoy = LocalDateTime.now().plusYears(5);

            //creamos la card nueva
            Card newcard = new Card(client.getFirstName() + " " + client.getLastName(), number, cvv, fechahoy, LocalDateTime.now(), type, color);

            //asignamos el cliente logueado a la cuenta y viceversa
            newcard.setClient(client);
            client.addCard(newcard);
            cardRepository.save(newcard);

            return "mensaje.exito";
        }else{
            return "mensaje.limit.card.number";
        }
    }


    public String deleteCard(Client client, String number) {

        Set<Card> cardFiltradas = client.getCards().stream().filter(card -> card.getNumber().equals(number)).collect(Collectors.toSet());
        if(cardFiltradas == null){
            return "mensaje.card.notfound";
        }

        List<Integer> cardActualizadas = cardFiltradas.stream().map(card -> cardRepository.updateCardById(card.getId(), false)).collect(Collectors.toList());
        Integer registrosActualizados = cardActualizadas.get(0);

        return "mensaje.exito";
    }
}
