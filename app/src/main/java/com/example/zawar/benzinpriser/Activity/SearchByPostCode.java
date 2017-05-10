package com.example.zawar.benzinpriser.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zawar.benzinpriser.R;

public class SearchByPostCode extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button price;
    Spinner  fuelTypeSpinner, postNumberSpinner;
    private String radius,fuelType,postNr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_post_code);


        fuelTypeSpinner = (Spinner) findViewById(R.id.fuelspinner);
        postNumberSpinner = (Spinner) findViewById(R.id.postNumberspinner);
        price = (Button)findViewById(R.id.seachPrices);
        price.setOnClickListener(this);


        ArrayAdapter<CharSequence> arrayAdapterFuelType = ArrayAdapter.createFromResource(this,R.array.fuelArray,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> arrayAdapterPostNumber = ArrayAdapter.createFromResource(this,R.array.postNumber,android.R.layout.simple_spinner_item);



        arrayAdapterFuelType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterPostNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fuelTypeSpinner.setAdapter(arrayAdapterFuelType);
        postNumberSpinner.setAdapter(arrayAdapterPostNumber);
        fuelTypeSpinner.setOnItemSelectedListener(this);
        postNumberSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == price) {

                Intent intent = new Intent(this, BenzPricesActivity.class);

                String postNr[] = postNumberSpinner.getSelectedItem().toString().split(" ");
//                int valueOfRadius = Integer.parseInt(radius[0]);

                String fuelType = fuelTypeSpinner.getSelectedItem().toString();

                intent.putExtra("fuelType", fuelType);
                intent.putExtra("postNr", postNr[0]);

                startActivity(intent);
            }
        }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;

        switch (spinner.getId()){
            case R.id.fuelspinner:
                fuelType = String.valueOf(parent.getItemAtPosition(position));
                break;
            case R.id.postNumberspinner:
                postNr = String.valueOf(parent.getItemAtPosition(position));
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
