package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    CardRepository cardRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }



    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }



    @org.junit.jupiter.api.Test
    void testFindAllClients(){
        List<Client> clients = clientRepository.findAll();
        assertFalse(clients.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void testFindClientById() {
        Optional<Client> client = clientRepository.findById(1L);
        assertTrue(client.isPresent());
        assertEquals("tomas", client.orElseThrow().getFirstName());
    }

    @org.junit.jupiter.api.Test
    void testFindAccountAll(){
        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());

    }

    @org.junit.jupiter.api.Test
    void testFindAccountById(){
        Optional<Account> account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("VIN005", account.orElseThrow().getNumber());
    }

    @org.junit.jupiter.api.Test
    void testFindAllTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertFalse(transactions.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void testFindTransactionById(){
        Optional<Transaction> transaction = transactionRepository.findById(1L);
        assertTrue(transaction.isPresent());
        assertEquals(TransactionType.CREDIT, transaction.orElseThrow().getType());
    }

    @org.junit.jupiter.api.Test
    void testFindAllLoan(){
        List<Loan> loans =loanRepository.findAll();
        assertFalse(loans.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void testFindLoanById() {
        Optional<Loan> loan = loanRepository.findById(1L);
        assertTrue(loan.isPresent());
        assertEquals("Hipotecario", loan.orElseThrow().getName());
    }

    @org.junit.jupiter.api.Test
    void testFindAllClientLoan(){
        List<ClientLoan> clientLoans =clientLoanRepository.findAll();
        assertFalse(clientLoans.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void testFindClientLoanById() {
        Optional<ClientLoan> clientLoan = clientLoanRepository.findById(1L);
        assertTrue(clientLoan.isPresent());
        assertEquals(400000, clientLoan.orElseThrow().getAmount());
        assertEquals(60, clientLoan.orElseThrow().getPayments());
    }

    @org.junit.jupiter.api.Test
    void testFindCardAll(){
        List<Card> cards = cardRepository.findAll();
        assertFalse(cards.isEmpty());

    }

    @org.junit.jupiter.api.Test
    void testFindCardById(){
        Optional<Card> card = cardRepository.findById(1L);
        assertTrue(card.isPresent());
        assertEquals(1L, card.orElseThrow().getClient().getId());
    }

}
