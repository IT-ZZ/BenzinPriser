package com.example.zawar.benzinpriser.Model;

/**
 * Created by Zawar on 14-04-2017.
 */

public class Blyfri {

    public String price;
    public String time;

    public Blyfri(){

    }

    public Blyfri(String price, String time) {
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
