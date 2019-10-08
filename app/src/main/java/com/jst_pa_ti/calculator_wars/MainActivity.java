package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random seedGen = new Random();
        int seed = seedGen.nextInt();

        TextView seedView = findViewById(R.id.textView);
        seedView.setText("" + seed);

        RacunGenerator racunGenerator = new RacunGenerator(seed);

        for (int i = 0; i < 100; i++) {

            System.out.println(racunGenerator.getNumber());

        }

    }
}
