package com.example.luisguzmn.healthcare40;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {

    //VARIABLES
    SharedPreferences sp;
    //String cuenta;
    //String contraseña;
    EditText etEmail;
    EditText etPass;
    Button btnLog;
    String ip = "meddata.sytes.net";
    //String ip = "192.168.15.13";
    TextView txtCreateA;
    JSONArray jsonArray;
    int acceso=0;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PERMISSIONS
        checkPermissions();

        //LOGIN AUTO
        sp = getSharedPreferences("login",MODE_PRIVATE);
        if(sp.contains("email") && sp.contains("pass")){
            startActivity(new Intent(MainActivity.this,MainScreen.class));
            finish();   //finish current activity
        }

        //END LOGIN AUTO
        LottieAnimationView animationView = (LottieAnimationView)findViewById(R.id.animation_view);
        animationView.setMinAndMaxFrame(0,20);
        animationView.playAnimation();
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        btnLog = (Button) findViewById(R.id.btnLog);
        txtCreateA = (TextView) findViewById(R.id.txtCreateA);
        txtCreateA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, CrearCuenta.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(i,0);
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleyPetition("http://"+ ip +"/phpfiles/data_.php?email=" + etEmail.getText().toString()
                        + "&pass=" +etPass.getText().toString());

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
                    GlobalVars g = (GlobalVars)getApplication();
                    g.setName(jsonArray.getString(0));
                    g.setEmail(jsonArray.getString(1));
                    g.setPass(jsonArray.getString(2));
                    g.setBirth(jsonArray.getString(3));
                    g.setGender(jsonArray.getString(4));
                    g.setCountry(jsonArray.getString(5));
                    g.setDeviceS(jsonArray.getInt(6));
                    g.setDeviceH(jsonArray.getInt(7));
                    g.setDeviceB(jsonArray.getInt(8));
                    g.setDays_ex(jsonArray.getInt(9));
                    g.setHours_ex(jsonArray.getString(10));
                    g.setEx_int(jsonArray.getInt(11));
                    g.setWeight(jsonArray.getInt(12));
                    g.setHeight(jsonArray.getInt(13));
                    g.setImage(jsonArray.getString(14));
                    if (etEmail.getText().toString().equals(g.getEmail()) &
                            etPass.getText().toString().equals(g.getPass())) {
                        //
                        SharedPreferences.Editor e = sp.edit();
                        e.putString("email",etEmail.getText().toString());
                        e.putString("pass",etPass.getText().toString());
                        e.putString("name",g.getName());
                        e.putString("country",g.getCountry());
                        e.putString("imagen",g.getImage());
                        e.putString("birth",g.getBirth());
                        e.putString("gender",g.getGender());
                        e.putInt("deviceS",g.getDeviceS());
                        e.putInt("deviceH",g.getDeviceH());
                        e.putInt("deviceB",g.getDeviceB());
                        e.putInt("daysEx",g.getDays_ex());
                        e.putString("hoursEx",g.getHours_ex());
                        e.putInt("exInt",g.getEx_int());
                        e.putInt("weight",g.getWeight());
                        e.putInt("height",g.getHeight());
                        e.putBoolean("connected",true);
                        e.apply();
                        //
                        Toast.makeText(getApplicationContext(), "Bienvenido " + g.getName(), Toast.LENGTH_SHORT).show();
                        LottieAnimationView animationView = (LottieAnimationView)findViewById(R.id.animation_view);
                        //animationView.setMinAndMaxFrame(20,100);
                        animationView.setSpeed(0.5f);
                        animationView.playAnimation(20,100);
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                Intent intent = new Intent(MainActivity.this,MainScreen.class);
                                startActivity(intent);
                            }
                        },3000);
                    } else {
                        Toast.makeText(getApplicationContext(), "Contraseña o usuario incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "El usuario no existe", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }



    public void onPause (){
        super.onPause();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mieditor = datos.edit();
        //mieditor.putString("correo",cuenta);
        //mieditor.putString("contraseña",contraseña);
        mieditor.putInt("acceso",acceso);
        mieditor.apply();
    }

    //===========================================================================
    //============================= PERMISSIONS  ====================================
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissionsList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};


    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissionsList) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
                    String permissions = "";
                    for (String per : permissionsList) {
                        permissions += "\n" + per;
                    }
                    // permissions list of don't granted permission
                }
                return;
            }
        }
    }


    /*
    public void onResume (){
        super.onResume();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        acceso=datos.getInt("acceso",0);
    } */
}


