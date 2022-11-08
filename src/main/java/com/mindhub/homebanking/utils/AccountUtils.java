package com.mindhub.homebanking.utils;

public final class AccountUtils {

    public AccountUtils() {
    }

    //genero aleatoriamente el numero de cuenta y concateno
    public static String getAccountNumber() {
        String numCuenta = "VIN-" + (int) (Math.random()*(99999999-0)+0);
        return numCuenta;
    }
}
