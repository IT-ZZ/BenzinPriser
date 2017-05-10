package com.example.zawar.benzinpriser.Activity;

import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.zawar.benzinpriser.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button price;
    Spinner radiusSpinner, fuelTypeSpinner;
    private String radius,fuelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radiusSpinner = (Spinner) findViewById(R.id.radiusspinner);
        fuelTypeSpinner = (Spinner) findViewById(R.id.fuelspinner);
        price = (Button)findViewById(R.id.seachPrices);
        price.setOnClickListener(this);

     ;

        ArrayAdapter<CharSequence> arrayAdapterRadius = ArrayAdapter.createFromResource(this,R.array.radiusArray,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> arrayAdapterFuelType = ArrayAdapter.createFromResource(this,R.array.fuelArray,android.R.layout.simple_spinner_item);

        arrayAdapterRadius.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterFuelType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        radiusSpinner.setAdapter(arrayAdapterRadius);
        fuelTypeSpinner.setAdapter(arrayAdapterFuelType);
        radiusSpinner.setOnItemSelectedListener(this);
        fuelTypeSpinner.setOnItemSelectedListener(this);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.SearchPostNr){
            Intent intent = new Intent(this,SearchByPostCode.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.afslut){
            finish();
        }
        else if(item.getItemId()==R.id.indstillinger){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        return true;
    }















    @Override
    public void onClick(View v) {
        if(v == price){

            Intent intent = new Intent(this,BenzPricesActivity.class);

            String radius [] = radiusSpinner.getSelectedItem().toString().split(" ");
            int valueOfRadius = Integer.parseInt(radius[0]);

            String fuelType = fuelTypeSpinner.getSelectedItem().toString();

            intent.putExtra("fuelType",fuelType);

            intent.putExtra("Radius",valueOfRadius);

            startActivity(intent);

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        switch (spinner.getId()){
            case R.id.radiusspinner:
                radius = String.valueOf(parent.getItemAtPosition(position));
                break;
            case R.id.fuelspinner:
                fuelType = String.valueOf(parent.getItemAtPosition(position));
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
