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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CreditcardDetailActivity extends AppCompatActivity {

    Button btnUpdate, btnDelete;
    EditText etCCid, etCCName, etCCNumber, etCCExpiry, etCustomerId;
    RequestQueue requestQueue;


    CreditCards creditCards;

    String token;

    String serverIPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard_detail);

        requestQueue = Volley.newRequestQueue(this);

        token = getIntent().getStringExtra("token");


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
        String url = getString(R.string.hostname)+"/api/creditcard/deletecreditcard/"+cCId;
        AuthorizedJsonRequest pkgProductRequest = new AuthorizedJsonRequest(
                Request.Method.DELETE,
                token, url, null,
                response -> {
                    System.out.println(response.toString());

                    Intent creditIntent = new Intent(this, CreditCardActivity.class);
                    creditIntent.putExtra("token", token);

                    startActivity(creditIntent);
                },
                error -> {
                    // Handle the error
                    Toast.makeText(
                            this,
                            "Error Making Request",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.e("travelexperts", "Reponse: " + new String(error.networkResponse.data, StandardCharsets.UTF_8));
                    Log.d("travelexperts", "request timed out");
                }
        );
        requestQueue.add(pkgProductRequest);
    }
}