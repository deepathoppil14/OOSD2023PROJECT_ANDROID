package com.example.oosd2023project_android;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/*
 * OOSD Workshop 8 - Team 2 - 2023
 *
 * This fragment is used as part of the registration process for
 * collecting the user's phone numbers and email address.
 */
public class ContactFragment extends RegistrationFragment {

    private EditText etCustHomePhone;
    private EditText etCustBusPhone;
    private EditText etCustEmail;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        etCustEmail = view.findViewById(R.id.etCustEmail);
        etCustBusPhone = view.findViewById(R.id.etCustBusPhone);
        etCustHomePhone = view.findViewById(R.id.etCustHomePhone);

        return view;
    }

    /*
     * Pulls corresponding values from customer object into text fields
     */
    @Override
    public void fromCustomer(Customer customer) {
        String email = customer.getCustEmail();
        String homePhone = customer.getCustHomePhone();
        String busPhone = customer.getCustBusPhone();

        if (email == null) { email = ""; }
        if (homePhone == null) { homePhone = ""; }
        if (busPhone == null) { busPhone = ""; }

        etCustHomePhone.setText(homePhone);
        etCustBusPhone.setText(busPhone);
        etCustEmail.setText(email);
    }

    /*
     * Inserts values from text fields into customer object
     */
    @Override
    public Customer intoCustomer(Customer customer) {
        customer.setCustHomePhone(etCustHomePhone.getText().toString());
        customer.setCustBusPhone(etCustBusPhone.getText().toString());
        customer.setCustEmail(etCustEmail.getText().toString());

        return customer;
    }
}