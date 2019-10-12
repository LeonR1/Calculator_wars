package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int stRacunov;
    public static String rez;
    public static TextView tvRacun, stRac, tvTime, tvLives;
    public static Button btns[] = new Button[12];
    public static String time;
    public Timer timer = new Timer(60);
    public Thread thread = new Thread(timer);
    public Handler handler = new Handler();
    public static int lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRacun = findViewById(R.id.tvRacun);
        final RacunGenerator racunGenerator = new RacunGenerator(Home.seed);
        initButtons();

        tvRacun.setText(racunGenerator.getGenRacun());
        stRac = findViewById(R.id.stRacunov);
        stRac.setText("Št. Pravilnih Računov: " + stRacunov);
        tvTime = findViewById(R.id.timer);
        tvLives = findViewById(R.id.lives);
        tvLives.setText("Lives: 3");

        lives = 3;
        stRacunov = 0;
        rez = "";

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
                    else if (x == 11 && rez.length() > 0) {

                        boolean pravilen = racunGenerator.checkRezultat(Integer.parseInt(rez));

                        if (pravilen) {

                            stRacunov++;
                            stRac.setText("Št. Pravilnih Računov: " + stRacunov);

                        }
                        else {

                            lives--;
                            tvLives.setText("Lives: " + lives);

                        }

                        racunGenerator.setDifficulty(stRacunov);
                        tvRacun.setText(racunGenerator.getGenRacun());

                        rez = "";

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

        Button resi = findViewById(R.id.button5);
        resi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rez = "" + racunGenerator.getRezultat();

                boolean pravilen = racunGenerator.checkRezultat(Integer.parseInt(rez));

                if (pravilen) {

                    stRacunov++;
                    stRac.setText("Št. Pravilnih Računov: " + stRacunov);

                }

                racunGenerator.setDifficulty(stRacunov);
                tvRacun.setText(racunGenerator.getGenRacun());

                rez = "";

            }

        });

        thread.start();

    }

    public void finish() {

        startActivity(new Intent(MainActivity.this, Home.class));

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

    class Timer implements Runnable {

        int sekund = 0;

        public Timer(int secund) {

            this.sekund = secund;

        }

        @Override
        public void run() {

            for (int i = sekund; i >= 0; i--) {

                if (lives == 0) {

                    finish();
                    return;

                }

                time = "Preostali čas: " + i + "s";
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        tvTime.setText(time);

                    }

                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            finish();

        }

    }

}
