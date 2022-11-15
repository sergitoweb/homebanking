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

    private long totalAmount;

    private long parcialAmount;

    private int clientNumber;

    private String tokenId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    @OneToMany(mappedBy="sharedTransaction", fetch=FetchType.EAGER)
    private Set<SharedTransactionAccount> fromAccounts = new HashSet<>();

    public SharedTransaction() {
    }

    public SharedTransaction(Account account, long totalAmount, long parcialAmount, int clientNumber, String tokenId) {
        this.totalAmount = totalAmount;
        this.parcialAmount = parcialAmount;
        this.clientNumber = clientNumber;
        this.account = account;
        this.tokenId = tokenId;
    }

    public long getId() {
        return id;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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

    public long getParcialAmount() {
        return parcialAmount;
    }

    public void setParcialAmount(long parcialAmount) {
        this.parcialAmount = parcialAmount;
    }
}
