package com.example.oosd2023project_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PackageActivity extends AppCompatActivity {

    ListView lvPackages;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        // Initialize ListView
        lvPackages = findViewById(R.id.lvPackages);
        requestQueue = Volley.newRequestQueue(this);
        getAllPackages();

    }
    private void getAllPackages() {
        // Create an ArrayList to store package names
        ArrayList<Package> packageNames = new ArrayList<>();
        // Create an ArrayAdapter with the simple_list_item_1 layout
        ArrayAdapter<Package> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, packageNames);
        // Set the adapter to your ListView
        lvPackages.setAdapter(adapter);
        // Make a network request to fetch package data
        String url = getString(R.string.hostname) +"/api/package/getallpackages";
        JsonArrayRequest packageRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        System.out.println(response);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject packageData = response.getJSONObject(i);
                            Integer id = packageData.getInt("id");
                            String pkgName = packageData.getString("pkgName");
                            String startDt = packageData.getString("pkgStartDate");
                            String endDt = packageData.getString("pkgEndDate");
                            String pkgDesc = packageData.getString("pkgDesc");
                            String pkgBasePrice = packageData.getString("pkgBasePrice");
                            String pkgAgencyCommission = packageData.getString("pkgAgencyCommission");
                            Package packagelist = new Package(id,pkgName,startDt,endDt,pkgDesc,pkgBasePrice,pkgAgencyCommission);
                            packageNames.add(packagelist);

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
        lvPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageDtlFragment packageDtlFragment = (PackageDtlFragment) getSupportFragmentManager().findFragmentById(R.id.fragPackageDetail);
                packageDtlFragment.displayPackageDtl(adapter.getItem(position));
            }
        });
        requestQueue.add(packageRequest);
    }





}