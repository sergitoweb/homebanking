package com.mindhub.homebanking.dtos;

import javax.validation.constraints.NotEmpty;

public class LoanAplicationDTO {


    //Este DTO debe tener id del préstamo, monto, cuotas y número de cuenta de destino
    @NotEmpty
    private long loanId;
    @NotEmpty
    private long amount;
    @NotEmpty
    private int payments;
    @NotEmpty
    private String toAccountNumber;


    public LoanAplicationDTO(long loanId, long amount, int payments, String toAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public long getLoanId() {
        return loanId;
    }


    public long getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

}
