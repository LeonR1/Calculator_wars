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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.getDefaultAdapter;

public class Streznik extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private static ArrayList<Naprava> naprave = new ArrayList<Naprava>();
    private ListView seznam_naprav;
    public static BluetoothAdapter bluetoothAdapter;
    UUID neki;
    BluetoothServerSocket server_socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streznik);

        seznam_naprav=findViewById(R.id.list);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

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
            //while(bluetoothAdapter==null);
            bluetoothAdapter.startDiscovery();
            System.out.println("zčnu");

            //nrdiSocket();
            //while(server_socket==null);
            //naredi strežnik viden drugim napravam
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);

            final AcceptThread test=new AcceptThread();
            Thread server=new Thread(new Runnable() {
                @Override
                public void run() {
                    test.run();
                }
            });
            server.start();



        }
        // Create a BroadcastReceiver for ACTION_FOUND.
        IntentFilter najden = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, najden);
        System.out.println("doba");

        seznam_naprav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(parent.getItemAtPosition(position).toString());


            }
        });

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
                seznam_naprav.setAdapter(new ArrayAdapter<Naprava>(context,android.R.layout.simple_list_item_1,naprave));
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
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

    /*public void nrdiSocket(){
        try {
            neki=UUID.randomUUID();
            socket=bluetoothAdapter.listenUsingRfcommWithServiceRecord("Calculator_wars", neki);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static void povezano(BluetoothSocket socket){
        System.out.println("oštijajjajajajaajajajajjaajjajajajajaj");

    }

       private class AcceptThread extends Thread {
                private final BluetoothServerSocket mmServerSocket;

                public AcceptThread() {
                    // Use a temporary object that is later assigned to mmServerSocket
                    // because mmServerSocket is final.
                    BluetoothServerSocket tmp = null;
                    try {
                        // MY_UUID is the app's UUID string, also used by the client code.
                        neki=UUID.randomUUID();
                        tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Calculator wars", neki);
                    } catch (IOException e) {
                        Log.e("Connection error", "Socket's listen() method failed", e);
                    }
                    mmServerSocket= tmp;
                }

                public void run() {
                    BluetoothSocket socket = null;
                    // Keep listening until exception occurs or a socket is returned.
                    while (true) {
                        System.out.println("oštijaa");
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
