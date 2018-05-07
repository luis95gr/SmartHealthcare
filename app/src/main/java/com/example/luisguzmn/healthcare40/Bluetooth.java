package com.example.luisguzmn.healthcare40;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguzmn.healthcare40.Helo.HeloConnection;

import java.util.ArrayList;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class Bluetooth extends AppCompatActivity {

    //VARIABLES
    BluetoothClass bluetoothClass;
    public String textoPosicion;
    Button b_on, b_off, b_disc, b_list;
    ListView list;
    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLED = 0;
    private static final int REQUEST_DISCOVERABLE = 0;
    SharedPreferences infoBluetooth;
    SharedPreferences spLogin;
    Button buttonHelo, btnJabra;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        //SHARED PREFERENCES
        infoBluetooth = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editorBluetooth = infoBluetooth.edit();
        spLogin=PreferenceManager.getDefaultSharedPreferences(this);
        //
        //CAST
        b_on = (Button) findViewById(R.id.on);
        b_off = (Button) findViewById(R.id.off);
        b_list = (Button) findViewById(R.id.list);
        list = (ListView) findViewById(R.id.viewlist);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buttonHelo = (Button)findViewById(R.id.buttonHelo);
        btnJabra = (Button)findViewById(R.id.btnJabra);

        //

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no disponible", Toast.LENGTH_LONG).show();
        }
        //BLUETOOTH START
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        ArrayList<String> devices = new ArrayList<String>();

        for (BluetoothDevice bt : pairedDevices) {
            BluetoothClass bluetoothClass = bt.getBluetoothClass();
            int tipoDeDispositivo = bluetoothClass.getDeviceClass();
            devices.add("Nombre: " + bt.getName() + "\n Class: " + tipoDeDispositivo + "\n DIRECCIÓN MAC: " + bt.getAddress());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(Bluetooth.this, android.R.layout.simple_list_item_1, devices);
        list.setAdapter(arrayAdapter);
        ///
        ////////////////////
        /////BUTTONS
        b_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLED);
            }
        });
        b_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.disable();
            }
        });

        b_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                ArrayList<String> devices = new ArrayList<String>();

                for (BluetoothDevice bt : pairedDevices) {
                    bluetoothClass = bt.getBluetoothClass();
                    int tipoDeDispositivo = bluetoothClass.getDeviceClass();
                    devices.add("Nombre: " + bt.getName() + "\n Clase: " + tipoDeDispositivo + "\n Dirección MAC: " + bt.getAddress());


                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(Bluetooth.this, android.R.layout.simple_list_item_1, devices);
                list.setAdapter(arrayAdapter);
            }
        });
        btnJabra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        buttonHelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spLogin.getBoolean("success",false)) {
                    Intent intent = new Intent(Bluetooth.this, crearCuentaHelo.class);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(Bluetooth.this, HeloConnection.class));
                }
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String textoPosicion = String.valueOf(adapterView.getItemAtPosition(position));
                String device[] = textoPosicion.split("\n", 5);
                String name = device[0].substring(6);
                String type = device[1].substring(7);
                String macAdress = device[2].substring(12);
                editorBluetooth.putString("name", name);
                editorBluetooth.putString("type", type);
                editorBluetooth.putString("macAdress", macAdress);
                editorBluetooth.apply();
                ShowDialog();
            }
        });

        String name = infoBluetooth.getString("name", "No name found");
        String type = infoBluetooth.getString("type", "No type found");
        String macAdress = infoBluetooth.getString("macAdress", "No MAC ADRESS found");

    }

    public void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("DISPOSITIVO");
        builder.setMessage("Seleccionaste: " + infoBluetooth.getString("name", "No name found") + "\n ¿Quiéres continuar?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(infoBluetooth.getString("name","No name found").equalsIgnoreCase("HeloLx")) {
                    Intent intent = new Intent(Bluetooth.this, PrincipalDashboard.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Bluetooth.this, "No HELOLX", Toast.LENGTH_LONG).show();
                }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                    }
                });
        builder.create().show();

    }

    public void onPause(){
        super.onPause();
        finish();
    }
}