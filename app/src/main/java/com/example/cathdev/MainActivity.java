package com.example.cathdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Maps";
    private static final int ERROR_DIALOG_REQUEST=9001;
    private static final int REQUEST_CODE_LOCATION_PERMISSION=1;
    ////
   private BottomNavigationView bottomNavigationView;
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    /////
    TextView firstname, lastname, usernamee, email,textLatLong;
    private ProgressBar progressBar;
    Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(this);
        String username = sharedPrefrencesHelper.getUsername();

        if (username == null || username.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        showAlertDialog();// pop up





        /*
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        usernamee = findViewById(R.id.username);
        email = findViewById(R.id.email);
        logoutBtn = findViewById(R.id.logoutBtn);
        firstname.setText(sharedPrefrencesHelper.getFirstname());
        lastname.setText(sharedPrefrencesHelper.getLastname());
        usernamee.setText(sharedPrefrencesHelper.getUsername());
        email.setText(sharedPrefrencesHelper.getEmail());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefrencesHelper.setFirstname(null);
                sharedPrefrencesHelper.setLastname(null);
                sharedPrefrencesHelper.setUsername(null);
                sharedPrefrencesHelper.setEmail(null);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });*/

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

}
