package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImp implements ClientService {


    private ClientRepository clientRepository;

    private AccountService accountService;


    public ClientServiceImp(ClientRepository clientRepository, AccountService accountService) {
        this.clientRepository = clientRepository;
        this.accountService = accountService;
    }

    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

    @Override
    public Optional<Client> findByEmail(String email){

        return clientRepository.findByEmail(email);
    }

    @Override
    public String agregarCliente(String firstName, String lastName, String email, String password){

        //si el email ya esta cargado, devuelve email en uso
        if (clientRepository.findByEmail(firstName).orElse(null) !=  null) {
            return "mensaje.emailClient.fail";
        }else{
            Client newclient = new Client(firstName, lastName, email, passwordEncoder().encode(password));
            clientRepository.save(newclient);

            //al momento de crear un cliente se le asigna una cuenta VIN
            accountService.agregarCuenta(newclient, AccountType.VIN, MoneyType.ARS);
            return "mensaje.exito";
        }
    }

    @Override
    public List<ClientDTO> showAll() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public Optional<Client> getClientAccounts (long id){
        return clientRepository.findById(id);
    }
}
