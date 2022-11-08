package com.mindhub.homebanking.utils;

public final class CardUtils {

    private CardUtils() {
    }

    public static int getCardCvv() {
        int cvv = (int) (Math.random()*(899-000)+100);
        return cvv;
    }

    public static String getCardNumber() {
        String number = (int) (Math.random()*(8999-0000)+1000) + "-" +(int) (Math.random()*(8999-0000)+1000)+"-"+(int) (Math.random()*(8999-0000)+1000) + "-" +(int) (Math.random()*(8999-0000)+1000);
        return number;
    }
}
