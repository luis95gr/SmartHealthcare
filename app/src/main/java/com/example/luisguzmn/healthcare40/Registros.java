package com.example.luisguzmn.healthcare40;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

//
public class Registros extends AppCompatActivity {
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;
    Button txtBR,txtHR,txtBP,txtECG,txtFatiga,txtMood,txtBRSend,btnHRS,btnBPS,btnECGS,btnFS,btnMS;
    JSONArray jsonArray;
    private String a;
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> mylistDates = new ArrayList<String>();
    ArrayList<String> mylistTime = new ArrayList<String>();
    ArrayList<String> mylistVar = new ArrayList<String>();
    ArrayList<String> share = new ArrayList<String>();

    String email;

    float[] floatValues;
    String var,date,time;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);

        email = sp.getString("email", "no email");

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registros");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);


        txtBRSend = (Button)findViewById(R.id.txtBRSend);
        txtBR = (Button) findViewById(R.id.txtBR);
        txtHR = (Button)findViewById(R.id.txtHR);
        txtBP = (Button)findViewById(R.id.txtBP);
        txtECG = (Button)findViewById(R.id.txtECG);
        txtFatiga = (Button)findViewById(R.id.txtFatiga);
        txtMood = (Button)findViewById(R.id.txtMood);
        btnHRS = (Button)findViewById(R.id.txtSHR);
        btnBPS = (Button)findViewById(R.id.btnBPS);
        btnECGS = (Button)findViewById(R.id.btnECGS);
        btnFS = (Button)findViewById(R.id.txtFS);
        btnMS = (Button)findViewById(R.id.txtSMood);


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
                            Intent intent = new Intent(Registros.this, MainScreen.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 2) {
                            Intent intent = new Intent(Registros.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 3) {
                            Intent intent = new Intent(Registros.this, Registros.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 4){
                            Intent intent = new Intent(Registros.this, Statistics.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }

                        return false;
                    }
                })
                .build();

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

