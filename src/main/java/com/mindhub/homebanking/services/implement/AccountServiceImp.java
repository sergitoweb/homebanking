package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.utils.AccountUtils.getAccountNumber;
import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImp implements AccountService {


    private AccountRepository accountRepository;


    public AccountServiceImp(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public String agregarCuenta(Client client, AccountType type, MoneyType tipomoneda) {

        if(type == null){
            return "mensaje.type.fail";
        }

        if((type.equals(AccountType.VIN) & (!tipomoneda.equals(MoneyType.ARS)))){
            return "mensaje.typemoney.fail";
        }

        //si el cliente logueado tiene menos de 3 cuentas me deja crear una mas...
        if (client.getAccounts().size()<3) {
            //genero aleatoriamente el numero de cuenta y concateno
            String numCuenta = getAccountNumber(type);

            Account newAccount = new Account(LocalDateTime.now(), 0.0, numCuenta, type, tipomoneda);

            //asignamos el cliente logueado a la cuenta y viceversa
            newAccount.setClient(client);
            client.addAccount(newAccount);

            accountRepository.save(newAccount);
            return "mensaje.exito";
        }else{
            return "mensaje.limit.account.number";
        }
    }


    @Override
    public Optional<Account> getAccountById(@PathVariable long id){
        return accountRepository.findById(id);
    }
    @Override
    public List<AccountDTO> showAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public boolean validarCuenta(String numberAccount){
        return !(accountRepository.findByNumber(numberAccount).orElse(null)==null);
    }
    @Override
    public boolean validarCuenta(Client client, String number){
        return client.getAccounts().stream().anyMatch(account -> account.getNumber().equals(number));
    }
    @Override
    public boolean validarAmount(String number, float amount){
        Account cuenta = accountRepository.findByNumber(number).orElse(null);
        double newbalance = cuenta.getBalance();
        return newbalance>=(double) amount;
    }
    @Override
    public Account obtenerCuenta(String number){
        return accountRepository.findByNumber(number).orElse(null);
    }


}
