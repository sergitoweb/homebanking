package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.SharedTransaction;
import com.mindhub.homebanking.models.SharedTransactionAccount;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionAccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

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


    public String makeSharedTransaction(Long amount, int sharedAccounts, String toAccountNumber){
        long amountPerAccount = (long) (amount /  sharedAccounts);
        Account account = accountRepository.findByNumber(toAccountNumber).orElse(null);
        SharedTransaction sharedTransaction = new SharedTransaction(account,amount,amountPerAccount,sharedAccounts);
        sharedTransaction = sharedTransactionRepository.save(sharedTransaction);
        accountRepository.save(account);

        System.out.println("Id de sharedTransaction: " + sharedTransaction.getId());

        String linkPago= "http://localhost:8080/web/transfers.html?" + "amount=" + amountPerAccount + "&toAccount=" + toAccountNumber + "&description= Shared"  ;
        return linkPago;
    }

    public boolean paySharedTransaction(long id, String fromAccountNumber, Client clientelogueado){

        Account account = accountRepository.findByNumber(fromAccountNumber).orElse(null);
        SharedTransaction sharedTransaction = sharedTransactionRepository.findById(id).orElse(null);

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

}
