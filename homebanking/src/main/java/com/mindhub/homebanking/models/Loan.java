package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double maxAmount;
    @ElementCollection
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy = "loan",fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    //Constructors

    public Loan() {
    }
    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    //Getters and Setters

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }
    public void addClientLoans(ClientLoan cl) {
        cl.setLoan(this);
        clientLoans.add(cl);
    }
    public Set<Client> getClients() {
        Set<Client> clients = new HashSet<>();
        for (ClientLoan clientLoan : clientLoans) {
            clients.add(clientLoan.getClient());
        }
        return clients;
    }
}
