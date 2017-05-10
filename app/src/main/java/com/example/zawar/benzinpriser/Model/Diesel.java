package com.example.zawar.benzinpriser.Model;

/**
 * Created by Zawar on 14-04-2017.
 */

public class Diesel {

    public String price;
    public String time;

    public Diesel(){

    }

    public Diesel(String price, String time) {
        this.price = price;
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}
