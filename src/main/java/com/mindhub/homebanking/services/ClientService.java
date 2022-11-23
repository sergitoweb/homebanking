package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    public Optional<Client> findByEmail(String email);

    public String agregarCliente(String firstName, String lastName, String email, String password);

    public List<ClientDTO> showAll();

    public Optional<Client> getClientAccounts (long id);
}
