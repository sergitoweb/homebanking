package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.models.MoneyType;

public interface CriptoService {

    public String comprarCripto(Client client, float amountArsBuy, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount);

    public String venderCripto(Client client, float amountCriptoSell, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount);

    public Cripto verCotizacion(String coin);
}
