package com.mindhub.homebanking;

import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionAccountRepository;
import com.mindhub.homebanking.repositories.SharedTransactionRepository;
import com.mindhub.homebanking.services.*;
import com.mindhub.homebanking.services.implement.SharedTransactionServiceImp;

import static org.mockito.Mockito.mock;

public class SharedTransactionServiceTest {

    SharedTransactionRepository sharedTransactionRepository = mock(SharedTransactionRepository.class);
    SharedTransactionAccountRepository sharedTransactionAccountRepository = mock(SharedTransactionAccountRepository.class);
    AccountRepository accountRepository = mock(AccountRepository.class);
    AccountService accountService = mock(AccountService.class);
    NotificationService notificationService = mock(NotificationService.class);
    TransactionService transactionService = mock(TransactionService.class);

    SharedTransactionService sharedTransaction = new SharedTransactionServiceImp(sharedTransactionRepository,
            sharedTransactionAccountRepository, accountRepository, accountService, notificationService, transactionService);

   // SharedTransaction st1 = new SharedTransaction(account3,1000,500,2,"F8607AFF-48E7-4250-ADAF-0BEA7898FE7B","Shared Transaction");



}
