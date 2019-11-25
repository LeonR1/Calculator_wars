package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.jst_pa_ti.calculator_wars.Streznik.povezave_public;

public class Konec extends AppCompatActivity {

    private static TextView my_score;
    private static TextView opponent_score;
    private static TextView me_text;
    private static TextView opponent_text;
    private static ListView seznam_rezultatov;
    //private static ArrayList<Rezultat> list=new ArrayList<Rezultat>();
    private static Context tole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konec);

        tole=this;

        seznam_rezultatov=findViewById(R.id.seznamcek);
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
        if(Streznik.jeStreznik) {
            //Streznik.rezultati.addAll(Streznik.rezultati);
            seznam_rezultatov.setAdapter(new ArrayAdapter<Rezultat>(tole, android.R.layout.simple_list_item_1, Streznik.rezultati));

            //poslji score
            String s="";
            for(int i=0; i<Streznik.rezultati.size(); i++){
                s=s+"\n"+Streznik.rezultati.get(i).toStringPro();
            }

            MainActivity.stanje=3;

            byte[] skip=(s).getBytes();
            for(int i=0; i<povezave_public.length; i++){
                if(povezave_public[i]==null){
                    break;
                }
                povezave_public[i].write(skip);
            }
        }else{
            seznam_rezultatov.setAdapter(new ArrayAdapter<Rezultat>(tole, android.R.layout.simple_list_item_1, Streznik.rezultati));
        }
    }
}
