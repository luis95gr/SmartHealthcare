package com.example.luisguzmn.healthcare40;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.Attributes;

import static android.text.Html.fromHtml;

public class Profile extends AppCompatActivity {

    //VARIABLES
    Button button_horas1;
    Button button_horas2;
    Button button_horas4;
    Button button_nv7;
    Button button_nv6;
    Button button_nv5;
    Button button_nv4;
    Button button_nv3;
    Button button_nv2;
    Button button_nv1;
    String horas;
    int ejercicioInt;
    int ejercicioDias;
    TextView text_monday;
    TextView text_tuesday;
    TextView text_wednesday;
    TextView text_thursday;
    TextView text_friday;
    TextView text_saturday;
    TextView text_sunday;
    TextView text_name;
    TextView text_country;
    TextView text_gender;
    TextView text_birthday;
    TextView text_height;
    TextView text_weight;
    LottieAnimationView bici;
    ImageButton imB_acos;
    ImageButton imB_sentado;
    ImageButton imB_parado;
    ImageButton imB_caminando;
    ImageButton imB_corriendo;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

        //CAST
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
        text_name = (TextView) findViewById(R.id.text_name);
        text_country = (TextView) findViewById(R.id.text_country);
        text_gender = (TextView) findViewById(R.id.text_gender);
        text_birthday = (TextView) findViewById(R.id.text_birthday);
        text_height = (TextView) findViewById(R.id.text_height);
        text_weight = (TextView) findViewById(R.id.text_weight);
        ImageView image_profile = (ImageView) findViewById(R.id.image_profile);
        bici = (LottieAnimationView) findViewById(R.id.bicicleta);
        imB_acos = (ImageButton) findViewById(R.id.imageButtonAcos);
        imB_sentado = (ImageButton) findViewById(R.id.imageButtonSen);
        imB_parado = (ImageButton) findViewById(R.id.imageButtonPara);
        imB_caminando = (ImageButton) findViewById(R.id.imageButtonCami);
        imB_corriendo = (ImageButton) findViewById(R.id.imageButtonCorri);
        button_nv1 = (Button) findViewById(R.id.nivel1);
        button_nv2 = (Button) findViewById(R.id.nivel2);
        button_nv3 = (Button) findViewById(R.id.nivel3);
        button_nv4 = (Button) findViewById(R.id.nivel4);
        button_nv5 = (Button) findViewById(R.id.nivel5);
        button_nv6 = (Button) findViewById(R.id.nivel6);
        button_nv7 = (Button) findViewById(R.id.nivel7);
        button_horas1 = (Button) findViewById(R.id.button_horas1);
        button_horas2 = (Button) findViewById(R.id.button_horas2_3);
        button_horas4 = (Button) findViewById(R.id.button_horas4);
        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarMain);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
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
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_main).withIcon(GoogleMaterial.Icon.gmd_accessibility);
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
                            Intent intent = new Intent(Profile.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 1) {
                            Intent intent = new Intent(Profile.this, MainScreen.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }

                        return false;
                    }
                })
                .build();

        //-------------------------------------------------------------------------------------------
        //MENU

        //PROFILE SCREEN
        Picasso.with(this).load("http://meddata.sytes.net/newuser/profileImg/" + sp.getString("imagen","No image"))
                .resize(150, 150).centerCrop().into(image_profile);
        bici.setSpeed(4f);
        bici.playAnimation();
        text_name.setText(Html.fromHtml(getApplicationContext().getResources().getText(R.string.name) + sp.getString("name","Name") + "<br>"));
        text_country.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.country) + sp.getString("country","country") + "<br><br>"));
        text_gender.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.gender) + sp.getString("gender","Gender") + "<br><br>"));
        text_birthday.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.birthday) + sp.getString("birth","Birth") + "<br><br>"));
        text_height.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.height) + sp.getInt("height",0) + " cm" + "<br><br>"));
        text_weight.setText(Html.fromHtml(getApplicationContext().getResources().getString(R.string.weight) + sp.getInt("weight",0) + " kg" + "<br><br>"));

        //DATOS PERSONALES///////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        text_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Hola", Toast.LENGTH_SHORT).show();
            }
        });

        //INTENSIDAD DE EJERCICIO ////////////////////////////////////////////////////////////////////////////////////////
        //CAMBIO DE COLOR
        ejercicioInt = sp.getInt("exInt",0);
        if (ejercicioInt == 1) {
            imB_acos.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
        } else {
            imB_acos.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
        }

        if (ejercicioInt == 2) {
            imB_sentado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
        } else {
            imB_sentado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
        }

        if (ejercicioInt == 3) {
            imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
        } else {
            imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
        }

        if (ejercicioInt == 4) {
            imB_caminando.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
        } else {
            imB_caminando.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
        }

        if (ejercicioInt == 5) {
            imB_corriendo.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
        } else {
            imB_corriendo.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
        }
        //TOCAR BOTONES /////////////////////////////////////////////
        imB_acos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioInt = 1;
                imB_acos.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
                imB_sentado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_caminando.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_corriendo.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
            }
        });

        imB_sentado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioInt = 2;
                imB_sentado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
                imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_caminando.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_corriendo.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
            }
        });

        imB_parado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioInt = 3;
                imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
                imB_sentado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_acos.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_caminando.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_corriendo.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
            }
        });

        imB_caminando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioInt = 4;
                imB_caminando.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
                imB_sentado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_acos.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_corriendo.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
            }
        });

        imB_corriendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioInt = 5;
                imB_corriendo.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen_seleccionado));
                imB_sentado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_acos.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_caminando.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
                imB_parado.setBackground(getApplicationContext().getDrawable(R.drawable.border_imagen));
            }
        });
        //INTENSIDAD DE EJERCICIO ////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //DIAS A LA SEMANA////////////////////////////////////////////////////////////////////////////////////////////////
        //CAMBIAR COLOR///////////////////////////////////////////////////////////////////////////////////////////////////
        ejercicioDias = sp.getInt("daysEx",0);

        if (ejercicioDias == 1) {
            button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv2.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv3.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv4.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (ejercicioDias == 2) {
            button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv3.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv4.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (ejercicioDias == 3) {
            button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv4.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (ejercicioDias == 4) {
            button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (ejercicioDias == 5) {
            button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv5.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (ejercicioDias == 6) {
            button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv5.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv6.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (ejercicioDias == 7) {
            button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv5.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv6.setBackgroundColor(Color.parseColor("#46B4CC"));
            button_nv7.setBackgroundColor(Color.parseColor("#46B4CC"));
        }

        ////TOCAR BOTONES ////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        button_nv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioDias = 1;
                button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        button_nv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioDias = 2;
                button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        button_nv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioDias = 3;
                button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        button_nv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioDias = 4;
                button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv5.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        button_nv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioDias = 5;
                button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv5.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv6.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        button_nv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioDias = 6;
                button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv5.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv6.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv7.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        button_nv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejercicioDias = 7;
                button_nv1.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv2.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv3.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv4.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv5.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv6.setBackgroundColor(Color.parseColor("#46B4CC"));
                button_nv7.setBackgroundColor(Color.parseColor("#46B4CC"));

            }
        });
        ////END TOCAR BOTONES ////////////////////////////////////////////////////////////////////////////////////////////////

        //HORAS AL DIA////////////////////////////////////////////////////////////////////////////////////////////////
        //CAMBIAR COLOR///////////////////////////////////////////////////////////////////////////////////////////////////
        horas = sp.getString("hoursEx","0");

        if (horas.equalsIgnoreCase("1 or less")) {
            button_horas1.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2));
            button_horas2.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
            button_horas4.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
        }
        if (horas.equalsIgnoreCase("2-3 or less")) {
            button_horas2.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2));
            button_horas1.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
            button_horas4.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
        }
        if (horas.equalsIgnoreCase("4 or more")) {
            button_horas4.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2));
            button_horas1.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
            button_horas2.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
        }
        //END CAMBIAR COLOR///////////////////////////////////////////////////////////////////////////////////////////////////
        //TOCAR BOTONES //////////////////////////////////////////////////////////////////////////////////////////////////
        button_horas1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_horas1.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2));
                button_horas2.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
                button_horas4.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
                horas = "1 or less";
            }
        });
        button_horas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_horas2.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2));
                button_horas1.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
                button_horas4.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
                horas = "2-3 or less";
            }
        });
        button_horas4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_horas4.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2));
                button_horas1.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
                button_horas2.setBackground(getApplicationContext().getDrawable(R.drawable.round_border2_deseleccionado));
                horas = "4 or more";
            }
        });


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

        //END DIAS
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
                    startActivity(new Intent(Profile.this,MainScreen.class));
                    return true;

                case R.id.logout:
                    SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.clear();
                    e.apply();
                    startActivity(new Intent(Profile.this,MainActivity.class));
                    finish();

                    default:
                        return super.onOptionsItemSelected(item);
            }


        }
        //END MENU 3 DOTS

}
