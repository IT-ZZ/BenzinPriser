package com.example.zawar.benzinpriser.Model;

/**
 * Created by Zawar on 14-04-2017.
 */

public class Prices {

    public Blyfri blyfri;
    public Diesel diesel;

    public Prices() {

    }

    public Prices(Blyfri blyfri, Diesel diesel) {
        this.blyfri = blyfri;
        this.diesel = diesel;
    }

    public Blyfri getBlyfri() {
        return blyfri;
    }

    public Diesel getDiesel() {
        return diesel;
    }
}
