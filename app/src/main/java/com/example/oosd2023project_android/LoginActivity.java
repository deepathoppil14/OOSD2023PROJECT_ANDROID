package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        requestQueue = Volley.newRequestQueue(this);

        // on "Login" button click
        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            // put the username/password into a json object
            // {
            //     "custUsername": "...",
            //     "custPassword": "...",
            // }
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            JSONObject loginBody = new JSONObject();

            try {
                loginBody.put("custUsername", username);
                loginBody.put("custPassword", password);

            }
            catch (JSONException e) {
                Toast.makeText(
                        LoginActivity.this,
                        "Error parsing username/password",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Create a JSON request (sends/recieves JSON)
            JsonObjectRequest request = new JsonObjectRequest(
                    StringRequest.Method.POST,
                    "http://10.187.238.58:8080/workshop-7-1.0-SNAPSHOT/api/auth/login",
                    loginBody,
                    response -> {
                        // request success
                        try {
//                           pass the token to the next activity so they can make API
//                          requests...
//                            String token = response.getString("token");
//                            Intent intent = new Intent(this, CustomerPageActivity.class);
//                            intent.putExtra("token", token);

                            Toast.makeText(
                                    this,
                                    "Token: " + response.getString("token"),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                        catch (JSONException e) {
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Error parsing token",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    },
                    error -> {
                        // request error
                        Toast.makeText(
                                this,
                                "Error Making Request",
                                Toast.LENGTH_SHORT)
                                .show();
                    });

            // queue the request to be sent
            requestQueue.add(request);
        });


//        If you need to make a request for customer data, you need to add
//        the token to the headers like this (make sure you always pass the token through intents):
//
//        request.getHeaders().put("Authorization", "Bearer " + token);
    }
}