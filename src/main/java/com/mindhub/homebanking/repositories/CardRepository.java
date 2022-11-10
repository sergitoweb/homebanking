package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByCardHolder (String cardHolder);
    List<Card> findByThruDateOrderByCardHolder(LocalDateTime thruDate);

    Optional<Card> findByNumber(String number);


    @Query("SELECT c FROM Card c WHERE number=?1")
    Optional<Card> buscarPorNumero(String number);

    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.active = ?2 WHERE c.id = ?1")
    int updateCardById(long id, boolean b);
}
