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
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<Object> createTransaction(HttpSession session, @Valid @RequestParam long amount, @Valid @RequestParam String description, @Valid @RequestParam String fromAccountNumber, @Valid @RequestParam String toAccountNumber){


        String result = transactionService.makeTransaction(amount,description,fromAccountNumber,toAccountNumber,(Client) session.getAttribute("client"));

        if (result.equals("mensaje.exito")) {

            if(((Client) session.getAttribute("client")).isHasTelegram()){
                notificationService.sendNotification(((Client) session.getAttribute("client")).getEmail(),
                        "Se ha realizado una transferencia desde " + fromAccountNumber +
                                " hacia la cuenta " + toAccountNumber + " con monto " + amount);
            }

            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }

    }
    @PostMapping("/transactions/shared")
    public ResponseEntity<Object> createSharedTransaction(HttpSession session,  @Valid @RequestParam long amount, @Valid @RequestParam String description, @Valid @RequestParam String fromAccountNumber, @Valid @RequestParam String toAccountNumber, @RequestParam int numberSharedBetwen){


        String result = transactionService.makeTransaction(amount,description,fromAccountNumber,toAccountNumber,(Client) session.getAttribute("client"));
        String linkPago = transactionService.makeSharedTransaction(amount,numberSharedBetwen, fromAccountNumber);
        System.out.println(linkPago);

        if (result.equals("mensaje.exito")) {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }

    }

}
