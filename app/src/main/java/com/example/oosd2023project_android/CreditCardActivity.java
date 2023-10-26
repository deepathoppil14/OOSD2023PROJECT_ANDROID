package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreditCardActivity extends AppCompatActivity {

    EditText etCustomerId,etCreditId,etCardType,etExpiryDate,etCardNumber;
    ListView lvCreditCards;
    Button btnSaveCard;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        etCustomerId = findViewById(R.id.etCustomerId);
        etCreditId = findViewById(R.id.etCreditId);
        etCardType = findViewById(R.id.etCardType);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCardNumber = findViewById(R.id.etCardNumber);
        lvCreditCards = findViewById(R.id.lvCreditCards);
        btnSaveCard = findViewById(R.id.btnSaveCard);
        requestQueue = Volley.newRequestQueue(this);
        Intent i = getIntent();
        String customerId = i.getStringExtra("custId");
        System.out.println(customerId);
        getAllCreditCards(105);
    }

    private void getAllCreditCards(int CustId) {
        ArrayList<CreditCards> cardDetails = new ArrayList<>();
        ArrayAdapter<CreditCards> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardDetails);
        // Set the adapter to your ListView
        lvCreditCards.setAdapter(adapter);

        String url =getString(R.string.hostname)+"/api/creditcard/getcreditcards/"+CustId;
        JsonArrayRequest creditCardRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        System.out.println(response);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject creditCardData = response.getJSONObject(i);
                            System.out.println(creditCardData);

                            CreditCards cardlist = new CreditCards(creditCardData.getInt("cCid"),creditCardData.getString("cCName"),
                                    creditCardData.getInt("cCid"),creditCardData.getString("cCExpiry"),
                                    creditCardData.getInt("customerId")
                                    );
                            cardDetails.add(cardlist);
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        lvCreditCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        requestQueue.add(creditCardRequest);
    }
}