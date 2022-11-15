package com.mindhub.homebanking.models;

public class Cripto {
        private double ask;
        private double totalAsk;
        private double bid;
        private double totalBid;
        private double time;

        private MoneyType name;


    public Cripto() {
    }

    public Cripto(double ask, double totalAsk, double bid, double totalBid, double time) {
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

    public double getTotalAsk() {
        return totalAsk;
    }

    public void setTotalAsk(double totalAsk) {
        this.totalAsk = totalAsk;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getTotalBid() {
        return totalBid;
    }

    public void setTotalBid(double totalBid) {
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
