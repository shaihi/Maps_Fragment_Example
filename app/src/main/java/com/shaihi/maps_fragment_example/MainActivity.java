package com.shaihi.maps_fragment_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addressBtn = findViewById(R.id.addressBtn);
        Button mapBtn = findViewById(R.id.mapBtn);

        AddressFragment addressFragment = AddressFragment.newInstance();
        MapFragment mapFragment = MapFragment.newInstance("Harofe 40, Haifa, Israel");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mapFragment).commit();

        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, addressFragment).commit();
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mapFragment).commit();
            }
        });
    }
}