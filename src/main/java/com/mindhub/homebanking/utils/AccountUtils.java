package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.AccountType;

public final class AccountUtils {

    public AccountUtils() {
    }

    //genero aleatoriamente el numero de cuenta y concateno
    public static String getAccountNumber(AccountType type) {
        String numCuenta = type.name() + (int) (Math.random()*(99999999-0)+0);
        return numCuenta;
    }
}
