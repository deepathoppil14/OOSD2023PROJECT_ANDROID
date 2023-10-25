package com.example.oosd2023project_android;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginFragment extends RegistrationFragment {

    private EditText etCustUsername;
    private EditText etCustPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etCustUsername = view.findViewById(R.id.etCustUsername);
        etCustPassword = view.findViewById(R.id.etCustPassword);

        return view;
    }

    public String getCustUsername() {
        EditText etCustUsername = getView().findViewById(R.id.etCustUsername);
        return etCustUsername.getText().toString();
    }

    public String getCustPassword() {
        EditText etCustPassword = getView().findViewById(R.id.etCustPassword);
        return etCustPassword.getText().toString();
    }

    @Override
    public void fromCustomer(Customer customer) {
        String username = customer.getCustUsername();
        String password = customer.getCustPassword();

        if (username == null) { username = ""; }
        if (password == null) { password = ""; }

        etCustUsername.setText(username);
        etCustPassword.setText(password);
    }

    @Override
    public Customer intoCustomer(Customer customer) {
        customer.setCustUsername(etCustUsername.getText().toString());
        customer.setCustPassword(etCustPassword.getText().toString());

        return customer;
    }
}