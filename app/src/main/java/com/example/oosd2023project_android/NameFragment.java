package com.example.oosd2023project_android;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/*
 * OOSD Workshop 8 - Team 2 - 2023
 *
 * This fragment collects a new customer's first and last names as
 * part of the registration process
 */
public class NameFragment extends RegistrationFragment {

    private EditText etCustFirstName;
    private EditText etCustLastName;

    public NameFragment() {
        // Required empty public constructor
    }

    public static NameFragment newInstance() {
        NameFragment fragment = new NameFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_name, container, false);

        etCustFirstName = view.findViewById(R.id.etCustFirstName);
        etCustLastName = view.findViewById(R.id.etCustLastName);

        return view;
    }

    public void fromCustomer(Customer customer) {
        String firstName = customer.getCustFirstName();
        String lastName = customer.getCustLastName();

        if (firstName == null) { firstName = ""; }
        if (lastName == null) { lastName = ""; }

        etCustFirstName.setText(firstName);
        etCustLastName.setText(lastName);
    }

    public Customer intoCustomer(Customer customer) {
        customer.setCustFirstName(etCustFirstName.getText().toString());
        customer.setCustLastName(etCustLastName.getText().toString());

        return customer;
    }
}