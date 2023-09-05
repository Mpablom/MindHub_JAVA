package com.mindhub.homebanking.DTO;


public class LoanApplicationDTO {
    private double amount;
    private Long loanId;
    private Integer payments;
    private String toAccountNumber;

    public Long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
