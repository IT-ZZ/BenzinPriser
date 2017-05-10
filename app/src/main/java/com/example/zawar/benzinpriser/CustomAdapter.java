package com.example.zawar.benzinpriser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zawar.benzinpriser.Model.PetrolStation;

import java.util.ArrayList;

/**
 * Created by Zawar on 16-04-2017.
 */

public class CustomAdapter extends ArrayAdapter <PetrolStation> {


    private String fuelType;

    public CustomAdapter(Context context, ArrayList<PetrolStation> users) {
        super(context,0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PetrolStation petrolStation = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_benzprices, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.nameView);
        TextView address = (TextView) convertView.findViewById(R.id.addView);
        TextView price = (TextView) convertView.findViewById(R.id.priceView);
        ImageView logo = (ImageView) convertView.findViewById(R.id.logoView);

        // Populate the data into the template view using the data object
        name.setText(petrolStation.getName());
        address.setText(petrolStation.getAddress() +" , "+petrolStation.getPostCode());
//        Set logo for differnet petrolstaions
        logo.setImageResource(petrolStation.getLogo(petrolStation.getName()));


        if(fuelType.startsWith("D")){

            price.setText(petrolStation.getPrices().getDiesel().getPrice());
        }

        else{
            price.setText(petrolStation.getPrices().getBlyfri().getPrice());
        }
        // Return the completed view to render on screen
        return convertView;
    }

    public void setFuelType(String fuel){
        this.fuelType=fuel;
    }



}
