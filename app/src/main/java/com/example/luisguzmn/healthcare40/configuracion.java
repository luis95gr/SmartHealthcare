package com.example.luisguzmn.healthcare40;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.luisguzmn.healthcare40.HealthcareInfo.MenuHinfo;
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
import java.util.Calendar;
import java.util.Date;

public class configuracion extends AppCompatActivity {
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday,swStatus;
    EditText etName,etEmail,etPais,etGenero,etNacimiento,etAltur,etPeso,etPass;
    Switch switchK;

    ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

        menu();
        dias();

        etEmail = (EditText)findViewById(R.id.etEmail);
        etName = (EditText)findViewById(R.id.etNombre);
        profileImg = (ImageView)findViewById(R.id.profile_image);
        switchK = (Switch)findViewById(R.id.switchK);
        etPais = (EditText)findViewById(R.id.etPais);
        etGenero = (EditText)findViewById(R.id.etGenero);
        etNacimiento = (EditText)findViewById(R.id.etNacimiento);
        etAltur = (EditText)findViewById(R.id.etAltura);
        etPeso = (EditText)findViewById(R.id.etPeso);
        etPass = (EditText)findViewById(R.id.etPass);
        swStatus = (TextView)findViewById(R.id.swStatus);



        Picasso.with(this).load("http://meddata.sytes.net/newuser/profileImg/" + sp.getString("imagen", "No Image"))
                .resize(250, 250).centerCrop().into(profileImg);

       switchK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                etName.setEnabled(true);
                etName.setFocusable(true);
                etName.setFocusableInTouchMode(true);
                etName.setText(sp.getString("name","no name"));

                etEmail.setEnabled(true);
                etEmail.setFocusable(true);
                etEmail.setFocusableInTouchMode(true);
                etEmail.setText(sp.getString("email","no email"));

                etPais.setEnabled(true);
                etPais.setFocusable(true);
                etPais.setFocusableInTouchMode(true);
                etPais.setText(sp.getString("country","no country"));

                etGenero.setEnabled(true);
                etGenero.setFocusable(true);
                etGenero.setFocusableInTouchMode(true);
                etGenero.setText(sp.getString("gender","no gender"));

                etNacimiento.setEnabled(true);
                etNacimiento.setFocusable(true);
                etNacimiento.setFocusableInTouchMode(true);
                etNacimiento.setText(sp.getString("birth","no birthday"));


                etAltur.setEnabled(true);
                etAltur.setFocusable(true);
                etAltur.setFocusableInTouchMode(true);
                etAltur.setText(String.valueOf(sp.getInt("height",0)));

                etPeso.setEnabled(true);
                etPeso.setFocusable(true);
                etPeso.setFocusableInTouchMode(true);
                etPeso.setText(String.valueOf(sp.getInt("weight",0)));

                etPass.setEnabled(true);
                etPass.setFocusable(true);
                etPass.setFocusableInTouchMode(true);
                etPass.setText(sp.getString("pass","no pass"));

                swStatus.setText("Actualizar");


                }else{
                    etName.setEnabled(false);
                    etName.setFocusable(false);
                    etName.setFocusableInTouchMode(false);

                    etEmail.setEnabled(false);
                    etEmail.setFocusable(false);
                    etEmail.setFocusableInTouchMode(false);

                    etPeso.setEnabled(false);
                    etPeso.setFocusable(false);
                    etPeso.setFocusableInTouchMode(false);

                    etPais.setEnabled(false);
                    etPais.setFocusable(false);
                    etPais.setFocusableInTouchMode(false);

                    etGenero.setEnabled(false);
                    etGenero.setFocusable(false);
                    etGenero.setFocusableInTouchMode(false);

                    etNacimiento.setEnabled(false);
                    etNacimiento.setFocusable(false);
                    etNacimiento.setFocusableInTouchMode(false);

                    etAltur.setEnabled(false);
                    etAltur.setFocusable(false);
                    etAltur.setFocusableInTouchMode(false);

                    etPass.setEnabled(false);
                    etPass.setFocusable(false);
                    etPass.setFocusableInTouchMode(false);
                    swStatus.setText("Editar");


                }

           }
       });
    }

    private void menu(){
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);

        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Configuración");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        GlobalVars g = (GlobalVars) getApplication();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this).withCompactStyle(true)
                .addProfiles(
                        new ProfileDrawerItem().withName(sp.getString("name","no name")).
                                withEmail(sp.getString("email","no email")).withIcon("http://meddata.sytes.net/newuser/profileImg/"
                                +sp.getString("imagen", "No Image")))
                .withSelectionListEnabledForSingleProfile(false).withHeaderBackground(R.drawable.header2)
                .build();

        //Image Menu
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);

                Picasso.with(imageView.getContext()).load("http://meddata.sytes.net/newuser/profileImg/"
                        + sp.getString("imagen", "No Image"))
                        .placeholder(placeholder).into(imageView);
            }
            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_main).withIcon(GoogleMaterial.Icon.gmd_home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profile).withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_records).withIcon(GoogleMaterial.Icon.gmd_assignment);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_statistics).withIcon(GoogleMaterial.Icon.gmd_insert_chart);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_item_healt).withIcon(GoogleMaterial.Icon.gmd_local_hospital);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.drawer_item_settinds).withIcon(GoogleMaterial.Icon.gmd_settings);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName(R.string.drawer_item_about).withIcon(GoogleMaterial.Icon.gmd_more);

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
                        if (position == 1) {
                            Intent intent = new Intent(configuracion.this, MainScreen.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 2) {
                            Intent intent = new Intent(configuracion.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 3) {
                            Intent intent = new Intent(configuracion.this, Registros.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 4){
                            Intent intent = new Intent(configuracion.this, Statistics.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 5){
                            Intent intent = new Intent(configuracion.this, MenuHinfo.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 8){
                            Intent intent = new Intent(configuracion.this, AboutUs.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }



                        return false;
                    }
                })
                .build();
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
