package com.example.oosd2023project_android;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/*
 * OOSD Workshop 8 - Team 2 - 2023
 *
 * This fragment collects username/password information from the
 * user, either for logging-in or creating a new account (therefore
 * it has additional methods just for retrieving the username password
 * values, unlike other RegistrationFragments)
 * Author: Grayson
 */
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

    // Gets the username
    public String getCustUsername() {
        EditText etCustUsername = getView().findViewById(R.id.etCustUsername);
        return etCustUsername.getText().toString();
    }

    // gets the password
    public String getCustPassword() {
        EditText etCustPassword = getView().findViewById(R.id.etCustPassword);
        return etCustPassword.getText().toString();
    }

    // Takes custUsername/custPassword and updates the values of the text fields
    @Override
    public void fromCustomer(Customer customer) {
        String username = customer.getCustUsername();
        String password = customer.getCustPassword();

        if (username == null) { username = ""; }
        if (password == null) { password = ""; }

        etCustUsername.setText(username);
        etCustPassword.setText(password);
    }

    // takes the textfields and updates custUsername/custPassword
    @Override
    public Customer intoCustomer(Customer customer) {
        customer.setCustUsername(etCustUsername.getText().toString());
        customer.setCustPassword(etCustPassword.getText().toString());

        return customer;
    }
}