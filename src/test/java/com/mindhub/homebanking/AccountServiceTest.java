package com.mindhub.homebanking;



import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.implement.AccountServiceImp;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    AccountRepository accountRepository = mock(AccountRepository.class);
    AccountService accountService = new AccountServiceImp(accountRepository);

    List<Account> cuentas = Arrays.asList(
            new Account(LocalDateTime.parse("2021-09-08T00:00:00"),10000,"VIN005", AccountType.VIN, MoneyType.ARS),
            new Account(LocalDateTime.parse("2022-09-08T00:00:00"),40000,"VIN006",AccountType.VIN,MoneyType.ARS),
            new Account(LocalDateTime.parse("2022-09-08T00:00:00"),12000,"VIN007",AccountType.VIN,MoneyType.ARS),
            new Account(LocalDateTime.parse("2022-09-08T00:00:00"),36000,"VIN008",AccountType.VIN,MoneyType.ARS),
            new Account(LocalDateTime.parse("2021-09-08T00:00:00"),1,"CRY001",AccountType.CRY,MoneyType.DAI),
            new Account(LocalDateTime.parse("2022-09-08T00:00:00"),10000,"VIN010",AccountType.VIN,MoneyType.ARS)
    );

    @Test
    public void testAgregarCuenta(){
        when(accountRepository.save(cuentas.get(0))).thenReturn(cuentas.get(0));
        Client client = mock(Client.class);
        String result = accountService.agregarCuenta(client,AccountType.VIN,MoneyType.ARS);
        assertNotNull(result);
        assertEquals("mensaje.exito",result);
    }

    @Test
    public void testGetAccountById(){
        when(accountRepository.findById(1L)).thenReturn(Optional.of(cuentas.get(0)));
        Account cuenta = accountService.getAccountById(1L).orElse(null);
        assertNotNull(cuenta);
        assertEquals(cuentas.get(0).getNumber(),cuenta.getNumber());
    }

    @Test
    public void testShowAll(){
        when(accountRepository.findAll()).thenReturn(cuentas);
        assertFalse(accountService.showAll().isEmpty());
    }

    @Test
    public void testValidarCuenta1(){
        when(accountRepository.findByNumber("VIN005")).thenReturn(Optional.of(cuentas.get(0)));
        assertTrue(accountService.validarCuenta("VIN005"));
    }

    @Test
    public void testValidarCuenta2(){
        when(accountRepository.findByNumber("VIN005")).thenReturn(Optional.of(cuentas.get(0)));
        Client client = mock(Client.class);
        assertTrue(accountService.validarCuenta(client,"VIN005"));
    }

    @Test
    public void testValidarAmount(){
        when(accountRepository.findByNumber("VIN005")).thenReturn(Optional.of(cuentas.get(0)));
        Boolean result = accountService.validarAmount("VIN005", 10001f);
        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testObtenerCuenta(){
        when(accountRepository.findByNumber("VIN005")).thenReturn(Optional.of(cuentas.get(0)));
        Account cuenta = accountService.obtenerCuenta("VIN005");
        assertNotNull(cuenta);
        assertEquals(cuentas.get(0).getNumber(),cuenta.getNumber());
    }

}
