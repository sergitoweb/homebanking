package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CriptoService {

    @Autowired
    private TransactionService transactionService;


    public String comprarCripto(Client client, float amountArsBuy, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount) {
        //totalAsk - precio de compra con comisiones incluida
        float montoCripto = (float)amountArsBuy/cripto.getTotalAsk();

      String debitAccount= transactionService.makeTransactionCripto(montoCripto,amountArsBuy, "Buy cripto "+cripto.getName(),originAccount, destinationAccount, client,tipomoneda);

        return debitAccount;
    }


    public String venderCripto(Client client, float amountCriptoSell, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount){
        //totalBid - precio de venta con comisiones incluida
        float amountArsGet = amountCriptoSell * cripto.getTotalBid();
        String creditAccount = transactionService.makeTransactionCripto(amountCriptoSell,amountArsGet, "Sell cripto "+cripto.getName(),originAccount, destinationAccount, client,tipomoneda);
        return creditAccount;
    }


    public Cripto verCotizacion(String coin){

            String url = "https://criptoya.com/api/decrypto/"+coin+"/ars";
            RestTemplate restTemplate = new RestTemplate();

            //con restemplate lo capturo y convierto en cripto
            Cripto cripto = restTemplate.getForEntity(url, Cripto.class).getBody();
            cripto.setName(MoneyType.valueOf(coin.toUpperCase()));

        return cripto;
    }


}
