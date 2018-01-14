package com.yjb.language.proxy.staticproxy;

public class BuyCarProxy implements IBuyCar{

    private Customer customer;

    public BuyCarProxy(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void buyCar() {
        int cash=customer.getCash();
        if(cash<100000){
            System.out.println("buyCar: 你的钱不够买一辆车");
            return;
        }
        customer.buyCar();
    }
}
