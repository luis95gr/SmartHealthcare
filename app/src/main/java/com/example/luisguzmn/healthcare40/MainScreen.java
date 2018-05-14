package com.example.luisguzmn.healthcare40;

import android.Manifest;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.graphics.Color;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InvalidObjectException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


public class MainScreen extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //VARIABLES
    BluetoothAdapter bluetoothAdapter;
    ProgressBar progressbar;
    ProgressBarAnimation progressBarAnimation;
    TextView textNivel,textPercentage;
    //AGREGAR ESTAS VARIABALES
    int headphones = 0;
    int smartwatch = 0;
    int tipoDisp = 0;
    File image;
    //
    private ImageView imageView;
    ImageButton imageButtonSS,imageButtonSH,imageButtonSW;
    Button buttonStart;
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;
    private static final int REQUEST_ENABLED = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE = 0;
    //
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    private File file = new File(ruta_fotos);
    String path;
    File fileImagen;
    Bitmap imageBitmap;
    //
    //UPLOAD IMAGES
    //Image request code
    private int PICK_IMAGE_REQUEST = 0;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;
    String mCurrentPhotoPath;
    //
    //STATISTICS
    TextView textMeasures,textFaltan;
    DecimalFormat decimalFormat = new DecimalFormat("##");
    JSONArray jsonArray;
    float[] floatValuesBr,floatValuesBpmax,floatValuesBpmin,floatValuesHr,floatSteps;
    ArrayList<String> brList,bpmaxList,bpminList,hrList,moodList,fatigueList,ecgList,stepsList;
    String json;
    SharedPreferences spDataNivel;
    float maxBr,maxBpmax,maxBpmin,maxHr,maxSteps;
    float minBr,minBpmax,minBpmin,minHr,minSteps;
    float avgBr,avgBpmax,avgBpmin,avgHr,avgSteps;
    float lastBr,lastBpmax,lastBpmin,lastHr,lastSteps;
    float totalMeasures;
    int nivel,top,bottom;
    int fromPercentage,toPercentage;
    Animation animationBlink;
    ImageView iconArrowRight,iconArrowLeft;
    TextView textBpmaxAvg,textBpminAvg,textBrAvg,textHrAvg,textStepsAvg;
    TextView textBpmaxLast,textBpminLast,textBrLast,textHrLast,textStepsLast;
    CardView cardView;
    Handler handler,handler2;
    SharedPreferences sp;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        menu();
        dias();
        requestStoragePermission();
        //
        sp = getSharedPreferences("login", MODE_PRIVATE);
        spDataNivel = PreferenceManager.getDefaultSharedPreferences(this);
        //
        //CAST
        imageView = (ImageView) findViewById(R.id.image_profile);
        TextView textNameMain = (TextView) findViewById(R.id.textNameMain);
        imageButtonSW = (ImageButton) findViewById(R.id.imageButtonSW);
        imageButtonSH = (ImageButton) findViewById(R.id.imageButtonSH);
        imageButtonSS = (ImageButton) findViewById(R.id.imageButtonSS);
        buttonStart = (Button)findViewById(R.id.buttonStart);
        progressbar = (ProgressBar)findViewById(R.id.progressBar);
        textNivel = (TextView)findViewById(R.id.text_nivel);
        textPercentage = (TextView)findViewById(R.id.text_percentage);
        brList = new ArrayList<String>();
        bpmaxList = new ArrayList<String>();
        bpminList = new ArrayList<String>();
        hrList = new ArrayList<String>();
        moodList = new ArrayList<String>();
        fatigueList = new ArrayList<String>();
        ecgList = new ArrayList<String>();
        stepsList = new ArrayList<String>();
        textMeasures = (TextView)findViewById(R.id.text_measures);
        textFaltan = (TextView)findViewById(R.id.text_measures_faltan);
        iconArrowRight = (ImageView)findViewById(R.id.iconArrowRight);
        iconArrowLeft = (ImageView)findViewById(R.id.iconArrowLeft);
        animationBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        iconArrowRight.startAnimation(animationBlink);
        //
        textBpmaxAvg = (TextView)findViewById(R.id.textBpmaxAvg);
        textBpminAvg = (TextView)findViewById(R.id.textBpminAvg);
        textBrAvg = (TextView)findViewById(R.id.textBrAvg);
        textHrAvg = (TextView)findViewById(R.id.textHrAvg);
        textStepsAvg = (TextView)findViewById(R.id.textStepsAvg);
        textBpmaxLast = (TextView)findViewById(R.id.textBpmaxLast);
        textBpminLast = (TextView)findViewById(R.id.textBpminLast);
        textBrLast = (TextView)findViewById(R.id.textBrLast);
        textHrLast = (TextView)findViewById(R.id.textHrLast);
        textStepsLast = (TextView)findViewById(R.id.textStepsLast);
        cardView = (CardView)findViewById(R.id.cardView);
        handler = new Handler();
        handler2 = new Handler();
        //
        //MAIN SCREEN
        Picasso.with(this).load("http://meddata.sytes.net/newuser/profileImg/" + sp.getString("imagen", "No Image"))
                .resize(250, 250).centerCrop().into(imageView);
        textNameMain.setText(sp.getString("name", "No name"));
        //
        if (internet()) {
            VolleyPetitionBr("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "BR");
            VolleyPetitionBpmax("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "BPmax");
            VolleyPetitionBpmin("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "BPmin");
            VolleyPetitionHr("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "HR");
            VolleyPetitionMood("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "Mood");
            VolleyPetitionFatigue("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "Fatigue");
            VolleyPetitionEcg("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "ecg");
            VolleyPetitionSteps("http://meddata.sytes.net/grafico/dataShow.php?email=" + sp.getString("email", "no") + "&var=" + "Steps");
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    totalMeasures = spDataNivel.getFloat("sizeBr",0) + spDataNivel.getFloat("sizeBpmax",0) +
                            spDataNivel.getFloat("sizeBpmin",0) + spDataNivel.getFloat("sizeHr",0) + spDataNivel.getFloat("sizeMood",0) +
                            spDataNivel.getFloat("sizeFatigue",0) + spDataNivel.getFloat("sizeEcg",0) +
                            spDataNivel.getFloat("sizeSteps",0);

                    //CALCULATE LEVEL
                    if (totalMeasures < 10){
                        nivel = 1;
                    }
                    if (totalMeasures < 20 && totalMeasures >= 10) {
                        nivel = 2;
                    }
                    if (totalMeasures < 30 && totalMeasures >= 20){
                        nivel = 3;
                    }
                    if (totalMeasures < 40 && totalMeasures >= 30){
                        nivel = 4;
                    }
                    if (totalMeasures < 50 && totalMeasures >= 40){
                        nivel = 5;
                    }
                    if (totalMeasures < 60 && totalMeasures >= 50){
                        nivel = 6;
                    }
                    if (totalMeasures < 70 && totalMeasures >= 60){
                        nivel = 7;
                    }
                    if (totalMeasures < 80 && totalMeasures >= 70){
                        nivel = 8;
                    }
                    if (totalMeasures < 90 && totalMeasures >= 80){
                        nivel = 9;
                    }
                    if (totalMeasures < 100 && totalMeasures >= 90){
                        nivel = 10;
                    }
                    if (totalMeasures < 110 && totalMeasures >= 100){
                        nivel = 11;
                    }
                    if (totalMeasures < 120 && totalMeasures >= 110){
                        nivel = 12;
                    }
                    if (totalMeasures < 130 && totalMeasures >= 120){
                        nivel = 13;
                    }
                    if (totalMeasures < 140 && totalMeasures >= 130){
                        nivel = 14;
                    }
                    if (totalMeasures < 150 && totalMeasures >= 140){
                        nivel = 15;
                    }
                    //
                    textNivel.setText("Nivel " + nivel);
                    textMeasures.setText("Llevas: " + decimalFormat.format(totalMeasures));
                    textFaltan.setText("Te faltan: " + decimalFormat.format(nivel*10-totalMeasures));
                    //CALCULATE PERCENTAGE
                    toPercentage = Integer.parseInt(decimalFormat.format((totalMeasures-(nivel-1)*10)*10));
                    //ANIMATIONS
                    progressBarAnimation = new ProgressBarAnimation(progressbar,0,toPercentage);
                    progressBarAnimation.setDuration(1500);
                    progressbar.startAnimation(progressBarAnimation);
                    startCountAnimation(0,toPercentage);


            }
        }, 1000);
        //RECOVER SIGNALS FOR LEVEL AND STATISTICS




        //
        //CARDVIEW INFO
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (spDataNivel.getFloat("avgBpmax",0) != 0) {
                    textBpmaxAvg.setText(decimalFormat.format(spDataNivel.getFloat("avgBpmax", 0)));
                }else textBpmaxAvg.setText("No hay datos");
                if (spDataNivel.getFloat("avgBpmin",0) != 0) {
                    textBpminAvg.setText(decimalFormat.format(spDataNivel.getFloat("avgBpmin", 0)));
                } else textBpminAvg.setText("No hay datos");
                if (spDataNivel.getFloat("avgBr",0) != 0) {
                    textBrAvg.setText(decimalFormat.format(spDataNivel.getFloat("avgBr", 0)));
                } else textBrAvg.setText("No hay datos");
                if (spDataNivel.getFloat("avgHr",0) != 0) {
                    textHrAvg.setText(decimalFormat.format(spDataNivel.getFloat("avgHr", 0)));
                } else textHrAvg.setText("No hay datos");
                if (spDataNivel.getFloat("avgSteps",0) != 0) {
                    textStepsAvg.setText(decimalFormat.format(spDataNivel.getFloat("avgSteps", 0)));
                } else textStepsAvg.setText("No hay datos");
                //
                if (spDataNivel.getFloat("lastBpmax",0) != 0) {
                    textBpmaxLast.setText(decimalFormat.format(spDataNivel.getFloat("lastBpmax", 0)));
                } else textBpmaxLast.setText("No hay datos");
                if (spDataNivel.getFloat("lastBpmin",0) != 0) {
                    textBpminLast.setText(decimalFormat.format(spDataNivel.getFloat("lastBpmin", 0)));
                } else textBpminLast.setText("No hay datos");
                if (spDataNivel.getFloat("lastBr",0) != 0) {
                    textBrLast.setText(decimalFormat.format(spDataNivel.getFloat("lastBr", 0)));
                } else textBrLast.setText("No hay datos");
                if (spDataNivel.getFloat("lastHr",0) != 0) {
                    textHrLast.setText(decimalFormat.format(spDataNivel.getFloat("lastHr", 0)));
                } else textHrLast.setText("No hay datos");
                if (spDataNivel.getFloat("lastSteps",0) != 0) {
                    textStepsLast.setText(decimalFormat.format(spDataNivel.getFloat("lastSteps", 0)));
                } else textStepsLast.setText("No hay datos");
            }
        },1000);



        //INFO TRASLATION
        iconArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.INVISIBLE);
                textPercentage.setVisibility(View.INVISIBLE);
                textNivel.setVisibility(View.INVISIBLE);
                textMeasures.setVisibility(View.INVISIBLE);
                textFaltan.setVisibility(View.INVISIBLE);
                iconArrowRight.setVisibility(View.INVISIBLE);
                iconArrowRight.clearAnimation();
                iconArrowLeft.setVisibility(View.VISIBLE);
                iconArrowLeft.startAnimation(animationBlink);
                cardView.setVisibility(View.VISIBLE);
            }
        });
        iconArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                textPercentage.setVisibility(View.VISIBLE);
                textNivel.setVisibility(View.VISIBLE);
                textMeasures.setVisibility(View.VISIBLE);
                textFaltan.setVisibility(View.VISIBLE);
                iconArrowRight.setVisibility(View.VISIBLE);
                iconArrowLeft.setVisibility(View.INVISIBLE);
                iconArrowLeft.clearAnimation();
                iconArrowRight.startAnimation(animationBlink);
                cardView.setVisibility(View.INVISIBLE);
            }
        });
        //





        //////////////////////////////////////////////////////////////////////////////////////////////////
        //BOTONES
        imageButtonSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainScreen.this, "Tienes relojes emparejados", Toast.LENGTH_SHORT).show();
            }
        });
        imageButtonSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "Tienes audífonos emparejados", Toast.LENGTH_SHORT).show();

            }
        });
        imageButtonSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this, "Sensores emparejados", Toast.LENGTH_SHORT).show();
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

        // END BLUETOOTH
        //set image profile
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }
    ////////////////VOLLEYS///////////////////////////
    /////BR
    public void VolleyPetitionBr(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        brList.add(value);
                    }if(brList.get(0).equals("null")){}
                    else {
                        floatValuesBr = new float[brList.size()];
                        for (int i = 0; i < brList.size(); i++) {
                            floatValuesBr[i] = Float.parseFloat(brList.get(i));
                        }
                        if (floatValuesBr.length != 0) {
                            lastBr = floatValuesBr[floatValuesBr.length - 1];
                            //MAX VALUE
                            maxBr = floatValuesBr[0];
                            for (int i = 1; i < floatValuesBr.length; i++) {
                                if (floatValuesBr[i] > maxBr) {
                                    maxBr = floatValuesBr[i];
                                }
                            }
                            //MIN VALUE
                            minBr = floatValuesBr[0];
                            for (int i = 1; i < floatValuesBr.length; i++) {
                                if (floatValuesBr[i] < minBr) {
                                    minBr = floatValuesBr[i];
                                }
                            }
                            //AVG VALUE
                            avgBr = floatValuesBr[0];
                            for (int i = 1; i < floatValuesBr.length; i++) {
                                avgBr = avgBr + floatValuesBr[i];
                            }
                            avgBr = avgBr / floatValuesBr.length;
                            //
                            spDataNivelEditor.putFloat("minBr", minBr);
                            spDataNivelEditor.putFloat("maxBr", maxBr);
                            spDataNivelEditor.putFloat("avgBr", avgBr);
                            spDataNivelEditor.putFloat("sizeBr", floatValuesBr.length);
                            spDataNivelEditor.putFloat("lastBr", lastBr);
                            spDataNivelEditor.apply();
                        }
                    }
                    //Toast.makeText(MainScreen.this, ""+decimalFormat.format(avgBr), Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        queue.add(stringRequest);
    }

    ///////////BPMAX
    public void VolleyPetitionBpmax(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        bpmaxList.add(value);
                    }if(bpmaxList.get(0).equals("null")){}
                    else {
                        floatValuesBpmax = new float[bpmaxList.size()];
                        for (int i = 0; i < bpmaxList.size(); i++) {
                            floatValuesBpmax[i] = Float.parseFloat(bpmaxList.get(i));
                        }
                        //MAX VALUE
                        if (floatValuesBpmax.length != 0) {
                            lastBpmax = floatValuesBpmax[floatValuesBpmax.length - 1];
                            //MAX VALUE
                            maxBpmax = floatValuesBpmax[0];
                            for (int i = 1; i < floatValuesBpmax.length; i++) {
                                if (floatValuesBpmax[i] > maxBpmax) {
                                    maxBpmax = floatValuesBpmax[i];
                                }
                            }
                            //MIN VALUE
                            minBpmax = floatValuesBpmax[0];
                            for (int i = 1; i < floatValuesBpmax.length; i++) {
                                if (floatValuesBpmax[i] < minBpmax) {
                                    minBpmax = floatValuesBpmax[i];
                                }
                            }
                            //AVG VALUE
                            avgBpmax = floatValuesBpmax[0];
                            for (int i = 1; i < floatValuesBpmax.length; i++) {
                                avgBpmax = avgBpmax + floatValuesBpmax[i];
                            }
                            avgBpmax = avgBpmax / floatValuesBpmax.length;
                            //
                            spDataNivelEditor.putFloat("minBpmax", minBpmax);
                            spDataNivelEditor.putFloat("maxBpmax", maxBpmax);
                            spDataNivelEditor.putFloat("avgBpmax", avgBpmax);
                            spDataNivelEditor.putFloat("sizeBpmax", floatValuesBpmax.length);
                            spDataNivelEditor.putFloat("lastBpmax", lastBpmax);
                            spDataNivelEditor.apply();
                        }
                    }
                    //Toast.makeText(MainScreen.this, ""+floatValuesBpmax.length, Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        queue.add(stringRequest);
    }

    ///////BPMIN
    public void VolleyPetitionBpmin(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        bpminList.add(value);
                    }if(bpminList.get(0).equals("null")){}
                    else {
                        floatValuesBpmin = new float[bpminList.size()];
                        for (int i = 0; i < bpminList.size(); i++) {
                            floatValuesBpmin[i] = Float.parseFloat(bpminList.get(i));
                        }
                        if (floatValuesBpmin.length != 0) {
                            lastBpmin = floatValuesBpmin[floatValuesBpmin.length - 1];
                            //MAX VALUE
                            maxBpmin = floatValuesBpmin[0];
                            for (int i = 1; i < floatValuesBpmin.length; i++) {
                                if (floatValuesBpmin[i] > maxBpmin) {
                                    maxBpmin = floatValuesBpmin[i];
                                }
                            }
                            //MIN VALUE
                            minBpmin = floatValuesBpmin[0];
                            for (int i = 1; i < floatValuesBpmin.length; i++) {
                                if (floatValuesBpmin[i] < minBpmin) {
                                    minBpmin = floatValuesBpmin[i];
                                }
                            }
                            //AVG VALUE
                            avgBpmin = floatValuesBpmin[0];
                            for (int i = 1; i < floatValuesBpmin.length; i++) {
                                avgBpmin = avgBpmin + floatValuesBpmin[i];
                            }
                            avgBpmin = avgBpmin / floatValuesBpmin.length;
                            //
                            spDataNivelEditor.putFloat("minBpmin", minBpmin);
                            spDataNivelEditor.putFloat("maxBpmin", maxBpmin);
                            spDataNivelEditor.putFloat("avgBpmin", avgBpmin);
                            spDataNivelEditor.putFloat("sizeBpmin", floatValuesBpmin.length);
                            spDataNivelEditor.putFloat("lastBpmin", lastBpmin);
                            spDataNivelEditor.apply();
                        }
                    }
                    //Toast.makeText(MainScreen.this, ""+floatValuesBpmin.length, Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        queue.add(stringRequest);
    }
    ////////HR
    public void VolleyPetitionHr(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        hrList.add(value);
                    }if(hrList.get(0).equals("null")){}
                    else {
                        floatValuesHr = new float[hrList.size()];
                        for (int i = 0; i < hrList.size(); i++) {
                            floatValuesHr[i] = Float.parseFloat(hrList.get(i));
                        }
                        if (floatValuesHr.length != 0) {
                            lastHr = floatValuesHr[floatValuesHr.length - 1];
                            //MAX VALUE
                            maxHr = floatValuesHr[0];
                            for (int i = 1; i < floatValuesHr.length; i++) {
                                if (floatValuesHr[i] > maxHr) {
                                    maxHr = floatValuesHr[i];
                                }
                            }
                            //MIN VALUE
                            minHr = floatValuesHr[0];
                            for (int i = 1; i < floatValuesHr.length; i++) {
                                if (floatValuesHr[i] < minHr) {
                                    minHr = floatValuesHr[i];
                                }
                            }
                            //AVG VALUE
                            avgHr = floatValuesHr[0];
                            for (int i = 1; i < floatValuesHr.length; i++) {
                                avgHr = avgHr + floatValuesHr[i];
                            }
                            avgHr = avgHr / floatValuesHr.length;
                            //
                            spDataNivelEditor.putFloat("minHr", minHr);
                            spDataNivelEditor.putFloat("maxHr", maxHr);
                            spDataNivelEditor.putFloat("avgHr", avgHr);
                            spDataNivelEditor.putFloat("sizeHr", floatValuesHr.length);
                            spDataNivelEditor.putFloat("lastHr", lastHr);
                            spDataNivelEditor.apply();
                        }
                    }
                    //Toast.makeText(MainScreen.this, ""+floatValuesHr[0], Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        queue.add(stringRequest);
    }
    ////////MOOD
    public void VolleyPetitionMood(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        moodList.add(value);
                    }
                    if (!moodList.isEmpty()) {
                        spDataNivelEditor.putFloat("sizeMood", moodList.size());
                        spDataNivelEditor.apply();
                    }
                    //Toast.makeText(MainScreen.this, ""+moodList.size(), Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        queue.add(stringRequest);
    }
    //////////////FATIGUE
    public void VolleyPetitionFatigue(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        fatigueList.add(value);
                    }
                    if (!fatigueList.isEmpty()) {
                        spDataNivelEditor.putFloat("sizeFatigue", fatigueList.size());
                        spDataNivelEditor.apply();
                    }
                    //Toast.makeText(MainScreen.this, ""+fatigueList.size(), Toast.LENGTH_LONG).show();


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
    /////////ECG
    public void VolleyPetitionEcg(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        ecgList.add(value);
                    }
                    if (!ecgList.isEmpty()) {
                        spDataNivelEditor.putFloat("sizeEcg", ecgList.size());
                        spDataNivelEditor.apply();
                    }
                    //Toast.makeText(MainScreen.this, ""+ecgList.size(), Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        queue.add(stringRequest);
    }
    ////////STEPS
    public void VolleyPetitionSteps(String URL) {
        final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(json);
                        value = jsonObj.getString("value");
                        stepsList.add(value);
                    }if(stepsList.get(0).equals("null")){}
                    else{
                    floatSteps = new float[stepsList.size()];
                    for (int i = 0; i <  stepsList.size(); i++) {
                        floatSteps[i] = Float.parseFloat(stepsList.get(i));
                    }
                    if (floatSteps.length != 0) {
                        lastSteps = floatSteps[floatSteps.length - 1];
                        //MAX VALUE
                        maxSteps = floatSteps[0];
                        for (int i = 1; i < floatSteps.length; i++) {
                            if (floatSteps[i] > maxSteps) {
                                maxSteps = floatSteps[i];
                            }
                        }
                        //MIN VALUE
                        minSteps = floatSteps[0];
                        for (int i = 1; i < floatSteps.length; i++) {
                            if (floatSteps[i] < minSteps) {
                                minSteps = floatSteps[i];
                            }
                        }
                        //AVG VALUE
                        avgSteps = floatSteps[0];
                        for (int i = 1; i < floatSteps.length; i++) {
                            avgSteps = avgSteps + floatSteps[i];
                        }
                        avgSteps = avgSteps / floatSteps.length;
                        //
                        spDataNivelEditor.putFloat("minSteps", minSteps);
                        spDataNivelEditor.putFloat("maxSteps", maxSteps);
                        spDataNivelEditor.putFloat("avgSteps", avgSteps);
                        spDataNivelEditor.putFloat("sizeSteps", floatSteps.length);
                        spDataNivelEditor.putFloat("lastSteps", lastSteps);
                        spDataNivelEditor.apply();
                    }
                    }
                    //Toast.makeText(MainScreen.this, ""+floatSteps[0], Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }
        });
        queue.add(stringRequest);
    }

    ////////////// END VOLLEYS/////////////////////////////////////


    private void startCountAnimation(int from, int to) {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textPercentage.setText(animation.getAnimatedValue().toString()+"%");
            }
        });
        animator.start();
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    public void uploadMultipart() {
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //getting user email
        String email = sp.getString("email","no email");
        //getting the actual path of the image
        String path = getPath(filePath);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId,"http://meddata.sytes.net/newuser/update.php")
                    .addFileToUpload(path, "photo") //Adding file
                    .addParameter("email", email) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public void ShowDialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("CAMBIAR IMAGEN DE PERFIL");
        //builder.setMessage("");
        builder.setPositiveButton("BUSCAR EN EL DISPOSITIVO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showFileChooser();
            }
        })
        .setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setNegativeButton("TOMAR FOTO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File

                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(MainScreen.this,
                                        "com.example.android.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, 1);
                            }
                        }


                    }
                });
        builder.create().show();
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadMultipart();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            uploadMultipart2();
        }
    }

    public void uploadMultipart2() {
        //getting user email
        String email = "c";
        //getting the actual path of the image
        String path = mCurrentPhotoPath;

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId,"http://meddata.sytes.net/newuser/update.php")
                    .addFileToUpload(path, "photo") //Adding file
                    .addParameter("email", email) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected boolean internet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
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
                startActivity(new Intent(MainScreen.this,configuracion.class));
                return true;

            case R.id.logout:
                SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
                final SharedPreferences.Editor spDataNivelEditor = spDataNivel.edit();
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.apply();
                spDataNivelEditor.clear();
                spDataNivelEditor.apply();
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

    private void menu(){
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarMain);
        toolbar.setTitle("Principal");
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
                        if (position == 2) {
                            Intent intent = new Intent(MainScreen.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 3) {
                            Intent intent = new Intent(MainScreen.this, Registros.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 4){
                            Intent intent = new Intent(MainScreen.this, Statistics.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 5){
                            Intent intent = new Intent(MainScreen.this, MenuHinfo.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 7){
                            Intent intent = new Intent(MainScreen.this, configuracion.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 8){
                            Intent intent = new Intent(MainScreen.this, AboutUs.class);
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


