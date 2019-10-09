package com.jst_pa_ti.calculator_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Streznik extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private static ArrayList<Naprava> naprave = new ArrayList<Naprava>();
    private ListView seznam_naprav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streznik);

        seznam_naprav=findViewById(R.id.list);

        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

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
            bluetoothAdapter.startDiscovery();
            System.out.println("zčnu");
        }
        // Create a BroadcastReceiver for ACTION_FOUND.
        IntentFilter najden = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, najden);
        System.out.println("doba");

        seznam_naprav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("ejla kej prtiskaš");
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
}
