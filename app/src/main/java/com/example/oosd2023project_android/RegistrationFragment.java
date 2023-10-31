package com.example.oosd2023project_android;

import androidx.fragment.app.Fragment;

/*
 * OOSD Workshop 8 - Team 2 - 2023
 *
 * This class represents an abstract fragment used as part of the registration
 * process. It includes methods to pull the relevant information out of the fragment
 * and into a customer object, or vice-versa.
 * Author : Grayson
 */
public abstract class RegistrationFragment extends Fragment {
    // put the customer data into the fragment's input fields
    public abstract void fromCustomer(Customer customer);
    // put the fragment's data into the corresponding customer fields
    public abstract Customer intoCustomer(Customer customer);
}
