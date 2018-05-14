package com.example.luisguzmn.healthcare40;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisguzmn.healthcare40.Helo.Utils;
import com.worldgn.connector.Connector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


public class DrivingDashboard extends AppCompatActivity {

    //VARIABLES
    TextView text_monday, text_tuesday, text_wednesday, text_thursday, text_friday, text_saturday, text_sunday;
    TextView txtFatiga,txtHr,txtMood,txtBp,txtBr,txtRes;
    boolean booleanStart;
    Chronometer chronometer;
    private long timeWhenStopped = 0;
    StringTokenizer stringTokenizer;
    String tokenH,tokenM,tokenS;
    static final int REQUEST_LOCATION = 1;
    //
    //HELO VARIABLES
    IntentFilter intentFilter2;
    MeasurementReceiver heloMeasurementReceiver2;
    //
    public final String BROADCAST_ACTION_BP_MEASUREMENT = "com.worldgn.connector.BP_MEASUREMENT";
    public final String BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE = "com.worldgn.connector.MEASURE_WRITE_FAILURE";
    public final String BROADCAST_ACTION_HR_MEASUREMENT = "com.worldgn.connector.HR_MEASUREMENT";
    public final String BROADCAST_ACTION_BR_MEASUREMENT = "com.worldgn.connector.BR_MEASUREMENT";
    public final String BROADCAST_ACTION_FATIGUE_MEASUREMENT = "com.worldgn.connector.FATIGUE_MEASUREMENT";
    public final String BROADCAST_ACTION_MOOD_MEASUREMENT = "com.worldgn.connector.MOOD_MEASUREMENT";
    public final String BROADCAST_ACTION_STEPS_MEASUREMENT = "com.worldgn.connector.STEPS_MEASUREMENT";
    //
    public final String INTENT_KEY_HR_MEASUREMENT = "HR_MEASUREMENT";
    public final String INTENT_KEY_BR_MEASUREMENT = "BR_MEASUREMENT";
    public final String INTENT_KEY_BP_MEASUREMENT_MAX = "BP_MEASUREMENT_MAX";
    public final String INTENT_KEY_BP_MEASUREMENT_MIN = "BP_MEASUREMENT_MIN";
    public final String INTENT_KEY_MOOD_MEASUREMENT = "MOOD_MEASUREMENT";
    public final String INTENT_KEY_FATIGUE_MEASUREMENT = "FATIGUE_MEASUREMENT";
    public final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";
    public  final String INTENT_KEY_STEPS_MEASUREMENT = "STEPS_MEASUREMENT";
    //
    //SEND VARIABLES
    final String ip = "meddata.sytes.net";
    SharedPreferences spLogin;
    String stringEmail,stringPass,stringDate,stringHour;
    Date date;
    SimpleDateFormat sdfHour;
    SimpleDateFormat sdfDate;
    TextView textEj,txtDis;
    LocationManager locationManager,locationManagerDis;
    LocationListener locationListener,locationListenerDis;
    Location location;
    double longi,lati;
    CountDownTimer countDownTimer,countDownTimerHr,countDownTimerBp,countDownTimerBr,countDownTimerMF;
    ProgressBar progressBar;
    int conduccion = 1;
    double speed;
    String stringTime;
    DecimalFormat decimalFormat = new DecimalFormat("##.##");
    Handler handler;
    String stringSpeed;
    ArrayList<Double> speedList;
    double speedAvg;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_dashboard);
        dias();
        //SHARED
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        stringEmail = spLogin.getString("email", null);
        stringPass = spLogin.getString("pass", null);
        //CAST
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        txtBp = (TextView) findViewById(R.id.txtBp);
        txtBr = (TextView) findViewById(R.id.txtBr);
        txtFatiga = (TextView) findViewById(R.id.txtFatiga);
        txtMood = (TextView) findViewById(R.id.txtMood);
        txtHr = (TextView) findViewById(R.id.txtHr);
        txtRes = (TextView) findViewById(R.id.txtRes);
        txtDis = (TextView) findViewById(R.id.txtDis);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        handler = new Handler();
        speedList = new ArrayList<Double>();
        double speedItem,speedAvg;
        //HELO CAST
        heloMeasurementReceiver2 = new MeasurementReceiver();
        intentFilter2 = new IntentFilter();
        intentFilter2.addAction(BROADCAST_ACTION_STEPS_MEASUREMENT);
        intentFilter2.addAction(BROADCAST_ACTION_BP_MEASUREMENT);
        intentFilter2.addAction(BROADCAST_ACTION_BR_MEASUREMENT);
        intentFilter2.addAction(BROADCAST_ACTION_FATIGUE_MEASUREMENT);
        intentFilter2.addAction(BROADCAST_ACTION_MOOD_MEASUREMENT);
        intentFilter2.addAction(BROADCAST_ACTION_HR_MEASUREMENT);
        intentFilter2.addAction(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE);
        //
        chronometer.setText("00:00:00");

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getLatitude();
                speed = location.getSpeed()*3.6;
                stringSpeed = decimalFormat.format(location.getSpeed()*3.6);
                txtDis.setText("Velocidad: " + stringSpeed + " km/hr");
                speedList.add(speed);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(heloMeasurementReceiver2, intentFilter2);
        Connector.getInstance().stopStepsHRDynamicMeasurement();
    }

    //START
    public void start(View view){
        Connector.getInstance().measureMF();
        startcountDownTimerHr();
        booleanStart = true;
        //VIEWS
        progressBar.setVisibility(View.VISIBLE);
        startcountDownTimer();
        txtDis.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.VISIBLE);
        txtRes.setVisibility(View.INVISIBLE);
        //CHRONO
        chronometer.setText("00:00:00");
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String t = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
                chronometer.setText(t);
            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        //DISTANCE
        locationManagerDis = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //PERMISSION
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            location = locationManagerDis.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        //
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longi = location.getLongitude();
                lati = location.getLatitude();
                Toast.makeText(DrivingDashboard.this, "LO: " + longi + "LA: " + lati, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    //
    //STOP
    public void stop(View view){
        progressBar.setVisibility(View.INVISIBLE);
        countDownTimer.cancel();
        booleanStart = false;
        txtDis.setVisibility(View.VISIBLE);
        chronometer.stop();
        Toast.makeText(DrivingDashboard.this, "STOP", Toast.LENGTH_SHORT).show();
        stringTokenizer = new StringTokenizer(chronometer.getText().toString(),":");
        tokenH = stringTokenizer.nextToken();
        tokenM = stringTokenizer.nextToken();
        tokenS = stringTokenizer.nextToken();
        chronometer.setVisibility(View.INVISIBLE);
        txtRes.setVisibility(View.VISIBLE);
        stringTime = tokenH + ":" + tokenM + ":" + tokenS;
        txtRes.setText("Tiempo: " + tokenH + " hrs " + tokenM + " min " + tokenS + " seg ");
        //
        //SPEED PROMEDIO
        if (!speedList.isEmpty()) {
            speedAvg = speedList.get(0);
            for (int i = 1; i < speedList.size(); i++) {
                speedAvg = speed + speedList.get(i);
            }
            speedAvg = speedAvg / speedList.size();
        }
        date = Calendar.getInstance().getTime();
        sdfDate = new SimpleDateFormat("yyyy.MM.dd");
        sdfHour = new SimpleDateFormat("h:mm:a");
        stringDate = sdfDate.format(date);
        stringHour = sdfHour.format(date);
        VolleyPetition("http://" + ip + "/dataVar/registerAuto.php?email=" + stringEmail + "&pass=" + stringPass +
                "&var=" + "Duracion" + "&value=" + stringTime + "&date=" + stringDate + "&time=" + stringHour + "&viaje=" + conduccion
                + "&velocidad=" + decimalFormat.format(speedAvg));
        //
        conduccion++;
    }

    public void startcountDownTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setMax(100);
                long pr = (millisUntilFinished / 1000) * 100000 / 60000;
                int progress = (int) pr;
                progressBar.setProgress(progress);
                String timeRemain = Long.toString(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                startcountDownTimer();
            }
        }.start();
    }
    //COUNTDOWN MEASURES
    public void startcountDownTimerHr(){
        countDownTimerHr = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!booleanStart) countDownTimerHr.cancel();
            }
            @Override
            public void onFinish() {
                Connector.getInstance().measureHR();
                startcountDownTimerBp();
            }
        }.start();
    }
    public void startcountDownTimerBp(){
        countDownTimerBp = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!booleanStart) countDownTimerBp.cancel();
            }
            @Override
            public void onFinish() {
                Connector.getInstance().measureBP();
                startcountDownTimerBr();
            }
        }.start();
    }
    public void startcountDownTimerBr(){
        countDownTimerBr = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!booleanStart) countDownTimerBr.cancel();
            }
            @Override
            public void onFinish() {
                Connector.getInstance().measureBr();
                startcountDownTimerMF();
            }
        }.start();
    }
    public void startcountDownTimerMF(){
        countDownTimerMF = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!booleanStart) countDownTimerMF.cancel();
            }
            @Override
            public void onFinish() {
                Connector.getInstance().measureMF();
                startcountDownTimerHr();
            }
        }.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                break;
        }
    }


    public void pause(View view){
        booleanStart = false;
        chronometer.stop();
        timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
    }
    public void reanudar(View view){
        booleanStart = true;
        Connector.getInstance().measureMF();
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();

    }
    //
    public class MeasurementReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    String max = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    String min = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    txtBp.setText(max + "/" + min);
                    //RECOVER DATE AND HOUR
                    date = Calendar.getInstance().getTime();
                    sdfDate = new SimpleDateFormat("yyyy.MM.dd");
                    sdfHour = new SimpleDateFormat("h:mm:a");
                    stringDate = sdfDate.format(date);
                    stringHour = sdfHour.format(date);
                    VolleyPetition("http://" + ip + "/dataVar/registerAuto.php?email=" + stringEmail + "&pass=" + stringPass +
                            "&var=" + "BPmax" + "&value=" + max + "&date=" + stringDate + "&time=" + stringHour + "&viaje=" + conduccion + "&velocidad=" +stringSpeed);
                    VolleyPetition("http://" + ip + "/dataVar/registerAuto.php?email=" + stringEmail + "&pass=" + stringPass +
                            "&var=" + "BPmin" + "&value=" + min + "&date=" + stringDate + "&time=" + stringHour + "&viaje=" + conduccion + "&velocidad=" +speed);
                    //
                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {
                    String br = intent.getStringExtra(INTENT_KEY_BR_MEASUREMENT);
                    txtBr.setText(br);
                    //RECOVER DATE AND HOUR
                    date = Calendar.getInstance().getTime();
                    sdfDate = new SimpleDateFormat("yyyy.MM.dd");
                    sdfHour = new SimpleDateFormat("h:mm:a");
                    stringDate = sdfDate.format(date);
                    stringHour = sdfHour.format(date);
                    VolleyPetition("http://" + ip + "/dataVar/registerAuto.php?email=" + stringEmail + "&pass=" + stringPass +
                            "&var=" + "BR" + "&value=" + br + "&date=" + stringDate + "&time=" + stringHour + "&viaje=" + conduccion + "&velocidad=" +stringSpeed);
                    //
                } else if (intent.getAction().equals(BROADCAST_ACTION_FATIGUE_MEASUREMENT)) {
                    String fatigue = intent.getStringExtra(INTENT_KEY_FATIGUE_MEASUREMENT);
                    txtFatiga.setText(fatigue);
                    //RECOVER DATE AND HOUR
                    date = Calendar.getInstance().getTime();
                    sdfDate = new SimpleDateFormat("yyyy.MM.dd");
                    sdfHour = new SimpleDateFormat("h:mm:a");
                    stringDate = sdfDate.format(date);
                    stringHour = sdfHour.format(date);
                    VolleyPetition("http://" + ip + "/dataVar/registerAuto.php?email=" + stringEmail + "&pass=" + stringPass +
                            "&var=" + "Fatigue" + "&value=" + fatigue + "&date=" + stringDate + "&time=" + stringHour + "&viaje=" + conduccion + "&velocidad=" +stringSpeed);
                } else if (intent.getAction().equals(BROADCAST_ACTION_MOOD_MEASUREMENT)) {
                    String mood = intent.getStringExtra(INTENT_KEY_MOOD_MEASUREMENT);
                    txtMood.setText(mood);
                    //RECOVER DATE AND HOUR
                    date = Calendar.getInstance().getTime();
                    sdfDate = new SimpleDateFormat("yyyy.MM.dd");
                    sdfHour = new SimpleDateFormat("h:mm:a");
                    stringDate = sdfDate.format(date);
                    stringHour = sdfHour.format(date);
                    VolleyPetition("http://" + ip + "/dataVar/registerAuto.php?email=" + stringEmail + "&pass=" + stringPass +
                            "&var=" + "Mood" + "&value=" + mood + "&date=" + stringDate + "&time=" + stringHour + "&viaje=" + conduccion + "&velocidad=" +stringSpeed);
                } else if (intent.getAction().equals(BROADCAST_ACTION_HR_MEASUREMENT)) {
                    String hr = intent.getStringExtra(INTENT_KEY_HR_MEASUREMENT);
                    txtHr.setText(hr);
                    //RECOVER DATE AND HOUR
                    date = Calendar.getInstance().getTime();
                    sdfDate = new SimpleDateFormat("yyyy.MM.dd");
                    sdfHour = new SimpleDateFormat("h:mm:a");
                    stringDate = sdfDate.format(date);
                    stringHour = sdfHour.format(date);
                    VolleyPetition("http://" + ip + "/dataVar/registerAuto.php?email=" + stringEmail + "&pass=" + stringPass +
                            "&var=" + "HR" + "&value=" + hr + "&date=" + stringDate + "&time=" + stringHour + "&viaje=" + conduccion + "&velocidad=" +stringSpeed);

                } else if (intent.getAction().equals(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE)) {
                    String message = intent.getStringExtra(INTENT_MEASUREMENT_WRITE_FAILURE);
                    Toast.makeText(DrivingDashboard.this, message, Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(BROADCAST_ACTION_STEPS_MEASUREMENT)) {
                    String steps = intent.getStringExtra(INTENT_KEY_STEPS_MEASUREMENT);
                    Toast.makeText(DrivingDashboard.this, "" + steps, Toast.LENGTH_SHORT).show();
                    textEj.setText("Pasos: " + steps);
                }
            }
        }
    }


    private void VolleyPetition(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.drivingDash), "Guardado", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DrivingDashboard.this, "Error!" , Toast.LENGTH_LONG).show();

            }
        });
        queue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(heloMeasurementReceiver2);
    }



    private void dias(){
        //CAST DAYS
        text_monday = (TextView)findViewById(R.id.text_monday);
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
