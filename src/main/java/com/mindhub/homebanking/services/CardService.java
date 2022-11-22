package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

public interface CardService {

    public String agregarCard(Client client, CardColor color, CardType type);

    public String deleteCard(Client client, String number);
}
