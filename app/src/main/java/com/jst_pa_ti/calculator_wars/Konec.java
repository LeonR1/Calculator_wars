package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.jst_pa_ti.calculator_wars.Streznik.povezave_public;
import static com.jst_pa_ti.calculator_wars.Streznik.rezultati;

public class Konec extends AppCompatActivity {


    private static ListView seznam_rezultatov;
    private static TextView zmago;
    //private static ArrayList<Rezultat> list=new ArrayList<Rezultat>();
    private static Context tole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konec);

        tole=this;
        seznam_rezultatov=findViewById(R.id.seznamcek);
        TextView ime=findViewById(R.id.ime);
        zmago=findViewById(R.id.zmagovalec);
        ime.setText("Ime vaše naprave:\n"+Home.ime);
        String s=String.format("Zivljenja: %s \n skipi: %s \n racuni: %s",MainActivity.lives,MainActivity.skips,MainActivity.stRacunov);
        Thread rezultati=new Thread(new Runnable() {
            @Override
            public void run() {
                while(!MainActivity.prejel&&Streznik.rezultati.size()!=Streznik.st_odjemalca+1){
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

        final Context to=this;

        Button nazaj=findViewById(R.id.back);
        nazaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent zacni=new Intent(to, Home.class);
                to.startActivity(zacni);
                finish();
            }
        });
    }
    public static void prikazi(){
        String os=String.format("Zivljenja: %s \n skipi: %s \n racuni: %s",MainActivity.olives,MainActivity.oskips,MainActivity.ostRacunov);

        if(Streznik.jeStreznik) {
            //Streznik.rezultati.addAll(Streznik.rezultati);
           // seznam_rezultatov.setAdapter(new ArrayAdapter<Rezultat>(tole, android.R.layout.simple_list_item_1, Streznik.rezultati));

            ArrayList<RezultatiZmagovalec> rez=new ArrayList<>();
            for(int i=0; i<Streznik.rezultati.size(); i++){
                rez.add(new RezultatiZmagovalec(Streznik.rezultati.get(i)));
            }
            seznam_rezultatov.setAdapter(new ArrayAdapter<RezultatiZmagovalec>(tole, android.R.layout.simple_list_item_1, rez));
            nastaviZmagovalca();

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
            //seznam_rezultatov.setAdapter(new ArrayAdapter<Rezultat>(tole, android.R.layout.simple_list_item_1, Streznik.rezultati));

            ArrayList<RezultatiZmagovalec> rez=new ArrayList<>();
            for(int i=0; i<Streznik.rezultati.size(); i++){
                rez.add(new RezultatiZmagovalec(Streznik.rezultati.get(i)));
            }
            seznam_rezultatov.setAdapter(new ArrayAdapter<RezultatiZmagovalec>(tole, android.R.layout.simple_list_item_1, rez));
            nastaviZmagovalca();

        }
    }

    @Override
    public void onBackPressed() {
        Intent zacni=new Intent(this, Home.class);
        this.startActivity(zacni);
        finish();
    }
    public static void nastaviZmagovalca(){
        int maks=-9999;
        for(int i=0; i<Streznik.rezultati.size(); i++){//najdi maks rezultat
            if(zracuniRezultat(Streznik.rezultati.get(i))>maks){
                maks=zracuniRezultat(Streznik.rezultati.get(i));
            }
        }
        for(int i=0; i<Streznik.rezultati.size(); i++){//najdi maks rezultat
            if(zracuniRezultat(Streznik.rezultati.get(i))==maks){
                zmago.setText("Zmagovalec je:\n"+Streznik.rezultati.get(i).vrniIme());
            }
        }
    }

    public static int zracuniRezultat(Rezultat r){
        return Integer.parseInt(r.vrniRacune())+(2*Integer.parseInt(r.vrniZivljenja()))+(2*Integer.parseInt(r.vrniPreskoke()));
    }
}


class RezultatiZmagovalec {

    private Rezultat rez;


    public RezultatiZmagovalec(Rezultat rez) {
        this.rez = rez;
    }

    public int zracuniRezultat(Rezultat r) {
        return Integer.parseInt(r.vrniRacune()) + (2 * Integer.parseInt(r.vrniZivljenja())) + (2 * Integer.parseInt(r.vrniPreskoke()));
    }

    @Override
    public String toString() {

        return rez.vrniIme() + "\n" + "Zivljenja: " + rez.vrniZivljenja() + "\n" + "Preskoki: " + rez.vrniPreskoke() + "\n" + "St. računov: " + rez.vrniRacune() + "\n" + "Rezultat: " + zracuniRezultat(rez);
    }
}
