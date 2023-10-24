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

import org.json.JSONException;
import org.json.JSONObject;

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

        FragmentManager manager = getSupportFragmentManager();

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

        btnSubmit.setOnClickListener(view -> {
            CustomerFragment currentFragment = fragments.get(pageIndex);
            currentFragment.intoCustomer(customer);

            // if there are more fragments
            if (pageIndex + 1 < fragments.size()) {
                // get the next fragment
                CustomerFragment nextFragment  = fragments.get(pageIndex + 1);

                // replace the current fragment with the next
                manager.beginTransaction()
                        .replace(R.id.detailsFragment, nextFragment)
                        // on commit (once view is inflated, update the new fragment
                        // with the customer data)
                        .runOnCommit(() -> nextFragment.fromCustomer(customer))
                        .commit();

                pageIndex++;

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
                submitCustomer();
            }
        });

        btnCancel.setOnClickListener(view -> {
            CustomerFragment currentFragment = fragments.get(pageIndex);
            currentFragment.intoCustomer(customer);
            // operates exactly like the above button, but in reverse
            if (pageIndex - 1 >= 0) {
                CustomerFragment previousFragment = fragments.get(pageIndex - 1);

                manager.beginTransaction()
                        .replace(R.id.detailsFragment, previousFragment)
                        .runOnCommit(() -> previousFragment.fromCustomer(customer))
                        .commit();

                pageIndex--;

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
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("customerId", 0);
            jsonObject.put("custFirstName", customer.getCustFirstName());
            jsonObject.put("custLastName", customer.getCustLastName());
            jsonObject.put("custAddress", customer.getCustAddress());
            jsonObject.put("custCity", customer.getCustCity());
            jsonObject.put("custProv", customer.getCustProv());
            jsonObject.put("custPostal", customer.getCustPostal());
            jsonObject.put("custCountry", customer.getCustCountry());
            jsonObject.put("custBusPhone", customer.getCustBusPhone());
            jsonObject.put("custHomePhone", customer.getCustHomePhone());
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
                "http://10.187.195.112:8080/workshop-7-1.0-SNAPSHOT/api/customers/register",
                jsonObject,
                response -> {
                    Log.d("travelexperts", "SUCCESS");
                    Toast.makeText(
                            this,
                            "Registration Successful!",
                            Toast.LENGTH_LONG)
                            .show();

                    Intent intent = new Intent(this, LoginActivity.class);
                    new Handler()
                            .postDelayed(() -> {
                                startActivity(intent);
                            }, 5000);
                },
                error -> {
                    Toast.makeText(
                                    this,
                                    "Registration Failed!",
                                    Toast.LENGTH_LONG)
                            .show();
                    Log.e("travelexperts", "registration error");
                });

        requestQueue.add(request);
    }
}