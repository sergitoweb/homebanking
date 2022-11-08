package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByName(String name);

    List<Loan> findByNameContaining(String name);

    List<Loan> findByMaxAmountLessThan(Double maxAmount);

}
