package com.example.luisguzmn.healthcare40;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguzmn.healthcare40.HealthcareInfo.MenuHinfo;
import com.example.luisguzmn.healthcare40.Helo.HeloConnection;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class Bluetooth extends AppCompatActivity {

    //VARIABLES
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;
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
        menu();
        dias();
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

    private void menu(){
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarMain);
        toolbar.setTitle("Selecciona tu dispositivo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GlobalVars g = (GlobalVars) getApplication();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void dias(){
        //
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
        //
        //DIAS
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        String currentDateTimeStrin = formatter.format(today);

        if (currentDateTimeStrin.equalsIgnoreCase("Monday") || currentDateTimeStrin.equalsIgnoreCase("Lunes")) {
            text_monday.setBackgroundColor(Color.parseColor("#b8ddcd"));
        } else {
            text_monday.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (currentDateTimeStrin.equalsIgnoreCase("Tuesday") || currentDateTimeStrin.equalsIgnoreCase("Martes")) {
            text_tuesday.setBackgroundColor(Color.parseColor("#b8ddcd"));
        } else {
            text_tuesday.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (currentDateTimeStrin.equalsIgnoreCase("Wednesday") || currentDateTimeStrin.equalsIgnoreCase("Miércoles")) {
            text_wednesday.setBackgroundColor(Color.parseColor("#b8ddcd"));
        } else {
            text_wednesday.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (currentDateTimeStrin.equalsIgnoreCase("Thursday") || currentDateTimeStrin.equalsIgnoreCase("Jueves")) {
            text_thursday.setBackgroundColor(Color.parseColor("#b8ddcd"));
        } else {
            text_thursday.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (currentDateTimeStrin.equalsIgnoreCase("Friday") || currentDateTimeStrin.equalsIgnoreCase("Viernes")) {
            text_friday.setBackgroundColor(Color.parseColor("#b8ddcd"));
        } else {
            text_friday.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (currentDateTimeStrin.equalsIgnoreCase("Saturday") || currentDateTimeStrin.equalsIgnoreCase("Sábado")) {
            text_saturday.setBackgroundColor(Color.parseColor("#b8ddcd"));
        } else {
            text_saturday.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (currentDateTimeStrin.equalsIgnoreCase("Sunday") || currentDateTimeStrin.equalsIgnoreCase("Domingo")) {
            text_sunday.setBackgroundColor(Color.parseColor("#b8ddcd"));
        } else {
            text_sunday.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////
    }
}