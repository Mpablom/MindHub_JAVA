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
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("STARTED!!");
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {

			LocalDate today = LocalDate.now();

			Client client1 = new Client("Melba", "Morel","melba@mindhub.com");
			clientRepository.save(client1);

			Client client2 = new Client("Pablo", "Moya","pablomoya@mindhub.com");
			clientRepository.save(client2);

			Account account3 = new Account("VIN-003",today,2000,client2);
			accountRepository.save(account3);


			Account account1 = new Account("VIN-001",today,5000,client1);
			accountRepository.save(account1);

			LocalDate tomorrow = today.plusDays(1);
			Account account2 = new Account("VIN-002", tomorrow, 7500,client1);
			accountRepository.save(account2);
		};
	}
}
