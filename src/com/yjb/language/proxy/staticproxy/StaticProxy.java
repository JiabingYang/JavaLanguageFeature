package com.yjb.language.proxy.staticproxy;

public class StaticProxy {

    public static void main(String[] args) {
        Customer customer = new Customer();
        customer.setCash(120000);
        BuyCarProxy buyCarProxy = new BuyCarProxy(customer);
        buyCarProxy.buyCar();

        Customer customer1 =new Customer();
        customer1.setCash(90000);
        BuyCarProxy buyCarProxy1 =new BuyCarProxy(customer1);
        buyCarProxy1.buyCar();
    }
}
