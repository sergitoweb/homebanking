package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class SharedTransactionAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sharedTransaction_id")
    private SharedTransaction sharedTransaction;

    public SharedTransactionAccount() {
    }

    public SharedTransactionAccount(Account account, SharedTransaction sharedTransaction) {
        this.account = account;
        this.sharedTransaction = sharedTransaction;
    }

    public long getId() {
        return id;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public SharedTransaction getSharedTransaction() {
        return sharedTransaction;
    }

    public void setSharedTransaction(SharedTransaction sharedTransaction) {
        this.sharedTransaction = sharedTransaction;
    }
}
