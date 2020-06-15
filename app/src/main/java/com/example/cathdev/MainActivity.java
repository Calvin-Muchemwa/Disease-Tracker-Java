package com.example.cathdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

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
}
