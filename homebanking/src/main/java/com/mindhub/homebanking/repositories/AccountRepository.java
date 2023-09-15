package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByNumber(String number);
    boolean existsByNumber(String accountNumber);
    Account save(Account account);
    List<Account> findAll();
    int countActiveAccountsByClientAndActiveIsTrue(Client client);
}
