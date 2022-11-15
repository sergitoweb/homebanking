package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.MoneyType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


public class AccountDTO {

    private long id;
    private LocalDateTime creationDate;
    private double balance;
    private String number;

    private AccountType type;

    private Set<TransactionDTO> transactions;

    private Set<SharedTransactionDTO> sharedTransactions;

    private MoneyType typemoney;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.number = account.getNumber();
        this.type = account.getType();
        this.typemoney=account.getTypemoney();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
        this.sharedTransactions = account.getSharedTransactions().stream()
                .map(sharedTransaction -> new SharedTransactionDTO(sharedTransaction)).collect(Collectors.toSet());
    }

    public AccountDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public AccountType getType() {
        return type;
    }

    public Set<SharedTransactionDTO> getSharedTransactions() {
        return sharedTransactions;
    }

    public MoneyType getTypemoney() {
        return typemoney;
    }
}
