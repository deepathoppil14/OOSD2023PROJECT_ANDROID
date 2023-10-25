package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
 * This activity is responsible for guiding the user through registering a new customer
 * account.
 */
public class RegisterActivity extends AppCompatActivity {

    // Customer object that will be incrementally filled with data as user goes through the registration
    // process
    private Customer customer;

    // A list of "CustomerFragment" fragments for registration details
    private List<RegistrationFragment> fragments;
    // The index of the current fragment within the fragments list
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
        fragments.add((RegistrationFragment) manager.findFragmentById(R.id.detailsFragment));
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
            RegistrationFragment currentFragment = fragments.get(pageIndex);
            currentFragment.intoCustomer(customer);

            // if there are more fragments following the current one
            if (pageIndex + 1 < fragments.size()) {
                // ... then get the next fragment
                RegistrationFragment nextFragment  = fragments.get(pageIndex + 1);

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
            RegistrationFragment currentFragment = fragments.get(pageIndex);
            currentFragment.intoCustomer(customer);
            // operates exactly like the above button, but in reverse
            if (pageIndex - 1 >= 0) {
                RegistrationFragment previousFragment = fragments.get(pageIndex - 1);

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
                // we're on the very first fragment and the user clicked the back button, so
                // return to the login screen
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

    /*
     * This method is responsible for attempting to register the user with the rest service
     * api.
     *
     * If unsuccessful, it will display a message to the user explaining the error with the
     * registration
     */
    private void submitCustomer() {
        // Attempt to register a new customer with the REST service
        JSONObject jsonObject = new JSONObject();

        try {
            // id is auto_increment, but Gson will not deserialize if incomplete
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
            Toast.makeText(this, "Error Processing Registration Information", Toast.LENGTH_LONG).show();
            Log.e("travelexperts", "JSON Error: " + ex.getMessage());
            return;
        }

        // Create a volley request queue and jsonobject request to register the customer
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.hostname) + "/api/customers/register",
                jsonObject, // (the json object containing customer details)
                response -> {
                    // on success ...
                    Log.d("travelexperts", "Registration Successful");
                    Toast.makeText(
                            this,
                            "Registration Successful!",
                            Toast.LENGTH_LONG)
                            .show();

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                },
                error -> {
                    // on failure ...
                    Log.e("travelexperts", "registration error");
                    NetworkResponse res = error.networkResponse;
                    // either no response from server, or the server had an internal error
                    if (res == null || res.statusCode == 500) {
                        Toast.makeText(
                                this,
                                "Server Error!",
                                Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    // request went through, but there was something wrong with it
                    if (res.statusCode == 400) {
                        // convert the response body into a json object
                        String resDataString = new String(res.data, StandardCharsets.UTF_8);
                        JSONObject resJson;

                        try {
                            resJson = new JSONObject(resDataString);
                            // error_type: validation, username conflict, etc...
                            String errorType = resJson.getString("error_type");

                            if (errorType.equals("validation_errors")) {
                                // There was a validation error, show the user only the first error
                                // as a toast
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
                                else // error name unrecognized (an error of the error really)
                                    Toast.makeText(this, "Unknown Error", Toast.LENGTH_LONG).show();
                            }
                            else if (errorType.equals("username_taken")) {
                                // username conflict
                                Toast.makeText(this, "Username is Already Taken", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        // send the request
        requestQueue.add(request);
    }
}