package com.example.oosd2023project_android;

import androidx.fragment.app.Fragment;

public abstract class CustomerFragment extends Fragment {
    public abstract void fromCustomer(Customer customer);
    public abstract Customer intoCustomer(Customer customer);
}
