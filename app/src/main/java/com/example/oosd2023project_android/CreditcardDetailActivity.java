package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CreditcardDetailActivity extends AppCompatActivity {

    Button btnUpdate, btnDelete;
    EditText etCCid, etCCName, etCCNumber, etCCExpiry, etCustomerId;
    RequestQueue requestQueue;


    CreditCards creditCards;

    String serverIPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard_detail);

        requestQueue = Volley.newRequestQueue(this);


        btnDelete = findViewById(R.id.btnDelete);
        etCCid = findViewById(R.id.etCCid);
        etCCName = findViewById(R.id.etCCName);
        etCCNumber = findViewById(R.id.etCCNumber);
        etCCExpiry = findViewById(R.id.etCCExpiry);
        etCustomerId = findViewById(R.id.etCustomerId);

        Intent intent = getIntent();
        String  mode = intent.getStringExtra("mode");
        if(mode.equals("edit")){
            creditCards = (CreditCards) intent.getSerializableExtra("CreditCards"); //serializable so typecast to Product
            etCCid.setText(creditCards.getCreditCardId()+"");
            etCCName.setText(creditCards.getcCName());
            etCCNumber.setText(creditCards.getcCNumber());
            etCCExpiry.setText(creditCards.getcExpiry());
            etCustomerId.setText(creditCards.getCustomerId() + "");

            btnDelete.setEnabled(true);
        }else{
            btnDelete.setEnabled(false);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCreditcard(creditCards.getCreditCardId());
               // Executors.newSingleThreadExecutor().execute(new DeleteCreditcard(listViewCreditcard.getCreditCardId()));
            }
        });

    }


    private void DeleteCreditcard(int cCId) {

        // Make a network request to fetch package data
        String url = getString(R.string.hostname)+"/api/creditcard/deletecredircard/"+cCId;
        JsonArrayRequest pkgProductRequest = new JsonArrayRequest(
                Request.Method.DELETE, url, null,
                response -> {
                    System.out.println(response.toString());

                    Intent creditIntent = new Intent(this, CreditCardActivity.class);

                    startActivity(creditIntent);
                },
                error -> {
                    // Handle the error
                    Toast.makeText(
                            this,
                            "Error Making Request",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.d("travelexperts", "request timed out");
                }
        );
        requestQueue.add(pkgProductRequest);
    }
/*
    private class PostCreditcard implements Runnable {

        private Creditcard creditcard;
        public PostCreditcard(Creditcard creditcard) {this.creditcard = creditcard;}

        @Override
        public void run() {
            String url = "192.168.1.89:8080/workshop7-REST-CreditCard-1.0-SNAPSHOT/api/creditcard/postcreditcard";
            JSONObject obj = new JSONObject();
            try {
                obj.put("CCId",creditcard.getCreditCardId()+"");
                obj.put("CCName", creditcard.getcCName());
                obj.put("CCNumber",creditcard.getcCName());
                obj.put("CCExpiry",creditcard.getcCExpiry());
                obj.put("customerId",creditcard.getCustomerId());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("alice", "response=" + response);
                            VolleyLog.wtf(response.toString(), "utf-8");

                            //display result message
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("alice", "error=" + error);
                            VolleyLog.wtf(error.getMessage(), "utf-8");
                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }
    }*/

}