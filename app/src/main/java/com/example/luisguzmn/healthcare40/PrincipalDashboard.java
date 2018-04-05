package com.example.luisguzmn.healthcare40;

import android.Manifest;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.example.luisguzmn.healthcare40.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.worldgn.connector.Connector;
import com.worldgn.connector.DeviceItem;

import com.worldgn.connector.ScanCallBack;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PrincipalDashboard extends AppCompatActivity {
    public final String BROADCAST_ACTION_BP_MEASUREMENT = "com.worldgn.connector.BP_MEASUREMENT";
    public final String BROADCAST_ACTION_ECG_MEASUREMENT = "com.worldgn.connector.ECG_MEASUREMENT";
    public final String BROADCAST_ACTION_APPVERSION_MEASUREMENT = "com.worldgn.connector.APPVERSION_MEASUREMENT";
    public final String BROADCAST_ACTION_HELO_DEVICE_RESET = "com.worldgn.connector.ACTION_HELO_DEVICE_RESET";
    public final String BROADCAST_ACTION_HELO_CONNECTED = "com.worldgn.connector.ACTION_HELO_CONNECTED";
    public final String BROADCAST_ACTION_HELO_DISCONNECTED = "com.worldgn.connector.ACTION_HELO_DISCONNECTED";
    public final String BROADCAST_ACTION_HELO_BONDED = "com.worldgn.connector.ACTION_HELO_BONDED";
    public final String BROADCAST_ACTION_HELO_UNBONDED = "com.worldgn.connector.ACTION_HELO_UNBONDED";
    public final String BROADCAST_ACTION_HELO_FIRMWARE = "com.worldgn.connector.ACTION_HELO_FIRMWARE";
    public final String BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE = "com.worldgn.connector.MEASURE_WRITE_FAILURE";
    public final String BROADCAST_ACTION_BATTERY = "com.worldgn.connector.BATTERY";
    public final String BROADCAST_ACTION_HR_MEASUREMENT = "com.worldgn.connector.HR_MEASUREMENT";
    public final String BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT = "com.worldgn.connector.DYNAMIC_HR_MEASUREMENT";
    public final String BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT = "com.worldgn.connector.DYNAMIC_STEPS_MEASUREMENT";
    public final String BROADCAST_ACTION_BR_MEASUREMENT = "com.worldgn.connector.BR_MEASUREMENT";
    public final String BROADCAST_ACTION_SLEEP = "com.worldgn.connector.SLEEP";
    public final String BROADCAST_ACTION_FATIGUE_MEASUREMENT = "com.worldgn.connector.FATIGUE_MEASUREMENT";
    public final String BROADCAST_ACTION_MOOD_MEASUREMENT = "com.worldgn.connector.MOOD_MEASUREMENT";
    public final String BROADCAST_ACTION_STEPS_MEASUREMENT = "com.worldgn.connector.STEPS_MEASUREMENT";

    public final String INTENT_KEY_HR_MEASUREMENT = "HR_MEASUREMENT";
    public final String INTENT_KEY_BR_MEASUREMENT = "BR_MEASUREMENT";
    public final String INTENT_KEY_BATTERY = "BATTERY";
    public final String INTENT_KEY_BP_MEASUREMENT_MAX = "BP_MEASUREMENT_MAX";
    public final String INTENT_KEY_BP_MEASUREMENT_MIN = "BP_MEASUREMENT_MIN";
    public final String INTENT_KEY_MOOD_MEASUREMENT = "MOOD_MEASUREMENT";
    public final String INTENT_KEY_FATIGUE_MEASUREMENT = "FATIGUE_MEASUREMENT";
    public final String INTENT_KEY_STEPS_MEASUREMENT = "STEPS_MEASUREMENT";
    public final String INTENT_KEY_ECG_MEASUREMENT = "ECG_MEASUREMENT";
    public final String INTENT_KEY_FIRMWARE = "HELO_FIRMWARE";
    public final String INTENT_KEY_MAC = "HELO_MAC";
    public final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";


    //VARIABLES
    TextView text_monday;
    TextView text_tuesday;
    TextView text_wednesday;
    TextView text_thursday;
    TextView text_friday;
    TextView text_saturday;
    TextView text_sunday;
    //
    LottieAnimationView animationViewWelcome;
    LottieAnimationView animationViewEcg;
    ProgressBar progressBar;
    TextView textWelcome;
    TextView textWelcome2;
    TextView textReloj;
    Button buttonBP;
    Button buttonECG;
    Button buttonBR;
    Button buttonMood;
    Button buttonFatigue;
    PermissionListener permissionListener;
    IntentFilter intentFilter;
    MeasurementReceiver heloMeasurementReceiver;
    CountDownTimer countDownTimer;
    Timer timerSteps = new Timer();
    Timer timerHR = new Timer();
    //
    //MEASUREMENT VARIABLES
    Boolean booleanAnimations = true;
    TextView textHR;
    TextView textSteps;
    TextView textBP;
    TextView textBR;
    TextView textMood;
    TextView textFatigue;
    //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_dashboard);

        //CAST
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
        //
        animationViewEcg = (LottieAnimationView)findViewById(R.id.animation_ecg);
        animationViewWelcome = (LottieAnimationView)findViewById(R.id.animation_welcome);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        textWelcome = (TextView)findViewById(R.id.textWelcome);
        textWelcome2 = (TextView)findViewById(R.id.textWelcome2);
        textReloj = (TextView)findViewById(R.id.textReloj);
        textHR = (TextView)findViewById(R.id.textHR);
        textSteps = (TextView)findViewById(R.id.textSteps);
        buttonBP = (Button) findViewById(R.id.buttonBP);
        buttonECG = (Button) findViewById(R.id.buttonECG);
        buttonBR = (Button) findViewById(R.id.buttonBR);
        buttonMood = (Button) findViewById(R.id.buttonMood);
        buttonFatigue = (Button) findViewById(R.id.buttonFatigue);
        //MEASURES CAST
        textBP = (TextView)findViewById(R.id.textBP);
        //
        //HELO CAST
        heloMeasurementReceiver = new MeasurementReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_BP_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_APPVERSION_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BATTERY);
        intentFilter.addAction(BROADCAST_ACTION_ECG_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_FATIGUE_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_MOOD_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_STEPS_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_HR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_SLEEP);
        intentFilter.addAction(BROADCAST_ACTION_HELO_CONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DISCONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_HELO_BONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_UNBONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_FIRMWARE);
        intentFilter.addAction(BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE);

        Typeface font = Typeface.createFromAsset(getAssets(),"Jellee-Roman.ttf");
        textWelcome2.setTypeface(font);
        textWelcome.setTypeface(font);

        //ANIMATION CONFIGURATION
        //ECG
        //BALLOONS
        animationViewWelcome.setSpeed(1);
        animationViewWelcome.playAnimation();
        //
        //STEPS
        timerSteps.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Connector.getInstance().getStepsData();
            }
        },0,2000);
        //
        //HEART RATE
        timerHR.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Connector.getInstance().measureHR();
                booleanAnimations=false;
            }
        },0,50000);
        //
        //ANIMATIONS
        textWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationViewWelcome.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                textReloj.setVisibility(View.VISIBLE);
                animationViewEcg.setVisibility(View.VISIBLE);
                textWelcome.setVisibility(View.INVISIBLE);
                textWelcome2.setVisibility(View.INVISIBLE);
                //ANIMATION
                animationViewEcg.setSpeed(3);
                animationViewEcg.setMaxFrame(100);
                animationViewEcg.loop(false);
                animationViewEcg.playAnimation(0,100);
                //
            }
        });


        //

    }
    //MEASURES TIMER
    public void startcountDownTimer(){
        animationViewEcg.loop(true);
        animationViewEcg.playAnimation();
        countDownTimer = new CountDownTimer(50000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setMax(100);
                long pr = (millisUntilFinished/1000)*2;
                int progress = (int)pr;
                progressBar.setProgress(progress);
                String timeRemain = Long.toString(millisUntilFinished/1000);
                textReloj.setText(timeRemain);
            }

            @Override
            public void onFinish() {
                textReloj.setText("DONE!");
                progressBar.setProgress(100);
                animationViewEcg.cancelAnimation();
            }
        }.start();
    }
    //


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(heloMeasurementReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(heloMeasurementReceiver);
    }

    public void measureBP(View view) {
        Connector.getInstance().measureBP();
        startcountDownTimer();
    }


    public void measureEcg(View view) {
        //Connector.getInstance().measureECG();
        startcountDownTimer();
    }

    public void measureBR(View view) {
        Connector.getInstance().measureBr();
    }

    public void measureMF(View view) {
        Connector.getInstance().measureMF();
    }

    public void measurePW(View view) {
//        Connector.getInstance().sendBpData(getApplicationContext());
    }

    public class MeasurementReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {

                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    String bpmax = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    String bpmin = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    textReloj.setVisibility(View.INVISIBLE);
                    textBP.setVisibility(View.VISIBLE);
                    textBP.setText("BP max: " + bpmax + "\n BP min: "+bpmin);
                } else if (intent.getAction().equals(BROADCAST_ACTION_ECG_MEASUREMENT)) {
                    //updateUI(intent, "ECG", INTENT_KEY_ECG_MEASUREMENT, ecg_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {

                    //updateUI(intent, "BR", INTENT_KEY_BR_MEASUREMENT, br_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_FATIGUE_MEASUREMENT)) {
                    //updateUI(intent, "FATIGUE", INTENT_KEY_FATIGUE_MEASUREMENT, fatigue_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_MOOD_MEASUREMENT)) {
                    //updateUI(intent, "MOOD", INTENT_KEY_MOOD_MEASUREMENT, mood_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_STEPS_MEASUREMENT)) {
                    String br= intent.getStringExtra(INTENT_KEY_STEPS_MEASUREMENT);
                    textSteps.setText("Steps: "+br);
                } else if (intent.getAction().equals(BROADCAST_ACTION_HR_MEASUREMENT)) {
                    //updateUI(intent, "HR", INTENT_KEY_HR_MEASUREMENT, hr_val);
                    String hr= intent.getStringExtra(INTENT_KEY_HR_MEASUREMENT);
                    textHR.setText("Heart Rate: "+hr);
                    //
                    if(booleanAnimations) {
                        animationViewWelcome.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        textReloj.setVisibility(View.VISIBLE);
                        animationViewEcg.setVisibility(View.VISIBLE);
                        textWelcome.setVisibility(View.INVISIBLE);
                        textWelcome2.setVisibility(View.INVISIBLE);
                        //ANIMATION
                        animationViewEcg.setSpeed(3);
                        animationViewEcg.setMaxFrame(100);
                        animationViewEcg.loop(false);
                        animationViewEcg.playAnimation(0, 100);
                    }
                } else if (intent.getAction().equals(BROADCAST_ACTION_SLEEP)) {
                    //updateUI(intent, "SLEEP", "com.wgn.SLEEP_ALL_DATA", sleep_val);
//                    updateUI(intent, "SLEEP", Constants.INTENT_KEY_SLEEP, sleep_val);
                }


            }
        }
    }


    public void stepcount(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}


