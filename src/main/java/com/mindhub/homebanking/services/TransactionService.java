package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.MoneyType;

public interface TransactionService {

    public String makeTransaction(Long amount, String description, String fromAccountNumber, String toAccountNumber, Client clientelogueado);

    public String makeTransactionCripto(float amountCripto, float amountArs, String description, String originNumber, String destinationNumber, Client clientelogueado, MoneyType tipomoneda);

    public boolean makeTransactionLoan(Account accountDestination, long amount, Loan loan);
}
