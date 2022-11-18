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
    private NotificationService notificationService;

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

        if(accountOrigin.getType().equals(accountDestination.getType()) & accountOrigin.getTypemoney().equals(accountDestination.getTypemoney())) {

            Transaction transaction = new Transaction(amount, description, LocalDateTime.now(), TransactionType.DEBIT);
            accountOrigin.addTransaction(transaction);
            accountOrigin.setBalance(accountOrigin.getBalance() - amount);

            transactionRepository.save(transaction);

            //crear la transaccion y asignarselo a la cuenta de destino
            //a la cuenta de destino sumarle el monto a su balance

            Transaction transactiondestination = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT);
            accountDestination.addTransaction(transactiondestination);
            accountDestination.setBalance(accountDestination.getBalance() + amount);

            transactionRepository.save(transactiondestination);

            if(clientelogueado.isHasTelegram()){
                notificationService.sendNotification(((Client) session.getAttribute("client")).getEmail(),
                        "Se ha realizado una transferencia desde " + fromAccountNumber +
                                " hacia la cuenta " + toAccountNumber + " con monto " + amount);
            }

            return "mensaje.exito";
        }else{
            return "mensaje.account.incompatible";
        }

    }

    @Transactional
    public String makeTransactionCripto(float amountCripto, float amountArs, String description, String originNumber,String destinationNumber, Client clientelogueado,MoneyType tipomoneda) {


        if (!accountService.validarCuenta(destinationNumber)) {
            return "mensaje.originAccount.invalid";
        }
        if (!accountService.validarCuenta(originNumber)) {
            return "mensaje.destinationAccount.invalid";
        }
        if (!accountService.validarCuenta(clientelogueado, destinationNumber)) {
            return "mensaje.originAccountNotLogin";
        }
        if (!accountService.validarCuenta(clientelogueado, originNumber)) {
            return "mensaje.originAccountNotLogin";
        }
        if (destinationNumber == originNumber) {
            return "mensaje.originAccount.destinationAccount.equals";
        }

        // al momento de comprar cripto de debe debitar ars y acreditar cripto
        // al momento de vender cripto se debe debitar cripto y acreditar ars

        Account accountOrigin = accountRepository.findByNumber(originNumber).orElse(null);
        Account accountDestination = accountRepository.findByNumber(destinationNumber).orElse(null);

        //si es compra cripto... entonces
        if (accountOrigin.getType().equals(AccountType.VIN) && accountDestination.getType().equals(AccountType.CRY)) {
            if (accountDestination.getTypemoney().equals(tipomoneda)) {

                if (accountService.validarAmount(originNumber, amountArs)== false) {
                    return "mensaje.accountNotFounds";
                }

                Transaction transaction = new Transaction(amountArs, description, LocalDateTime.now(), TransactionType.DEBIT);
                accountOrigin.addTransaction(transaction);
                accountOrigin.setBalance(accountOrigin.getBalance() - amountArs);

                transactionRepository.save(transaction);

                Transaction transactiondestination = new Transaction(amountCripto, description, LocalDateTime.now(), TransactionType.CREDIT);
                accountDestination.addTransaction(transactiondestination);
                accountDestination.setBalance(accountDestination.getBalance() + amountCripto);

                transactionRepository.save(transactiondestination);

                if(clientelogueado.isHasTelegram()){
                    notificationService.sendNotification(((Client) session.getAttribute("client")).getEmail(),
                            "Se ha realizado la compra de " + amountArs + "ARS en "+ tipomoneda + " desde "
                                    + accountOrigin.getNumber() + ". acreditando en la cuenta " +
                                    accountDestination.getNumber() + " un monto de " + amountCripto + tipomoneda);
                }


                return "mensaje.exito";

            } else {
                return "mensaje.moneyType.incompatible";
            }

            //si es una venta de cripto... entonces
        } else if (accountOrigin.getType().equals(AccountType.CRY) && accountDestination.getType().equals(AccountType.VIN)) {
            if (accountOrigin.getTypemoney().equals(tipomoneda)) {

                if (!accountService.validarAmount(destinationNumber, amountCripto)) {
                    return "mensaje.accountNotFounds";
                }

                Transaction transaction = new Transaction(amountCripto, description, LocalDateTime.now(), TransactionType.DEBIT);
                accountOrigin.addTransaction(transaction);
                accountOrigin.setBalance(accountOrigin.getBalance() - amountCripto);

                session.setAttribute("client", clientRepository.findByEmail(clientelogueado.getEmail()).orElse(null));

                transactionRepository.save(transaction);

                Transaction transactiondestination = new Transaction(amountArs, description, LocalDateTime.now(), TransactionType.CREDIT);
                accountDestination.addTransaction(transactiondestination);
                accountDestination.setBalance(accountDestination.getBalance() + amountArs);

                transactionRepository.save(transactiondestination);

                if(clientelogueado.isHasTelegram()){
                    notificationService.sendNotification(((Client) session.getAttribute("client")).getEmail(),
                            "Se ha realizado la venta de " + amountCripto + tipomoneda + " desde "
                                    + accountOrigin.getNumber() + ". Acreditando en la cuenta " +
                                    accountDestination.getNumber() + " un monto de " + amountArs);
                }


                return "mensaje.exito";

            } else {
                return "mensaje.moneyType.incompatible";
            }

        } else {
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
