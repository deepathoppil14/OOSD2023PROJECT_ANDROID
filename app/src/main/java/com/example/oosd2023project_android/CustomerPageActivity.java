package com.example.oosd2023project_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CustomerPageActivity extends AppCompatActivity {

    MenuItem miPackages,miPayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);
        Toast.makeText(this, R.id.miPackages + "", Toast.LENGTH_LONG).show();

        Toolbar tbTools = findViewById(R.id.tbTools);
        setSupportActionBar(tbTools);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miPackages)
        {
            startActivity(new Intent(this, PackageActivity.class));
        }
        else if (item.getItemId() == R.id.miPayment)
        {
            startActivity(new Intent(this, PackageActivity.class));
        } else
        {
            Toast.makeText(this, "That item is not implemented", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}