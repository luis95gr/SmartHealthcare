package com.example.luisguzmn.healthcare40;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Timer;

public class PopUp extends AppCompatActivity {

    //VARIABLES
    SharedPreferences sp,spPopup;
    TextView textName,textGender,textWeight,textAge,textHeight;
    TextView textBRtitle,textBPtitle,textMoodTitle, textFatigueTitle,textHRtitle,textStepsTitle;
    int intWeight,intHeight;
    //
    TextView textEAge,textEGender,textEWeight,textEHeight;
    //
    String stringBpmax, stringBpmin, stringBr, stringMood, stringFatigue,stringHr,stringSteps;
    int width;
    int height;
    //
    TextView textMeasureBr,textMeasureBp,textMeasureMood,textMeasureFatigue,textMeasureHr,textMeasureSteps;
    //
    int intNormalBpmax,intNormalBpmin,intNormalBrmin,intNormalBrmax;
    int intMeasureBpmax,intMeasureBpmin,intMeasureBr,intMeasureHr;
    double doubleMeasureSteps,doubleLength;
    String stringDistance;
    TextView textInfo,textMessage,textStatus;
    double doubleMaxHr,doubleMinHr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);
        //STRUCTURE
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int)(width*.75),(int)(height*.6));
        //CAST
        sp = getSharedPreferences("login",MODE_PRIVATE);
        spPopup = PreferenceManager.getDefaultSharedPreferences(this);
        //
        textName = (TextView)findViewById(R.id.textName);
        textGender = (TextView)findViewById(R.id.textGender);
        textHeight = (TextView)findViewById(R.id.textHeight);
        textWeight = (TextView)findViewById(R.id.textWeight);
        textAge = (TextView)findViewById(R.id.textAge);
        //
        textEHeight = (TextView)findViewById(R.id.textEHeight);
        textEWeight = (TextView)findViewById(R.id.textEWeight);
        textEGender = (TextView)findViewById(R.id.textEGender);
        textEAge = (TextView)findViewById(R.id.textEAge);
        //
        textBRtitle = (TextView)findViewById(R.id.textBRtitle);
        textBPtitle = (TextView)findViewById(R.id.textBPtitle);
        textMoodTitle = (TextView)findViewById(R.id.textMoodTitle);
        textFatigueTitle = (TextView)findViewById(R.id.textFatigueTitle);
        textHRtitle = (TextView)findViewById(R.id.textHRTitle);
        textStepsTitle = (TextView)findViewById(R.id.textStepsTitle);
        ///FONTS
        Typeface font = Typeface.createFromAsset(getAssets(), "Jellee-Roman.ttf");
        textWeight.setTypeface(font);
        textHeight.setTypeface(font);
        textGender.setTypeface(font);
        textName.setTypeface(font);
        textAge.setTypeface(font);
        //
        textEAge.setTypeface(font);
        textEGender.setTypeface(font);
        textEWeight.setTypeface(font);
        textEHeight.setTypeface(font);
        //CAST
        textMeasureBp = (TextView)findViewById(R.id.textMeasureBp);
        textMeasureBr = (TextView)findViewById(R.id.textMeasureBr);
        textMeasureFatigue = (TextView)findViewById(R.id.textMeasureFatigue);
        textMeasureMood = (TextView)findViewById(R.id.textMeasureMood);
        textMeasureHr = (TextView)findViewById(R.id.textMeasureHR);
        textMeasureSteps = (TextView)findViewById(R.id.textMeasureSteps);
        textInfo = (TextView)findViewById(R.id.textInfo);
        textMessage = (TextView)findViewById(R.id.textMessage);
        textStatus = (TextView)findViewById(R.id.textStatus);
        //FONTS
        textMeasureMood.setTypeface(font);
        textMeasureFatigue.setTypeface(font);
        textMeasureBr.setTypeface(font);
        textMeasureBp.setTypeface(font);
        textMeasureHr.setTypeface(font);
        textMeasureSteps.setTypeface(font);
        //FONTS
        Typeface font2 = Typeface.createFromAsset(getAssets(), "Hanken-Light.ttf");
        textBRtitle.setTypeface(font2);
        textFatigueTitle.setTypeface(font2);
        textMoodTitle.setTypeface(font2);
        textBPtitle.setTypeface(font2);
        textHRtitle.setTypeface(font2);
        textStepsTitle.setTypeface(font2);
        textInfo.setTypeface(font2);
        //
        textName.setText(sp.getString("name","No name"));
        textGender.setText(sp.getString("gender","No gender"));
        intHeight = sp.getInt("height",0);
        intWeight = sp.getInt("weight",0);
        textHeight.setText(intHeight + " cm");
        textWeight.setText(intWeight + " kg");
        textAge.setText(""+age());
        //
        //RECEIVE DATA
        stringBpmax = spPopup.getString("BPmax","0");
        stringBpmin = spPopup.getString("BPmin","0");
        stringBr = spPopup.getString("BR","0");
        stringMood = spPopup.getString("Mood","0");
        stringFatigue = spPopup.getString("Fatigue","0");
        stringHr = spPopup.getString("HR","0");
        stringSteps = spPopup.getString("Steps","0");
        int MFdecision = spPopup.getInt("Fat",3);

        //STEPS
        if (!stringSteps.equals("0") && stringHr.equals("0")){
            getSupportActionBar().setTitle("Info Pasos");
            textStepsTitle.setVisibility(View.VISIBLE);
            textMeasureSteps.setVisibility(View.VISIBLE);
            textMeasureSteps.setText(stringSteps + " pasos");
            doubleMeasureSteps = Double.parseDouble(stringSteps);
            //
            if (sp.getString("gender","No gender").equalsIgnoreCase("Male")) {
                doubleLength = sp.getInt("height",0)*0.415;
            }
            if (sp.getString("gender","No gender").equalsIgnoreCase("Female")) {
                doubleLength = sp.getInt("height",0)*0.413;
            }
            textInfo.setText("Se recomienda dar un total de 10,000 pasos por día.");
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            stringDistance = decimalFormat.format((doubleLength*doubleMeasureSteps)/100);
            textStatus.setText("Has recorrido: " + stringDistance+ " metros aprox.");
            textMessage.setText("Te faltan: " + (10000-(int)doubleMeasureSteps) + " pasos");

        }

        //HEART RATE
        if (!stringHr.equals("0") && stringSteps.equals("0")) {
            getSupportActionBar().setTitle("Info Ritmo Cardiaco");
            textHRtitle.setVisibility(View.VISIBLE);
            textMeasureHr.setVisibility(View.VISIBLE);
            textMeasureHr.setText(stringHr + " bpm");
            intMeasureHr = Integer.parseInt(stringHr);
            //
            if (sp.getString("gender","No gender").equalsIgnoreCase("Male")) {
                doubleMaxHr = 203.7 / (1 + Math.exp(0.033 * (age() - 104.3)));
            }
            if (sp.getString("gender","No gender").equalsIgnoreCase("Female")) {
                doubleMaxHr = 190.2 / (1 + Math.exp(0.0453 * (age() - 107.5)));
            }
            doubleMinHr = -2.4*((double)(sp.getInt("exInt",1)))+62.8;
            //
            int MaxHr = (int) Math.round(doubleMaxHr);
            int MinHr = (int) Math.round(doubleMinHr);
            //
            textInfo.setText("De acuerdo a tu edad, tu rango normal de ritmo cardiaco es: " + MinHr + " a " + MaxHr);
            //OUT OF RANGE
            if (intMeasureHr < doubleMinHr || intMeasureHr > doubleMaxHr){
                textMessage.setText("PELIGRO!");
                textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                if (intMeasureHr < doubleMinHr){
                    textStatus.setText("TAQUICARDIA");
                }
                if (intMeasureHr > doubleMaxHr){
                    textStatus.setText("BRADICARDIA");
                }
            }
            //NORMAL RANGE
            if (intMeasureHr <= doubleMaxHr-90 && intMeasureHr >= doubleMinHr+10 ){
                textMessage.setText("NO TE PREOCUPES!");
                textMessage.setTextColor(Color.parseColor("#3CB371")); //COLOR GREEN
                textStatus.setText("VALOR NORMAL");
            }
            //BRADICARDIA
            if (intMeasureHr > doubleMinHr && intMeasureHr < doubleMinHr+10){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //YELLOW
                textStatus.setText("LIGERA BRADICARDIA");
            }
            //TAQUICARDIA
            if (intMeasureHr < doubleMaxHr && intMeasureHr > doubleMaxHr-90){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //YELLOW
                textStatus.setText("LIGERA TAQUICARDIA");
            }
        }
        //

        //BLOOD PRESSURE
        if(!stringBpmax.equals("0") && stringHr.equals("0") && stringHr.equals("0")) {
            getSupportActionBar().setTitle("Info Presión Sanguínea");
            textBPtitle.setVisibility(View.VISIBLE);
            textMeasureBp.setVisibility(View.VISIBLE);
            textMeasureBp.setText("Sistólica " + stringBpmax + " mmHg" + "\n Diastolica: " + stringBpmin + " mmHg");
            intMeasureBpmax = Integer.parseInt(stringBpmax);
            intMeasureBpmin = Integer.parseInt(stringBpmin);
            //CHECK BP SYSTOLIC VALUES
            if(20 <= age() && age()<= 24) {
                intNormalBpmax = 120;
                intNormalBpmin =79;
                textInfo.setText("De acuerdo a tu edad, tu rango normal de presión sistólica es:  "
                        + (intNormalBpmax-10) + " a " +(intNormalBpmax+10) + " y diastolica es " +
                        (intNormalBpmin-5) + " a " + (intNormalBpmin+5));
            }
            /////NORMAL VALUE//////////
            if(intMeasureBpmax >= intNormalBpmax-29 && intMeasureBpmax <= intNormalBpmax+19 &&
                    intMeasureBpmin >= intNormalBpmin-19 && intMeasureBpmin <= intNormalBpmin+9) {
                textMessage.setText("NO TE PREOCUPES!");
                textMessage.setTextColor(Color.parseColor("#3CB371")); //COLOR GREEN
                //HIGH NORMAL VALUE
                if (intMeasureBpmax > intNormalBpmax && intMeasureBpmin > intNormalBpmin){
                    textStatus.setText("ALTA PRESIÓN NORMAL");
                }
                //NORMAL VALUE
                if (intMeasureBpmax == intNormalBpmax && intMeasureBpmin == intNormalBpmin){
                    textStatus.setText("PRESIÓN NORMAL");
                }
                //LOW NORMAL VALUE
                if (intMeasureBpmax < intNormalBpmax && intMeasureBpmin < intNormalBpmin){
                    textStatus.setText("BAJA PRESIÓN NORMAL");
                }
                if (intMeasureBpmax > intNormalBpmax && intMeasureBpmin < intNormalBpmin){
                    if (intMeasureBpmax - intNormalBpmax > intNormalBpmin - intMeasureBpmin){
                        textStatus.setText("ALTA PRESIÓN NORMAL");
                    } else textStatus.setText("BAJA PRESIÓN NORMAL");

                }

            }
            //////LOW VALUE///////
            if (intMeasureBpmax <= intNormalBpmax-30 && intMeasureBpmin <= intNormalBpmin-20){
                //DANGEROUSLY LOW VALUE
                if (intMeasureBpmax <= intNormalBpmax-70 && intMeasureBpmin <= intNormalBpmin-47){
                    textMessage.setText("PELIGRO!");
                    textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                    textStatus.setText("PRESIÓN PELIGROSAMENTE BAJA");
                }
                //TOO LOW VALUE
                if (intMeasureBpmax <= intNormalBpmax-60 && intMeasureBpmax >= intNormalBpmax-69 &&
                        intMeasureBpmin <= intNormalBpmin-40 && intMeasureBpmin >= intNormalBpmin-46){
                    textMessage.setText("PELIGRO");
                    textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                    textStatus.setText("PRESIÓN MUY BAJA");
                }
                //BORDER LOW VALUE
                if (intMeasureBpmax <= intNormalBpmax-30 && intMeasureBpmax >= intNormalBpmax-59 &&
                        intMeasureBpmin <= intNormalBpmin-20 && intMeasureBpmin >= intNormalBpmin-39){
                    textMessage.setText("TOMA PRECAUCIONES!");
                    textMessage.setTextColor(Color.parseColor("#FFD700")); //COLOR YELLOW
                    textStatus.setText("LÍMITE DE PRESIÓN BAJA");
                }
            }
            //////HIGH VALUE/////
            if (intMeasureBpmax >= intNormalBpmax+20 && intMeasureBpmin >= intNormalBpmin+10){
                //STAGE 1
                if (intMeasureBpmax < intNormalBpmax+40 && intMeasureBpmax >= intNormalBpmax+20 &&
                        intMeasureBpmin <= intNormalBpmin+19 && intMeasureBpmin >= intNormalBpmin+10){
                    textMessage.setText("TOMA PRECAUCIONES!");
                    textMessage.setTextColor(Color.parseColor("#FFD700")); //YELLOW
                    textStatus.setText("HIPERTENSIÓN: NIVEL 1");
                }
                //STAGE 2
                if (intMeasureBpmax < intNormalBpmax+60 && intMeasureBpmax >= intNormalBpmax+40 &&
                        intMeasureBpmin <= intNormalBpmin+29 && intMeasureBpmin >= intNormalBpmin+20){
                    textMessage.setText("TOMA PRECAUCIONES!");
                    textMessage.setTextColor(Color.parseColor("#FFD700")); //YELLOW
                    textStatus.setText("HIPERTENSIÓN: NIVEL 2");
                }
                //STAGE 3
                if (intMeasureBpmax < intNormalBpmax+90 && intMeasureBpmax >= intNormalBpmax+60 &&
                        intMeasureBpmin <= intNormalBpmin+39 && intMeasureBpmin >= intNormalBpmin+30){
                    textMessage.setText("PELIGRO!");
                    textMessage.setTextColor(Color.parseColor("#8B0000")); //RED
                    textStatus.setText("HIPERTENSIÓN: NIVEL 3");
                }
                //STAGE 4
                if (intMeasureBpmax >= intNormalBpmax+90 && intMeasureBpmin >= intNormalBpmin+40){
                    textMessage.setText("PELIGRO!");
                    textMessage.setTextColor(Color.parseColor("#8B0000")); //RED
                    textStatus.setText("HIPERTENSIÓN: NIVEL 3");
                }
            }
            //ISOLATED VALUES
            //HIGH ISOLATED SYSTOLIC
            if (intMeasureBpmax >= intNormalBpmax+20 && intMeasureBpmax <= intNormalBpmax+39 &&
                    intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //YELLOW
                textStatus.setText("HIPERTENSIÓN SISTÓLICA AISLADA NIVEL 1");
            }
            if (intMeasureBpmax >= intNormalBpmax+40 && intMeasureBpmax <= intNormalBpmax+59 &&
                    intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //YELLOW
                textStatus.setText("HIPERTENSIÓN SISTÓLICA AISLADA NIVEL 2");
            }
            if (intMeasureBpmax >= intNormalBpmax+60 && intMeasureBpmax <= intNormalBpmax+89 &&
                    intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
                textMessage.setText("PELIGRO");
                textMessage.setTextColor(Color.parseColor("#8B0000")); //RED
                textStatus.setText("HIPERTENSIÓN SISTÓLICA AISLADA NIVEL 3");
            }
            if (intMeasureBpmax >= intNormalBpmax+90 && intMeasureBpmin>= intNormalBpmin-19 && intMeasureBpmin<= intNormalBpmin+9){
                textMessage.setText("PELIGRO!");
                textMessage.setTextColor(Color.parseColor("#8B0000")); //RED
                textStatus.setText("HIPERTENSIÓN SISTÓLICA AISLADA NIVEL 4");
            }
            //LOW ISOLATED SYSTOLIC
            if (intMeasureBpmax <= intNormalBpmax-30 && intMeasureBpmax >= intNormalBpmax-59 &&
                    intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //COLOR YELLOW
                textStatus.setText("LÍMITE DE HIPOTENSIÓN SISTÓLICA AISLADA");
            }
            if (intMeasureBpmax <= intNormalBpmax-60 && intMeasureBpmax >= intNormalBpmax-69 &&
                    intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
                textMessage.setText("PELIGRO!");
                textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                textStatus.setText("HIPOTENSIÓN SISTÓLICA AISLADA MUY BAJA");
            }
            if (intMeasureBpmax <= intNormalBpmax-70 && intMeasureBpmin <= intNormalBpmin-47 &&
                    intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
                textMessage.setText("PELIGRO!");
                textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                textStatus.setText("HIPOTENSIÓN SISTÓLICA PELIGROSA");
            }
            //

        }
        //BREATH RATE
        if(!stringBr.equals("0") && stringHr.equals("0") && stringHr.equals("0")) {
            getSupportActionBar().setTitle("Info de frec respiratoria");
            textBRtitle.setVisibility(View.VISIBLE);
            textMeasureBr.setText(stringBr + " bpm");
            textMeasureBr.setVisibility(View.VISIBLE);
            intMeasureBr = Integer.parseInt(stringBr);
            if (age() >= 18){
                intNormalBrmin = 12;
                intNormalBrmax = 20;
            }
            textInfo.setText("De acuerdo a tu edad, tu rango normal de frecuencia respiratoria es: "
                    + intNormalBrmin + " a " + intNormalBrmax );
            //HIGH NORMAL BREATH RATE
            if (intMeasureBr <= intNormalBrmax && intMeasureBr >= intNormalBrmax-1){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //COLOR YELLOW
                textStatus.setText("FRECUENCIA RESPIRATORIA ALTA NORMAL");
            }
            //NORMAL BREATH RATE
            if (intMeasureBr <= intNormalBrmax-2 && intMeasureBr >= intNormalBrmin+2){
                textMessage.setText("NO TE PREOCUPES!");
                textMessage.setTextColor(Color.parseColor("#3CB371")); //COLOR GREEN
                textStatus.setText("FRECUENCIA RESPIRATORIA NORMAL");
            }
            //LOW NORMAL BREATH RATE
            if (intMeasureBr <= intNormalBrmin+1 && intMeasureBr >= intNormalBrmin){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //COLOR YELLOW
                textStatus.setText("FRECUENCIA RESPIRATORIA BAJA NORMAL");
            }
            //TACHYPNEA
            if (intMeasureBr > intNormalBrmax && intMeasureBr <= intNormalBrmax+5) {
                textMessage.setText("PELIGRO!");
                textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                textStatus.setText("TAQUIPNEA LIGERA");
            }
            if (intMeasureBr >= intNormalBrmax+6){
                textMessage.setText("PELIGRO!");
                textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                textStatus.setText("TAQUIPNEA SEVERA");
            }
            //BRADIPNEA
            if (intMeasureBr < intNormalBrmin && intMeasureBr >= intNormalBrmin-5){
                textMessage.setText("TOMA PRECAUCIONES!");
                textMessage.setTextColor(Color.parseColor("#FFD700")); //COLOR YELLOW
                textStatus.setText("BRADIPNEA LIGERA");
            }
            if (intMeasureBr <= intNormalBrmin-6){
                textMessage.setText("PELIGRO!");
                textMessage.setTextColor(Color.parseColor("#8B0000"));  //COLOR RED
                textStatus.setText("BRADIPNEA SEVERA");
            }
            //
        }
        //MOOD
        if(!stringMood.equals("0") && MFdecision == 1 && stringHr.equals("0") && stringHr.equals("0")) {
            getSupportActionBar().setTitle("Info de humor");
            textMoodTitle.setVisibility(View.VISIBLE);
            textMeasureMood.setText(stringMood);
            textMeasureMood.setVisibility(View.VISIBLE);
        }
        //FATIGUE
        if(!stringFatigue.equals("0") && MFdecision == 2 && stringHr.equals("0") && stringHr.equals("0")) {
            getSupportActionBar().setTitle("Info de fatiga");
            textFatigueTitle.setVisibility(View.VISIBLE);
            textMeasureFatigue.setText(stringFatigue);
            textMeasureFatigue.setVisibility(View.VISIBLE);
        }

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
        String stringBirth = sp.getString("birth","Birth");
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

    //MENU 3 DOTS
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_popup,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.moreInfo:
                //startActivity(new Intent(PopUp.this, HealthcareInfo.class));
                return true;

            case R.id.sendData:
                //ENVIAR ANALISIS : STATUS
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //END MENU 3 DOTS

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor spPopupEditor = spPopup.edit();
        spPopupEditor.putString("HR","0");
        spPopupEditor.putString("Steps","0");
        spPopupEditor.apply();
    }




}
