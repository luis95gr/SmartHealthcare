package com.example.luisguzmn.healthcare40;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;


import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.graphics.Color;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



public class MainScreen extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //VARIABLES
    BluetoothAdapter bluetoothAdapter;
    //AGREGAR ESTAS VARIABALES
    int headphones = 0;
    int smartwatch = 0;
    int tipoDisp = 0;


    ImageButton imageButtonSS;
    ImageButton imageButtonSH;
    ImageButton imageButtonSW;
    Button buttonStart;
    TextView text_monday;
    TextView text_tuesday;
    TextView text_wednesday;
    TextView text_thursday;
    TextView text_friday;
    TextView text_saturday;
    TextView text_sunday;
    TextView texto;
    private static final int REQUEST_ENABLED = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE = 0;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //
        //PRUEBA
        SharedPreferences spMeasuresSaved;
        spMeasuresSaved = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spMeasuresSavedEditor = spMeasuresSaved.edit();
        int contador=1;
        String[] stringsSet = new String[5];
        stringsSet[contador] = "odds";
        contador++;
        stringsSet[contador] = "odsdf";
        contador++;
        stringsSet[contador] = "prfs";
        for (int mor=1;mor<=contador; mor++){
            spMeasuresSavedEditor.putString("stringSet" + (mor), stringsSet[mor]);
        }
        spMeasuresSavedEditor.apply();
        //

        //CAST
        ImageView image_profile = (ImageView) findViewById(R.id.image_profile);
        TextView textNameMain = (TextView) findViewById(R.id.textNameMain);
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
        imageButtonSW = (ImageButton) findViewById(R.id.imageButtonSW);
        imageButtonSH = (ImageButton) findViewById(R.id.imageButtonSH);
        imageButtonSS = (ImageButton) findViewById(R.id.imageButtonSS);
        buttonStart = (Button)findViewById(R.id.buttonStart);

        //set image profile
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this,UploadImage.class);
                startActivity(intent);
            }
        });
        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Main");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        GlobalVars g = (GlobalVars) getApplication();
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName(g.getName()).
                                withEmail(g.getEmail()).withIcon(getDrawable(R.drawable.profile)))
                .withSelectionListEnabledForSingleProfile(false).withHeaderBackground(R.drawable.header)
                .build();
        //if you want to update the items at a later time it is recommended to keep it in a variable
        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_main).withIcon(GoogleMaterial.Icon.gmd_accessibility);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profile).withIcon(GoogleMaterial.Icon.gmd_account_balance);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_records).withIcon(GoogleMaterial.Icon.gmd_add_to_photos);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_statistics).withIcon(GoogleMaterial.Icon.gmd_adb);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_item_healt).withIcon(GoogleMaterial.Icon.gmd_attach_file);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.drawer_item_settinds).withIcon(GoogleMaterial.Icon.gmd_bluetooth);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName(R.string.drawer_item_about).withIcon(GoogleMaterial.Icon.gmd_terrain);

        new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar).withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        new DividerDrawerItem(),
                        item6,
                        item7
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (position == 2) {
                            Intent intent = new Intent(MainScreen.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 4){
                            Intent intent = new Intent(MainScreen.this, Statistics.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }

                        return false;
                    }
                })
                .build();

        //-------------------------------------------------------------------------------------------
        //MENU

        //MAIN SCREEN
        LottieAnimationView animationView_nivel = (LottieAnimationView) findViewById(R.id.animation_view_nivel);
        LottieAnimationView animationView_nivel_rojo = (LottieAnimationView) findViewById(R.id.animation_view_nivel_rojo);
        Picasso.with(this).load("http://meddata.sytes.net/newuser/profileImg/" + sp.getString("imagen", "No Image"))
                .resize(250, 250).centerCrop().into(image_profile);
        animationView_nivel.setSpeed(10f);
        animationView_nivel_rojo.setSpeed(100f);
        animationView_nivel.playAnimation(0, 10);
        animationView_nivel_rojo.playAnimation(0, 30);
        textNameMain.setText(sp.getString("name", "No name"));

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
        //////////////////////////////////////////////////////////////////////////////////////////////////
        //BOTONES
        imageButtonSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageButtonSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        imageButtonSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, Bluetooth.class);
                startActivity(intent);
            }
        });
        ///////////////////////////////////////////////////////////////////////
        ///AQUI ES LA PARTE DEL BLUETOOTH DENTRO DEL ON CREATE
        //BLUETOOTH/////////
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices
                = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            //Toast.makeText(getApplicationContext(), pairedDevices.size() + " bLUETOOTH DEVICEs paired.", Toast.LENGTH_SHORT).show();
            for (BluetoothDevice device : pairedDevices) {

                BluetoothClass bluetoothClass = device.getBluetoothClass();
                int estado = device.getBondState();

                if (estado == BluetoothDevice.BOND_BONDED) {
                    int tipoDeDispositivo = bluetoothClass.getDeviceClass();
                    if (tipoDeDispositivo == 1028 || tipoDeDispositivo==7936) {
                        headphones = headphones + 1;
                        imageButtonSH.getBackground().setTint(Color.parseColor("#ADFF2F"));
                    }

                    if (tipoDeDispositivo == 268 || tipoDeDispositivo==7936) {
                        imageButtonSW.getBackground().setTint(Color.parseColor("#ADFF2F"));
                        smartwatch = smartwatch + 1;
                    }
                    if(tipoDeDispositivo == 1032){
                        //imageButtonSS.getBackground().setTint(Color.parseColor("#ADFF2F"));
                    }
                }else{
                    imageButtonSH.getBackground().setTint(Color.parseColor("#b8ddcd"));
                    imageButtonSW.getBackground().setTint(Color.parseColor("#b8ddcd"));
                    imageButtonSS.getBackground().setTint(Color.parseColor("#b8ddcd"));
                }


            }
        }


        //Toast.makeText(getApplicationContext(), smartwatch + " smartwatch paired.", Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(), headphones + " headphones paired.", Toast.LENGTH_SHORT).show();


        // END BLUETOOTH

    }









    //ELIMINAR BACK PRESS
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK ? true : super.onKeyDown(keyCode, event));
    }
    //MENU 3 DOTS
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(MainScreen.this,MainScreen.class));
                return true;

            case R.id.logout:
                SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.apply();
                SharedPreferences spLogin = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor spLoginEditor = spLogin.edit();
                spLoginEditor.putBoolean("success",false);
                spLoginEditor.apply();
                startActivity(new Intent(MainScreen.this,MainActivity.class));
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }


    }
    //END MENU 3 DOTS


}


