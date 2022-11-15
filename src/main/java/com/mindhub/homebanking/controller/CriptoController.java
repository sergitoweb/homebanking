package com.mindhub.homebanking.controller;

import com.google.gson.Gson;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CriptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CriptoController {

    @Autowired
    private CriptoService criptoService;

    @Autowired
    private MessageSource mensajes;

    @GetMapping("/crypto")
    public List<Cripto> verCotizacion(){

       List<Cripto> listacripto= new ArrayList<>();
        String[ ] coin = {"BTC", "DAI","USDT"};
        for(int i=0;i<3;i++){
            String url = "https://criptoya.com/api/decrypto/"+coin[i]+"/ars";
            RestTemplate restTemplate = new RestTemplate();

            //con restemplate lo capturo y convierto en cripto
            Cripto cripto = restTemplate.getForEntity(url, Cripto.class).getBody();
            cripto.setName(MoneyType.valueOf(coin[i].toUpperCase()));
            listacripto.add(cripto);
        }

        return listacripto;
    }

    @PostMapping("/crypto/buy")
    public ResponseEntity<Object> buyCrypto(HttpSession session, @RequestParam Long amountArsBuy,@RequestParam double totalAsk, @RequestParam String tipomoneda, @RequestParam String originAccount, @RequestParam String destinationAccount ) {

        // <-- atributos: num de origen, num destino, monto a comprar, cliente,-->


        String result =criptoService.comprarCripto((Client) session.getAttribute("client"),amountArsBuy,totalAsk,MoneyType.valueOf(tipomoneda.toUpperCase()),originAccount,destinationAccount);

        if (result.equals("mensaje.exito")){
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.OK);

        }else{
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }
    }
}
