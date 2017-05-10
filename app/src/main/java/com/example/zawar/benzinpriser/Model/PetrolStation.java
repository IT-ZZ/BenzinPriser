package com.example.zawar.benzinpriser.Model;
import com.example.zawar.benzinpriser.R;


/**
 * Created by Zawar on 14-04-2017.
 */

public class PetrolStation {


    public String address;
    public String postCode;
    public String name;
    public String key;
    public Prices prices;

    public PetrolStation() {

    }


    public PetrolStation(String address, String postCode, String name, String key, Prices prices) {
        this.address = address;
        this.postCode = postCode;
        this.name = name;
        this.key = key;
        this.prices = prices;
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Prices getPrices() {
        return prices;
    }


    public int getLogo(String s) {

        int x = 0;
        if (s.startsWith("Shell")) {
            x = R.drawable.shell;
        }
        else if (s.startsWith("OK")) {
            x = R.drawable.ok;
        }
        else if (s.startsWith("Uno-X")) {
            x = R.drawable.unox;
        }

        else if (s.startsWith("Q8")) {
            x = R.drawable.q8;
        }

        else if (s.startsWith("Ingo")) {
            x = R.drawable.ingo;
        }

        else if (s.startsWith("Circle")) {
            x = R.drawable.circlek;
        }
        else{
            x = R.drawable.logo;
        }

        return x;
    }
}

