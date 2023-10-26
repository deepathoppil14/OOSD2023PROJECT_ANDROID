package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.concurrent.Executors;

public class CreditcardDetailActivity extends AppCompatActivity {

    Button btnUpdate, btnDelete;
    EditText etCCid, etCCName, etCCNumber, etCCExpiry, etCustomerId;
    RequestQueue requestQueue;
    ListViewCreditcard listViewCreditcard;

    MainApplication mainApplication;
    String serverIPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard_detail);

        requestQueue = Volley.newRequestQueue(this);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        etCCid = findViewById(R.id.etCCid);
        etCCName = findViewById(R.id.etCCName);
        etCCNumber = findViewById(R.id.etCCNumber);
        etCCExpiry = findViewById(R.id.etCCExpiry);
        etCustomerId = findViewById(R.id.etCustomerId);

        Intent intent = getIntent();
        listViewCreditcard = (ListViewCreditcard) intent.getSerializableExtra("listviewcreditcard");

        Executors.newSingleThreadExecutor().execute(new GetCredictcard(listViewCreditcard.getCustomerId()));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ccId = Integer.parseInt(etCCid.getText().toString());
                String ccName = etCCName.getText().toString();
                String ccNumber = etCCNumber.getText().toString();
                String ccExpiry = etCCExpiry.getText().toString();
                int customerId = Integer.parseInt(etCustomerId.getText().toString());

                Creditcard creditcard = null;
                creditcard = new Creditcard(ccId, ccName, ccNumber, ccExpiry, customerId);
                Executors.newSingleThreadExecutor().execute(new PostCreditcard(creditcard));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new DeleteCreditcard(listViewCreditcard.getCreditCardId()));
            }
        });
    }

    private class GetCredictcard implements Runnable {
        private int customerId;
        public GetCredictcard(int customerId) { this.customerId = customerId; }

        @Override
        public void run() {
            StringBuffer buffer = new StringBuffer();
            String url = "192.168.1.89:8080/workshop7-REST-CreditCard-1.0-SNAPSHOT/api/creditcard/getcreditcards/"+customerId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an Agent
                    JSONObject creditcard = null;
                    try {
                        creditcard = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //update ListView with the adapter of Credit card
                    final JSONObject finalCreditcard = creditcard;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                etCCid.setText(finalCreditcard.getInt("CCId") + "");
                                etCCName.setText(finalCreditcard.getString("CCName"));
                                etCCNumber.setText(finalCreditcard.getString("CCNumber"));
                                etCCExpiry.setText(finalCreditcard.getString("CCExpiry"));
                                etCustomerId.setText(finalCreditcard.getInt("customerId") + "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.wtf(error.getMessage(), "utf-8");
                }
            });
            requestQueue.add(stringRequest);
        }
    }


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
    }

    private class DeleteCreditcard implements Runnable {
        private int CCId;
        public DeleteCreditcard(int creditCardId) {this.CCId = creditCardId; }

        @Override
        public void run() {
            StringBuffer buffer = new StringBuffer();
            String url = "192.168.1.89:8080/workshop-7-1.0-SNAPSHOT/api/creditcard/deletecredircard"+CCId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.wtf(error.getMessage(), "utf-8");
                }
            });

            requestQueue.add(stringRequest);
        }
    }
}