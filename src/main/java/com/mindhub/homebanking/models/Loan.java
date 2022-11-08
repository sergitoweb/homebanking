package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    private String name;
    @Column(name="max_amount")
    private Double maxAmount;

    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments= new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan(String name, Double maxAmount) {
        this.name = name;
        this.maxAmount = maxAmount;
    }

    public Loan() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    public void addPayment(Integer payment){
        payments.add(payment);
    }

    @JsonIgnore
    public List<Client> getClients(){
        return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toList());
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }
}
