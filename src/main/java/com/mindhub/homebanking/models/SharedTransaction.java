package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SharedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;

    private long amount;

    private int clientNumber;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    @OneToMany(mappedBy="sharedTransaction", fetch=FetchType.EAGER)
    private Set<SharedTransactionAccount> fromAccounts = new HashSet<>();


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<SharedTransactionAccount> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Set<SharedTransactionAccount> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public void addToAccount(SharedTransactionAccount sharedTransactionAccount){
        sharedTransactionAccount.setSharedTransaction(this);
        fromAccounts.add(sharedTransactionAccount);
    }
}
