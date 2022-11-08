package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAmountLessThan (long amount);
    List<Transaction> findByDate (LocalDateTime date);
    List<Transaction> findByDescriptionContaining(String description);

}
