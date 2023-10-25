package com.example.oosd2023project_android;

import androidx.fragment.app.Fragment;

/*
 * This class represents a fragment that is part of the registration process,
 * it is responsible for collecting/displaying customer information and has
 * helper functions to easily pull data out of / put data into a customer
 * (since each of these fragments will only update a small part of the customer)
 */
public abstract class RegistrationFragment extends Fragment {
    // put the customer data into the fragment's input fields
    public abstract void fromCustomer(Customer customer);
    // put the fragment's data into the corresponding customer fields
    public abstract Customer intoCustomer(Customer customer);
}
