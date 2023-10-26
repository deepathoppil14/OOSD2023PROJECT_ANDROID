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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class CreditCardActivity extends AppCompatActivity {

    EditText etCreditCustomerId,etCreditId,etCardType,etExpiryDate,etCardNumber;
    ListView lvCreditCards;
    Button btnSaveCard;

    RequestQueue requestQueue;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        etCreditCustomerId = findViewById(R.id.etCreditCustomerId);
        etCreditId = findViewById(R.id.etCreditId);
        etCardType = findViewById(R.id.etCardType);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCardNumber = findViewById(R.id.etCardNumber);
        lvCreditCards = findViewById(R.id.lvCreditCards);
        btnSaveCard = findViewById(R.id.btnSaveCard);
        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        //this fetches customer data through an API request
        AuthorizedJsonRequest request = new AuthorizedJsonRequest(
                Request.Method.GET,
                token,
                getString(R.string.hostname) + "/api/customers/my-id",
                null,
                response -> {
                    Log.d("JSON Response", response.toString());
                    try {
                        int custID = response.getInt("customerId");
                        getAllCreditCards(custID);
                        etCreditCustomerId.setText(custID+ "");
                        etCreditId.setText("0");

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    // Handle the error
                    Toast.makeText(
                            this,
                            "Error Making Request",
                            Toast.LENGTH_SHORT
                    ).show();
                });

        requestQueue.add(request);

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ccId = Integer.parseInt(etCreditId.getText().toString());
                String ccName = etCardType.getText().toString();
                String ccNumber = etCardNumber.getText().toString();
                String ccExpiry = etExpiryDate.getText().toString();
                int customerId = Integer.parseInt(etCreditCustomerId.getText().toString());

                CreditCards creditcard = null;
                creditcard = new CreditCards(ccId, ccName, ccNumber, ccExpiry, customerId);
                postCreditcard(creditcard,token);
            }
        });

    }

    private void getAllCreditCards(int CustId) {
        ArrayList<CreditCards> cardDetails = new ArrayList<>();
        ArrayAdapter<CreditCards> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardDetails);
        // Set the adapter to your ListView
        lvCreditCards.setAdapter(adapter);

        String url =getString(R.string.hostname)+"/api/creditcard/getcreditcards";
        AuthorizedJsonRequest creditCardRequest = new AuthorizedJsonRequest(
                Request.Method.GET,
                getIntent().getStringExtra("token"),
                url,
                null,
                response -> {
                    try {
                        String cardListString = response.getString("creditcards");
                        JSONArray cardList = new JSONArray(cardListString);
//                        JSONArray cardList = response.getJSONArray("creditcards");
                        Log.d("travelexperts", "CARD LIST: " + cardList.toString());
//
                        for (int i = 0; i < cardList.length(); i++) {
                            JSONObject cardJson = cardList.getJSONObject(i);
                            CreditCards card = new CreditCards(
                                    cardJson.getInt("creditCardId"),
                                    cardJson.getString("ccName"),
                                    cardJson.getString("ccNumber"),
                                    cardJson.getString("ccExpiry"),
                                    cardJson.getInt("customerId"));

                            cardDetails.add(card);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
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
                    Log.e("travelexperts", error.toString());
                    Log.e("travelexperts", "Error making request");
                }
        );
        lvCreditCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent creditIntent = new Intent(getApplicationContext(), CreditcardDetailActivity.class);
                creditIntent.putExtra("token", token);
                creditIntent.putExtra("CustId", CustId);
                creditIntent.putExtra("CreditCards", (Serializable) adapter.getItem(position));
                creditIntent.putExtra("mode","edit");
                startActivity(creditIntent);
            }
        });
        requestQueue.add(creditCardRequest);
    }

    private void postCreditcard(CreditCards creditcard,String token) {
        // Define the URL for the POST request
        String url = getString(R.string.hostname) + "/api/creditcard/postcreditcard";

        // Create a JSON object to hold the credit card data
        JSONObject creditCardJson = new JSONObject();
        try {
            creditCardJson.put("creditCardId", creditcard.getCreditCardId());
            creditCardJson.put("ccName", creditcard.getcCName());
            creditCardJson.put("ccNumber", creditcard.getcCNumber());
            creditCardJson.put("ccExpiry", creditcard.getcExpiry());
            creditCardJson.put("customerId", creditcard.getCustomerId());
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the JSON creation error here
            return;
        }
        System.out.println("response is :"+ creditCardJson);


        AuthorizedJsonRequest request = new AuthorizedJsonRequest(
                Request.Method.POST,
                token, //this is the stored token
                url,
                creditCardJson,
                response -> {
                    try {
                        Log.d("JSON Response", response.toString());

                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("travelexperts", error.toString());
                    Toast.makeText(getApplicationContext(), "Erorr", Toast.LENGTH_LONG).show();
                });

        requestQueue.add(request);


    }

/*
    private class PostCreditcard implements Runnable {

        private CreditCards creditcard;
        public PostCreditcard(CreditCards creditcard) {this.creditcard = creditcard;}

        @Override
        public void run() {
            String url = getString(R.string.hostname)+"/api/creditcard/postcreditcard";
            JSONObject obj = new JSONObject();
            System.out.println(obj.toString());
            try {
                obj.put("CCId",creditcard.getCreditCardId()+"");
                obj.put("CCName", creditcard.getcCName());
                obj.put("CCNumber",creditcard.getcCNumber());
                obj.put("CCExpiry",creditcard.getcExpiry());
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