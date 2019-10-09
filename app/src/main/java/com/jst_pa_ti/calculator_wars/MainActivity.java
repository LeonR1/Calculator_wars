package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int stRacunov = 0;
    public static String rez = "";
    public static TextView tvRacun;
    public static Button btns[] = new Button[12];
    public static int btnId[] = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random seedGen = new Random();
        int seed = seedGen.nextInt();

        tvRacun = findViewById(R.id.tvRacun);
        final RacunGenerator racunGenerator = new RacunGenerator(seed);
        initButtons();

        for (int i = 0; i < btns.length; i++) {

            btns[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int x = v.getId();
                    x -= 2131165218;

                    if (x >= 0 && x <= 9) {

                        rez += x;
                        tvRacun.setText(racunGenerator.getRacun().replace("?", rez));

                    }
                    else if (x == 10 && rez.length() > 0) {

                        rez = rez.substring(0, rez.length() - 1);

                        if (rez.length() > 0)
                            tvRacun.setText(racunGenerator.getRacun().replace("?", rez));
                        else
                            tvRacun.setText(racunGenerator.getRacun());

                    }

                }

            });

        }

        Button button = findViewById(R.id.btnGenRacun);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tvRacun.setText(racunGenerator.getGenRacun());

            }

        });

    }

    private void initButtons() {

        btns[0] = findViewById(R.id.btn0);
        btns[1] = findViewById(R.id.btn1);
        btns[2] = findViewById(R.id.btn2);
        btns[3] = findViewById(R.id.btn3);
        btns[4] = findViewById(R.id.btn4);
        btns[5] = findViewById(R.id.btn5);
        btns[6] = findViewById(R.id.btn6);
        btns[7] = findViewById(R.id.btn7);
        btns[8] = findViewById(R.id.btn8);
        btns[9] = findViewById(R.id.btn9);
        btns[10] = findViewById(R.id.btnEqual);
        btns[11] = findViewById(R.id.btnDelete);

    }

}
