package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.utils.AccountUtils.getAccountNumber;
import static java.util.stream.Collectors.toList;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    public String agregarCuenta(Client client) {

        //si el cliente logueado tiene menos de 3 cuentas me deja crear una mas...
        if (client.getAccounts().size()<3) {
            //genero aleatoriamente el numero de cuenta y concateno
            String numCuenta = getAccountNumber();

            //creamos la cuenta nueva
            Account newAccount = new Account(LocalDateTime.now(), 0.0, numCuenta);

            //asignamos el cliente logueado a la cuenta y viceversa
            newAccount.setClient(client);
            client.addAccount(newAccount);

            accountRepository.save(newAccount);
            return "mensaje.exito";
        }else{
            return "mensaje.limit.account.number";
        }
    }



    public Optional<Account> getAccountById(@PathVariable long id){
        return accountRepository.findById(id);
    }

    public List<AccountDTO> showAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }


    public boolean validarCuenta(String numberAccount){
        return !(accountRepository.findByNumber(numberAccount).orElse(null)==null);
    }

    public boolean validarCuenta(Client client, String number){
        return client.getAccounts().stream().anyMatch(account -> account.getNumber().equals(number));
    }

    public boolean validarAmount(String number, long amount){
        Account cuenta = accountRepository.findByNumber(number).orElse(null);
        return cuenta.getBalance()>=amount;
    }


}
