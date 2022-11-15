package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageSource mensajes;

    //muestra todas las cuentas registradas
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.showAll();
    }

    //ver cuenta por su id
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountService.getAccountById(id).map(AccountDTO::new).orElse(null);
    }

    //un cliente logueado puede crear una cuenta y debe indicar el tipo CRY o VIN
    @PostMapping("/clients/current/accounts/{type},{typeMoney}")
    public ResponseEntity<Object> createAccount(HttpSession session, @PathVariable String type,@PathVariable String typeMoney){


        String result =  accountService.agregarCuenta((Client) session.getAttribute("client"), AccountType.valueOf(type),MoneyType.valueOf(typeMoney));


        if (result.equals("mensaje.exito")){
           return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()),HttpStatus.OK);

       }else{
           return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
       }
    }


    @GetMapping("/crypto/{coin}") // solo puede recibir btc-usdt-dai
    public Cripto verCotizacion(@PathVariable String coin){
        String url = "https://criptoya.com/api/decrypto/"+coin+"/ars";
        RestTemplate restTemplate = new RestTemplate();

        //con restemplate lo capturo y convierto en string
        String cripto = restTemplate.getForObject(url, String.class);

        //con gson paso los string a cada atributo del mismo nombre...
        Cripto data = new Gson().fromJson(cripto, Cripto.class);

        data.setName(MoneyType.valueOf(coin.toUpperCase()));

        //cuando vaya a comprar cripto setear el tipobalance de account...
        return data;
    }

}
