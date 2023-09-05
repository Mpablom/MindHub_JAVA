package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String name;
    private double amount;
    private List<Integer> payments;

    //Constructor

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    // Getters

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

}
