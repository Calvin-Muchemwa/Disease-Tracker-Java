package com.example.cathdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

   private BottomNavigationView bottomNavigationView;
    private SharedPrefrencesHelper sharedPrefrencesHelper;

    TextView firstname, lastname, usernamee, email;
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

        showAlertDialog();
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

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Are you infected with Covid-19");
        final String[] items = {"Yes","No","Not Sure"};
        final int checkedItem = 1;

        alertDialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Toast.makeText(MainActivity.this, "Selected :  Yes", Toast.LENGTH_LONG).show();
                                        break;
                                    case 1:
                                        dialog.dismiss();

                                        break;
                                    case 2:
                                        Toast.makeText(MainActivity.this, "Selected : Not Sure", Toast.LENGTH_LONG).show();
                                        break;

                                }
                            }
                        });
                    }
                });

                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast.makeText(MainActivity.this, "Selected :  Yes", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(MainActivity.this, "Selected : No", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Toast.makeText(MainActivity.this, "Selected : Not Sure", Toast.LENGTH_LONG).show();
                                break;

                        }
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
