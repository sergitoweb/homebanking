package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.MoneyType;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public String agregarCuenta(Client client, AccountType type, MoneyType tipomoneda);

    public Optional<Account> getAccountById( long id);

    public List<AccountDTO> showAll();

    public boolean validarCuenta(String numberAccount);

    public boolean validarCuenta(Client client, String number);

    public boolean validarAmount(String number, float amount);

    public Account obtenerCuenta(String number);



}
