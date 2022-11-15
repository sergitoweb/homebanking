package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.models.MoneyType;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class CriptoService {

    @Autowired
    private TransactionService transactionService;


    public String comprarCripto(Client client, Long amountArsBuy, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount) {


        /*
        *   cuenta de origen: debitar (restar) ars siempre que sea del cliente logueado
        *   validar cuenta y monto para saber si tiene fondo. accountService
        *
        *   cuenta destino: acreditar (sumar) coin luego de verificar que pertenesca la cliente logueado y que sea de tipo coin(cry)
        *
        *   si no tiene cuenta cry se debe lanzar un error
        *
        *
        *   totalAsk tiene el precio de la cripto a comprar
        *   amountBuy tiene el monto en ars que quiere comprar el cliente ( por ejemplo: quiere comprar 2mil pesos en btc)
        *   2mil ARS pero btc sale 5millones, division
        */

        amountArsBuy = amountArsBuy/(long)cripto.getTotalAsk();

        String debitAccount= transactionService.debitTransaction(amountArsBuy, "Buy cripto "+cripto.getName(), originAccount, client,tipomoneda);

        return debitAccount;
    }
}
