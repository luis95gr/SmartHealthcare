package com.example.luisguzmn.healthcare40;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisguzmn.healthcare40.HealthcareInfo.MenuHinfo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Statistics extends AppCompatActivity {

    JSONArray jsonArray;
    private String a,time;
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> mylistDates = new ArrayList<String>();
    ArrayList<String> mylistVar = new ArrayList<String>();
    ArrayList<String> mylistTime = new ArrayList<String>();

    float[] floatValues;
    private LineChart mChart;
    String var,date;
    TextView minValue,maxValue,avgValue;
    Button btnBR,btnHR,btnBPmax,btnBPmin,btnMood,btnFatigue;
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;
    final Handler handler = new Handler();
    int vCalm,vTired,vNormal;


    private PieChart pChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        menu();
        dias();
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);

        pChart = (PieChart)findViewById(R.id.piechart);
        mChart = (LineChart)findViewById(R.id.linechart);

        pChart.setVisibility(View.INVISIBLE);
        mChart.setVisibility(View.INVISIBLE);



        //Dias
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);




        //mChart = (LineChart) findViewById(R.id.chart);
        minValue = (TextView)findViewById(R.id.min_value);
        maxValue = (TextView)findViewById(R.id.max_value);
        avgValue = (TextView)findViewById(R.id.avg_value);

        btnBR = (Button)findViewById(R.id.btnBR);
        btnHR = (Button)findViewById(R.id.btnHR);
        btnBPmax = (Button)findViewById(R.id.btnBPmax);
        btnBPmin = (Button)findViewById(R.id.btnBPmin);
        btnMood = (Button)findViewById(R.id.btnMood);
        btnFatigue = (Button)findViewById(R.id.btnFatigue);





     mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

                float x = e.getX();
                float y = e.getY();
                Toast.makeText(getApplicationContext(),"Valor: "+String.valueOf(y)+"\nFecha: "
                        +x, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });




        btnBR.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
            String email = sp.getString("email","no email");
            String var = "BR";
            @Override
            public void onClick(View v) {
                btnBR.getBackground().setColorFilter(Color.parseColor("#58ACFA"), PorterDuff.Mode.SRC_ATOP);
                btnHR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmax.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmin.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnFatigue.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnMood.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);


                pChart.setVisibility(View.INVISIBLE);
                    mChart.setVisibility(View.VISIBLE);

                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mChart.clear();
                mylistDates.clear();
                mylistVar.clear();
                mylistTime.clear();
                mylist.clear();
                floatValues = new float[mylist.size()];
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+var);
            }
        });

         btnHR.setOnClickListener(new View.OnClickListener() {
             SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
             String email = sp.getString("email","no email");
             String var = "HR";
        @Override
        public void onClick(View v) {
            btnBR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
            btnHR.getBackground().setColorFilter(Color.parseColor("#58ACFA"), PorterDuff.Mode.SRC_ATOP);
            btnBPmax.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
            btnBPmin.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
            btnFatigue.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
            btnMood.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);

            pChart.setVisibility(View.INVISIBLE);
                mChart.setVisibility(View.VISIBLE);
                mChart.clear();

          mylist = new ArrayList<String>();
           mylistDates = new ArrayList<String>();
          mylistVar = new ArrayList<String>();
           mylistTime = new ArrayList<String>();
            mylistDates.clear();
            mylistVar.clear();
            mylistTime.clear();
            mylist.clear();
            floatValues = new float[mylist.size()];
            VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+var);
            }
    });

        btnBPmax.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
            String email = sp.getString("email","no email");
            String var = "BPmax";
            @Override
            public void onClick(View v) {
                btnBR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnHR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmax.getBackground().setColorFilter(Color.parseColor("#58ACFA"), PorterDuff.Mode.SRC_ATOP);
                btnBPmin.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnFatigue.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnMood.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);

                    pChart.setVisibility(View.INVISIBLE);
                    mChart.setVisibility(View.VISIBLE);
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mChart.clear();
                mylistDates.clear();
                mylistVar.clear();
                mylistTime.clear();
                mylist.clear();
                floatValues = new float[mylist.size()];
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+var);
                }
        });

        btnBPmin.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
            String email = sp.getString("email","no email");
            String var = "BPmin";
            @Override
            public void onClick(View v) {
                btnBR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnHR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmax.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmin.getBackground().setColorFilter(Color.parseColor("#58ACFA"), PorterDuff.Mode.SRC_ATOP);
                btnFatigue.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnMood.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);

                    pChart.setVisibility(View.INVISIBLE);
                    mChart.setVisibility(View.VISIBLE);
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mChart.clear();
                mylistDates.clear();
                mylistVar.clear();
                mylistTime.clear();
                mylist.clear();
                floatValues = new float[mylist.size()];
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+var);
            }
        });

        btnFatigue.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
            String email = sp.getString("email","no email");
            String var = "Fatigue";
            @Override
            public void onClick(View v) {
                btnBR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnHR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmax.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmin.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnFatigue.getBackground().setColorFilter(Color.parseColor("#58ACFA"), PorterDuff.Mode.SRC_ATOP);
                btnMood.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);

                    pChart.setVisibility(View.VISIBLE);
                    mChart.setVisibility(View.INVISIBLE);
                    pChart.clear();
                mChart.clear();
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistDates.clear();
                mylistVar.clear();
                mylistTime.clear();
                mylist.clear();
                floatValues = new float[mylist.size()];
                VolleyPetitionFatigue("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+var);
            }
        });

        btnMood.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
            String email = sp.getString("email","no email");
            String var = "mood";
            @Override
            public void onClick(View v) {
                btnBR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnHR.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmax.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnBPmin.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnFatigue.getBackground().setColorFilter(Color.parseColor("#A4A4A4"), PorterDuff.Mode.SRC_ATOP);
                btnMood.getBackground().setColorFilter(Color.parseColor("#58ACFA"), PorterDuff.Mode.SRC_ATOP);

                pChart.setVisibility(View.VISIBLE);
                mChart.setVisibility(View.INVISIBLE);
                pChart.clear();
                mChart.clear();
                mylist = new ArrayList<String>();
                mylistDates = new ArrayList<String>();
                mylistVar = new ArrayList<String>();
                mylistTime = new ArrayList<String>();
                mylistDates.clear();
                mylistVar.clear();
                mylistTime.clear();
                mylist.clear();
                floatValues = new float[mylist.size()];
                VolleyPetitionMood("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+var);
            }
        });
}
    private void internet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            setData();
        } else {
        }
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
                        setData();

                        minValue.setText("Min: "+String.valueOf(getMinValue(floatValues)));
                        maxValue.setText("Max: "+String.valueOf(getMaxValue(floatValues)));
                        avgValue.setText("Prom: "+String.valueOf((getMaxValue(floatValues)+getMinValue(floatValues))/2));
                    }
                    for(int i = 0; i<mylist.size();i++) {

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
    public void VolleyPetitionFatigue(String URL) {
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
                    for(int i = 0; i<mylist.size();i++) {
                        if(mylist.get(i).equals("Calm")){
                            vCalm++;
                        } else if(mylist.get(i).equals("Tired")){
                            vTired++;
                        } else if(mylist.get(i).equals("Normal")){
                            vNormal++;
                        }
                    }

                    minValue.setText("Calm: "+ String.valueOf(vCalm));
                    maxValue.setText("Tired: "+ String.valueOf(vTired));
                    avgValue.setText("Normal: "+ String.valueOf(vNormal));
                    PsetDataFatiga();
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
    public void VolleyPetitionMood(String URL) {
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
                    for(int i = 0; i<mylist.size();i++) {
                        if(mylist.get(i).equals("Calm")){
                            vCalm++;
                        } else if(mylist.get(i).equals("Depression")){
                            vTired++;
                        } else if(mylist.get(i).equals("Normal")){
                            vNormal++;
                        }
                    }
                    minValue.setText("Calm: "+ String.valueOf(vCalm));
                    maxValue.setText("Depression: "+ String.valueOf(vTired));
                    avgValue.setText("Normal: "+ String.valueOf(vNormal));
                    PsetDataMood();
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


    public static float getMaxValue(float[] array) {
        float maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }
    public static float getMinValue(float[] array) {
        float minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }
    private void PsetDataFatiga() {


        pChart.setUsePercentValues(true);
        pChart.getDescription().setEnabled(false);
        pChart.setExtraOffsets(0, 0, 0, 0);
        pChart.setDragDecelerationFrictionCoef(.9f);
        pChart.setRotationAngle(0);
        pChart.setRotationEnabled(true);
        pChart.setHighlightPerTapEnabled(true);
        pChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        pChart.setDrawEntryLabels(true);
        pChart.setEntryLabelColor(Color.WHITE);
        pChart.setEntryLabelTextSize(10f);

        pChart.setDrawHoleEnabled(true);
        pChart.setHoleRadius(28f);
        pChart.setTransparentCircleRadius(31f);
        pChart.setTransparentCircleColor(Color.BLACK);
        pChart.setTransparentCircleAlpha(50);
        pChart.setHoleColor(Color.WHITE);
        pChart.setDrawCenterText(true);
        pChart.setCenterText("Fatiga");
        pChart.setCenterTextSize(12f);
        pChart.setCenterTextColor(Color.RED);

        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.parseColor("#58ACFA"));
        colors.add(Color.parseColor("#01DFD7"));
        colors.add(Color.parseColor("#31B404"));

        PieEntry CashBalance = new PieEntry(vCalm, "Calm");
        PieEntry ConsumptionBalance = new PieEntry(vNormal, "Normal");
        PieEntry Country = new PieEntry(vTired,"Tired");
        pieEntryList.add(CashBalance);
        pieEntryList.add(ConsumptionBalance);
        pieEntryList.add(Country);

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(10f);
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.BLUE);
        pieData.setValueTextSize(12f);
        pieData.setValueFormatter(new PercentFormatter());
        pChart.setData(pieData);
        pChart.highlightValues(null);
        //
        pChart.invalidate();
    }
    private void PsetDataMood() {


        pChart.setUsePercentValues(true);
        pChart.getDescription().setEnabled(false);
        pChart.setExtraOffsets(0, 0, 0, 0);
        pChart.setDragDecelerationFrictionCoef(.9f);
        pChart.setRotationAngle(0);
        pChart.setRotationEnabled(true);
        pChart.setHighlightPerTapEnabled(true);
        pChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        pChart.setDrawEntryLabels(true);
        pChart.setEntryLabelColor(Color.WHITE);
        pChart.setEntryLabelTextSize(12f);

        pChart.setDrawHoleEnabled(true);
        pChart.setHoleRadius(28f);
        pChart.setTransparentCircleRadius(31f);
        pChart.setTransparentCircleColor(Color.BLACK);
        pChart.setTransparentCircleAlpha(50);
        pChart.setHoleColor(Color.WHITE);
        pChart.setDrawCenterText(true);
        pChart.setCenterText("Mood");
        pChart.setCenterTextSize(10f);
        pChart.setCenterTextColor(Color.RED);

        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#58ACFA"));
        colors.add(Color.parseColor("#01DFD7"));
        colors.add(Color.parseColor("#31B404"));

        PieEntry CashBalance = new PieEntry(vCalm, "Calm");
        PieEntry ConsumptionBalance = new PieEntry(vNormal, "Normal");
        PieEntry Country = new PieEntry(vTired,"Depression");
        pieEntryList.add(CashBalance);
        pieEntryList.add(ConsumptionBalance);
        pieEntryList.add(Country);

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(10f);
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.BLUE);
        pieData.setValueTextSize(12f);
        pieData.setValueFormatter(new PercentFormatter());
        pChart.setData(pieData);
        pChart.highlightValues(null);
        pChart.invalidate();
    }

    private void setData(){


        mChart.setTouchEnabled(true);
        mChart.animateY(1000, Easing.EasingOption.Linear);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.setBorderWidth(1);
        mChart.getXAxis().setLabelRotationAngle(90);
        mChart.getXAxis().setTextSize(8f);
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < floatValues.length; i++) {
            yVals.add(new Entry(i,floatValues[i]));
        }

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mylistDates.get((int) value);
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(6, true);
        leftAxis.setAxisMinValue(getMinValue(floatValues)-1);
        leftAxis.setAxisMaxValue(getMaxValue(floatValues)+1);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(0.001f);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        LineDataSet set1;
