package com.example.cathdev;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrencesHelper {

    private SharedPreferences sharedPreferences;
    private Context context;
    private int No5_infected;
    private String firstname = "firstname", lastname = "lastname", username = "username", email = "email",isInfected="YES/NO/NOT SURE", Date="",Address="";
    public SharedPrefrencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("login_session",
                Context.MODE_PRIVATE);
        this.context = context;
    }
    public String getDate() {
        return sharedPreferences.getString(Date, "");
    }
    public String getFirstname() {
        return sharedPreferences.getString(firstname, "");
    }
    public String getLastname() {
        return sharedPreferences.getString(lastname, "");
    }
    public String getUsername() {
        return sharedPreferences.getString(username, "");
    }
    public String getEmail() {
        return sharedPreferences.getString(email, "");
    }
    public String  getIsInfected(){ return sharedPreferences.getString(isInfected,"");}
    public String getAddress(){return sharedPreferences.getString(Address,"");}
    public int getNo_5_infected(){return sharedPreferences.getInt(String.valueOf(No5_infected),0);}


    public void setIsInfected(String isInfected) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(String.valueOf(this.isInfected), isInfected);
        edit.commit();
    }

    public void setNo5_infected(int No5_infected) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(String.valueOf(this.No5_infected), No5_infected);
        edit.commit();
    }


    public void setAddress(String Address) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.Address, Address);
        edit.commit();
    }


    public void setDate(String Date) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.Date, Date);
        edit.commit();
    }

    public void setFirstname(String firstname) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.firstname, firstname);
        edit.commit();
    }
    public void setLastname(String lastname) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.lastname, lastname);
        edit.commit();
    }
    public void setUsername(String username) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.username, username);
        edit.commit();
    }
    public void setEmail(String email) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.email, email);
        edit.commit();
    }
}
