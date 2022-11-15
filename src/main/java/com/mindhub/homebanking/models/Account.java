package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    private AccountType type;

    private Double balance;
    @NotEmpty
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<SharedTransaction> sharedTransactions = new HashSet<>();

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<SharedTransactionAccount> pagosSharedTransactions = new HashSet<>();

    private MoneyType typemoney;

    public Account(LocalDateTime creationDate, double balance, String number,AccountType type, MoneyType tipo) {
        this.creationDate = creationDate;
        this.type = type;
        this.balance = balance;
        this.number = number;
        this.typemoney=tipo;
    }

    public Account(){

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

    @JsonIgnore()
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }


    public Set<SharedTransaction> getSharedTransactions() {
        return sharedTransactions;
    }

    public void setSharedTransactions(Set<SharedTransaction> sharedTransactions) {
        this.sharedTransactions = sharedTransactions;
    }

    public void addSharedTransaction(SharedTransaction sharedTransaction){
        sharedTransaction.setAccount(this);
        sharedTransactions.add(sharedTransaction);
    }

    public Set<SharedTransactionAccount> getPagosSharedTransactions() {
        return pagosSharedTransactions;
    }

    public void setPagosSharedTransactions(Set<SharedTransactionAccount> pagosSharedTransactions) {
        this.pagosSharedTransactions = pagosSharedTransactions;
    }

    public void addPagoSharedTransaction(SharedTransactionAccount sharedTransactionAccount){
        sharedTransactionAccount.setAccount(this);
        pagosSharedTransactions.add(sharedTransactionAccount);
    }

    public MoneyType getTypemoney() {
        return typemoney;
    }

    public void setTypemoney(MoneyType typemoney) {
        this.typemoney = typemoney;
    }
}
