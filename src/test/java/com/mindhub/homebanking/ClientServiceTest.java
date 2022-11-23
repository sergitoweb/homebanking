package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.implement.ClientServiceImp;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientServiceTest {


    ClientRepository clientRepository = mock(ClientRepository.class);

    AccountService accountService = mock(AccountService.class);

    ClientService clientService = new ClientServiceImp(clientRepository,accountService);

    List<Client> clientes = Arrays.asList(
            new Client("Enzo", "Don", "enzo@admin", "123"),
            new Client("Sergio", "Noir", "sergio@sergio", "123"),
            new Client("Lucas", "Tecera", "admin@admin", "123")
    );

    @Test
    public void findByEmailTest(){
        when(clientRepository.findByEmail("enzo@admin")).thenReturn(Optional.of(clientes.get(0)));
        Client client = clientService.findByEmail("enzo@admin").orElse(null);
        Assert.assertEquals(clientes.get(0).getEmail(),client.getEmail());
    }

    @Test
    public void findByEmailTestFail(){
        when(clientRepository.findByEmail("enzo@admin")).thenReturn(Optional.of(clientes.get(1)));
        Client client = clientService.findByEmail("enzo@admin").orElse(null);
        Assert.assertNotEquals(clientes.get(0),client.getEmail());

    }

    @Test
    public void agregarCliente(){
        when(clientRepository.save(clientes.get(0))).thenReturn(clientes.get(0));
        String result = clientService.agregarCliente("Sergio","Noir","noir@admin.com","123");
        assertNotNull(result);
        assertEquals("mensaje.exito",result);
    }

    @Test
    public void agregarClienteFail(){
        when(clientRepository.save(clientes.get(0))).thenReturn(clientes.get(0));
        when(clientRepository.findByEmail("sergio@sergio")).thenReturn(Optional.of(clientes.get(1)));
        String result = clientService.agregarCliente("Sergio","Noir","sergio@sergio","123");
        assertNotNull(result);
        assertEquals("mensaje.emailClient.fail",result);
    }

    /*
    @Test
    public void showAll(){
        when(clientRepository.findAll()).thenReturn(clientes);
        List<ClientDTO> listaClientes= clientService.showAll();
        assertNotNull(listaClientes);
        List<ClientDTO> clientesDTO=clientes.stream().map(ClientDTO::new).collect(Collectors.toList());
        boolean resultado = listaClientes
        //assertEquals(clientes.stream().map(ClientDTO::new).collect(Collectors.toList()), listaClientes);
    }*/

}
