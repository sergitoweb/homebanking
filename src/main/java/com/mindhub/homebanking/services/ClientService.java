package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    public Optional<Client> findByEmail(String email){

        return clientRepository.findByEmail(email);
    }

    public String agregarCliente(String firstName, String lastName, String email, String password){

        //si el email ya esta cargado, devuelve email en uso
        if (clientRepository.findByEmail(firstName).orElse(null) !=  null) {
            return "mensaje.emailClient.fail";
        }else{
            Client newclient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
            clientRepository.save(newclient);

            //al momento de crear un cliente se le asigna una cuenta VIN
            accountService.agregarCuenta(newclient, AccountType.VIN);
            return "mensaje.exito";
        }
    }

    public List<ClientDTO> showAll() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }


    public Optional<Client> getClientAccounts (long id){
        return clientRepository.findById(id);
    }
}
