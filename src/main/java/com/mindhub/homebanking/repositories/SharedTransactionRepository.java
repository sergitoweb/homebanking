package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.dtos.SharedTransactionDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.SharedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.OptionalDouble;

@RepositoryRestResource
public interface SharedTransactionRepository extends JpaRepository<SharedTransaction, Long> {


    Optional<SharedTransaction> findByTokenId(String tokenId);
}
