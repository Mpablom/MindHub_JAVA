package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Client;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loans = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();
    private Set<String> roles = new HashSet<>();
    private int activeAccountCount;

//Constructor

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(toSet());
        this.loans = client.getClientLoans().stream()
                .map(ClientLoanDTO::new)
                .collect(Collectors.toSet());
        this.cards = client.getCards().stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
        this.activeAccountCount = client.getActiveAccountCount();
    }

    //Getters

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    public Set<String> getRoles() {
        return roles;
    }
    public int getActiveAccountCount() {
        return activeAccountCount;
    }
}
