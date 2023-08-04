package com.mindhub.homebanking.models;

import jakarta.persistence.*;

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
    Set<Account> account = new HashSet<>();

    public Client() {
    }

    public Client(String firstName, String lastName, String email, Set<Account> account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.account = account;
    }

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

    public Set<Account> getAccount() {
        return account;
    }

    public void addAccount(Account acc) {
        acc.setClient(this);
        account.add(acc);
    }
}
