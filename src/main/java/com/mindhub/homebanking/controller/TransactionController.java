package com.mindhub.homebanking.controller;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.NotificationService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageSource mensajes;

    @Autowired
    private NotificationService notificationService;


    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication, @Valid @RequestParam long amount, @Valid @RequestParam String description, @Valid @RequestParam String fromAccountNumber, @Valid @RequestParam String toAccountNumber){

        Client clientelogueado = clientService.findByEmail(authentication.getName()).orElse(null);
        String result = transactionService.makeTransaction(amount,description,fromAccountNumber,toAccountNumber,clientelogueado);

        if (result.equals("mensaje.exito")) {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }

    }
}
