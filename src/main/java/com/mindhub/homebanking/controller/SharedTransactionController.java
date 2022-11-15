package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.SharedTransaction;
import com.mindhub.homebanking.services.SharedTransactionService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class SharedTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SharedTransactionService sharedTransactionService;

    @Autowired
    private MessageSource mensajes;



    @PostMapping("/transactions/shared")
    public ResponseEntity<Object> createSharedTransaction(HttpSession session, @Valid @RequestParam long amount, @Valid @RequestParam String description, @Valid @RequestParam String fromAccountNumber, @Valid @RequestParam String toAccountNumber, @RequestParam int numberSharedBetwen){


        String result = transactionService.makeTransaction(amount,description,fromAccountNumber,toAccountNumber,(Client) session.getAttribute("client"));

        if (result.equals("mensaje.exito")) {
            String linkPago = sharedTransactionService.makeSharedTransaction(amount,numberSharedBetwen, fromAccountNumber);
            System.out.println(linkPago);
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/transactions/shared/pay")
    public ResponseEntity<Object> paySharedTransaction(HttpSession session, @RequestParam String tokenId,@RequestParam String fromAccountNumber){

        boolean result = sharedTransactionService.paySharedTransaction(tokenId,fromAccountNumber,(Client) session.getAttribute("client"));

        if (result) {
            return new ResponseEntity<>(mensajes.getMessage("Pago realizado", null, LocaleContextHolder.getLocale()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(mensajes.getMessage("Pago no realizado", null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }

    }



    @GetMapping("/transactions/shared")
    public SharedTransactionDTO getSharedTransaction(@RequestParam @NotNull long id){
        return sharedTransactionService.findById(id);
    }


}
