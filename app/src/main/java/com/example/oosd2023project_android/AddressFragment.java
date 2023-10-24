package com.example.oosd2023project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;

public class AddressFragment extends CustomerFragment {

    private EditText etCustAddress;
    private EditText etCustCity;
    private EditText etCustProv;
    private EditText etCustCountry;
    private EditText etCustPostal;

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        etCustAddress = view.findViewById(R.id.etCustAddress);
        etCustCity = view.findViewById(R.id.etCustCity);
        etCustPostal = view.findViewById(R.id.etCustPostal);
        etCustProv = view.findViewById(R.id.etCustProv);
        etCustCountry = view.findViewById(R.id.etCustCountry);

        return view;
    }

    @Override
    public void fromCustomer(Customer customer) {
        String address = customer.getCustAddress();
        String city = customer.getCustCity();
        String postal = customer.getCustPostal();
        String prov = customer.getCustProv();
        String country = customer.getCustCountry();

        if (address == null) { address = ""; }
        if (city == null) { city = ""; }
        if (postal == null) { postal = ""; }
        if (prov == null) { prov = ""; }
        if (country == null) { country = ""; }

        etCustAddress.setText(address);
        etCustCity.setText(city);
        etCustPostal.setText(postal);
        etCustProv.setText(prov);
        etCustCountry.setText(country);
    }

    @Override
    public Customer intoCustomer(Customer customer) {
        customer.setCustAddress(etCustAddress.getText().toString());
        customer.setCustCity(etCustCity.getText().toString());
        customer.setCustPostal(etCustPostal.getText().toString());
        customer.setCustProv(etCustProv.getText().toString());
        customer.setCustCountry(etCustCountry.getText().toString());

        return customer;
    }
}