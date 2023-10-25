package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class RegisterActivity extends AppCompatActivity {

    private Customer customer;

    private List<CustomerFragment> fragments;
    int pageIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fragment Manager
        FragmentManager manager = getSupportFragmentManager();

        // Customer stores the incomplete customer data entered by the user
        customer = new Customer();
        fragments = new ArrayList<>();

        setContentView(R.layout.activity_register);

        // first fragment is required by the layout editor, so don't add it twice
        fragments.add((CustomerFragment) manager.findFragmentById(R.id.detailsFragment));
        fragments.add(new AddressFragment());
        fragments.add(new ContactFragment());
        fragments.add(new LoginFragment());

        // the currently selected "page" (fragment)
        pageIndex = 0;

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Submit / Next Button
        btnSubmit.setOnClickListener(view -> {
            // Get the current fragment, and have it update the customer data with
            // whatever has been entered
            CustomerFragment currentFragment = fragments.get(pageIndex);
            currentFragment.intoCustomer(customer);

            // if there are more fragments following the current one
            if (pageIndex + 1 < fragments.size()) {
                // ... then get the next fragment
                CustomerFragment nextFragment  = fragments.get(pageIndex + 1);

                // replace the current fragment with the next
                manager.beginTransaction()
                        .replace(R.id.detailsFragment, nextFragment)
                        // on commit (once view is inflated, update the new fragment
                        // with the customer data)
                        .runOnCommit(() -> nextFragment.fromCustomer(customer))
                        .commit();

                // increment pageIndex
                pageIndex++;

                // update the button labels
                if (pageIndex == fragments.size() - 1) {
                    btnSubmit.setText("Submit");
                }
                else {
                    btnSubmit.setText("Next");
                }

                if (pageIndex == 0) {
                    btnCancel.setText("Cancel");
                }
                else {
                    btnCancel.setText("Prev");
                }
            }
            else {
                // otherwise, we've reached the end of the pages so it must be time
                // to submit
                submitCustomer();
            }
        });

        // The Cancel / Prev Button
        btnCancel.setOnClickListener(view -> {
            // Get the current fragment and have it store its data in the customer object
            CustomerFragment currentFragment = fragments.get(pageIndex);
            currentFragment.intoCustomer(customer);
            // operates exactly like the above button, but in reverse
            if (pageIndex - 1 >= 0) {
                CustomerFragment previousFragment = fragments.get(pageIndex - 1);

                manager.beginTransaction()
                        .replace(R.id.detailsFragment, previousFragment)
                        // load the customer data into the new fragment
                        .runOnCommit(() -> previousFragment.fromCustomer(customer))
                        .commit();

                pageIndex--;

                // update button labels
                if (pageIndex == fragments.size() - 1) {
                    btnSubmit.setText("Submit");
                }
                else {
                    btnSubmit.setText("Next");
                }

                if (pageIndex == 0) {
                    btnCancel.setText("Cancel");
                }
                else {
                    btnCancel.setText("Prev");
                }
            }
            else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        });
//        Log.d("travelexperts", "Customer Data:\n" +
//                "{\n" +
//                "     custFirstName: " + customer.getCustFirstName() + "\n" +
//                "     custLastName:  " + customer.getCustLastName() + "\n" +
//                "     custAddress:   " + customer.getCustAddress() + "\n" +
//                "     custCity:      " + customer.getCustCity() + "\n" +
//                "     custProv:      " + customer.getCustProv() + "\n" +
//                "     custCountry:   " + customer.getCustCountry() + "\n" +
//                "     custPostal:    " + customer.getCustPostal() + "\n" +
//                "     custBusPhone:  " + customer.getCustBusPhone() + "\n" +
//                "     custHomePhone: " + customer.getCustHomePhone() + "\n" +
//                "     custEmail:     " + customer.getCustEmail() + "\n" +
//                "     custUsername:  " + customer.getCustUsername() + "\n" +
//                "     custPassword:  " + customer.getCustPassword() + "\n" +
//                "}");
    }

    private void submitCustomer() {
        // Attempt to register a new customer with the REST service
        JSONObject jsonObject = new JSONObject();

        try {
            // is auto_increment, but Gson will not deserialize if incomplete
            jsonObject.put("customerId", null);
            jsonObject.put("custFirstName", customer.getCustFirstName());
            jsonObject.put("custLastName", customer.getCustLastName());
            jsonObject.put("custAddress", customer.getCustAddress());
            jsonObject.put("custCity", customer.getCustCity());
            jsonObject.put("custProv", customer.getCustProv());
            jsonObject.put("custPostal", customer.getCustPostal());
            jsonObject.put("custCountry", customer.getCustCountry());
            jsonObject.put("custBusPhone", customer.getCustBusPhone());
            jsonObject.put("custHomePhone", customer.getCustHomePhone());
            // field is required to be present, even if null
            jsonObject.put("agentId", null);
            jsonObject.put("custEmail", customer.getCustEmail());
            jsonObject.put("custUsername", customer.getCustUsername());
            jsonObject.put("custPassword", customer.getCustPassword());
        }
        catch (JSONException ex) {
            Log.e("travelexperts", "JSON Error: " + ex.getMessage());
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.hostname) + "/api/customers/register",
                jsonObject,
                response -> {
                    Log.d("travelexperts", "Registration Successful");
                    Toast.makeText(
                            this,
                            "Registration Successful!",
                            Toast.LENGTH_LONG)
                            .show();

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
//                    new Handler()
//                            .postDelayed(() -> {
//                                startActivity(intent);
//                            }, 5000);
                },
                error -> {
                    Log.e("travelexperts", "registration error");
                    NetworkResponse res = error.networkResponse;
                    if (res == null) {
                        Toast.makeText(
                                this,
                                "Server Error!",
                                Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    if (res.statusCode == 400) {
                        // BAD REQUEST --> there were validation errors most likely
                        String resDataString = new String(res.data, StandardCharsets.UTF_8);
                        JSONObject resJson;

                        try {
                            resJson = new JSONObject(resDataString);

                            if (resJson.getString("error_type").equals("validation_errors")) {
                                String validationErrorString = resJson.getString("validation_errors");
                                JSONArray validationErrors = new JSONArray(validationErrorString);
                                String firstError = validationErrors.getString(0);
                                String errorField = null;
                                if (firstError.equals("custFirstName")) { errorField = "First Name"; }
                                else if (firstError.equals("custLastName")) { errorField = "Last Name"; }
                                else if (firstError.equals("custAddress")) { errorField = "Street Address"; }
                                else if (firstError.equals("custCity")) { errorField = "City"; }
                                else if (firstError.equals("custProv")) { errorField = "Province"; }
                                else if (firstError.equals("custCountry")) { errorField = "Country"; }
                                else if (firstError.equals("custPostal")) { errorField = "Postal Code"; }
                                else if (firstError.equals("custHomePhone")) { errorField = "Home Phone"; }
                                else if (firstError.equals("custBusPhone")) { errorField = "Business Phone"; }
                                else if (firstError.equals("custEmail")) { errorField = "Email Address"; }
                                else if (firstError.equals("custUsername")) { errorField = "Username"; }
                                else if (firstError.equals("custPassword")) { errorField = "Password"; }

                                if (errorField != null)
                                    Toast.makeText(this, "Invalid " + errorField, Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(this, "Unknown Error", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        requestQueue.add(request);
    }
}