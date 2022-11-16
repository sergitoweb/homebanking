package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.SharedTransaction;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SharedTransactionDTO {

    private long id;

    private String toAccount;

    private long amount;

    private int clientNumber;

    public String description;
    private String tokenId;

    private Set<String> fromAccounts = new HashSet<>();

    public SharedTransactionDTO() {
    }

    public SharedTransactionDTO(SharedTransaction sharedTransaction) {
        this.id = sharedTransaction.getId();
        this.toAccount = sharedTransaction.getAccount().getNumber();
        this.amount = sharedTransaction.getTotalAmount();
        this.clientNumber = sharedTransaction.getClientNumber();
        this.description = sharedTransaction.getDescription();
        this.tokenId = sharedTransaction.getTokenId();
        this.fromAccounts = sharedTransaction.getFromAccounts().stream()
                .map(sharedTransactionAccount -> sharedTransactionAccount.getAccount().getNumber()).collect(Collectors.toSet());
    }


    public long getId() {
        return id;
    }

    public String getToAccount() {
        return toAccount;
    }

    public long getAmount() {
        return amount;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public String getTokenId() {
        return tokenId;
    }

    public Set<String> getFromAccounts() {
        return fromAccounts;
    }

}
