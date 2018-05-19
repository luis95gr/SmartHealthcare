package com.example.luisguzmn.healthcare40;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.graphics.YuvImage;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.ShareDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.worldgn.connector.Connector;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;


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
    public final String BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT = "com.worldgn.connector.DYNAMIC_HR_MEASUREMENT";
    public final String BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT = "com.worldgn.connector.DYNAMIC_STEPS_MEASUREMENT";


    public final String INTENT_KEY_HR_MEASUREMENT = "HR_MEASUREMENT";
    public final String INTENT_KEY_BR_MEASUREMENT = "BR_MEASUREMENT";
    public final String INTENT_KEY_BP_MEASUREMENT_MAX = "BP_MEASUREMENT_MAX";
    public final String INTENT_KEY_BP_MEASUREMENT_MIN = "BP_MEASUREMENT_MIN";
    public final String INTENT_KEY_MOOD_MEASUREMENT = "MOOD_MEASUREMENT";
    public final String INTENT_KEY_FATIGUE_MEASUREMENT = "FATIGUE_MEASUREMENT";
    public final String INTENT_KEY_STEPS_MEASUREMENT = "STEPS_MEASUREMENT";
    public final String INTENT_KEY_ECG_MEASUREMENT = "ECG_MEASUREMENT";
    public final String INTENT_KEY_ECG_VDO = "ECG_VDO";
    public final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";
    //
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    public final static String ACTION_GATT_CONNECTED =
            "com.example.luisguzmn.healthcare40.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.luisguzmn.healthcare40.ACTION_GATT_DISCONNECTED";



    //VARIABLES
    TextView text_monday, text_tuesday, text_wednesday, text_thursday, text_friday, text_saturday, text_sunday;
    //
    LottieAnimationView animationViewWelcome, animationViewEcg, animationFbIcon,animationInfo,animationHr;
    ProgressBar progressBar;
    TextView textWelcome, textWelcome2;
    TextView textReloj,textBluetooth;
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
    boolean booleanAnimations = true;
    TextView textHR, textSteps;
    boolean booleanMood, booleanFatigue;
    boolean booleanDynamic = true;
    //
    String hr, steps, bpmax, bpmin, br, mood, fatigue,ecgVdo,ecg;
    String hrPopup;
    double doubleHr;
    double doubleMaxHr;
    double doubleMinHr;
    //
    TextView textBP, textBR, textMood, textFatigue;
    //SAVED MEASURES
    boolean booleanBpMeasure,booleanBrMeasure,booleanEcgMeasure,booleanMoodMeasure,booleanFatigueMeasure= false;
    int contBp, contBr, contMood, contFatigue,contEcg = 0;
    String[] stringBpmaxSaved,stringBpminSaved,stringBrSaved,stringMoodSaved,stringFatigueSaved;
    SharedPreferences spMeasuresSaved;
    Calendar calendar;
    String stringDateBpSaved, stringDateBrSaved, stringDateMoodSaved, stringDateFatigueSaved,stringDateEcgSaved;
    String stringHourBpSaved, stringHourBrSaved, stringHourMoodSaved, stringHourFatigueSaved,stringHourEcgSaved;
    String stringDate;
    String stringHour,stringHourEcgDb;
    Date date;
    SimpleDateFormat sdfHour,sdfHourDbEcg;
    SimpleDateFormat sdfDate;
    //SEND DATA
    SharedPreferences spLogin;
    String stringEmail;
    String stringPass;
    final String ip = "meddata.sytes.net";
    String path,pathSend;
    //
    //FACEBOOK
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    //
    //POPUP
    SharedPreferences spPopup;
    //TEST
    Handler handlerBp,handlerBr,handlerMood,handlerFatiga;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.principal_dashboard);
        dias();
        handlerBp = new Handler();
        handlerBr = new Handler();
        handlerMood = new Handler();
        handlerFatiga = new Handler();
        //SHARED PREFERENCES
        spMeasuresSaved = PreferenceManager.getDefaultSharedPreferences(this);
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        stringEmail = spLogin.getString("email",null);
        stringPass = spLogin.getString("pass",null);
        //
        //INIT FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        //
        animationHr = (LottieAnimationView) findViewById(R.id.animation_hr);
        //
        animationHr.setImageAssetsFolder("Healthcare4.0\\app\\src\\main\\assets");

        //
        animationViewEcg = (LottieAnimationView) findViewById(R.id.animation_ecg);
        animationViewWelcome = (LottieAnimationView) findViewById(R.id.animation_welcome);
        animationFbIcon = (LottieAnimationView) findViewById(R.id.animation_fb);
        animationInfo = (LottieAnimationView) findViewById(R.id.animation_info);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textWelcome = (TextView) findViewById(R.id.textWelcome);
        textWelcome2 = (TextView) findViewById(R.id.textWelcome2);
        textReloj = (TextView) findViewById(R.id.textReloj);
        textHR = (TextView) findViewById(R.id.textHR);
        textSteps = (TextView) findViewById(R.id.textSteps);
        textBluetooth = (TextView)findViewById(R.id.textBluetooth);
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
        stringBpmaxSaved= new String[20];
        stringBpminSaved = new String[20];
        stringBrSaved = new String[20];
        stringMoodSaved = new String[20];
        stringFatigueSaved = new String[20];

        //
        //CAST DAYS
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
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
        intentFilter.addAction(BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT);
        //POPUP CAST
        spPopup =PreferenceManager.getDefaultSharedPreferences(this);
        //
        //FONTS
        Typeface font = Typeface.createFromAsset(getAssets(), "Jellee-Roman.ttf");
        textWelcome2.setTypeface(font);
        textWelcome.setTypeface(font);
        textReloj.setTypeface(font);
        //

        //CUBES ANIMATION
        animationViewWelcome.setSpeed(1);
        animationViewWelcome.playAnimation();
        //
        //ANIMATION OF BUTTONS
        animationBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        //DYNAMIC
        //Connector.getInstance().startStepsHRDynamicMeasurement();
        //booleanDynamic = true;
        //STEPS
       /* timerSteps.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    //Connector.getInstance().getStepsData();
                }
            }, 0, 2000);
        //
        //HEART RATE
        booleanAnimations = true;
        timerHR.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!booleanDynamic) {
                    Connector.getInstance().measureHR();
                }
            }
            }, 0, 50000);*/

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
        //ANIMATION CLICK INFO
        animationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalDashboard.this,PopUp.class);
                startActivity(intent);
            }
        });
        //HEART RATE POP UP
        textHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor spPopupEditor = spPopup.edit();
                spPopupEditor.putString("HR",hr);
                spPopupEditor.apply();
                Intent intentP = new Intent(PrincipalDashboard.this,PopUp.class);
                startActivity(intentP);
            }
        });
        //STEPS POP UP
        textSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor spPopupEditor = spPopup.edit();
                spPopupEditor.putString("Steps",steps);
                spPopupEditor.apply();
                Intent intentP = new Intent(PrincipalDashboard.this,PopUp.class);
                startActivity(intentP);
            }
        });
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
                buttonMood.setText("HUMOR");
                buttonMood.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                buttonFatigue.clearAnimation();
                buttonFatigue.setText("FATIGA");
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
        Connector.getInstance().startStepsHRDynamicMeasurement();
        booleanDynamic = true;
    }


    /////////////////MEASURES///////////////////
    public void measureBP(View view) {
        Connector.getInstance().stopStepsHRDynamicMeasurement();
        booleanDynamic = false;
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureBP();
        startcountDownTimer();
        //BUTTONS
        buttonBP.startAnimation(animationBlink);
        buttonBP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonBP.setText("Midiendo");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureBR(View view) {
        Connector.getInstance().stopStepsHRDynamicMeasurement();
        booleanDynamic = false;
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureBr();
        startcountDownTimer();
        //BUTTONS
        buttonBR.startAnimation(animationBlink);
        buttonBR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonBR.setText("Midiendo");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureMF(View view) {
        Connector.getInstance().stopStepsHRDynamicMeasurement();
        booleanDynamic = false;
        SharedPreferences.Editor spPopupEditor = spPopup.edit();
        booleanMood = true;
        spPopupEditor.putInt("Fat",1);
        spPopupEditor.apply();
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureMF();
        startcountDownTimer();
        //BUTTONS
        buttonMood.startAnimation(animationBlink);
        buttonMood.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonMood.setText("Midiendo");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureMFatigue(View view) {
        Connector.getInstance().stopStepsHRDynamicMeasurement();
        booleanDynamic = false;
        SharedPreferences.Editor spPopupEditor = spPopup.edit();
        booleanFatigue = true;
        spPopupEditor.putInt("Fat",2);
        spPopupEditor.apply();
        time = 40000;
        textReloj.setText("40");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureMF();
        startcountDownTimer();
        //BUTTONS
        buttonFatigue.startAnimation(animationBlink);
        buttonFatigue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonFatigue.setText("Midiendo");
        //ELSE BUTTONS
        buttonBP.setEnabled(false);
        buttonECG.setEnabled(false);
        buttonBR.setEnabled(false);
        buttonMood.setEnabled(false);
        buttonFatigue.setEnabled(false);
        //
    }

    public void measureEcg(View view) {
        Connector.getInstance().stopStepsHRDynamicMeasurement();
        booleanDynamic = false;
        time = 120000;
        textReloj.setText("120");
        textReloj.setVisibility(View.VISIBLE);
        Connector.getInstance().measureECG();
        startcountDownTimer();
        //BUTTONS
        buttonECG.startAnimation(animationBlink);
        buttonECG.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        buttonECG.setText("Midiendo");
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
                //
                SharedPreferences.Editor spPopupEditor = spPopup.edit();
                //
                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    bpmax = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    bpmin = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    textReloj.setVisibility(View.INVISIBLE);
                    textBP.setVisibility(View.VISIBLE);
                    handlerBp.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textBP.setText("BP max: " + bpmax + "\n BP min: " + bpmin);
                        }
                    },500);

                    Connector.getInstance().startStepsHRDynamicMeasurement();
                    booleanDynamic = true;
                    booleanBpMeasure = true;
                    //
                    animationInfo.setVisibility(View.VISIBLE);
                    animationInfo.playAnimation();
                    spPopupEditor.putString("BPmax",bpmax);
                    spPopupEditor.putString("BPmin",bpmin);
                    spPopupEditor.apply();
                    //
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
                    ecgVdo = intent.getStringExtra(INTENT_KEY_ECG_VDO);
                    ecg = intent.getStringExtra(INTENT_KEY_ECG_MEASUREMENT);
                    Connector.getInstance().startStepsHRDynamicMeasurement();
                    booleanDynamic = true;
                    booleanEcgMeasure = true;
                    textReloj.setText("HECHO!");
                    Toast.makeText(PrincipalDashboard.this, ecg, Toast.LENGTH_LONG).show();
                    ////////////////////////BREATH RATE////////////////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {
                    br = intent.getStringExtra(INTENT_KEY_BR_MEASUREMENT);
                    handlerBr.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textBR.setText("Frec Respiratoria:" + br);
                        }
                    },500);
                    textReloj.setVisibility(View.INVISIBLE);
                    textBR.setVisibility(View.VISIBLE);
                    Connector.getInstance().startStepsHRDynamicMeasurement();
                    booleanDynamic = true;
                    booleanBrMeasure = true;
                    //
                    animationInfo.setVisibility(View.VISIBLE);
                    animationInfo.playAnimation();
                    spPopupEditor.putString("BR",br);
                    spPopupEditor.apply();
                    //
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
                        handlerFatiga.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textFatigue.setText("Fatiga: " + fatigue);
                            }
                        },500);
                        //
                        animationInfo.setVisibility(View.VISIBLE);
                        animationInfo.playAnimation();
                        spPopupEditor.putString("Fatigue",fatigue);
                        spPopupEditor.apply();
                        //
                        textReloj.setVisibility(View.INVISIBLE);
                        if (booleanFatigue) {
                            textFatigue.setVisibility(View.VISIBLE);
                            booleanFatigueMeasure = true;
                            booleanFatigue = false;
                        }
                    }
                    Connector.getInstance().startStepsHRDynamicMeasurement();
                    booleanDynamic = true;
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
                        handlerMood.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textMood.setText("Humor: " + mood);
                            }
                        },500);
                        //
                        animationInfo.setVisibility(View.VISIBLE);
                        animationInfo.playAnimation();
                        spPopupEditor.putString("Mood",mood);
                        spPopupEditor.apply();
                        //
                        textReloj.setVisibility(View.INVISIBLE);
                        if (booleanMood) {
                            textMood.setVisibility(View.VISIBLE);
                            booleanMood = false;
                            booleanMoodMeasure = true;
                        }
                    }
                    Connector.getInstance().startStepsHRDynamicMeasurement();
                    booleanDynamic = true;
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
                    textSteps.setText("Pasos: " + steps);
                    /////////////////////////////////////HEART RATE//////////////////////////////
                } else if (intent.getAction().equals(BROADCAST_ACTION_HR_MEASUREMENT)) {
                    hr = intent.getStringExtra(INTENT_KEY_HR_MEASUREMENT);
                    textHR.setText("Ritmo cardiaco: " + hr);
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

                } else if (intent.getAction().equals(BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT)) {
                    hr = intent.getStringExtra(INTENT_KEY_HR_MEASUREMENT);
                    textHR.setText("Ritmo cardiaco: " +hr);
                    //
                    //HIDE WELCOME ANIMATION
                    if (booleanAnimations) {
                        booleanAnimations = false;
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
                    //
                    doubleHr = Double.parseDouble(hr);
                    if (spLogin.getString("gender","No gender").equalsIgnoreCase("Male")) {
                        doubleMaxHr = 203.7 / (1 + Math.exp(0.033 * (age() - 104.3)));
                    }
                    if (spLogin.getString("gender","No gender").equalsIgnoreCase("Female")) {
                        doubleMaxHr = 190.2 / (1 + Math.exp(0.0453 * (age() - 107.5)));
                    }
                    doubleMinHr = -2.4*((double)(spLogin.getInt("exInt",1)))+62.8;
                    if (doubleHr < doubleMinHr || doubleHr > doubleMaxHr){
                        animationHr.setAnimation("heartRateBri.json");
                        animationHr.playAnimation();
                        animationHr.loop(true);
                    }
                    if (doubleHr <= doubleMaxHr-90 && doubleHr >= doubleMinHr+10 ){
                        animationHr.setAnimation("heartRateGreen.json");
                        animationHr.playAnimation();
                        animationHr.loop(true);
                    }
                    if (doubleHr > doubleMinHr && doubleHr < doubleMinHr+10){
                        animationHr.setAnimation("heartRateYellow.json");
                        animationHr.playAnimation();
                        animationHr.loop(true);
                    }
                    if (doubleHr < doubleMaxHr && doubleHr > doubleMaxHr-90){
                        animationHr.setAnimation("heartRateYellow.json");
                        animationHr.playAnimation();
                        animationHr.loop(true);
                    }

                    //
                } else if (intent.getAction().equals(BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT)) {
                    steps = intent.getStringExtra(INTENT_KEY_STEPS_MEASUREMENT);
                    textSteps.setText("Pasos: " + steps);
                } else if (intent.getAction().equals(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE)) {
                    String message = intent.getStringExtra(INTENT_MEASUREMENT_WRITE_FAILURE);
                    Toast.makeText(PrincipalDashboard.this, message, Toast.LENGTH_LONG).show();
                }


            }
        }
    }
    ///BUTTONS SAVE/CANCEL
    public void saveMeasure(View view) {
        animationInfo.setVisibility(View.INVISIBLE);
        animationFbIcon.setVisibility(View.INVISIBLE);
        //RECOVER DATE AND HOUR
        date = Calendar.getInstance().getTime();
        sdfDate = new SimpleDateFormat("yyyy.MM.dd");
        sdfHour = new SimpleDateFormat("h:mm:a");
        sdfHourDbEcg = new SimpleDateFormat("h-mm-a");
        stringDate = sdfDate.format(date);
        stringHour = sdfHour.format(date);
        stringHourEcgDb = sdfHourDbEcg.format(date);
        checkConnection();
        //
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
        //POPUP
        SharedPreferences.Editor spPopupEditor = spPopup.edit();
        spPopupEditor.clear();
        spPopupEditor.apply();
        //
    }

    public void cancelMeasure(View view) {
        animationInfo.setVisibility(View.INVISIBLE);
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
        Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Cancelado", Snackbar.LENGTH_SHORT);
        snackbar.show();
        //POPUP
        SharedPreferences.Editor spPopupEditor = spPopup.edit();
        spPopupEditor.clear();
        spPopupEditor.apply();
        //
        if(!booleanDynamic){
            Connector.getInstance().startStepsHRDynamicMeasurement();
            booleanDynamic = true;
        }
    }



    public void ShowDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("INTERNET NO DISPONIBLE");
        builder.setMessage("ENCIENDE TU CONEXIÓN A INTERNET");
        builder.setPositiveButton("REINTENTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkConnection();
            }
        })
                .setNegativeButton("GUARDAR EN EL DISPOSITIVO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //SAVE ON SHARED PREFERENCES
                        if (booleanBpMeasure) {
                            contBp++;
                            booleanBpMeasure = false;
                            //DATE AND HOUR SAVE
                            stringDateBpSaved = stringDate;
                            stringHourBpSaved= stringHour;
                            //
                            //ENVIAR INFO A SHARED    // SEND ON PAUSE
                            stringBpmaxSaved[contBp] = "BPmax," + bpmax + "," + stringDateBpSaved + "," + stringHourBpSaved;
                            stringBpminSaved[contBp] = "BPmin," + bpmin + "," + stringDateBpSaved + "," + stringHourBpSaved;
                            //
                        }
                        if (booleanBrMeasure){
                            contBr++;
                            booleanBrMeasure = false;
                            //DATE AND HOUR SAVE
                            stringDateBrSaved= stringDate;
                            stringHourBrSaved= stringHour;
                            //
                            //ENVIAR INFO A SHARED
                            stringBrSaved[contBr] = "BR," + br + "," + stringDateBrSaved + "," + stringHourBrSaved;
                            //
                        }
                        if (booleanMoodMeasure){
                            contMood++;
                            booleanMoodMeasure = false;
                            //DATE AND HOUR SAVE
                            stringDateMoodSaved= stringDate;
                            stringHourMoodSaved = stringHour;
                            //
                            //ENVIAR INFO A SHARED
                            stringMoodSaved[contMood] = "Mood," + mood + "," + stringDateMoodSaved+","+stringHourMoodSaved;
                            //
                        }
                        if (booleanFatigueMeasure){
                            contFatigue++;
                            booleanFatigueMeasure = false;
                            //DATE AND HOUR SAVE
                            stringDateFatigueSaved = stringDate;
                            stringHourFatigueSaved= stringHour;
                            //
                            //ENVIAR INFO A SHARED
                            stringFatigueSaved[contFatigue] = "Fatigue," + fatigue + "," + stringDateFatigueSaved+","+stringHourFatigueSaved;
                            //
                            //
                        }
                        if (booleanEcgMeasure){
                            contEcg++;
                            booleanEcgMeasure = false;
                            //DATE AND HOUR SAVE
                            stringDateEcgSaved = stringDate;
                            stringHourEcgSaved = stringHour;
                            //
                            //CREATE TXT FILE
                            try {
                                File myDir = new File(Environment.getExternalStorageDirectory() + "/SmartHealthcareECG");
                                if (!myDir.exists()) {
                                    myDir.mkdirs();
                                }
                                File file = new File(myDir, stringHourEcgSaved + "-" + stringDateEcgSaved);
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(ecgVdo.getBytes());
                                fos.close();
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Guardado en el dispositivo", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } catch (java.io.IOException e){
                                e.printStackTrace();
                            }
                            //

                        }
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Saved", Snackbar.LENGTH_SHORT)
                                .setAction("Guardado", null);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.GREEN);
                        snackbar.show();

                        //START SERVICE
                    }
                });
        builder.create().show();
    }

    public void checkConnection(){
        if(internet()){
            if (booleanBpMeasure) {
                booleanBpMeasure = false;
                VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                        "&var=" + "BPmax" + "&value=" + bpmax + "&date=" + stringDate + "&time=" + stringHour);
                VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                        "&var=" + "BPmin" + "&value=" + bpmin + "&date=" + stringDate + "&time=" + stringHour);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Guardado", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            if (booleanBrMeasure) {
                booleanBrMeasure = false;
                VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                        "&var=" + "BR" + "&value=" + br + "&date=" + stringDate + "&time=" + stringHour);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Guardado", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            if (booleanMoodMeasure) {
                booleanMoodMeasure = false;
                VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                        "&var=" + "Mood" + "&value=" + mood + "&date=" + stringDate + "&time=" + stringHour);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Guardado", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            if (booleanFatigueMeasure) {
                booleanFatigueMeasure = false;
                VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                        "&var=" + "Fatigue" + "&value=" + fatigue + "&date=" + stringDate + "&time=" + stringHour);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Guardado", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            if (booleanEcgMeasure) {
                booleanEcgMeasure = false;
                //CREATE FILE
                try {
                    path = Environment.getExternalStorageDirectory() + "/SmartHealthcareECG";
                    Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                    File myDir = new File(path);
                    myDir.mkdirs();
                    File file = new File(myDir, stringHourEcgDb + "-" + stringDate);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(ecgVdo.getBytes());
                    fos.close();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.principal_dashboard), "Guardado", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    pathSend = path + "/" + stringHourEcgDb + "-" + stringDate;
                    Toast.makeText(this, pathSend, Toast.LENGTH_SHORT).show();
                } catch (java.io.IOException e){
                    e.printStackTrace();
                }
                //UPLOAD FILE
                try {
                    String uploadId = UUID.randomUUID().toString();

                            //Creating a multi part request
                                               new MultipartUploadRequest(getApplicationContext(), uploadId,"http://meddata.sytes.net/dataVar/registerECG.php")
                                                       .addFileToUpload(pathSend, "value") //Adding file
                            .addParameter("email", stringEmail) //Adding text parameter to the request
                                                       .addParameter("pass",stringPass)
                                                       .addParameter("var","ecg")
                                                       .addParameter("date",stringDate)
                                                       .addParameter("time",stringHour)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload

                } catch (Exception exc) {
                    Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ShowDialog();
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

    @Override
    protected void onPause() {
        super.onPause();
        final SharedPreferences.Editor spMeasuresSavedEditor = spMeasuresSaved.edit();
        //SEND CONTS
        spMeasuresSavedEditor.putInt("contBp",contBp);
        spMeasuresSavedEditor.putInt("contBr",contBr);
        spMeasuresSavedEditor.putInt("contMood",contMood);
        spMeasuresSavedEditor.putInt("contFatigue",contFatigue);
        //
        //SEND DATA TO SHARED PREFERENCES
        ///////BP//////
        for (int i=1 ; i<=contBp ; i++) {
            spMeasuresSavedEditor.putString("stringBpmaxSaved" + i, stringBpmaxSaved[i]);
            spMeasuresSavedEditor.putString("stringBpminSaved" + i, stringBpminSaved[i]);
        }
        ///////BR//////
        for (int j=1 ; j<=contBr; j++) {
            spMeasuresSavedEditor.putString("stringBrSaved" + j, stringBrSaved[j]);
        }
        ///////MOOD//////
        for (int k=1; k<=contMood; k++) {
            spMeasuresSavedEditor.putString("stringMoodSaved" + k, stringMoodSaved[k]);
        }
        ///////FATIGUE//////
        for (int m=1; m<=contFatigue; m++) {
            spMeasuresSavedEditor.putString("stringFatigueSaved" + m, stringFatigueSaved[m]);
        }

        spMeasuresSavedEditor.apply();
        //SERVICE
        if (contBp!=0 || contBr !=0 || contMood !=0 || contFatigue !=0) {
            contBp = contBr = contMood = contFatigue = 0;
            Intent intent = new Intent(PrincipalDashboard.this, serviceInternet.class);
            startService(intent);
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

    //
    public void fbShare(View view) {
        //INFO
        String share="";
        if (booleanBpMeasure) {
            share = "BP Max: " + bpmax + " BP Min: " + bpmin;
        }
        if (booleanBrMeasure) {
            share = "Frecuencia respiratoria: " + br;
        }
        if (booleanMoodMeasure) {
            share = "Humor: " + mood;
        }
        if (booleanFatigueMeasure) {
            share = "Fatiga: " + fatigue;
        }
        //
        //
        Intent shareContent = new Intent(android.content.Intent.ACTION_SEND);
        shareContent.setType("text/plain");
        shareContent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        //
        //CLIPBOARD
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myclip = ClipData.newPlainText("info",share);
        clipboard.setPrimaryClip(myclip);
        Toast.makeText(PrincipalDashboard.this, "\n" +
                "Información guardada en el portapapeles, pégala para compartirla en Facebook", Toast.LENGTH_LONG).show();
        //
        shareContent.putExtra(Intent.EXTRA_SUBJECT,  "http://meddata.sytes.net/");
        shareContent.putExtra(Intent.EXTRA_TEXT, share);

        startActivity(Intent.createChooser(shareContent, "Share"));


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

    private int age() {
        //YEAR,MONTH,DAY
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");
        String stringYear = sdfDate.format(date);
        sdfDate = new SimpleDateFormat("MM");
        String stringMonth = sdfDate.format(date);
        sdfDate = new SimpleDateFormat("dd");
        String stringDay = sdfDate.format(date);
        int intYearActual = Integer.parseInt(stringYear);
        int intMonthActual = Integer.parseInt(stringMonth);
        int intDayActual = Integer.parseInt(stringDay);
        //
        //BIRTH
        String stringBirth = spLogin.getString("birth","Birth");
        StringTokenizer tokenBirth = new StringTokenizer(stringBirth, "-");
        //TOKENS
        String tokenYear = tokenBirth.nextToken();
        String tokenMonth = tokenBirth.nextToken();
        String tokenDay = tokenBirth.nextToken();
        int year = Integer.parseInt(tokenYear);
        int month = Integer.parseInt(tokenMonth);
        int day = Integer.parseInt(tokenDay);
        //
        //CALCULATE AGE
        int ageYears = intYearActual - year;
        //
        if (intMonthActual-month < 0) {
            ageYears = ageYears-1;
        }
        return ageYears;
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                status = 1;
                textBluetooth.setText("Conectado");
                return;
            } else if (newState == STATE_DISCONNECTED) {
                status = 0;
                textBluetooth.setText("Desconectado");
                return;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(heloMeasurementReceiver);
    }
}





