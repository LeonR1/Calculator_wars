package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int stRacunov = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random seedGen = new Random();
        int seed = seedGen.nextInt();

        final RacunGenerator racunGenerator = new RacunGenerator(seed);

        Button button = findViewById(R.id.btnGenRacun);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView textView = findViewById(R.id.tvRacun);
                textView.setText(racunGenerator.getRacun());

            }

        });

    }
}
