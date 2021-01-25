package com.example.cathdev;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "Maps";
    private static final int ERROR_DIALOG_REQUEST=9001;
    private Toolbar toolbar;
    private com.android.volley.RequestQueue rQueue;
    ////
   private BottomNavigationView bottomNavigationView;
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    /////
   private  TextView firstname, lastname, usernamee, email,textLatLong;
   private ProgressBar progressBar;
  private   Button logoutBtn;
  private String ADDZ="";
  private String url2="https://lamp.ms.wits.ac.za/home/s2115284/projloginn/Top3Loc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(this);
        String username = sharedPrefrencesHelper.getUsername();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*if (username == null || username.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

         */

        showAlertDialog();// pop up



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
//////////////
        FloatingActionButton myFab =findViewById(R.id.check_FAB);
      myFab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(isServicesOK()){
                  Intent intent= new Intent(MainActivity.this,Maps.class);
                  startActivity(intent);
              }

          }
      });

    }


    Home homefragment= new Home();
    Account accountfragment= new Account();
    Screening screeningfragment= new Screening();



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_cc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,accountfragment).commit();
                return true;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homefragment).commit();
                return true;
            case R.id.nav_fav:

                try {
                    getResultAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,screeningfragment).commit();

                return true;

        }
        return false;
    }
String selection;
    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Are you infected with Covid-19");
        final String[] items = {"Yes","No","Not Sure"};
        final int checkedItem = -1;

        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection = items[which];

            }
        });

        alertDialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                switch (selection) {
                    case ("Yes"):
                        Toast.makeText(MainActivity.this, "Selected :  Yes", Toast.LENGTH_LONG).show();
                        sharedPrefrencesHelper.setIsInfected(selection);
                        Intent intent= new Intent(MainActivity.this,Maps.class);
                        startActivity(intent);
                        break;
                    case ("No"):
                        Toast.makeText(MainActivity.this, "Selected :  NO", Toast.LENGTH_LONG).show();
                        sharedPrefrencesHelper.setIsInfected(selection);
                        //Intent intent2 = new Intent(MainActivity.this,Maps.class);
                        //startActivity(intent2);
                         dialog.dismiss();

                        break;
                    case ("Not Sure"):

                        sharedPrefrencesHelper.setIsInfected(selection);
                        Toast.makeText(MainActivity.this, "Selected : Not Sure", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,screeningfragment).commit();
                        break;
                }
            }
        });


        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }



    //initialize the intent to mapActivity




    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS){
            //everything is fine and usercan make requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an resovable error coccured
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog =GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this,"You cant make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
///Functions to use for varibles for fragment String Request
    public String getUsernamez(){
        return sharedPrefrencesHelper.getUsername();

    }
    public String getEmailz(){
        return sharedPrefrencesHelper.getEmail();

    }

    public String getFL(){
        String FL="";
        FL +=sharedPrefrencesHelper.getFirstname();
        FL+=" ";
        FL+=sharedPrefrencesHelper.getLastname();
return FL;
    }
    public String getID(){
        return sharedPrefrencesHelper.getID();

    }
    public String getADDZ(){
        return sharedPrefrencesHelper.getADDZ();

    }
    private void getResultAddress() throws IOException {




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

                    Log.d("GET", "onResponse: Getting address json array");

                    for (int i=0; i<array.length(); i++ ){

                        JSONObject ob=array.getJSONObject(i);
                        double Latitude = ob.getDouble("LATITUDE");
                        double Longitude = ob.getDouble("LONGITUDE");
                        String Addy=Address_get(Latitude,Longitude);//CONVERT  LATITUDE AND LONGITUDE FROM SERVER TO ADDY
                        //Addys.add(Addy);
                        ADDZ+=Addy;
                        ADDZ+="\n";
                        Log.d("ADD", "onResponse: "+ADDZ);




                    }
                    Log.d(TAG, "onResponse: setting sharedpref ADDZ TO : " +ADDZ);
                    sharedPrefrencesHelper.setADDZ(ADDZ);

                } catch (JSONException | IOException e) {
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


                Map<String, String> params = new HashMap<String, String>();
                params.put("email",getEmailz());

                return params;
            }
        };


        rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(stringRequest);

    }
    private String Address_get(double latitude,double longitude) throws IOException {
        Geocoder geocoder;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        return address;
    }
}
