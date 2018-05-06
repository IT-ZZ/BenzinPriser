package com.example.zawar.benzinpriser.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zawar.benzinpriser.CustomAdapter;
import com.example.zawar.benzinpriser.Model.Fuel;
import com.example.zawar.benzinpriser.Model.PetrolStation;
import com.example.zawar.benzinpriser.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class BenzPricesActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener, PopupMenu.OnMenuItemClickListener, LocationListener {

    private String fuelType, setFuelTypeDB, postNumber;
    private String stName, stPrice, stAdd, stKey;
    private int radius;
    private final String TAG = "TAG";
    private DatabaseReference mDatabase, mGeoFire;
    GeoFire geoFire;
    GoogleApiClient mGoogleApiClient;
    public Location LastLocation;
    public static final int MY_PERMISSION_REQUEST_GPS_LOCATION = 1;
    CustomAdapter customAdapter;
    ArrayList<PetrolStation> arrayOfPetrolStation;
    PetrolStation petrolStaion;
    private static int pos;
    private GeoQuery geoQuery;
    private ProgressDialog progressDialog;
    LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        radius = getIntent().getIntExtra("Radius", 4);
        fuelType = getIntent().getStringExtra("fuelType");
        postNumber = getIntent().getStringExtra("postNr");
        if (fuelType.startsWith("D")) {
            setFuelTypeDB = "diesel";
        } else {
            setFuelTypeDB = "blyfri";
        }

        Log.d(TAG, fuelType);
        //***************************//
        //**************************//
        //**************************//

        geoFire = new GeoFire(mGeoFire);
        Log.d(TAG, mDatabase.toString());

        petrolStaion = new PetrolStation();

        if (postNumber == null)
            mGoogleApiClient.connect();
        else
            searchStationbyPostNumber();

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void getPetroStationsKeys(Location location) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("TankStationer");
        progressDialog.setMessage("Loading");
        progressDialog.show();

        GeoLocation geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
        geoQuery = geoFire.queryAtLocation(geoLocation, radius);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                getStationDetail(key);
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                geoQuery.removeAllListeners();
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Toast.makeText(BenzPricesActivity.this, "Feji i forbindelse,Prøve igen senere", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void getStationDetail(String key) {

        mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                petrolStaion = dataSnapshot.getValue(PetrolStation.class);
                customAdapter.setFuelType(fuelType);
                customAdapter.add(petrolStaion);
                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        arrayOfPetrolStation = new ArrayList<>();

        customAdapter = new CustomAdapter(this, arrayOfPetrolStation);
        ListView listView = new ListView(this);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
        setContentView(listView);


    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
    }

    protected void startLocationUpdates() {



            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }


    @Override
    public void onLocationChanged(Location location) {

    }

    private void searchStationbyPostNumber() {


        mDatabase.orderByChild("postCode").equalTo(postNumber).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                petrolStaion = dataSnapshot.getValue(PetrolStation.class);
                customAdapter.setFuelType(fuelType);
                customAdapter.add(petrolStaion);
                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //        Data Source
        arrayOfPetrolStation = new ArrayList<>();

        // Create the adapter to convert the array to views
        customAdapter = new CustomAdapter(this,arrayOfPetrolStation);
        ListView listView = new ListView(this);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
        setContentView(listView);

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {


        int checkForPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (checkForPermission == 0) {
            LastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (LastLocation != null) {

                getPetroStationsKeys(LastLocation);

            }



            if (LastLocation == null) {
                //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                startLocationUpdates();
                Toast.makeText(this, "App kunne ikke finde din placering", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (checkForPermission == -1) {
            //Any message to user before promoting for permission access

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_GPS_LOCATION);


        }

    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_GPS_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    if (LastLocation != null) {

                            getPetroStationsKeys(LastLocation);



                    } else if (LastLocation == null) {
//                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        if location is null, promote for settings or make location non null
                        Toast.makeText(this, "Sørge for at du har tændt for GPS'en", Toast.LENGTH_SHORT).show();
                        finish();


                    }
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {


                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                        if user denied permission with checking don't ask again, Here can I show explaintaion to the user
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Location Request");
                        builder.setMessage("Your location permission is required to find the nearest PetrolStations");
                        builder.setIcon(android.R.drawable.ic_menu_mylocation);
                        AlertDialog.Builder ok = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(BenzPricesActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSION_REQUEST_GPS_LOCATION);

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();


//                        finish();

                    } else {
//                        User checked the box don't ask again, promote for change the settings
                        Toast.makeText(this, "Your permission has denied ", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Fejl i forbindelse, Prøve igen senere", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Fejl i forbindelse, Prøve igen senere", Toast.LENGTH_SHORT).show();
        finish();

    }



    //List item click
    public void onItemClick(AdapterView<?> l, View v, int position, long id){
        pos = position;
        stName = arrayOfPetrolStation.get(position).getName();
        stKey = arrayOfPetrolStation.get(position).getKey();
        stAdd = arrayOfPetrolStation.get(position).getAddress()+" , "+arrayOfPetrolStation.get(position).getPostCode();
        if(setFuelTypeDB == "blyfri") {
            stPrice = arrayOfPetrolStation.get(position).getPrices().getBlyfri().getPrice();
        }
        else {
            stPrice = arrayOfPetrolStation.get(position).getPrices().getDiesel().getPrice();
        }

        showPopupMenu(v);
    }

    public void showPopupMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.details:
                Intent newActivity = new Intent(this, PetrolStation_MapAkt.class);
                String stDieselPrice = arrayOfPetrolStation.get(pos).getPrices().getDiesel().getPrice();
                String stDieselTime = arrayOfPetrolStation.get(pos).getPrices().getBlyfri().getTime();

                String stBlyfriPrice = arrayOfPetrolStation.get(pos).getPrices().getBlyfri().getPrice();
                String stBlyfriTIme = arrayOfPetrolStation.get(pos).getPrices().getBlyfri().getTime();

                newActivity.putExtra("Key",stKey);
                newActivity.putExtra("Add",stAdd);
                newActivity.putExtra("Name", stName);
                newActivity.putExtra("BlyfriPrice",stBlyfriPrice);
                newActivity.putExtra("BlyfriTime", stBlyfriTIme);
                newActivity.putExtra("DieselPrice", stDieselPrice);
                newActivity.putExtra("DieselTime", stDieselTime);


                startActivity(newActivity);

                return true;


            case R.id.enterPrice:


                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                Date date = new Date();
                gregorianCalendar.setTime(date);
                int todayDate = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
                int month = gregorianCalendar.get(Calendar.MONTH);
                month += 1;
                int year = gregorianCalendar.get(Calendar.YEAR);
                int hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = gregorianCalendar.get(Calendar.MINUTE);
                final String priceReportTime = String.valueOf(todayDate+"-"+month+"-"+year+" "+hour+":"+minute);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("IndberetPris");
                builder.setMessage("Nuværende Pris: "+ stPrice);
//                builder.setMessage(priceReportTime);
                builder.setIcon(arrayOfPetrolStation.get(pos).getLogo(arrayOfPetrolStation.get(pos).getName()));


                final EditText input = new EditText(this);

                input.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
                
                builder.setView(input);

                AlertDialog.Builder ok = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String pris = input.getText().toString();

                        if (pris.isEmpty()) {

                            Toast.makeText(BenzPricesActivity.this, "Pris felt er tomt ", Toast.LENGTH_SHORT).show();
                        } else if (pris != null) {
                            Double prisen = Double.parseDouble(pris);
                            if (prisen > 8 && prisen < 14) {


                                Fuel fuel = new Fuel(pris,priceReportTime);

                                mDatabase.child(stKey).child("prices").child(setFuelTypeDB).setValue(fuel, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if(databaseError != null){
                                                    Toast.makeText(BenzPricesActivity.this, "Fejl i systemet. Prøv igen senere", Toast.LENGTH_SHORT).show();

                                                }
                                                else{
                                                    Toast.makeText(BenzPricesActivity.this, "Prisen er opdateret", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });

                                Toast.makeText(BenzPricesActivity.this, "Tak for din indberetning", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Toast.makeText(BenzPricesActivity.this, "Din pris svarer ikke til markeds priser", Toast.LENGTH_SHORT).show();

                            }

                        }






                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();




                return true;
            case R.id.drivetoStation:

                String addressforNavi = stAdd;

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ addressforNavi);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

                return true;
            default:
                return false;

        }
    }



}
