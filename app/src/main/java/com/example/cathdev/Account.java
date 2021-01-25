package com.example.cathdev;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment  implements  View.OnClickListener{
    Activity SharedPrefrencesHelper= getActivity();
    private String url="https://lamp.ms.wits.ac.za/home/s2115284/projloginn/Update.php";


    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    public Account() {
        ////
    }
    private EditText et_Username,et_Email;
    private Button logoutBtn;
    private TextView et_Name,et_check;
 String email;
 String username;
 String name;
 String ID;
 ImageView check;
 Toolbar toolbar;

    private com.android.volley.RequestQueue rQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_account, container, false);

        check=(ImageView)rootview.findViewById(R.id.save);
        et_Email=(EditText)rootview.findViewById(R.id.et_Email);
        logoutBtn=(Button)rootview.findViewById(R.id.Btn_logout);
        et_Username=(EditText)rootview.findViewById(R.id.et_username);
        et_check=(TextView)rootview.findViewById(R.id.et_Check_in);
        et_Name=(TextView)rootview.findViewById(R.id.name);
        email=((MainActivity) getActivity()).getEmailz();
        ID=((MainActivity) getActivity()).getID();
        username=((MainActivity) getActivity()).getUsernamez();
        name=((MainActivity) getActivity()).getFL();
        check.setOnClickListener(this);

        return rootview;

    }

//since cant use Activity methods we have to make a plan
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        check.setOnClickListener(this);
        et_Email.setText("Email: "+email);
        et_Username.setText("Username: "+username);
        et_Name.setText(name);
        et_check.setText(((MainActivity) getActivity()).getADDZ());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
        check.setOnClickListener(this);
        et_Email.setText("Email: "+email);
        et_Username.setText("Username: "+username);
        et_Name.setText(name);
        et_check.setText(((MainActivity) getActivity()).getADDZ());


    }


private void getts() {
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("success").equals("1")) {
                               Toast.makeText(getContext(),"Details Successfully Updated",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {


                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", et_Username.getText().toString());
            params.put("email", et_Email.getText().toString());
            params.put("id",ID);
            return params;
        }
    };
    RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    rQueue.add(stringRequest);

}



    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.save) {
            getts();
        }
    }












}