txtBR.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String variable="BR";
        Intent intent = new Intent(getApplicationContext(),RegistrosEmty.class);
        intent.putExtra("key",variable);
        startActivity(intent);
    }
});
        txtHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variable="HR";
                Intent intent = new Intent(getApplicationContext(),RegistrosEmty.class);
                intent.putExtra("key",variable);
                startActivity(intent);
            }
        });

        txtECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variable="ecg";
                Intent intent = new Intent(getApplicationContext(),RegistrosEmty.class);
                intent.putExtra("key",variable);
                startActivity(intent);
            }
        });

        txtBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variable="BP";
                Intent intent = new Intent(getApplicationContext(),RegistrosEmty.class);
                intent.putExtra("key",variable);
                startActivity(intent);
            }
        });

        txtFatiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variable="fatigue";
                Intent intent = new Intent(getApplicationContext(),RegistrosEmty.class);
                intent.putExtra("key",variable);
                startActivity(intent);
            }
        });

        txtMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variable="Mood";
                Intent intent = new Intent(getApplicationContext(),RegistrosEmty.class);
                intent.putExtra("key",variable);
                startActivity(intent);
            }
        });

        txtBRSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                share = new ArrayList<String>();
                String variable = "BR";
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+variable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
                        shareContent.setType("text/plain");
                        shareContent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        //
                        //CLIPBOARD
                        String text="Datos Usuario: "+ email+" | SmartHealth App";
                        for(int i=0;i<share.size();i++){
                            text = text +share.get(i);

                        }

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData myclip = ClipData.newPlainText("info",text);
                        clipboard.setPrimaryClip(myclip);
                        Toast.makeText(Registros.this,"Información guardada en portapapeles",
                                Toast.LENGTH_LONG).show();
                        //
                        shareContent.putExtra(Intent.EXTRA_SUBJECT,  "http://meddata.sytes.net/");
                        shareContent.putExtra(Intent.EXTRA_TEXT, text);

                        startActivity(Intent.createChooser(shareContent, "Share"));

                    }
                }, 1000);





            }
        });

        btnHRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                share = new ArrayList<String>();
                String variable = "HR";
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+variable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
                        shareContent.setType("text/plain");
                        shareContent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        //
                        //CLIPBOARD
                        String text="Datos Usuario: "+ email+" | SmartHealth App";
                        for(int i=0;i<share.size();i++){
                            text = text +share.get(i);

                        }

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData myclip = ClipData.newPlainText("info",text);
                        clipboard.setPrimaryClip(myclip);
                        Toast.makeText(Registros.this,"Información guardada en portapapeles",
                                Toast.LENGTH_LONG).show();
                        //
                        shareContent.putExtra(Intent.EXTRA_SUBJECT,  "http://meddata.sytes.net/");
                        shareContent.putExtra(Intent.EXTRA_TEXT, text);

                        startActivity(Intent.createChooser(shareContent, "Share"));

                    }
                }, 1000);





            }
        });

        btnBPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                share = new ArrayList<String>();
                String variable = "BP";
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+variable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
                        shareContent.setType("text/plain");
                        shareContent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        //
                        //CLIPBOARD
                        String text="Datos Usuario: "+ email+" | SmartHealth App";
                        for(int i=0;i<share.size();i++){
                            text = text +share.get(i);

                        }

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData myclip = ClipData.newPlainText("info",text);
                        clipboard.setPrimaryClip(myclip);
                        Toast.makeText(Registros.this,"Información guardada en portapapeles",
                                Toast.LENGTH_LONG).show();
                        //
                        shareContent.putExtra(Intent.EXTRA_SUBJECT,  "http://meddata.sytes.net/");
                        shareContent.putExtra(Intent.EXTRA_TEXT, text);

                        startActivity(Intent.createChooser(shareContent, "Share"));

                    }
                }, 1000);





            }
        });

        btnECGS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                share = new ArrayList<String>();
                String variable = "ECG";
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+variable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
                        shareContent.setType("text/plain");
                        shareContent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        //
                        //CLIPBOARD
                        String text="Datos Usuario: "+ email+" | SmartHealth App";
                        for(int i=0;i<share.size();i++){
                            text = text +share.get(i);

                        }

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData myclip = ClipData.newPlainText("info",text);
                        clipboard.setPrimaryClip(myclip);
                        Toast.makeText(Registros.this,"Información guardada en portapapeles",
                                Toast.LENGTH_LONG).show();
                        //
                        shareContent.putExtra(Intent.EXTRA_SUBJECT,  "http://meddata.sytes.net/");
                        shareContent.putExtra(Intent.EXTRA_TEXT, text);

                        startActivity(Intent.createChooser(shareContent, "Share"));

                    }
                }, 1000);





            }
        });

        btnFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                share = new ArrayList<String>();
                String variable = "fatigue";
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+variable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
                        shareContent.setType("text/plain");
                        shareContent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        //
                        //CLIPBOARD
                        String text="Datos Usuario: "+ email+" | SmartHealth App";
                        for(int i=0;i<share.size();i++){
                            text = text +share.get(i);

                        }


                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData myclip = ClipData.newPlainText("info",text);
                        clipboard.setPrimaryClip(myclip);
                        Toast.makeText(Registros.this,"Información guardada en portapapeles",
                                Toast.LENGTH_LONG).show();
                        //
                        shareContent.putExtra(Intent.EXTRA_SUBJECT,  "http://meddata.sytes.net/");
                        shareContent.putExtra(Intent.EXTRA_TEXT, text);

                        startActivity(Intent.createChooser(shareContent, "Share"));

                    }
                }, 1000);





            }
        });

        btnMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                share = new ArrayList<String>();
                String variable = "mood";
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+variable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
                        shareContent.setType("text/plain");
                        shareContent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        //
                        //CLIPBOARD
                        String text="Usuario: "+ email+" | SmartHealth App";
                        for(int i=0;i<share.size();i++){
                            text = text +share.get(i);

                        }

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData myclip = ClipData.newPlainText("info",text);
                        clipboard.setPrimaryClip(myclip);
                        Toast.makeText(Registros.this,"Información guardada en portapapeles",
                                Toast.LENGTH_LONG).show();
                        //
                        shareContent.putExtra(Intent.EXTRA_SUBJECT,  "http://meddata.sytes.net/");
                        shareContent.putExtra(Intent.EXTRA_TEXT, text);

                        startActivity(Intent.createChooser(shareContent, "Share"));

                    }
                }, 1000);





            }
        });

    }
    public void VolleyPetition(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value, date;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        a = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(a);
                        value = jsonObj.getString("value");
                        date = jsonObj.getString("date");
                        time = jsonObj.getString("time");
                        var = jsonObj.getString("var");
                        mylist.add(value);
                        mylistDates.add(date);
                        mylistTime.add(time);
                        mylistVar.add(var);
                    }
                    if(var.equals("null")|var.equals("Mood") | var.equals("ecg") | var.equals("Fatigue")) {

                    }else {
                        floatValues = new float[mylist.size()];
                        for (int i = 0; i < mylist.size(); i++) {
                            floatValues[i] = Float.parseFloat(mylist.get(i));
                        }
                    }
                    for(int i = 0; i<mylist.size();i++) {
                        share.add("\n--------------"+"\nVariable: "+mylistVar.get(i).toString()+"\nValor: "+mylist.get(i).toString()+"\nFecha: "+
                        mylistDates.get(i).toString()+"\nHora: "+mylistTime.get(i).toString()+"\n--------------");

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        queue.add(stringRequest);
    }


}