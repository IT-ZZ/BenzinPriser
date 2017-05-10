package com.example.zawar.benzinpriser.Activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zawar.benzinpriser.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PetrolStation_MapAkt extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double latitude;
    public double longitude;
    String stName, stAdd, stBlyfriPrice,stDieselPrice, stKey,stBlyfriTime,stDieselTime;
    DatabaseReference mref;
    String key;
    TextView stationName, stationAdd, stationBenzinPrice, stationbenzinDate, stationdieselPrice,stationdieselTime;
    SupportMapFragment mapFragment;
    private final static String TAG = "TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stKey = getIntent().getExtras().getString("Key");
        stName = getIntent().getExtras().getString("Name");
        stAdd = getIntent().getExtras().getString("Add");
        stBlyfriPrice = getIntent().getExtras().getString("BlyfriPrice");
        stBlyfriTime = getIntent().getExtras().getString("BlyfriTime");
        stDieselPrice = getIntent().getExtras().getString("DieselPrice");
        stDieselTime = getIntent().getExtras().getString("DieselTime");

        Log.d(TAG, stName);
        Log.d(TAG, stAdd);
        Log.d(TAG, stBlyfriPrice);
        Log.d(TAG, stBlyfriTime);
        Log.d(TAG, stDieselPrice);
        Log.d(TAG, stDieselTime);


        mref = FirebaseDatabase.getInstance().getReference("locationKey");


        mref.child(stKey).child("l").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            String string = dataSnapshot.getValue().toString();

            String[] parts = string.split(",");
            latitude = Double.parseDouble(parts[0].replaceAll("\\] ", "").replaceAll("\\[", "")); //
            longitude = Double.parseDouble(parts[1].replaceAll("\\[ ", "").replaceAll("\\]", ""));
            Log.d(TAG, String.valueOf(latitude));
            Log.d(TAG, String.valueOf(longitude));
            mapFragment.getMapAsync(PetrolStation_MapAkt.this);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    });





        setContentView(R.layout.activity_petrol_station__map_akt);

        stationName = (TextView) findViewById(R.id.stationNavn);
        stationAdd = (TextView) findViewById(R.id.addressView);
        stationBenzinPrice = (TextView) findViewById(R.id.prisView);
        stationbenzinDate = (TextView) findViewById(R.id.benzinIndberetDato);
        stationdieselPrice = (TextView) findViewById(R.id.dieselprisView);
        stationdieselTime = (TextView) findViewById(R.id.dieselIndberetDato);










        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        stationName.setText(stName);
        stationAdd.setText(stAdd);
        stationBenzinPrice.setText(stBlyfriPrice+" "+"Kr/l");
        stationbenzinDate.setText(stBlyfriTime);
        stationdieselPrice.setText(stDieselPrice+" "+"Kr/l");
        stationdieselTime.setText(stDieselTime);

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng petrolStation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(petrolStation).title(stName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(petrolStation , 15));

    }
}


