package com.example.androiduitesting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {

    // In DetailActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcity);

        Intent intent = getIntent();
        int position = intent.getIntExtra("ITEM_POSITION", -1); // -1 is a default value if not found
        String cityName = intent.getStringExtra("ITEM_NAME");

        // Use the retrieved data to populate the UI
        TextView cityTitle = findViewById(R.id.showCityText);
        // Display it
        if (cityName != null) {
            cityTitle.setText(cityName);
        }

        // set backbutton to finish current fragment and nav back to mainActivity
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}
