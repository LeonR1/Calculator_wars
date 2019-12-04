package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import static com.jst_pa_ti.calculator_wars.Home.bluetoothAdapter;
import static com.jst_pa_ti.calculator_wars.Home.seed;
import static com.jst_pa_ti.calculator_wars.Streznik.jeStreznik;
import static com.jst_pa_ti.calculator_wars.Streznik.nas_uuid;
import static com.jst_pa_ti.calculator_wars.Streznik.rezultati;
import static com.jst_pa_ti.calculator_wars.Streznik.st_odjemalca;

public class Odjemalec extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private static ArrayList<Odjemalec.Naprava> naprave = new ArrayList<Odjemalec.Naprava>();
    private ListView seznam_naprav;
    static TextView ime;
    private static Context mContext;
    public static MyBluetoothService.ConnectedThread povezava_public_o;

    public static Handler sporocila = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            //System.out.println(inputMessage.toString());
            //if(inputMessage.what==)

                String s = "";
                byte[] neki = (byte[]) inputMessage.obj;
                //arg1 je število bajtov
                for (int i = 0; i < inputMessage.arg1; i++) {
                    s = s + ((char) neki[i]) + "";
                }
                //System.out.println(s);
            if(true){
                if (inputMessage.arg2 == 0) {
                /*ime.setText(s);
                seed=Integer.parseInt(s);*/
                    String[] tab = s.split("\n");
                    seed = Integer.parseInt(tab[0]);
                    System.out.println(tab[1] + " " + tab[2] + " " + tab[3]);
                    MainActivity.skips = Integer.parseInt(tab[1]);
                    MainActivity.lives = Integer.parseInt(tab[2]);
                    MainActivity.trajanje = Integer.parseInt(tab[3]);
                    MainActivity.stanje=2;
                    //zacni();
                } else if (inputMessage.arg2 == 1) {//arg2 =1 -> prenesemo rezultat
                    String[] tab = s.split("\n");

                    rezultati.add(new Rezultat(tab[3],tab[1],tab[0],tab[2]));

                    if(rezultati.size()==st_odjemalca+1){
                        MainActivity.prejel=true;
                    }

                    MainActivity.oskips = Integer.parseInt(tab[0]);
                    MainActivity.olives = Integer.parseInt(tab[1]);
                    MainActivity.ostRacunov = Integer.parseInt(tab[2]);
                    MainActivity.nasprotnik = tab[3];

                    //MainActivity.prejel = true;
                }if(inputMessage.arg2==2){////arg2 =2 -> zacnemo igro
                    zacni();
                    //MainActivity.prejel=true;
                }
                if(inputMessage.arg2==3&&!jeStreznik){
                    String[] tab = s.split("\n");
                    for(int i=0; i<tab.length; i++){
                        String tab1[]=tab[i].split("%#%");
                        if(tab1[0].equals("")){
                            continue;
                        }
                        System.out.println("i:"+i);
                        System.out.println(tab[0]);
                        System.out.println(tab[1]);
                        System.out.println(tab[2]);
                        System.out.println(tab1[0]);
                        System.out.println(tab1[0]+" "+tab1[1]+" "+tab1[2]+" "+tab1[3]);
                        //System.out.println(tab[0]);
                        rezultati.add(new Rezultat(tab1[0],tab1[1],tab1[2],tab1[3]));
                        MainActivity.prejel = true;
                    }
                }

            }
        }

    };
public static void zacni(){
    Intent zacni=new Intent(mContext, MainActivity.class);
    mContext.startActivity(zacni);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odjemalec);

        mContext=this;

        Handler sporocila = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                System.out.println(inputMessage);
            }
        };

        ime=findViewById(R.id.textView);
        seznam_naprav = findViewById(R.id.list);

            bluetoothAdapter.startDiscovery();
            System.out.println("zčnu");

        IntentFilter najden = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, najden);
        System.out.println("doba");

        seznam_naprav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // System.out.println(parent.getItemAtPosition(position).toString().split("\n")[1]);
                //BluetoothDevice oo=

                BluetoothDevice naprava=bluetoothAdapter.getRemoteDevice(parent.getItemAtPosition(position).toString().split("\n")[1]);
                ParcelUuid list[] = naprava.getUuids();
                final Odjemalec.ConnectThread test=new Odjemalec.ConnectThread(naprava);
                Thread server=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        test.run();
                    }
                });
                server.start();


            }
        });
    }
    public static void povezano(BluetoothSocket mmSocket){

        System.out.println("Neki bo še iz tega");
        MyBluetoothService blutuf=new MyBluetoothService();
        final MyBluetoothService.ConnectedThread povezava= blutuf.new ConnectedThread(mmSocket);
        Thread poslusa=new Thread(new Runnable() {
            @Override
            public void run() {
                povezava_public_o=povezava;
                povezava.run();

            }
        });
        poslusa.start();
       // seed_demo.setText();
    }
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(nas_uuid);
            } catch (IOException e) {
                Log.e("Connection error", "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                System.out.println(connectException);
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("Connection error", "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            povezano(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("Connection error", "Could not close the client socket", e);
            }
        }
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
                if(naprave.size()==0&&deviceName!=null){
                    naprave.add(new Odjemalec.Naprava(deviceName,deviceHardwareAddress));
                    seznam_naprav.setAdapter(new ArrayAdapter<Odjemalec.Naprava>(context,android.R.layout.simple_list_item_1,naprave));
                }else if(deviceName!=null) {
                    for (int i = 0; i < naprave.size(); i++) {
                        if (deviceName==null) {
                            break;
                        }
                        if (naprave.get(i).vrniMac().equals(deviceHardwareAddress)) {
                            break;
                        }
                        if (i + 1 == naprave.size()) {
                            naprave.add(new Odjemalec.Naprava(deviceName, deviceHardwareAddress));
                            seznam_naprav.setAdapter(new ArrayAdapter<Odjemalec.Naprava>(context,android.R.layout.simple_list_item_1,naprave));
                        }
                    }
                }
                System.out.println(deviceName);
                seznam_naprav.setAdapter(new ArrayAdapter<Odjemalec.Naprava>(context,android.R.layout.simple_list_item_1,naprave));
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        bluetoothAdapter.cancelDiscovery();
        //bluetoothAdapter.disable();
        unregisterReceiver(receiver);
    }
    public class Naprava{
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
    }

}

