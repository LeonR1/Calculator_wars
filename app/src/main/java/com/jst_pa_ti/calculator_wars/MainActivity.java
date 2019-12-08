package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import static com.jst_pa_ti.calculator_wars.Streznik.rezultati;

public class MainActivity extends AppCompatActivity {

    public static int stRacunov;
    public static String rez;
    public static TextView tvRacun, stRac, tvTime, tvLives;
    public static Button btns[] = new Button[12];
    public static String time;
    public static int trajanje=10;
    public Timer timer = new Timer(trajanje);
    public Thread thread = new Thread(timer);
    public Handler handler = new Handler();
    public static int lives =3, skips=3;
    public static int stanje=0;//  0= igra se še ni začela  1=igra se je končala 3-prejmemo rezultate od strežnika
    public static int olives,oskips,ostRacunov; //nasprotnikov rezultat
    public static boolean prejel=false; //rata true ko prejmemo rezultat nasprotnika
    public static String nasprotnik; //ime nasprotnika

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stanje=1;

        tvRacun = findViewById(R.id.tvRacun);
        final RacunGenerator racunGenerator = new RacunGenerator(Home.seed);
        initButtons();

        tvRacun.setText(racunGenerator.getGenRacun());
        stRac = findViewById(R.id.stRacunov);
        stRac.setText("Correct solutions: " + stRacunov);
        tvTime = findViewById(R.id.timer);
        tvLives = findViewById(R.id.lives);
        tvLives.setText("Lives: "+lives);

        //lives = 3;
        stRacunov = 0;
        rez = "";
        //skips = 3;

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

                            if(lives>0)
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

        final Button btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setText("Skip: " + skips);
        btnSkip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tvRacun.setText(racunGenerator.getGenRacun());
                if(skips>0)
                    skips--;
                btnSkip.setText("Skip: " + skips);

                if (skips <= 0) {

                    btnSkip.setEnabled(false);

                }

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
        startActivity(new Intent(MainActivity.this, Konec.class));
        if(Streznik.jeStreznik){
            /*byte[] skip=(skips+"\n"+lives+"\n"+stRacunov+"\n"+Home.ime).getBytes();
            Streznik.povezave_public[].write(skip);*/
            rezultati.add(new Rezultat(Home.ime,lives+"",skips+"",stRacunov+""));

        }else{
            byte[] skip=(skips+"\n"+lives+"\n"+stRacunov+"\n"+Home.ime).getBytes();
            Odjemalec.povezava_public_o.write(skip);
            stanje=3;
        }
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

                if (lives <= 0) {

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
