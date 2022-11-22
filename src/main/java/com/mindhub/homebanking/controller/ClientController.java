package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.ClientServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageSource mensajes;

    @PostMapping("")
    public ResponseEntity<Object> register(@Valid @RequestParam String firstName,@Valid @RequestParam String lastName,@Valid @RequestParam String email,@NotEmpty @RequestParam String password) {

        //sino crea un cliente con los datos cargados y la password encriptada
        String result =clientService.agregarCliente(firstName, lastName, email, password);

        if (result.equals("mensaje.exito")) {
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }
    }

    //mostrar todos los clientes
    @GetMapping("")
    public List<ClientDTO> getClients() {
        return clientService.showAll();
    }

    //devuelve el cliente de ese id - en desuso
    @GetMapping("/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return clientService.getClientAccounts(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/current")
    public ClientDTO getClientByEmail(Authentication authentication){
        return new ClientDTO((Client) clientService.findByEmail(authentication.getName()).orElse(null));
    }

    //muestra las cuentas de un cliente logueado
    @GetMapping("/current/accounts")
    public Set<AccountDTO> getClientAccount(Authentication authentication) {

        Client clientelogueado = clientService.findByEmail(authentication.getName()).orElse(null);
        //invocamos al cliente logueado y le pedimos las cuentas...
        return (clientelogueado.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet()));
    }

    //muestra las card de un cliente logueado
    @GetMapping("/current/cards")
    public Set<CardDTO> getClientCard(Authentication authentication) {
        Client clientelogueado = clientService.findByEmail(authentication.getName()).orElse(null);
        return clientelogueado.getCards().stream().filter(card -> card.isActive() == true).map(CardDTO::new).collect(Collectors.toSet());
    }

}

