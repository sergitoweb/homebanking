package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    HttpSession session;

@Transactional
    public String makeTransaction(Long amount, String description, String fromAccountNumber, String toAccountNumber, Client clientelogueado){


        if(!accountService.validarCuenta(fromAccountNumber)){
            return "mensaje.originAccount.invalid";
        }
        if(!accountService.validarCuenta(toAccountNumber)){
            return "mensaje.destinationAccount.invalid";
        }
        if(!accountService.validarCuenta(clientelogueado,fromAccountNumber)){
            return "mensaje.originAccountNotLogin";
        }
        if(!accountService.validarAmount(fromAccountNumber,amount)){
            return "mensaje.accountNotFounds";
        }

        if(fromAccountNumber==toAccountNumber){
            return "mensaje.originAccount.destinationAccount.equals";
        }

        //crear la transaccion y asignarselo a la cuenta origen
        //a la cuenta de origen le descontemos el monto de su balance
        Account accountOrigin = accountRepository.findByNumber(fromAccountNumber).orElse(null);
        Account accountDestination = accountRepository.findByNumber(toAccountNumber).orElse(null);

        if(accountOrigin.getType().equals(accountDestination.getType())) {

            Transaction transaction = new Transaction(amount, description, LocalDateTime.now(), TransactionType.DEBIT);
            accountOrigin.addTransaction(transaction);
            accountOrigin.setBalance(accountOrigin.getBalance() - amount);

            session.setAttribute("client", clientRepository.findByEmail(clientelogueado.getEmail()).orElse(null));

            transactionRepository.save(transaction);

            //crear la transaccion y asignarselo a la cuenta de destino
            //a la cuenta de destino sumarle el monto a su balance

            Transaction transactiondestination = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT);
            accountDestination.addTransaction(transactiondestination);
            accountDestination.setBalance(accountDestination.getBalance() + amount);

            transactionRepository.save(transactiondestination);


            return "mensaje.exito";
        }else{
            return "mensaje.account.incompatible";
        }

    }

    public boolean makeTransactionLoan(Account accountDestination,long amount, Loan loan){

        if (accountDestination.getType().equals(AccountType.VIN)) {
            Transaction transactiondestination = new Transaction(amount, loan.getName() + " loan approved.", LocalDateTime.now(), TransactionType.CREDIT);
            accountDestination.addTransaction(transactiondestination);
            accountDestination.setBalance(accountDestination.getBalance() + amount);
            transactionRepository.save(transactiondestination);

            return true;
        }else{
            return false;
        }
    }






}
