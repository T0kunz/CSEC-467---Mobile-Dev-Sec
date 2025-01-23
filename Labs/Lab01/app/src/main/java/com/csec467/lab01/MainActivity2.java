package com.csec467.lab01;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button buttonDisplay = findViewById(R.id.buttonDisplay);
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = getIntent().getStringExtra("text");
                Toast.makeText(MainActivity2.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}