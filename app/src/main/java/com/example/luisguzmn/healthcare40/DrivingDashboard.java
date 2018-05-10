package com.example.luisguzmn.healthcare40;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.worldgn.connector.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class DrivingDashboard extends AppCompatActivity {

    //VARIABLES
    TextView text_monday, text_tuesday, text_wednesday, text_thursday, text_friday, text_saturday, text_sunday;
    Switch switchFatigue,switchHr,switchMood,switchBp,switchBr;
    boolean booleanFatigue,booleanHr,booleanMood,booleanBp,booleanBr;
    Timer timerFatigue = new Timer();
    Timer timerHr = new Timer();
    Timer timerMood = new Timer();
    Timer timerBp = new Timer();
    Timer timerBr = new Timer();
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
    TextView textEj;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_dashboard);
        dias();
        //SHARED
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        stringEmail = spLogin.getString("email",null);
        stringPass = spLogin.getString("pass",null);
        //CAST
        switchFatigue = (Switch)findViewById(R.id.switchFatigue);
        switchHr = (Switch)findViewById(R.id.switchHr);
        switchMood = (Switch)findViewById(R.id.switchMood);
        switchBp = (Switch)findViewById(R.id.switchBp);
        switchBr = (Switch)findViewById(R.id.switchBr);
        textEj = (TextView)findViewById(R.id.textEj);
        //
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
        //SWITCHES
        switchFatigue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchFatigue.isChecked()) booleanFatigue = true; else booleanFatigue = false;
            }
        });
        switchHr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchHr.isChecked()) booleanHr = true; else booleanHr = false;
            }
        });
        switchMood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchHr.isChecked()) booleanHr = true; else booleanHr = false;
            }
        });
        switchBp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchMood.isChecked()) booleanMood = true; else booleanMood = false;
            }
        });
        switchBr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchBr.isChecked()) booleanBr = true; else booleanBr = false;
            }
        });

        //
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(heloMeasurementReceiver2, intentFilter2);
    }

    //START
    public void start(View view){
        //TIMERS
        /*timerFatigue.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Connector.getInstance().getStepsData();
            }
        },0,2000);*/
        //Connector.getInstance().measureHR();
        Connector.getInstance().getStepsData();
        Toast.makeText(DrivingDashboard.this, "ENTRO", Toast.LENGTH_SHORT).show();
        //Connector.getInstance().measureMF();
    }
    //
    //STOP
    public void stop(View view){
        timerFatigue.cancel();
    }
    //
    public class MeasurementReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {
                Toast.makeText(DrivingDashboard.this, "JWHKE", Toast.LENGTH_LONG).show();
                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    String max = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    String min = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {

                } else if (intent.getAction().equals(BROADCAST_ACTION_FATIGUE_MEASUREMENT)) {
                    Toast.makeText(DrivingDashboard.this, "ENTRO", Toast.LENGTH_SHORT).show();
                    String fatigue = intent.getStringExtra(INTENT_KEY_FATIGUE_MEASUREMENT);
                    textEj.setText(fatigue);
                    Toast.makeText(DrivingDashboard.this, "" + fatigue, Toast.LENGTH_SHORT).show();
                    //RECOVER DATE AND HOUR
                    date = Calendar.getInstance().getTime();
                    sdfDate = new SimpleDateFormat("yyyy.MM.dd");
                    sdfHour = new SimpleDateFormat("h:mm:a");
                    stringDate = sdfDate.format(date);
                    stringHour = sdfHour.format(date);
                    VolleyPetition("http://" + ip + "/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                            "&var=" + "Fatigue" + "&value=" + fatigue + "&date=" + stringDate + "&time=" + stringHour);
                } else if (intent.getAction().equals(BROADCAST_ACTION_MOOD_MEASUREMENT)) {

                } else if (intent.getAction().equals(BROADCAST_ACTION_HR_MEASUREMENT)) {

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
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Guardado", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error!" , Toast.LENGTH_LONG).show();

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
