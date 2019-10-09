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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Odjemalec extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private static ArrayList<Odjemalec.Naprava> naprave = new ArrayList<Odjemalec.Naprava>();
    private ListView seznam_naprav;
    public static BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odjemalec);

        seznam_naprav = findViewById(R.id.list);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            // Intent intent = new Intent(Streznik.this, Home.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(getApplicationContext(), "Naprava ne podpira Bluetooth povezave. :(", Toast.LENGTH_LONG).show();

            //startActivity(intent);

        } else {
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



        }
        IntentFilter najden = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, najden);
        System.out.println("doba");

        seznam_naprav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // System.out.println(parent.getItemAtPosition(position).toString().split("\n")[1]);
                //BluetoothDevice oo=
               /* final Odjemalec.ConnectThread test=new Odjemalec.ConnectThread(.getItemAtPosition(position).toString().split("\n")[1]);
                Thread server=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        test.run();
                    }
                });
                server.start();*/


            }
        });
    }
    public static void povezano(BluetoothSocket mmSocket){

        System.out.println("Neki bo še iz tega");
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
                tmp = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
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
                naprave.add(new Odjemalec.Naprava(deviceName,deviceHardwareAddress));
                System.out.println(deviceName);
                seznam_naprav.setAdapter(new ArrayAdapter<Odjemalec.Naprava>(context,android.R.layout.simple_list_item_1,naprave));
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
}
