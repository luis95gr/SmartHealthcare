package com.example.luisguzmn.healthcare40;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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
import java.util.concurrent.ExecutionException;

public class Statistics extends AppCompatActivity {

    JSONArray jsonArray;
    private String a;
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> mylistDates = new ArrayList<String>();
    float[] floatValues;
    private LineChart mChart;
    String var,date;
    TextView minValue,maxValue,avgValue;
    Button buttonS;
    Button btnBR,btnHR,btnBPmax,btnBPmin,btnMood,btnFatigue;
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);

        //Dias
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);

        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Estadisticas");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this).withCompactStyle(true)
                .addProfiles(
                        new ProfileDrawerItem().withName(sp.getString("name","no name")).
                                withEmail(sp.getString("email","no email")).withIcon("http://meddata.sytes.net/newuser/profileImg/"
                                +sp.getString("imagen", "No Image")))
                .withSelectionListEnabledForSingleProfile(false).withHeaderBackground(R.drawable.header2)
                .build();


        //if you want to update the items at a later time it is recommended to keep it in a variable
        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_main).withIcon(GoogleMaterial.Icon.gmd_accessibility);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profile).withIcon(GoogleMaterial.Icon.gmd_account_balance);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_records).withIcon(GoogleMaterial.Icon.gmd_add_to_photos);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_statistics).withIcon(GoogleMaterial.Icon.gmd_adb);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_item_healt).withIcon(GoogleMaterial.Icon.gmd_attach_file);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.drawer_item_settinds).withIcon(GoogleMaterial.Icon.gmd_bluetooth);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName(R.string.drawer_item_about).withIcon(GoogleMaterial.Icon.gmd_terrain);

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
                            Intent intent = new Intent(Statistics.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 3) {
                            Intent intent = new Intent(Statistics.this, Registros.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 4){
                            Intent intent = new Intent(Statistics.this, Statistics.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }

                        return false;
                    }
                })
                .build();


        //-------------------------------------------------------------------------------------------
        //MENU

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


        mChart = (LineChart) findViewById(R.id.chart);
        minValue = (TextView)findViewById(R.id.min_value);
        maxValue = (TextView)findViewById(R.id.max_value);
        avgValue = (TextView)findViewById(R.id.avg_value);
        buttonS = (Button)findViewById(R.id.buttonShow);
        Button btnBR,btnHR,btnBPmax,btnBPmin,btnMood,btnFatigue;

        btnBR = (Button)findViewById(R.id.btnHR);
        btnHR = (Button)findViewById(R.id.btnHR);
        btnBPmax = (Button)findViewById(R.id.btnBPmax);
        btnBPmin = (Button)findViewById(R.id.btnBPmin);
        btnMood = (Button)findViewById(R.id.btnMood);

        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(false);
        mChart.setNoDataText("Presiona para ver el grafico");
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.setBorderWidth(1);
        mChart.getXAxis().setLabelRotationAngle(90);
        mChart.getXAxis().setTextSize(8f);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                float y = e.getY();
                Toast.makeText(getApplicationContext(),String.valueOf(y), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });



        //Spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.stringVar, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


      /*  dataVar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String pvar,pvar2;
                if (position == 0) {
                    pvar="";
                    mChart.clear();
                    minValue.setText("");
                    maxValue.setText("");
                    avgValue.setText("");
                }if (position == 1) {
                    pvar="BR";
                    mylistDates = new ArrayList<String>();
                    mChart.clear();
                    mylist.clear();
                    VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email=emilio.perez@udem.edu&var="+pvar);
                }if (position == 2) {
                    pvar="HR";
                    mylistDates = new ArrayList<String>();
                    mChart.clear();
                    mylist.clear();
                    VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email=emilio.perez@udem.edu&var="+pvar);
                }
                if (position == 3) {
                    pvar="BPmax";
                    mylistDates = new ArrayList<String>();
                    mChart.clear();
                    mylist.clear();
                    VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email=emilio.perez@udem.edu&var="+pvar);
                }
                if (position == 4) {
                    pvar="BPmin";
                    mylistDates = new ArrayList<String>();
                    mylist.clear();
                    VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email=emilio.perez@udem.edu&var="+pvar);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

       */// });

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
                    String value,date;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        a = jsonArray.getString(i);
                        JSONObject jsonObj = new JSONObject(a);
                        value = jsonObj.getString("value");
                        date = jsonObj.getString("date");
                        var = jsonObj.getString("var");
                        mylist.add(value);
                        mylistDates.add(date);
                    }
                    floatValues = new float[mylist.size()];
                    for (int i = 0; i < mylist.size(); i++) {
                        floatValues[i] = Float.parseFloat(mylist.get(i));
                    }

                    minValue.setText(String.valueOf(getMinValue(floatValues)));
                    maxValue.setText(String.valueOf(getMaxValue(floatValues)));
                    avgValue.setText(String.valueOf((getMaxValue(floatValues)+getMinValue(floatValues))/2));
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




    private void setData(){
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





    // getting the maximum value
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


}