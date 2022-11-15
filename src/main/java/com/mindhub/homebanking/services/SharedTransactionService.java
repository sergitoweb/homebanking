package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.repositories.SharedTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SharedTransactionService {

    @Autowired
    private SharedTransactionRepository sharedTransactionRepository;

    public SharedTransactionDTO findById(long id){
        return  new SharedTransactionDTO(sharedTransactionRepository.findById(id).orElse(null));
    }


    public String makeSharedTransaction(Long amount, int sharedAccounts, String toAccountNumber){
        int amountPerAccount = (int) (amount /  sharedAccounts);
        String linkPago= "http://localhost:8080/web/transfers.html?" + "amount=" + amountPerAccount + "&toAccount=" + toAccountNumber + "&description= Shared"  ;
        return linkPago;
    }



}
