package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;
import static com.jst_pa_ti.calculator_wars.Home.bluetoothAdapter;
import static com.jst_pa_ti.calculator_wars.Home.seed;
import static com.jst_pa_ti.calculator_wars.MainActivity.stRacunov;

public class Streznik extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private static ArrayList<Naprava> naprave = new ArrayList<Naprava>();
    //private ListView seznam_naprav;
    private static Context mContext;
    //UUID neki;
    BluetoothServerSocket server_socket;
    public static UUID nas_uuid=UUID.fromString("accf84f5-005f-477d-86e1-1b2254e88259");
    public static boolean jeStreznik;
    //public static MyBluetoothService.ConnectedThread povezava_public;
    public static MyBluetoothService.ConnectedThread [] povezave_public=new MyBluetoothService.ConnectedThread[7];//maks št odjemalcev
    public static int st_odjemalca=0; //stevilo odjemalcev
    public static ArrayList<Rezultat> rezultati=new ArrayList<Rezultat>();  //tabela rezultatov za vse igralce
    public static ListView povezani;
    static Button start;


    public static void zacni(){

        MainActivity.stanje=2;

        for(int i=0; i<povezave_public.length; i++){
            if(povezave_public[i]==null){
                break;
            }
            povezave_public[i].write("ola".getBytes());
        }
        //naredi strežnik neviden
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1);
        mContext.startActivity(discoverableIntent);

        Intent zacni=new Intent(mContext, MainActivity.class);
        mContext.startActivity(zacni);
    }

    public static Handler sporocila = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            //System.out.println(inputMessage.toString());
            //if(inputMessage.what==)

            String s="";
            byte [] neki=(byte[])inputMessage.obj;
            //arg1 je število bajtov
            for(int i=0; i<inputMessage.arg1; i++){
                s=s+((char)neki[i])+"";
            }
            //System.out.println(s);
            if(inputMessage.arg2==1){////arg2 =1 -> prenesemo rezultat
                String [] tab=s.split("\n");
                rezultati.add(new Rezultat(tab[3],tab[1],tab[0],tab[2]));
                /*MainActivity.oskips=Integer.parseInt(tab[0]);
                MainActivity.olives=Integer.parseInt(tab[1]);
                MainActivity.ostRacunov=Integer.parseInt(tab[2]);
                MainActivity.nasprotnik=tab[3];*/
                if(rezultati.size()==st_odjemalca){
                    MainActivity.prejel=true;
                }
                System.out.println("nekiniuredi");
            }/*if(inputMessage.arg2==-1){////arg2 =-1 -> prenesemo nastavitve igre
                String [] tab=s.split("\n");
                MainActivity.oskips=Integer.parseInt(tab[0]);
                MainActivity.olives=Integer.parseInt(tab[1]);
                MainActivity.ostRacunov=Integer.parseInt(tab[2]);
                MainActivity.nasprotnik=tab[3];
                MainActivity.prejel=true;
            }*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streznik);
        mContext=this;
       // seznam_naprav=findViewById(R.id.list);

        jeStreznik=true;
        start=findViewById(R.id.start);

        final TextView skipd=findViewById(R.id.skipsd);
        final TextView lived=findViewById(R.id.livesd);
        final TextView lengthd=findViewById(R.id.timed);

        povezani=findViewById(R.id.povezani);

        skipd.setText(MainActivity.skips+"");
        lived.setText(MainActivity.lives+"");
        lengthd.setText(MainActivity.trajanje+"");

        final Button ok= findViewById(R.id.enter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView skip=findViewById(R.id.skips);
                TextView live=findViewById(R.id.lives);
                TextView length=findViewById(R.id.time);

                TextView def=findViewById(R.id.privz);
                //def.setVisibility(View.INVISIBLE);
                def.setText("Nastavljene vrednosti: ");
                skip.setVisibility(View.INVISIBLE);
                live.setVisibility(View.INVISIBLE);
                length.setVisibility(View.INVISIBLE);

                if(skip.getText().length()!=0){
                    MainActivity.skips=Integer.parseInt(skip.getText()+"");
                }
                if(live.getText().length()!=0){
                    MainActivity.lives=Integer.parseInt(live.getText()+"");
                }
                if(length.getText().length()!=0){
                    MainActivity.trajanje=Integer.parseInt(length.getText()+"");
                }
                skipd.setText(MainActivity.skips+"");
                lived.setText(MainActivity.lives+"");
                lengthd.setText(MainActivity.trajanje+"");
                final AcceptThread test=new AcceptThread();
                Thread server=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        test.run();
                    }
                });
                server.start();

                TextView cakam=findViewById(R.id.wait1);
                TextView jst=findViewById(R.id.ta_naprava);
                ProgressBar vrti=findViewById(R.id.progress);
                cakam.setVisibility(View.VISIBLE);
                vrti.setVisibility(View.VISIBLE);
                jst.setVisibility(View.VISIBLE);
                jst.setText("Ime naprave: "+Home.ime);

                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                ok.setVisibility(View.INVISIBLE);


            }
        });

        Button start=findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zacni();
            }
        });
        /*bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
           // Intent intent = new Intent(Streznik.this, Home.class);
           // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(getApplicationContext(), "Naprava ne podpira Bluetooth povezave. :(", Toast.LENGTH_LONG).show();

            //startActivity(intent);

        }else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            //while(bluetoothAdapter==null);*/
            /*bluetoothAdapter.startDiscovery();
            System.out.println("zčnu");*/

            //nrdiSocket();
            //while(server_socket==null);
            //naredi strežnik viden drugim napravam





        //}
        // Create a BroadcastReceiver for ACTION_FOUND.
        /*IntentFilter najden = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, najden);
        System.out.println("doba");*/

        /*seznam_naprav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(parent.getItemAtPosition(position).toString());


            }
        });*/

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                //naprave.add(deviceName+ "\n" + device.getAddress());
                naprave.add(new Naprava(deviceName,deviceHardwareAddress));
                System.out.println(deviceName);

            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        bluetoothAdapter.cancelDiscovery();
        //bluetoothAdapter.disable();
        try {
            unregisterReceiver(receiver);
        }catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }
    }





    /*public void nrdiSocket(){
        try {
            neki=UUID.randomUUID();
            socket=bluetoothAdapter.listenUsingRfcommWithServiceRecord("Calculator_wars", neki);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static void povezano(final BluetoothSocket socket){
        MyBluetoothService blutuf=new MyBluetoothService();
        final MyBluetoothService.ConnectedThread povezava= blutuf.new ConnectedThread(socket);
        Thread poslusa=new Thread(new Runnable() {
            @Override
            public void run() {
                povezave_public[st_odjemalca]=povezava;
                int pred=naprave.size();
                naprave.add(new Naprava(socket.getRemoteDevice().getName(),socket.getRemoteDevice().getAddress()));
                if(pred<naprave.size()) {
                    poslji_parametre();
                    st_odjemalca++;
                }
                povezava.run();
            }
        });
        poslusa.start();


        Thread server=new Thread(new Runnable() {
            @Override
            public void run() {
                AcceptThread test=new AcceptThread();
                test.run();
            }
        });
        server.start();

        ((Streznik)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                povezani.setAdapter(new ArrayAdapter<Naprava>(mContext,android.R.layout.simple_list_item_1,naprave));
                start.setVisibility(View.VISIBLE);
            }
        });

    }
    public static void poslji_parametre(){
        byte[] neki=(seed+"\n"+MainActivity.skips+"\n"+MainActivity.lives+"\n"+MainActivity.trajanje).getBytes();
        Streznik.povezave_public[st_odjemalca].write(neki);
        //zacni();
    }


       private static class AcceptThread extends Thread {
                private final BluetoothServerSocket mmServerSocket;

                public AcceptThread() {
                    // Use a temporary object that is later assigned to mmServerSocket
                    // because mmServerSocket is final.
                    BluetoothServerSocket tmp = null;
                    try {
                        // MY_UUID is the app's UUID string, also used by the client code.
                        tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Calculator wars", nas_uuid);
                    } catch (IOException e) {
                        Log.e("Connection error", "Socket's listen() method failed", e);
                    }
                    mmServerSocket= tmp;
                }

                public void run() {
                    BluetoothSocket socket = null;
                    // Keep listening until exception occurs or a socket is returned.
                    while (true) {
                        try {
                            socket = mmServerSocket.accept();
                        } catch (IOException e) {
                            Log.e("Connection error", "Socket's accept() method failed", e);
                            break;
                        }

                        if (socket != null) {
                            // A connection was accepted. Perform work associated with
                            // the connection in a separate thread.
                            povezano(socket);
                            try {
                                mmServerSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }

                // Closes the connect socket and causes the thread to finish.
                public void cancel() {
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e("Connection error", "Could not close the connect socket", e);
                    }
                }
            }

}
class Rezultat{
    private String ime,zivljenja,preskoki,racuni;

    public Rezultat(String ime,String zivljenja,String preskoki,String racuni){
        this.ime=ime;
        this.zivljenja=zivljenja;
        this.preskoki=preskoki;
        this.racuni=racuni;
    }

    public String vrniIme(){
        return ime;
    }
    public String vrniZivljenja(){
        return zivljenja;
    }
    public String vrniPreskoke(){
        return preskoki;
    }
    public String vrniRacune(){
        return racuni;
    }
    @Override
    public String toString(){
        return ime+"\n"+"Zivljenja: "+zivljenja+"\n"+"Preskoki: "+preskoki+"\n"+"St. računov: "+racuni;
    }
    public String toStringPro(){
        return ime+"%#%"+zivljenja+"%#%"+preskoki+"%#%"+racuni;
    }
}
class Naprava{
    private String ime,mac;

    public Naprava(String ime,String mac){
        this.ime=ime;
        this.mac=mac;
    }
    public String vrniIme(){
        return ime;
    }
    public String vrniMac(){
        return mac;
    }
    public String toString(){
        return ime+"\n"+mac;
    }
    @Override
    public boolean equals(Object o){
        Naprava neki=(Naprava)o;
       if(neki.vrniMac().equals(this.vrniMac())){
           return true;
       }
       return false;
    }
}
