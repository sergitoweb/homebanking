package com.mindhub.homebanking;

import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.utils.AccountUtils;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class AccountUtilsTest {

    @Test
    public void accountNumberIsCreated(){
        String cardNumber = AccountUtils.getAccountNumber(AccountType.VIN);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void accountNumberIsFalse(){
        String cardNumber = AccountUtils.getAccountNumber(AccountType.CRY);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
}
