package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.SharedTransaction;
import com.mindhub.homebanking.models.SharedTransactionAccount;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionAccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.UUID;

@Service
public class SharedTransactionService {

    @Autowired
    private SharedTransactionRepository sharedTransactionRepository;

    @Autowired
    private SharedTransactionAccountRepository sharedTransactionAccountRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;



    public SharedTransactionDTO findById(long id){
        return  new SharedTransactionDTO(sharedTransactionRepository.findById(id).orElse(null));
    }


    public String makeSharedTransaction(Long amount, int sharedAccounts, String toAccountNumber, String description){
        long amountPerAccount = (long) (amount /  sharedAccounts);
        String tokenId = (UUID.randomUUID()).toString().toUpperCase();
        Account account = accountRepository.findByNumber(toAccountNumber).orElse(null);
        SharedTransaction sharedTransaction = new SharedTransaction(account,amount,amountPerAccount,sharedAccounts,tokenId,description);
        sharedTransaction = sharedTransactionRepository.save(sharedTransaction);
        accountRepository.save(account);

        System.out.println("Token: " + tokenId);

        String linkPago= "http://localhost:8080/web/pay-shared-transfers.html?tokenId=" + tokenId;


        return linkPago;
    }

        public boolean paySharedTransaction(String tokenId, String fromAccountNumber, Client clientelogueado){

        Account account = accountRepository.findByNumber(fromAccountNumber).orElse(null);
        SharedTransaction sharedTransaction = sharedTransactionRepository.findByTokenId(tokenId).orElse(null);

        transactionService.makeTransaction(sharedTransaction.getTotalAmount(), ("Pago compartido " + sharedTransaction.getId()),fromAccountNumber,sharedTransaction.getAccount().getNumber(),clientelogueado);


        if(account != null && sharedTransaction != null){
            SharedTransactionAccount sharedTransactionAccount = new SharedTransactionAccount(account,sharedTransaction);
            account.addPagoSharedTransaction(sharedTransactionAccount);
            sharedTransaction.addToAccount(sharedTransactionAccount);
            accountRepository.save(account);
            sharedTransactionRepository.save(sharedTransaction);
            sharedTransactionAccountRepository.save(sharedTransactionAccount);
            return true;
        }else {
            return false;
        }
    }

    public SharedTransactionDTO findByTokenId(String tokenId){
        return  new SharedTransactionDTO(sharedTransactionRepository.findByTokenId(tokenId).orElse(null));
    }
}
