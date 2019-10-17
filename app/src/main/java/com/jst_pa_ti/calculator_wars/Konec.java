package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Konec extends AppCompatActivity {

     static TextView my_score;
     static TextView opponent_score;
     static TextView me_text;
     static TextView opponent_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konec);

        my_score=findViewById(R.id.my_score);
        opponent_score=findViewById(R.id.opponent_score);
        me_text=findViewById(R.id.me);
        opponent_text=findViewById(R.id.opponent);
        String s=String.format("Zivljenja: %s \n skipi: %s \n racuni: %s",MainActivity.lives,MainActivity.skips,MainActivity.stRacunov);
        my_score.setText(s);
        me_text.setText(Home.ime);
        Thread rezultati=new Thread(new Runnable() {
            @Override
            public void run() {
                while(!MainActivity.prejel){
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable(){
                    public void run() {
                        prikazi();
                    }
                });

            }
        });
        rezultati.start();

    }
    public static void prikazi(){
        String os=String.format("Zivljenja: %s \n skipi: %s \n racuni: %s",MainActivity.olives,MainActivity.oskips,MainActivity.ostRacunov);
        opponent_score.setText(os);
        opponent_text.setText(MainActivity.nasprotnik);
    }
}
