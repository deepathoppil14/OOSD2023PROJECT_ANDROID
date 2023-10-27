package com.example.oosd2023project_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
//import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomerPageActivity extends AppCompatActivity {

    MenuItem miPackages,miPayment;

    private EditText etCustFirstName;
    private EditText etCustLastName;
    private EditText etCustCountry;
    private EditText etCustEmail;
    private EditText etCustCity;
    private EditText etCustProv;
    private EditText etCustPostal;
    private EditText etCustHomePhone;
    private EditText etCustAddress;
    private EditText etCustBusPhone;

    private String custUsername;
    private String custPassword;

    private String token; //adds a variable to store the token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);
//        Toast.makeText(this, R.id.miPackages + "", Toast.LENGTH_LONG).show();

        Toolbar tbTools = findViewById(R.id.tbTools);
        setSupportActionBar(tbTools);

        //this initializes the save button
        Button btnSave = findViewById(R.id.save);

        //this sets an OnClickListener for the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pulls customer data from the text fields
                Customer updatedCustomer = new Customer();
                updatedCustomer.setCustFirstName(etCustFirstName.getText().toString());
                updatedCustomer.setCustLastName(etCustLastName.getText().toString());
                updatedCustomer.setCustCountry(etCustCountry.getText().toString());
                updatedCustomer.setCustEmail(etCustEmail.getText().toString());
                updatedCustomer.setCustCity(etCustCity.getText().toString());
                updatedCustomer.setCustProv(etCustProv.getText().toString());
                updatedCustomer.setCustPostal(etCustPostal.getText().toString());
                updatedCustomer.setCustHomePhone(etCustHomePhone.getText().toString());
                updatedCustomer.setCustAddress(etCustAddress.getText().toString());
                updatedCustomer.setCustBusPhone(etCustBusPhone.getText().toString());

                //sends edited customer data to RESTful service
                updateCustomerData(updatedCustomer);
            }
        });

        etCustFirstName = findViewById(R.id.etCustFirstName);
        etCustLastName = findViewById(R.id.etCustLastName);
        etCustCountry = findViewById(R.id.etCustCountry);
        etCustEmail = findViewById(R.id.etCustEmail);
        etCustCity = findViewById(R.id.etCustCity);
        etCustProv = findViewById(R.id.etCustProv);
        etCustPostal = findViewById(R.id.etCustPostal);
        etCustHomePhone = findViewById(R.id.etCustHomePhone);
        etCustAddress = findViewById(R.id.etCustAddress);
        etCustBusPhone = findViewById(R.id.etCustBusPhone);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        //calls fetchCustomerData method to get data from the server
        fetchCustomerData();

        RequestQueue queue = Volley.newRequestQueue(this);

        //this fetches customer data through an API request
        AuthorizedJsonRequest request = new AuthorizedJsonRequest(
                Request.Method.GET,
                token,
                getString(R.string.hostname) + "/api/customers/my-data",
                null,
                response -> {
                    try {
                        Log.d("JSON Response", response.toString());

                        //this extracts values from the JSON response
                        String custFirstName = response.getString("custFirstName");
                        String custLastName = response.getString("custLastName");
                        String custCountry = response.getString("custCountry");
                        String custEmail = response.getString("custEmail");
                        String custCity = response.getString("custCity");
                        String custProv = response.getString("custProv");
                        String custPostal = response.getString("custPostal");
                        String custHomePhone = response.getString("custHomePhone");
                        String custAddress = response.getString("custAddress");
                        String custBusPhone = response.getString("custBusPhone");
                        custUsername = response.getString("custUsername");
                        custPassword = response.getString("custPassword");
                        //this sends those values into the UI
                        etCustFirstName.setText(custFirstName);
                        etCustLastName.setText(custLastName);
                        etCustCountry.setText(custCountry);
                        etCustEmail.setText(custEmail);
                        etCustCity.setText(custCity);
                        etCustProv.setText(custProv);
                        etCustPostal.setText(custPostal);
                        etCustHomePhone.setText(custHomePhone);
                        etCustAddress.setText(custAddress);
                        etCustBusPhone.setText(custBusPhone);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                });

        queue.add(request);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miPackages)
        {
            startActivity(new Intent(this, PackageActivity.class));
        }
        else if (item.getItemId() == R.id.miPayment)
        {
            Intent creditIntent = new Intent(this, CreditCardActivity.class);
            Log.d("travelexperts", "Putting token \"" + token + "\" in CreditCardActivity");
            creditIntent.putExtra("token", token);
            startActivity(creditIntent);
        } else
        {
            Toast.makeText(this, "That item is not implemented", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    //this method fetches data using the token that was created
    private void fetchCustomerData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        AuthorizedJsonRequest request = new AuthorizedJsonRequest(
                Request.Method.GET,
                token, //this is the stored token
                getString(R.string.hostname) + "/api/customers/my-data",
                null,
                response -> {
                    try {
                        Log.d("JSON Response", response.toString());

                        String custFirstName = response.getString("custFirstName");
                        String custLastName = response.getString("custLastName");
                        String custCountry = response.getString("custCountry");
                        String custEmail = response.getString("custEmail");
                        String custCity = response.getString("custCity");
                        String custProv = response.getString("custProv");
                        String custPostal = response.getString("custPostal");
                        String custHomePhone = response.getString("custHomePhone");
                        String custAddress = response.getString("custAddress");
                        String custBusPhone = response.getString("custBusPhone");

                        etCustFirstName.setText(custFirstName);
                        etCustLastName.setText(custLastName);
                        etCustCountry.setText(custCountry);
                        etCustEmail.setText(custEmail);
                        etCustCity.setText(custCity);
                        etCustProv.setText(custProv);
                        etCustPostal.setText(custPostal);
                        etCustHomePhone.setText(custHomePhone);
                        etCustAddress.setText(custAddress);
                        etCustBusPhone.setText(custBusPhone);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                });

        queue.add(request);
    }

//updates customer data through RESTful service using Gson
    private void updateCustomerData(Customer updatedCustomer) {
        Gson gson = new Gson();
        updatedCustomer.setCustUsername(custUsername);
        updatedCustomer.setCustPassword(custPassword);
        String customerJson = gson.toJson(updatedCustomer);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.hostname) + "/api/customers/update";

        /*
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Customer data updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(this, "Error updating customer data", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public byte[] getBody() {
                return customerJson.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
         */
        JSONObject jsonCustomer;
        try {
            jsonCustomer = new JSONObject(customerJson);
        }
        catch (JSONException e) {
            Toast.makeText(this, "JSON Error updating customer data", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthorizedJsonRequest request = new AuthorizedJsonRequest(
                Request.Method.PUT,
                token,
                url,
                jsonCustomer,
                response -> {
                    Toast.makeText(this, "Customer data updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e("travelexperts", gson.toJson(new String(error.networkResponse.data, StandardCharsets.UTF_8).toString()));
                    Toast.makeText(this, "Error updating customer data", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}