package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("STARTED!!");
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel","melba@mindhub.com");
			clientRepository.save(client1);

			LocalDate today = LocalDate.now();
			Account account1 = new Account("1a2b",today,5000,client1);
			accountRepository.save(account1);

			LocalDate tomorrow = today.plusDays(1);
			Account account2 = new Account("789012", tomorrow, 7500,client1);
			accountRepository.save(account2);
		};
	}
}
