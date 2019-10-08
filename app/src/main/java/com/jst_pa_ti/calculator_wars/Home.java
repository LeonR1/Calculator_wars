package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button streznik=findViewById(R.id.button);
        Button odjemalec=findViewById(R.id.button2);
        Button racun=findViewById(R.id.button3);

        streznik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Streznik.class));
            }
        });

        odjemalec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Odjemalec.class));
            }
        });

        racun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, MainActivity.class));
            }
        });
    }
}
