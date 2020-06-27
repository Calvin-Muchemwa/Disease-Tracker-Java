package com.example.cathdev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class RegisterActivity<RequestQueue> extends AppCompatActivity {
     CheckBox checkBox;
    TextView loginTV;
    EditText fisrtName, lastName, username, email, password, password1;
    Button registerBtn;
    private com.android.volley.RequestQueue rQueue;

    private static String url="https://lamp.ms.wits.ac.za/home/s2115284/projloginn/Registeration3.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginTV = findViewById(R.id.loginTV);
        fisrtName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password1 = findViewById(R.id.password1);
        registerBtn = findViewById(R.id.registerBtn);
        checkBox=findViewById(R.id.checkbox1);

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAction();
            }
        });
    }

    private void registerAction() {
        final String fname = fisrtName.getText().toString();
        final String lname = lastName.getText().toString();
        final String uname = username.getText().toString();
        final String mail = email.getText().toString();
        final String pswd = password.getText().toString();
        final String pswd1 = password1.getText().toString();


        if(!checkBox.isChecked()){
            checkBox.setError("You have to agree to terms to proceed");
            checkBox.requestFocus();
            return;
        }

        if (fname.isEmpty()) {
            fisrtName.setError("First name is required");
            fisrtName.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
            return;
        }
        if (uname.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return;
        }
        if (mail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (pswd.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (!pswd.equals(pswd1)) {
            password1.setError("Password mismatch");
            password1.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully! Now Login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {


                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", fname);
                params.put("lastname", lname);
                params.put("username", uname);
                params.put("email", mail);
                params.put("password", pswd);
                params.put("password1", pswd1);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(stringRequest);
    }
}
