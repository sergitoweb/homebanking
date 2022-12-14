package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.services.CriptoService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CriptoServiceImp implements CriptoService {


    private TransactionService transactionService;

    public CriptoServiceImp(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public String comprarCripto(Client client, float amountArsBuy, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount) {
        //totalAsk - precio de compra con comisiones incluida
        float montoCripto = (float)amountArsBuy/cripto.getTotalAsk();

      String debitAccount= transactionService.makeTransactionCripto(montoCripto,amountArsBuy, "Buy cripto "+cripto.getName(),originAccount, destinationAccount, client,tipomoneda);

        return debitAccount;
    }

    @Override
    public String venderCripto(Client client, float amountCriptoSell, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount){
        //totalBid - precio de venta con comisiones incluida
        float amountArsGet = amountCriptoSell * cripto.getTotalBid();
        String creditAccount = transactionService.makeTransactionCripto(amountCriptoSell,amountArsGet, "Sell cripto "+cripto.getName(),originAccount, destinationAccount, client,tipomoneda);
        return creditAccount;
    }

    @Override
    public Cripto verCotizacion(String coin){

            String url = "https://criptoya.com/api/decrypto/"+coin+"/ars";
            RestTemplate restTemplate = new RestTemplate();

            //con restemplate lo capturo y convierto en cripto
            Cripto cripto = restTemplate.getForEntity(url, Cripto.class).getBody();
            cripto.setName(MoneyType.valueOf(coin.toUpperCase()));

        return cripto;
    }


}
