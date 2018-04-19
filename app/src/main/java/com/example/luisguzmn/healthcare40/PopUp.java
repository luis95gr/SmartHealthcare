package com.example.luisguzmn.healthcare40;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;

public class PopUp extends AppCompatActivity {

    //VARIABLES
    SharedPreferences sp,spPopup;
    TextView textName,textGender,textWeight,textAge,textHeight;
    TextView textBRtitle,textBPtitle,textMoodTitle, textFatigueTitle;
    int intWeight,intHeight;
    //
    TextView textEAge,textEGender,textEWeight,textEHeight;
    //
    String stringBpmax, stringBpmin, stringBr, stringMood, stringFatigue;
    int width;
    int height;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);
        //STRUCTURE
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int)(width*.7),(int)(height*.5));
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
        //
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
        //
        //
        Typeface font2 = Typeface.createFromAsset(getAssets(), "Hanken-Light.ttf");
        textBRtitle.setTypeface(font2);
        textFatigueTitle.setTypeface(font2);
        textMoodTitle.setTypeface(font2);
        textBPtitle.setTypeface(font2);
        //
        textName.setText(sp.getString("name","No name"));
        textGender.setText(sp.getString("gender","No gender"));
        intHeight = sp.getInt("height",0);
        intWeight = sp.getInt("weight",0);
        textHeight.setText(intHeight + " cm");
        textWeight.setText(intWeight + " kg");
        //
        //RECEIVE DATA
        stringBpmax = spPopup.getString("BPmax","0");
        stringBpmin = spPopup.getString("BPmin","0");
        stringBr = spPopup.getString("BR","0");
        stringMood = spPopup.getString("Mood","0");
        stringFatigue = spPopup.getString("Fatigue","0");

        textBRtitle.setVisibility(View.VISIBLE);
        age();
        textBRtitle.setText(age());

        if(stringBpmax!="0") {
            getSupportActionBar().setTitle("Blood Pressure Info");
        }

        if(stringBr!="0") {
            getSupportActionBar().setTitle("Breath Rate Info");
            textBRtitle.setVisibility(View.VISIBLE);

        }

        if(stringMood!="0") {
            getSupportActionBar().setTitle("Mood Info");
        }

        if(stringFatigue!="0") {
            getSupportActionBar().setTitle("Fatigue Info");
        }
        //DELETE
        /*SharedPreferences.Editor spPopupEditor = spPopup.edit();
        spPopupEditor.clear();
        spPopupEditor.apply();*/
        //



    }

    private String age() {
        //YEAR,MONTH,DAY
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        String stringYear = sdfDate.format(date);
        sdfDate = new SimpleDateFormat("MM");
        String stringMonth = sdfDate.format(date);
        sdfDate = new SimpleDateFormat("dd");
        String stringDay = sdfDate.format(date);
        //
        //BIRTH
        String stringBirth = sp.getString("birth","Birth");
        StringTokenizer tokenBirth = new StringTokenizer(stringBirth, "-");
        //TOKENS
        String tokenYear = tokenBirth.nextToken();
        String tokenMonth = tokenBirth.nextToken();
        String tokenDay = tokenBirth.nextToken();
        //
        return tokenDay;
    }

}
