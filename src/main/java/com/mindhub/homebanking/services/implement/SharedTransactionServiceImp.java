package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.SharedTransaction;
import com.mindhub.homebanking.models.SharedTransactionAccount;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionAccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.NotificationService;
import com.mindhub.homebanking.services.SharedTransactionService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SharedTransactionServiceImp implements SharedTransactionService {


    private SharedTransactionRepository sharedTransactionRepository;


    private SharedTransactionAccountRepository sharedTransactionAccountRepository;

    private AccountRepository accountRepository;


    private AccountService accountService;


    private NotificationService notificationService;


    private TransactionService transactionService;

    public SharedTransactionServiceImp(SharedTransactionRepository sharedTransactionRepository,
                                       SharedTransactionAccountRepository sharedTransactionAccountRepository,
                                       AccountRepository accountRepository,
                                       AccountService accountService,
                                       NotificationService notificationService,
                                       TransactionService transactionService) {
        this.sharedTransactionRepository = sharedTransactionRepository;
        this.sharedTransactionAccountRepository = sharedTransactionAccountRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.transactionService = transactionService;
    }

    @Override
    public SharedTransactionDTO findById(long id){
        return  new SharedTransactionDTO(sharedTransactionRepository.findById(id).orElse(null));
    }

    @Override
    public String makeSharedTransaction(Long amount, int sharedAccounts,String fromAccountNumber, String toAccountNumber, String description, Client clientelogueado){
        long amountPerAccount = (long) (amount /  sharedAccounts);
        String tokenId = (UUID.randomUUID()).toString().toUpperCase();
        Account account = accountRepository.findByNumber(toAccountNumber).orElse(null);
        SharedTransaction sharedTransaction = new SharedTransaction(account,amount,amountPerAccount,sharedAccounts,tokenId,description);
        sharedTransaction = sharedTransactionRepository.save(sharedTransaction);
        accountRepository.save(account);

        System.out.println("Token: " + tokenId);
        String linkPago= "https://bancopasion.up.railway.app/web/pay-shared-transfers.html?tokenId=" + tokenId;

        if(clientelogueado.isHasTelegram()){
            notificationService.sendNotification(clientelogueado.getEmail(),
                    "Se ha realizado un pago compartido desde " +  toAccountNumber +
                            " hacia la cuenta " + fromAccountNumber + " con monto " + amount +
                    ". Link de pago: " + linkPago);
        }

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

            if(clientelogueado.isHasTelegram()){
                notificationService.sendNotification(clientelogueado.getEmail(),
                        "Se ha pagado un pago compartido desde " + fromAccountNumber +
                                " hacia la cuenta " + sharedTransaction.getAccount() + " con monto " + sharedTransaction.getParcialAmount());
            }

            return true;
        }else {
            return false;
        }
    }

    @Override
    public SharedTransactionDTO findByTokenId(String tokenId){
        return  new SharedTransactionDTO(sharedTransactionRepository.findByTokenId(tokenId).orElse(null));
    }
}
