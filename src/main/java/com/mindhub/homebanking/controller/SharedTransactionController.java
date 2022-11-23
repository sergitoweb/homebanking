package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SharedTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SharedTransactionService sharedTransactionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageSource mensajes;

    public SharedTransactionController() {
    }


    @PostMapping("/transactions/shared")
    public ResponseEntity<?> createSharedTransaction(Authentication authentication, @Valid @RequestParam long amount, @Valid @RequestParam String description, @Valid @RequestParam String fromAccountNumber, @Valid @RequestParam String toAccountNumber, @RequestParam int numberSharedBetwen){

        Client clientelogueado = clientService.findByEmail(authentication.getName()).orElse(null);
        String result = transactionService.makeTransaction(amount,"Pago compartido"+" "+description,fromAccountNumber,toAccountNumber,clientelogueado);

        if (result.equals("mensaje.exito")) {
            String linkPago = sharedTransactionService.makeSharedTransaction(amount,numberSharedBetwen,toAccountNumber, fromAccountNumber, "Pago compartido"+" "+description,clientelogueado);

            return new ResponseEntity<>(linkPago, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/transactions/shared/pay")
    public ResponseEntity<Object> paySharedTransaction(Authentication authentication, @RequestParam String tokenId,@RequestParam String fromAccountNumber){

        Client clientelogueado = clientService.findByEmail(authentication.getName()).orElse(null);
        boolean result = sharedTransactionService.paySharedTransaction(tokenId,fromAccountNumber,clientelogueado);

        if (result) {
            return new ResponseEntity<>(mensajes.getMessage("Pago realizado", null, LocaleContextHolder.getLocale()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(mensajes.getMessage("Pago no realizado", null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }

    }



    @GetMapping("/transactions/shared/{id}")
    public SharedTransactionDTO getSharedTransactionById(@PathVariable long id){
        return sharedTransactionService.findById(id);
    }
    @GetMapping("/transactions/shared/token/{tokenId}")
    public SharedTransactionDTO getSharedTransactionByTokenId(@PathVariable  String tokenId){
        return sharedTransactionService.findByTokenId(tokenId);
    }


}
