package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private MessageSource mensajes;


    //un cliente logueado puede crear una card
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createAccount(HttpSession session, @Valid @RequestParam CardType cardType, @Valid @RequestParam CardColor cardColor){


        Client clienteLogueado =  ((Client) session.getAttribute("client"));


        //List<Card> cardFiltradas = clienteLogueado.getCards().stream().filter(card -> card.getType()==cardType).collect(Collectors.toList());
        String result =cardService.agregarCard(clienteLogueado,cardColor,cardType);

        if (result.equals("mensaje.exito")){

            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/clients/current/cards/{number}")
    public ResponseEntity<Object> createAccount(HttpSession session, @Valid @RequestParam String number){

        Client clienteLogueado =  ((Client) session.getAttribute("client"));

        String result =cardService.deleteCard(clienteLogueado,number);
        if (result.equals("mensaje.exito")){

            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }

    }
}
