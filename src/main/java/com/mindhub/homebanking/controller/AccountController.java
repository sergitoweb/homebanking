package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/clients/current/accounts/{type}")
    public ResponseEntity<Object> createAccount(HttpSession session, @PathVariable String type){


        String result =  accountService.agregarCuenta((Client) session.getAttribute("client"), AccountType.valueOf(type));


        if (result.equals("mensaje.exito")){
           return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()),HttpStatus.OK);

       }else{
           return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
       }
    }

}
