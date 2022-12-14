package com.mindhub.homebanking.controller;

//import com.google.gson.Gson;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.CriptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CriptoController {

    @Autowired
    private CriptoService criptoService;

    @Autowired
    private ClientService clientService;

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
    public ResponseEntity<Object> buyCrypto(Authentication authentication, @RequestParam float amountArsBuy, @RequestParam String tipomoneda, @RequestParam String originAccount , @RequestParam String destinationAccount ) {

        // <-- atributos: num de origen, num destino, monto a comprar, cliente,-->
        Client clientelogueado = clientService.findByEmail(authentication.getName()).orElse(null);
        List<Cripto> cripto = verCotizacion().stream().filter(cripto1 -> cripto1.getName().equals(MoneyType.valueOf(tipomoneda.toUpperCase()))).collect(Collectors.toList());

        String result =criptoService.comprarCripto(clientelogueado,amountArsBuy,cripto.get(0),MoneyType.valueOf(tipomoneda.toUpperCase()),originAccount,destinationAccount);
        if (result.equals("mensaje.exito")){
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.OK);

        }else{
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/crypto/sell")
    public ResponseEntity<Object> sellCrypto(Authentication authentication, @RequestParam float amountCriptoSell, @RequestParam String tipomoneda, @RequestParam String originAccount , @RequestParam String destinationAccount) {

        Client clientelogueado = clientService.findByEmail(authentication.getName()).orElse(null);
        // <-- atributos: num de origen, num destino, monto a comprar, cliente,-->
        List<Cripto> cripto = verCotizacion().stream().filter(cripto1 -> cripto1.getName().equals(MoneyType.valueOf(tipomoneda.toUpperCase()))).collect(Collectors.toList());

        String result =criptoService.venderCripto(clientelogueado,amountCriptoSell,cripto.get(0),MoneyType.valueOf(tipomoneda.toUpperCase()),originAccount, destinationAccount);

        if (result.equals("mensaje.exito")){
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.OK);

        }else{
            return new ResponseEntity<>(mensajes.getMessage(result, null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
        }
    }


}
