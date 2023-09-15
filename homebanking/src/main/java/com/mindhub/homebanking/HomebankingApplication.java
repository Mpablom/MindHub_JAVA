package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.*;


@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("STARTED!!");
	}
}