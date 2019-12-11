package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Random;

import static com.jst_pa_ti.calculator_wars.MainActivity.prejel;
import static com.jst_pa_ti.calculator_wars.Streznik.rezultati;

public class Home extends AppCompatActivity {

    public static BluetoothAdapter bluetoothAdapter;
    public static int seed;
    public static String ime; //bluetooth ime naprave

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rezultati.clear();
        prejel=false;



        Random seedGen = new Random();
        seed = seedGen.nextInt();

        Button streznik = findViewById(R.id.button);
        Button odjemalec = findViewById(R.id.button2);
        Button racun = findViewById(R.id.button3);

        Uri uri1 = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.calc_l);
        ImageView calcL=findViewById(R.id.calc_l);
        Glide.with(this).load(uri1).into(calcL);

        Uri urir = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.calc_r);
        ImageView calcR=findViewById(R.id.calc_r);
        Glide.with(this).load(urir).into(calcR);

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

        //ti se malo hecaš fnt

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Naprava ne podpira Bluetooth povezave. :(", Toast.LENGTH_LONG).show();

        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
            ime=bluetoothAdapter.getName();
        }

    }
}
