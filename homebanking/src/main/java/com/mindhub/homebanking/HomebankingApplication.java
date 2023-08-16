package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CREDIT_CARD;
import static com.mindhub.homebanking.models.CardType.DEBIT_CARD;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("STARTED!!");
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			LocalDate today = LocalDate.now().plusDays(1);
			LocalDate tomorrow = today.plusDays(1);

			/*Creación de clientes 1 y 2 */

			Client client1 = new Client("Melba", "Morel","melba@mindhub.com");
			clientRepository.save(client1);

			Client client2 = new Client("Pablo", "Moya","pablomoya@mindhub.com");
			clientRepository.save(client2);

			/*Creación de account 3*/

			Account account3 = new Account("VIN-003",today,2000,client2);
			accountRepository.save(account3);

			/*Creación de account 1 y 2*/

			Account account1 = new Account("VIN-001",today,5000,client1);
			accountRepository.save(account1);
			Account account2 = new Account("VIN-002", tomorrow, 7500,client1);
			accountRepository.save(account2);

			/*Transacciones para account 1 y 2*/

			LocalDateTime todayTransactions = LocalDateTime.now();

			Transaction transaction1 = new Transaction(CREDIT,1000,"various",todayTransactions,account1);
			transactionRepository.save(transaction1);
			Transaction transaction2 = new Transaction(DEBIT,-700,"debit coke",todayTransactions,account1);
			transactionRepository.save(transaction2);
			Transaction transaction3 = new Transaction(CREDIT,210000,"crediting of assets",todayTransactions,account2);
			transactionRepository.save(transaction3);
			Transaction transaction4 = new Transaction(DEBIT,-50000,"payment of services",todayTransactions,account2);
			transactionRepository.save(transaction4);

			/*Transacciones para account 3*/

			Transaction transaction5 = new Transaction(CREDIT,150000,"crediting of assets",todayTransactions,account3);
			transactionRepository.save(transaction5);
			Transaction transaction6 = new Transaction(DEBIT,-100000,"payment of services",todayTransactions,account3);
			transactionRepository.save(transaction6);

			/*Creación de Loans*/

			/*List<Integer> mortage =Stream.of(12,24,36,48,60).collect(toCollection(ArrayList::new));
			List<Integer> personal =Stream.of(6,12,24).collect(toCollection(ArrayList::new));
			List<Integer> automotive =Stream.of(6,12,24,36).collect(toCollection(ArrayList::new));*/

			List<Integer> payments = Stream.of(6,12, 24, 36, 48, 60)
					.collect(Collectors.toList());

			Loan loan1 = new Loan("Mortgage",500000,payments.subList(1,6));
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal",100000,payments.subList(0,3));
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Automotive",300000,payments.subList(0,4));
			loanRepository.save(loan3);

			/*Creación de los loan para clients*/

			ClientLoan clientLoan1 = new ClientLoan(400000,60,client1,loan1);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(50000,12,client1,loan2);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan(100000,24,client2,loan2);
			clientLoanRepository.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan(200000,36,client2,loan3);
			clientLoanRepository.save(clientLoan4);

			/*Creación de Cards*/
			LocalDate fromDate = LocalDate.now();
			LocalDate thruDate = fromDate.plusYears(5);
			String cardholder = client1.getFirstName() +" "+client1.getLastName();
			String cardholder2 = client2.getFirstName()+" "+client2.getLastName();

			Card card1 = new Card(cardholder,DEBIT_CARD,GOLD,"1234 5678 9012 3456",123,thruDate,fromDate,client1);
			cardRepository.save(card1);
			Card card2 = new Card(cardholder,CREDIT_CARD,TITANIUM,"9876 5432 1098 7654",321,thruDate,fromDate,client1);
			cardRepository.save(card2);

			Card card3 = new Card(cardholder2,CREDIT_CARD,SILVER,"1231 2312 3123 1231",678,thruDate,fromDate,client2);
			cardRepository.save(card3);
		};
	}
}
