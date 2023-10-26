package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment fragment =
                (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.loginFragment);

        requestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.btnRegister).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // on "Login" button click
        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            // put the username/password into a json object
            // {
            //     "custUsername": "...",
            //     "custPassword": "...",
            // }
            String username = fragment.getCustUsername();
            String password = fragment.getCustPassword();

            Log.d("travelexperts", "custUsername: " + username);
            Log.d("travelexperts", "custPassword: " + password);

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
                    getString(R.string.hostname) + "/api/auth/login",
                    loginBody,
                    response -> {
                        // request success
                        try {
//                           pass the token to the next activity so they can make API
//                          requests...
                            String token = response.getString("token");
                            Intent intent = new Intent(this, CustomerPageActivity.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                            Log.d("travelexperts", "token: " + response.getString("token"));
                            /*Toast.makeText(
                                    this,
                                    "Token: " + response.getString("token"),
                                    Toast.LENGTH_LONG)
                                    .show();*/
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

                        Log.d("travelexperts", "request timed out");
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