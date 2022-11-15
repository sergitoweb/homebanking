package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.SharedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SharedTransactionRepository extends JpaRepository<SharedTransaction, Long> {
}