// create a dataset and give it a type
        set1 = new LineDataSet(yVals,var);
        set1.setFillAlpha(500);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setDrawCircleHole(false);
        set1.setCircleRadius(5f);
        set1.setDrawValues(true);
        set1.setValueTextSize(6f);
        set1.setColor(Color.RED);
        set1.setHighLightColor(Color.GRAY);
        set1.setHighlightLineWidth(1.5f);
        set1.setDrawFilled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets = new ArrayList<>();

        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.getXAxis().setLabelCount(floatValues.length,true);
    }
    private void menu(){
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Estadisticas");
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
                        if (position == 1) {
                            Intent intent = new Intent(Statistics.this, MainScreen.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 2) {
                            Intent intent = new Intent(Statistics.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 3) {
                            Intent intent = new Intent(Statistics.this, Registros.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 5){
                            Intent intent = new Intent(Statistics.this, MenuHinfo.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 7){
                            Intent intent = new Intent(Statistics.this, configuracion.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 8){
                            Intent intent = new Intent(Statistics.this, AboutUs.class);
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
                startActivity(new Intent(Statistics.this,configuracion.class));
                return true;

            case R.id.logout:
                SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences spDataNivel = PreferenceManager.getDefaultSharedPreferences(this);
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
                startActivity(new Intent(Statistics.this,MainActivity.class));
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
