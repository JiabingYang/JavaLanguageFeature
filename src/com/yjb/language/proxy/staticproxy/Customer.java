package com.yjb.language.proxy.staticproxy;

public class Customer implements IBuyCar {

    private int cash;//购车款

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    @Override
    public void buyCar() {
        System.out.println("buyCar: " + "买一辆车花费了-->" + cash + "元");
    }
}
