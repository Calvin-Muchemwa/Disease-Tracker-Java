package com.example.cathdev;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {
    private com.android.volley.RequestQueue rQueue;
    Dialog myDialog;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            showAlertDialog();

            Use_location.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {


                    try {
                        UseLocation();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try {
                        getResultData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            });

        }

    }
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM=15f;
    private static final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));
    //variables

    private List<Results_Data>list_data;
    private Boolean mLocationPermissionGranted= false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private static String url="https://lamp.ms.wits.ac.za/home/s2115284/projloginn/Check_In.php";
    private static String url2="https://lamp.ms.wits.ac.za/home/s2115284/projloginn/Res.php";
    private Button Use_location;
    String Longitude="";
    String Latitude="";
    String email;
    String Date="",Addy="";
    String isInfected="";
    LatLng ss;

    private ImageView mGps;
    private String apiKey= "AIzaSyBvg3J7NuFFI27RFm3o-7wRuLiUL3B1IUM";

    private GoogleApiClient mGoogleApiClient;
    //widgets
    private EditText mSearchText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(this);
        //mSearchText=(EditText)findViewById(R.id.input_search);
        list_data=new ArrayList<>();
        mGps=(ImageView) findViewById(R.id.ic_gps);
        Use_location =(Button) findViewById(R.id.Use_location);
        getLocationPermission();
        InitializePlaces();




    }
    private void intiMap(){
        Log.d(TAG, "intiMap: initializing map");
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(Maps.this);


    }


    private void geoLocate() throws IOException {
        Log.d(TAG, "geoLocate: GeolOCATTING");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder= new Geocoder(Maps.this);
        List<Address> list = new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(searchString,1);

        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOEXCEPTION "+ e.getMessage());
        }
        if (list.size()>0){
            Address address=list.get(0);
            Log.d(TAG, "geoLocate: FOund Location: "+address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }
    private void moveCamera(LatLng latlng,float zoom,String title) throws IOException {
        Log.d(TAG, "moveCamera: Moving the Camera to : lat: "+latlng.latitude + ", lng: "+ latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        ss=latlng;
        MarkerOptions options = new MarkerOptions().position(latlng).title(title);
        mMap.addMarker(options);
         Latitude =String.valueOf(latlng.latitude);
         Longitude = String.valueOf(latlng.longitude);
        hideSoftKeyboard();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: Getting Devices current Location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(mLocationPermissionGranted){
                Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: FOUND LOCATION");
                            Location currentLocation=(Location) task.getResult();


                            assert currentLocation != null;
                            try {
                                moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My Location");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(Maps.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: ");
        }

    }




    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted=true;
                intiMap();


            }else{
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for( int i =0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionGranted=true;
                    //initialize main
                    intiMap();
                }
            }
        }
    }

    private void InitializePlaces(){
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);

        }

        placesClient=Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment=(AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final  LatLng latLng=place.getLatLng();

                Log.i(TAG, "onPlaceSelected: "+latLng.latitude+"\n"+latLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void UseLocation() throws IOException {
        String check =sharedPrefrencesHelper.getDate();

        Log.d(TAG, "UseLocation: Longitude is : "+Longitude);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();



                            try {

                                response = response.replaceFirst("<font>.*?</font>", "");
                                int jsonStart = response.indexOf("{");
                                int jsonEnd = response.lastIndexOf("}");

                                if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                                    response = response.substring(jsonStart, jsonEnd + 1);
                                }
                                Log.i("tagconvertstr", "["+response+"]");
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optString("success").equals("1")) {
                                    Toast.makeText(Maps.this, "Check in has been logged", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                    //finish();
                                } else {
                                    Toast.makeText(Maps.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {


                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Maps.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                isInfected =sharedPrefrencesHelper.getIsInfected();
                Date= sharedPrefrencesHelper.getDate();
               String email =sharedPrefrencesHelper.getEmail();
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", Latitude);
                params.put("longitude", Longitude);
                params.put("email", email);
                Log.d(TAG, "getParams: "+ Date);
                params.put("isInfected",isInfected);
                params.put("date",Date);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(Maps.this);
        rQueue.add(stringRequest);
        Address_get();



    }

    String selection2;
    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Maps.this);

        alertDialog.setTitle("Would you like to check in using your current Date?");
        final String[] items = {"Yes","No"};
        final int checkedItem = -1;

        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection2 = items[which];

            }
        });

        alertDialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked Apply

                switch (selection2) {
                    case ("Yes"):
                        Toast.makeText(Maps.this, "Selected :  Yes", Toast.LENGTH_LONG).show();
                        DateFormat dd=new SimpleDateFormat("yyyy-MM-dd");
                        Date dates= new Date();
                        sharedPrefrencesHelper.setDate(dd.format(dates));
                        Date=sharedPrefrencesHelper.getDate();
                        Log.d(TAG, "    DATE from sharedprefernce: "+Date);
                        dialog.dismiss();

                        break;

                    case ("No"):
                        Toast.makeText(Maps.this, "Selected :  NO", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        showDateDialog();
                        Log.d(TAG, "onClick: ShowDateDialog");


                        break;

                }
            }
        });


        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    String tt="";
    private void showDateDialog(){
        Calendar cal=Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        int day  =cal.get(Calendar.DAY_OF_MONTH);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int years, int months, int dayOfMonth) {
                months+=1;
                tt=years+"-"+months+"-"+dayOfMonth;
                Log.d(TAG, "onDateSet: "+tt);
                sharedPrefrencesHelper.setDate(tt);
            }
        };
        final DatePickerDialog dialog= new DatePickerDialog(Maps.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);

        dialog.setTitle("Select Preferred Check in Date");

       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       dialog.show();
        Log.d(TAG, "showDateDialog: Just before onDateSet");




    }

    private void getResultData() throws IOException {

        Log.d(TAG, "getResultData:  getting results DAta");

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    response = response.replaceFirst("<font>.*?</font>", "");
                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    }



                   JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=  jsonObject.getJSONArray("hello");
                    Log.d(TAG, "onResponse: Array length : "+array.length() );
                    sharedPrefrencesHelper.setNo5_infected(array.length());

                    for (int i=0; i<array.length(); i++ ){

                        JSONObject ob=array.getJSONObject(i);
                        Results_Data listData=new Results_Data(ob.getString("EMAIL"));
                        list_data.add(listData);
                        Log.d(TAG, "onResponse: List Data is : "+list_data.get(0));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                isInfected =sharedPrefrencesHelper.getIsInfected();
                Date= sharedPrefrencesHelper.getDate();
                String email= sharedPrefrencesHelper.getEmail();
                Log.d(TAG, "getParams: Putting values in Blah");
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", Latitude);
                params.put("longitude", Longitude);
                params.put("email", email);
                params.put("date",Date);
                params.put("isInfected",isInfected);
                return params;
            }
        }
        ;
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        ShowPopUp();

    }




    private void ShowPopUp(){
        myDialog=new Dialog(this);
        myDialog.setContentView(R.layout.popup);

        TextView txtclose = (TextView) myDialog.findViewById(R.id.exitPop);
        TextView txtdate = (TextView) myDialog.findViewById(R.id.txtdate);
       TextView txtNo_ppl = (TextView) myDialog.findViewById(R.id.txtNo_ppl);
        TextView txtAddress = (TextView) myDialog.findViewById(R.id.txtAddress);
        txtAddress.setText(" Address: "+Addy);
        txtdate.setText("Date: "+sharedPrefrencesHelper.getDate());

        txtNo_ppl.setText("Number of Infected Users within a Radius of 5Km: "+Integer.toString(sharedPrefrencesHelper.getNo_5_infected()));
        txtclose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myDialog.dismiss();
        }
    });


    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialog.show();
}





    private void Address_get() throws IOException {

        LatLng jj=ss;
        Geocoder geocoder;
        geocoder = new Geocoder(Maps.this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(jj.latitude, jj.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        Addy=address;
        Log.d(TAG, "Address_get: Addy "+address);
    }




}








