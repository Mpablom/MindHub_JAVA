package com.mindhub.homebanking.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double amount;
    @ElementCollection
    private List<Integer> payments = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    //Constructors

    public ClientLoan() {
    }

    public ClientLoan(double amount, List<Integer> payments) {
        this.amount = amount;
        this.payments = payments;
    }

    //Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
