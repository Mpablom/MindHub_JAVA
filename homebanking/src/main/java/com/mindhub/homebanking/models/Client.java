package com.mindhub.homebanking.models;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy = "client",fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    private String password;

//Constructors

    public Client() {
    }
    public Client(String firstName, String lastName, String email,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    //Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addAccount(Account acc) {
        acc.setClient(this);
        accounts.add(acc);
    }
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addClientLoans(ClientLoan cl) {
        cl.setClient(this);
        clientLoans.add(cl);
    }
    public Set<Card> getCards() {
        return cards;
    }
    public void addCards(Card card) {
        card.setClient(this);
        cards.add(card);
    }
    public Set<Loan> getLoans() {
        Set<Loan> loans = new HashSet<>();
        for (ClientLoan clientLoan : clientLoans) {
            loans.add(clientLoan.getLoan());
        }
        return loans;
    }
}
