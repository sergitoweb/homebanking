package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.SharedTransactionAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SharedTransactionAccountRepository extends JpaRepository<SharedTransactionAccount, Long> {
}
