package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private ClientLoanRepository clientLoanRepository;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CardRepository cardRepository;


	@Bean
	public CommandLineRunner initData() {
		return (args) -> {
			// save a couple of customers

			Client client1 = new Client("tomas", "quinteros", "tomas@admin", passwordEncoder.encode("123"));
			Client client2 = new Client("jose", "perez", "jose@jose", passwordEncoder.encode("123"));
			Client client3 = new Client("admin", "admin", "admin@admin", passwordEncoder.encode("123"));


			Account account1 = new Account(LocalDateTime.parse("2021-09-08T00:00:00"),10000,"VIN005",AccountType.VIN);
			Account account2 = new Account(LocalDateTime.parse("2022-09-08T00:00:00"),40000,"VIN006",AccountType.VIN);
			Account account3 = new Account(LocalDateTime.parse("2022-09-08T00:00:00"),12000,"VIN007",AccountType.VIN);
			Account account4 = new Account(LocalDateTime.parse("2022-09-08T00:00:00"),36000,"VIN008",AccountType.VIN);


			LocalDateTime ldt = LocalDateTime.parse("2025-09-08T00:00:00");
			Transaction t1 = new Transaction(2000, "Transferencia recibida", ldt, TransactionType.CREDIT);
			Transaction t2 = new Transaction(4000, "Compra tienda xx", ldt, TransactionType.DEBIT);
			Transaction t3 = new Transaction(1000, "Transferencia recibida", ldt, TransactionType.CREDIT);
			Transaction t4 = new Transaction(200, "Compra tienda xx", ldt, TransactionType.DEBIT);
			Transaction t5 = new Transaction(8000, "Transferencia recibida", ldt, TransactionType.CREDIT);
			Transaction t6 = new Transaction(2000, "Compra tienda xx", ldt, TransactionType.DEBIT);
			Transaction t7 = new Transaction(700, "Transferencia recibida", ldt, TransactionType.CREDIT);
			Transaction t8 = new Transaction(2000, "Compra tienda xx", ldt, TransactionType.DEBIT);

			account1.addTransaction(t1); // transactions
			account1.addTransaction(t2); // transactions
			account2.addTransaction(t3); // transactions
			account2.addTransaction(t4); // transactions
			account3.addTransaction(t5); // transactions
			account3.addTransaction(t6); // transactions
			account4.addTransaction(t7); // transactions
			account4.addTransaction(t8); // transactions

			account1.setClient(client1);
			account2.setClient(client1);
			account3.setClient(client3);
			account4.setClient(client3);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client3.addAccount(account3);
			client3.addAccount(account3);
			client3.addAccount(account4);

			Loan loan1 = new Loan("Hipotecario", 500000.0);
			Loan loan2 = new Loan("Personal", 100000.0);
			Loan loan3 = new Loan("Automotriz", 300000.0);

			loan1.addPayment(12);
			loan1.addPayment(24);
			loan1.addPayment(36);
			loan1.addPayment(48);
			loan1.addPayment(60);

			loan2.addPayment(6);
			loan2.addPayment(12);
			loan2.addPayment(24);

			loan3.addPayment(6);
			loan3.addPayment(12);
			loan3.addPayment(24);
			loan3.addPayment(36);

			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, client1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24, client3, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36, client3, loan3);



			Card card1 = new Card("Tomas Quinteros","3325-6745-7876-4445", 990, LocalDateTime.parse("2023-09-08T00:00:00"), LocalDateTime.parse("2022-09-08T00:00:00"), CardType.CREDIT,CardColor.GOLD);
			Card card2 = new Card("Admin","1925-6745-7876-4445", 222, LocalDateTime.parse("2023-09-08T00:00:00"),LocalDateTime.parse("2022-09-08T00:00:00"), CardType.CREDIT,CardColor.SILVER);
			Card card3 = new Card("Admin","8889-6745-7876-4445", 350,LocalDateTime.parse("2023-09-08T00:00:00"), LocalDateTime.parse("2022-09-08T00:00:00"), CardType.DEBIT,CardColor.TITANIUM);

			client1.addCard(card1);
			client3.addCard(card2);
			client3.addCard(card3);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			transactionRepository.save(t1);
			transactionRepository.save(t2);
			transactionRepository.save(t3);
			transactionRepository.save(t4);
			transactionRepository.save(t5);
			transactionRepository.save(t6);
			transactionRepository.save(t7);
			transactionRepository.save(t8);

		};
	}

}
