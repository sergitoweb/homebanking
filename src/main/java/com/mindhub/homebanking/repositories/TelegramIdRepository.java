package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.TelegramId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramIdRepository extends JpaRepository<TelegramId, Long> {


    Optional<TelegramId> findByChatId(long chatId);

}
