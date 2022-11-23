package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.models.Client;

public interface SharedTransactionService {

    public SharedTransactionDTO findById(long id);

    public String makeSharedTransaction(Long amount, int sharedAccounts,String fromAccountNumber, String toAccountNumber, String description, Client clientelogueado);

    public boolean paySharedTransaction(String tokenId, String fromAccountNumber, Client clientelogueado);

    public SharedTransactionDTO findByTokenId(String tokenId);
}
