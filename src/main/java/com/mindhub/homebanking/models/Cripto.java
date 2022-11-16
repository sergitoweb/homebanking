package com.mindhub.homebanking.models;

public class Cripto {
        private double ask;
        //precio para comprar cripto
        private float totalAsk;
        private double bid;
        //precio para vender cripto
        private float totalBid;
        private double time;

        private MoneyType name;


    public Cripto() {
    }

    public Cripto(double ask, float totalAsk, double bid, float totalBid, double time) {
        this.ask = ask;
        this.totalAsk = totalAsk;
        this.bid = bid;
        this.totalBid = totalBid;
        this.time = time;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public float getTotalAsk() {
        return totalAsk;
    }

    public void setTotalAsk(float totalAsk) {
        this.totalAsk = totalAsk;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public float getTotalBid() {
        return totalBid;
    }

    public void setTotalBid(float totalBid) {
        this.totalBid = totalBid;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public MoneyType getName() {
        return name;
    }

    public void setName(MoneyType name) {
        this.name = name;
    }
}
