package com.example.luisguzmn.healthcare40;

import android.Manifest;

import android.content.SharedPreferences;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.example.luisguzmn.healthcare40.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.worldgn.connector.Connector;
import com.worldgn.connector.DeviceItem;

import com.worldgn.connector.ScanCallBack;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class PrincipalDashboard extends AppCompatActivity {
    public final String BROADCAST_ACTION_BP_MEASUREMENT = "com.worldgn.connector.BP_MEASUREMENT";
    public final String BROADCAST_ACTION_ECG_MEASUREMENT = "com.worldgn.connector.ECG_MEASUREMENT";
    public final String BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE = "com.worldgn.connector.MEASURE_WRITE_FAILURE";
    public final String BROADCAST_ACTION_HR_MEASUREMENT = "com.worldgn.connector.HR_MEASUREMENT";
    public final String BROADCAST_ACTION_BR_MEASUREMENT = "com.worldgn.connector.BR_MEASUREMENT";
    public final String BROADCAST_ACTION_SLEEP = "com.worldgn.connector.SLEEP";
    public final String BROADCAST_ACTION_FATIGUE_MEASUREMENT = "com.worldgn.connector.FATIGUE_MEASUREMENT";
    public final String BROADCAST_ACTION_MOOD_MEASUREMENT = "com.worldgn.connector.MOOD_MEASUREMENT";
    public final String BROADCAST_ACTION_STEPS_MEASUREMENT = "com.worldgn.connector.STEPS_MEASUREMENT";
    public final String BROADCAST_ACTION_RR_MEASUREMENT = "com.worldgn.connector.RRVALUE";

    public final String INTENT_KEY_HR_MEASUREMENT = "HR_MEASUREMENT";
    public final String INTENT_KEY_BR_MEASUREMENT = "BR_MEASUREMENT";
    public final String INTENT_KEY_BP_MEASUREMENT_MAX = "BP_MEASUREMENT_MAX";
    public final String INTENT_KEY_BP_MEASUREMENT_MIN = "BP_MEASUREMENT_MIN";
    public final String INTENT_KEY_MOOD_MEASUREMENT = "MOOD_MEASUREMENT";
    public final String INTENT_KEY_FATIGUE_MEASUREMENT = "FATIGUE_MEASUREMENT";
    public final String INTENT_KEY_STEPS_MEASUREMENT = "STEPS_MEASUREMENT";
    public final String INTENT_KEY_ECG_MEASUREMENT = "ECG_MEASUREMENT";
    public final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";
    public final String INTENT_KEY_RR = "RRVALUE";


    //VARIABLES
    TextView text_monday, text_tuesday, text_wednesday, text_thursday, text_friday, text_saturday, text_sunday;
    //
    LottieAnimationView animationViewWelcome, animationViewEcg, animationFbIcon;
    ProgressBar progressBar;
    TextView textWelcome, textWelcome2;
    TextView textReloj;
    Button buttonBP, buttonECG, buttonBR, buttonMood, buttonFatigue, buttonSave, buttonCancel;
    PermissionListener permissionListener;
    IntentFilter intentFilter;
    MeasurementReceiver heloMeasurementReceiver;
    CountDownTimer countDownTimer;
    Timer timerSteps = new Timer();
    Timer timerHR = new Timer();
    //
    //MEASUREMENT VARIABLES
    long time = 40000;
    Animation animationBlink;
    boolean booleanAnimations;
    TextView textHR, textSteps;
    boolean booleanMood, booleanFatigue;
    //
    String hr, steps, bpmax, bpmin, br, mood, fatigue;
    //
    TextView textBP, textBR, textMood, textFatigue;
    //SAVED MEASURES
    boolean booleanBpMeasure = false;
    boolean booleanBrMeasure = false;
    boolean booleanEcgMeasure = false;
    boolean booleanMoodMeasure = false;
    boolean booleanFatigueMeasure = false;
    int contBp, contBr, contMood, contFatigue = 1;
    double[] doubleBpmaxSaved, doubleBpminSaved, doubleBRSaved, doubleMoodSaved, doubleFatigueSaved;
    SharedPreferences spMeasuresSaved;
    Calendar calendar;
    Date[] datesBpmaxSaved, datesBpminSaved, datesBrSaved, datesMoodSaved, datesFatigueSaved;
    String hourBpmaxSaved, hourBpminSaved, hourBrSaved, hourMoodSaved, hourFatigueSaved;
    Date date;
    SimpleDateFormat sdf;
    //FACEBOOK
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.principal_dashboard);
        //SHARED PREFERENCES
        spMeasuresSaved = PreferenceManager.getDefaultSharedPreferences(this);
        //
        //INIT FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        //
        //CAST
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
        //
        animationViewEcg = (LottieAnimationView) findViewById(R.id.animation_ecg);
        animationViewWelcome = (LottieAnimationView) findViewById(R.id.animation_welcome);
        animationFbIcon = (LottieAnimationView) findViewById(R.id.animation_fb);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textWelcome = (TextView) findViewById(R.id.textWelcome);
        textWelcome2 = (TextView) findViewById(R.id.textWelcome2);
        textReloj = (TextView) findViewById(R.id.textReloj);
        textHR = (TextView) findViewById(R.id.textHR);
        textSteps = (TextView) findViewById(R.id.textSteps);
        buttonBP = (Button) findViewById(R.id.buttonBP);
        buttonECG = (Button) findViewById(R.id.buttonECG);
        buttonBR = (Button) findViewById(R.id.buttonBR);
        buttonMood = (Button) findViewById(R.id.buttonMood);
        buttonFatigue = (Button) findViewById(R.id.buttonFatigue);
        //MEASURES CAST
        textFatigue = (TextView) findViewById(R.id.textFatigue);
        textMood = (TextView) findViewById(R.id.textMood);
        textBP = (TextView) findViewById(R.id.textBP);
        textBR = (TextView) findViewById(R.id.textBR);
        //SAVING CAST
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        doubleBpminSaved = new double[50];
        doubleBpmaxSaved = new double[50];
        doubleBRSaved = new double[50];
        doubleMoodSaved = new double[50];
        doubleFatigueSaved = new double[50];

        calendar = Calendar.getInstance();
        datesBpmaxSaved = new Date[50];
        datesBpminSaved = new Date[50];
        datesBrSaved = new Date[50];
        datesMoodSaved = new Date[50];
        datesFatigueSaved = new Date[50];
        date = Calendar.getInstance().getTime();
        sdf = new SimpleDateFormat("h:mm:a");
        //
        //HELO CAST
        heloMeasurementReceiver = new MeasurementReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_BP_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_ECG_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_FATIGUE_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_MOOD_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_STEPS_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_HR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_SLEEP);
        intentFilter.addAction(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE);

        //FONTS
        Typeface font = Typeface.createFromAsset(getAssets(), "Jellee-Roman.ttf");
        textWelcome2.setTypeface(font);
        textWelcome.setTypeface(font);
        //

        //CUBES ANIMATION
        animationViewWelcome.setSpeed(1);
        animationViewWelcome.playAnimation();
        //
        //ANIMATION OF BUTTONS
        animationBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        //STEPS
        timerSteps.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Connector.getInstance().getStepsData();
            }
        }, 0, 1500);
        //
        //HEART RATE
        timerHR.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Connector.getInstance().measureHR();
                booleanAnimations = true;
            }
        }, 0, 50000);
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
                animationViewEcg.playAnimation(0, 100);
                textWelcome.setEnabled(false);
                //
            }
        });


        //

    }

    //MEASURES TIMER
    public void startcountDownTimer() {
        animationViewEcg.loop(true);
        animationViewEcg.playAnimation();
        textBP.setVisibility(View.INVISIBLE);
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setMax(100);
                long pr = (millisUntilFinished / 1000) * 100000 / time;
                int progress = (int) pr;
                progressBar.setProgress(progress);
                String timeRemain = Long.toString(millisUntilFinished / 1000);
                textReloj.setText(timeRemain);
            }

            @Override
            public void onFinish() {
                textReloj.setText("Wait...");
                progressBar.setProgress(100);
                animationViewEcg.loop(false);
                animationViewEcg.playAnimation(0, 100);
                //BUTTONS
                buttonSave.setVisibility(View.VISIBLE);
                buttonSave.setEnabled(true);
                //
                buttonCancel.setVisibility(View.VISIBLE);
                buttonCancel.setEnabled(true);
                ///
                buttonBP.clearAnimation();
                buttonBP.setText("BP");
                buttonBP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                buttonECG.clearAnimation();
                buttonECG.setText("ECG");
                buttonECG.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                buttonBR.clearAnimation();
                buttonBR.setText("BR");
                buttonBR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                buttonMood.clearAnimation();
                buttonMood.setText("MOOD");
                buttonMood.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                buttonFatigue.clearAnimation();
                buttonFatigue.setText("FATIGUE");
                buttonFatigue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                //

            }
        }.start();
    }
    //


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(heloMeasurementReceiver, intentFilter);
    }


    /////////////////MEASURES///////////////////
    public void measureBP(View view) {
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureBP();
        startcountDownTimer();
        //BUTTONS
        buttonBP.startAnimation(animationBlink);
        buttonBP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonBP.setText("Measuring");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureBR(View view) {
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureBr();
        startcountDownTimer();
        //BUTTONS
        buttonBR.startAnimation(animationBlink);
        buttonBR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonBR.setText("Measuring");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureMF(View view) {
        booleanMood = true;
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureMF();
        startcountDownTimer();
        //BUTTONS
        buttonMood.startAnimation(animationBlink);
        buttonMood.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonMood.setText("Measuring");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureMFatigue(View view) {
        booleanFatigue = true;
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureMF();
        startcountDownTimer();
        //BUTTONS
        buttonFatigue.startAnimation(animationBlink);
        buttonFatigue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonFatigue.setText("Measuring");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureEcg(View view) {
        time = 120000;
        textReloj.setText("120");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureECG();
        startcountDownTimer();
        //BUTTONS
        buttonECG.startAnimation(animationBlink);
        buttonECG.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonECG.setText("Measuring");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    ///////////////////////////////////////////////////////////////////////////////
    public class MeasurementReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {
                /////////////////BLOOD PRESSURE/////////////////////////////////////////
                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    bpmax = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    bpmin = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    textReloj.setVisibility(View.INVISIBLE);
                    textBP.setVisibility(View.VISIBLE);
                    textBP.setText("BP max: " + bpmax + "\n BP min: " + bpmin);
                    booleanBpMeasure = true;
                    //BUTTONS
                    buttonSave.setVisibility(View.VISIBLE);
                    buttonSave.setEnabled(true);
                    //
                    buttonCancel.setVisibility(View.VISIBLE);
                    buttonCancel.setEnabled(true);
                    //
                    animationFbIcon.setVisibility(View.VISIBLE);
                    animationFbIcon.playAnimation();
                    //////////////////////ECG////////////////////////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_ECG_MEASUREMENT)) {

                    ////////////////////////BREATH RATE////////////////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {
                    br = intent.getStringExtra(INTENT_KEY_BR_MEASUREMENT);
                    textBR.setText("Breath Rate: " + br);
                    textReloj.setVisibility(View.INVISIBLE);
                    textBR.setVisibility(View.VISIBLE);
                    booleanBrMeasure = true;
                    //BUTTONS
                    buttonSave.setVisibility(View.VISIBLE);
                    buttonSave.setEnabled(true);
                    //
                    buttonCancel.setVisibility(View.VISIBLE);
                    buttonCancel.setEnabled(true);
                    //
                    animationFbIcon.setVisibility(View.VISIBLE);
                    animationFbIcon.playAnimation();
                    /////////////////////////////////FATIGUE///////////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_FATIGUE_MEASUREMENT)) {
                    fatigue = intent.getStringExtra(INTENT_KEY_FATIGUE_MEASUREMENT);
                    if (!fatigue.isEmpty()) {
                        textFatigue.setText("Fatigue: " + fatigue);
                        textReloj.setVisibility(View.INVISIBLE);
                        if (booleanFatigue) {
                            textFatigue.setVisibility(View.VISIBLE);
                            booleanFatigueMeasure = true;
                            booleanFatigue = false;
                        }
                    }
                    //BUTTONS
                    buttonSave.setVisibility(View.VISIBLE);
                    buttonSave.setEnabled(true);
                    //
                    buttonCancel.setVisibility(View.VISIBLE);
                    buttonCancel.setEnabled(true);
                    //
                    animationFbIcon.setVisibility(View.VISIBLE);
                    animationFbIcon.playAnimation();
                    ///////////////////////////////MOOD////////////////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_MOOD_MEASUREMENT)) {
                    mood = intent.getStringExtra(INTENT_KEY_MOOD_MEASUREMENT);
                    if (!mood.isEmpty()) {
                        textMood.setText("Mood: " + mood);
                        textReloj.setVisibility(View.INVISIBLE);
                        if (booleanMood) {
                            textMood.setVisibility(View.VISIBLE);
                            booleanMood = false;
                            booleanMoodMeasure = true;
                        }
                    }
                    animationFbIcon.setVisibility(View.VISIBLE);
                    animationFbIcon.playAnimation();
                    //BUTTONS
                    buttonSave.setVisibility(View.VISIBLE);
                    buttonSave.setEnabled(true);
                    //
                    buttonCancel.setVisibility(View.VISIBLE);
                    buttonCancel.setEnabled(true);
                    ///////////////////////////////STEPS////////////////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_STEPS_MEASUREMENT)) {
                    steps = intent.getStringExtra(INTENT_KEY_STEPS_MEASUREMENT);
                    textSteps.setText("Steps: " + steps);
                    /////////////////////////////////////HEART RATE//////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_HR_MEASUREMENT)) {
                    hr = intent.getStringExtra(INTENT_KEY_HR_MEASUREMENT);
                    textHR.setText("Heart Rate: " + hr);
                    //
                    if (booleanAnimations) {
                        booleanAnimations = false;
                        animationViewWelcome.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        //textReloj.setVisibility(View.VISIBLE);
                        animationViewEcg.setVisibility(View.VISIBLE);
                        textWelcome.setVisibility(View.INVISIBLE);
                        textWelcome2.setVisibility(View.INVISIBLE);
                        //ANIMATION
                        animationViewEcg.setSpeed(3);
                        animationViewEcg.setMaxFrame(100);
                        animationViewEcg.loop(false);
                        animationViewEcg.playAnimation(0, 100);
                    }
                    //////////////////////////////////SLEEP////////////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_SLEEP)) {

                } else if (intent.getAction().equals(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE)) {
                    String message = intent.getStringExtra(INTENT_MEASUREMENT_WRITE_FAILURE);
                    Toast.makeText(PrincipalDashboard.this, message, Toast.LENGTH_LONG).show();
                }


            }
        }
    }

    ///BUTTONS SAVE/CANCEL
    public void saveMeasure(View view) {
        animationFbIcon.setVisibility(View.INVISIBLE);
        SharedPreferences.Editor spMeasuresSavedEditor = spMeasuresSaved.edit();
        if (booleanBpMeasure) {
            doubleBpmaxSaved[contBp] = Double.parseDouble(bpmax);
            doubleBpminSaved[contBp] = Double.parseDouble(bpmin);
            booleanBpMeasure = false;
            //DATE AND HOUR SAVE
            datesBpminSaved[contBp] = calendar.getTime();
            //
            contBp++;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Saved", Snackbar.LENGTH_SHORT);
            snackbar.show();
            //ENVIAR INFO
            checkConnection();
            //
        }
        if (booleanBrMeasure) {
            doubleBRSaved[contBr] = Double.parseDouble(br);
            booleanBrMeasure = false;
            //DATE AND HOUR SAVE
            datesBpmaxSaved[contBr] = calendar.getTime();
            //
            contBr++;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Saved", Snackbar.LENGTH_SHORT);
            snackbar.show();
            //ENVIAR INFO

            //
        }
        if (booleanMoodMeasure) {
            doubleMoodSaved[contMood] = Double.parseDouble(mood);
            booleanMoodMeasure = false;
            //DATE AND HOUR SAVE
            datesMoodSaved[contMood] = calendar.getTime();
            //
            contMood++;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Saved", Snackbar.LENGTH_SHORT);
            snackbar.show();
            //ENVIAR INFO

            //

        }
        if (booleanFatigueMeasure) {
            doubleFatigueSaved[contFatigue] = Double.parseDouble(fatigue);
            booleanFatigueMeasure = false;
            //DATE AND HOUR SAVE
            datesFatigueSaved[contFatigue] = calendar.getTime();
            //
            contFatigue++;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Saved", Snackbar.LENGTH_SHORT);
            snackbar.show();
            //ENVIAR INFO

            //
        }
        //BUTTONS
        buttonSave.setEnabled(false);
        buttonSave.setVisibility(View.INVISIBLE);
        buttonCancel.setEnabled(false);
        buttonCancel.setVisibility(View.INVISIBLE);
        //
        textBP.setVisibility(View.INVISIBLE);
        textBR.setVisibility(View.INVISIBLE);
        textMood.setVisibility(View.INVISIBLE);
        textFatigue.setVisibility(View.INVISIBLE);
        textReloj.setVisibility(View.VISIBLE);
        textReloj.setText("40");
        //ENABLE BUTTONS
        buttonBP.setEnabled(true);
        buttonECG.setEnabled(true);
        buttonBR.setEnabled(true);
        buttonMood.setEnabled(true);
        buttonFatigue.setEnabled(true);
        //
    }

    public void cancelMeasure(View view) {
        animationFbIcon.setVisibility(View.INVISIBLE);
        //BUTTONS
        buttonSave.setEnabled(false);
        buttonSave.setVisibility(View.INVISIBLE);
        buttonCancel.setEnabled(false);
        buttonCancel.setVisibility(View.INVISIBLE);
        //
        textBP.setVisibility(View.INVISIBLE);
        textBR.setVisibility(View.INVISIBLE);
        textMood.setVisibility(View.INVISIBLE);
        textFatigue.setVisibility(View.INVISIBLE);
        textReloj.setVisibility(View.VISIBLE);
        textReloj.setText("40");
        //ENABLE BUTTONS
        buttonBP.setEnabled(true);
        buttonECG.setEnabled(true);
        buttonBR.setEnabled(true);
        buttonMood.setEnabled(true);
        buttonFatigue.setEnabled(true);
        //
        booleanMoodMeasure = false;
        booleanFatigueMeasure = false;
        booleanBrMeasure = false;
        booleanBpMeasure = false;
        booleanEcgMeasure = false;
        Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Cancel", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    //
    public void fbShare(View view) {
        //INFO
        String share = "No info";
        if (booleanBpMeasure) {
            share = "BP Max: " + bpmax + "BP Min: " + bpmin;
            booleanBpMeasure = false;
        }
        if (booleanBrMeasure) {
            share = "Breath Rate: " + br;
            booleanBrMeasure = false;
        }
        if (booleanMoodMeasure) {
            share = "Mood: " + mood;
            booleanMoodMeasure = false;
        }
        if (booleanFatigueMeasure) {
            share = "Fatigue: " + fatigue;
            booleanFatigueMeasure = false;
        }
        //
        //
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setQuote(share)
                .setContentUrl(Uri.parse("http://meddata.sytes.net/"))
                .build();
        if (shareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(shareLinkContent);
        }
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Share Successful", Snackbar.LENGTH_LONG);
                snackbar.show();
                booleanMoodMeasure = false;
                booleanFatigueMeasure = false;
                booleanBrMeasure = false;
                booleanBpMeasure = false;
                booleanEcgMeasure = false;
            }

            @Override
            public void onCancel() {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Share cancel", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onError(FacebookException error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), error.getMessage(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(heloMeasurementReceiver);
    }


    public void ShowDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("DEVICE");
        builder.setMessage("Please turn on Internet Connection");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkConnection();
            }
        })
                .setNegativeButton("SAVE ON DEVICE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //START SERVICE
                        //SAVE ON SHARED PREFERENCES
                    }
                });
        builder.create().show();
    }
    protected boolean internet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection(){
        if(internet()){
            //SEND DATA
        }else {
            ShowDialog();
        }
    }
}





