package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
 * OOSD Workshop 8 - Team 2 - 2023
 *
 * This was just to demonstrate the functionality of the
 * AuthorizedJsonRequest class, however it wasnt deleted
 * just in case that would break the project before submission
 * Author: Grayson
 */
public class AuthRequestTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_request_test);

        Intent i = getIntent();
        String token = i.getStringExtra("token");

        RequestQueue queue = Volley.newRequestQueue(this);

        AuthorizedJsonRequest req = new AuthorizedJsonRequest(
                // method
                Request.Method.GET,
                // token
                token,
                // url
                getString(R.string.hostname) + "/api/customers/my-data",
                // body
                null,
                // response handler
                response -> {
                    TextView tvTest = findViewById(R.id.tvTest);
                    tvTest.setText(response.toString());
                },
                // error handler
                error -> {
                    Log.e("travelexperts", error.toString());
                });

        queue.add(req);
    }
}