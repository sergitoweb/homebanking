package com.mindhub.homebanking.models;

public class Cripto {
        private float ask;
        private float totalAsk;
        private float bid;
        private float totalBid;
        private float time;

        private CriptoType name;

    public Cripto(float ask, float totalAsk, float bid, float totalBid, float time) {
        this.ask = ask;
        this.totalAsk = totalAsk;
        this.bid = bid;
        this.totalBid = totalBid;
        this.time = time;
    }

        public float getAsk() {
            return ask;
        }

        public float getTotalAsk() {
            return totalAsk;
        }

        public float getBid() {
            return bid;
        }

        public float getTotalBid() {
            return totalBid;
        }

        public float getTime() {
            return time;
        }

        // Setter Methods

        public void setAsk( float ask ) {
            this.ask = ask;
        }

        public void setTotalAsk( float totalAsk ) {
            this.totalAsk = totalAsk;
        }

        public void setBid( float bid ) {
            this.bid = bid;
        }

        public void setTotalBid( float totalBid ) {
            this.totalBid = totalBid;
        }

        public void setTime( float time ) {
            this.time = time;
        }

    public CriptoType getName() {
        return name;
    }

    public void setName(CriptoType name) {
        this.name = name;
    }
}
