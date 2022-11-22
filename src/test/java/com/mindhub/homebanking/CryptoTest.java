package com.mindhub.homebanking;


import com.mindhub.homebanking.controller.CriptoController;
import com.mindhub.homebanking.models.Cripto;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CryptoTest {

    @Autowired
    private CriptoController criptoController;

   /* @Autowired
    private CriptoService criptoService;*/

    @Autowired
    private ClientRepository clientRepository;


    List<Cripto> listadocripto= criptoController.verCotizacion();
    @Before
    public void cryptoCheckNotNull(){
       // List<Cripto> listadocripto= criptoController.verCotizacion();
        assertTrue(!(listadocripto.isEmpty()));

    }

 /*  @Test //Client client, float amountArsBuy, Cripto cripto, MoneyType tipomoneda, String originAccount, String destinationAccount
    public void checkBuy(){
        List<Cripto> cripto = listadocripto.stream().filter(cripto1 -> cripto1.getName().equals(MoneyType.valueOf("DAI".toUpperCase()))).collect(Collectors.toList());
        Client client2 = clientRepository.findById(2L).orElse(null);
        String result = criptoService.comprarCripto(client2,5000,cripto.get(0),MoneyType.DAI,"VIN010","CRY001");
       assertNotNull(client2);
       assertNotNull(result);
    }*/

}
