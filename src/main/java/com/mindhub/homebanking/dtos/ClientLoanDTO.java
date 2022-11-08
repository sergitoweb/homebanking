package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private long id;

    private Double amount;

    private int payments;

    private long loanId;

    private String name;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id=clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name= clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();

    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }


}
