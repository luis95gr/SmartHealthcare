package com.example.luisguzmn.healthcare40;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Statistics extends MenuToolbar{//MenuToolbar extends AppCompatActivity {


    //DAYS VARIABLES
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;
    //
    //VARIABLES
    GraphView graphViewBP;
    TextView textView;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        //Set up Menu Drawer
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarMain);
//        MenuToolbar menu = new MenuToolbar(toolbar,"Main",this);
        this.setToolbar(toolbar);
        this.setContext(this);
        this.setTitleToolbar("Estadístics");
        this.loadToolbar();



        //CAST DAYS
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
        //
        dias();
        //CAST
        graphViewBP = (GraphView)findViewById(R.id.graphBP);
        textView = (TextView)findViewById(R.id.textView78);
        //
        //
        //BP GRAPH
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d7 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d8 = calendar.getTime();


        Date todays = Calendar.getInstance().getTime();
        SimpleDateFormat formatters = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String currentDateTimeStrin = formatters.format(todays);

        textView.setText(currentDateTimeStrin);


        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 2),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 4),
                new DataPoint(d5, 6),
                new DataPoint(d6, 3),
                new DataPoint(d7, 8),
                new DataPoint(d8, 3),
                new DataPoint(d3, 8),
                new DataPoint(d3, 3),
                new DataPoint(d8, 7),

        });
        series.setColor(Color.DKGRAY);
        graphViewBP.addSeries(series);
        //
        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 8),
                new DataPoint(d2, 9),
                new DataPoint(d3, 10),
                new DataPoint(d4, 4),
                new DataPoint(d5, 6),
                new DataPoint(d6, 3),
                new DataPoint(d3, 8),
                new DataPoint(d3, 3),
                new DataPoint(d3, 8),
                new DataPoint(d3, 3),
                new DataPoint(d3, 7),

        });


        /*graphViewBP.getSecondScale().addSeries(series2);
        graphViewBP.getSecondScale().setMinY(0);
        graphViewBP.getSecondScale().setMaxY(10);*/

        series.setTitle("BP Max");
        series2.setTitle("BP Min");
        graphViewBP.getLegendRenderer().setVisible(true);
        graphViewBP.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        //

        //PRUEBA
        final SharedPreferences spMeasuresSaved = PreferenceManager.getDefaultSharedPreferences(this);

        //
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Statistics.this);
                //Toast.makeText(Statistics.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_LONG).show();
                //
                Intent intent = new Intent(Statistics.this, PopUp.class);
                startActivity(intent);



                /*for (int i=1; i<=3;i++){
                    stringSet[i] = spMeasuresSaved.getString("stringSet"+i,"0");
                    Toast.makeText(Statistics.this,  stringSet[i], Toast.LENGTH_SHORT).show();
                }*/
                //startActivity(new Intent(Statistics.this, PopUp.class));
            }
        });

        // set date label formatter
        graphViewBP.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graphViewBP.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graphViewBP.getViewport().setScrollable(true);
       // set manual x bounds to have nice steps
        graphViewBP.getViewport().setXAxisBoundsManual(true);
        graphViewBP.getViewport().setMinX(d1.getTime());
        graphViewBP.getViewport().setMaxX(d3.getTime());
        // set manual y bounds to have nice steps
        graphViewBP.getViewport().setYAxisBoundsManual(true);
        graphViewBP.getViewport().setMinY(0);
        graphViewBP.getViewport().setMaxY(10);
        graphViewBP.getGridLabelRenderer().setHumanRounding(false);


        //graphViewBP.getViewport().setScrollable(true); // enables horizontal scrolling
        //graphViewBP.getViewport().setScrollableY(true); // enables vertical scrolling
        //graphViewBP.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        //graphViewBP.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        //graphViewBP.getViewport().scrollToEnd();
    }





























    private void dias(){
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

    public void ShowDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("INTERNET CONNECTION");
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
    protected boolean internetCheck() {
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
        if(internetCheck()){
            //SEND DATA
        }else {
            ShowDialog();
        }
    }

    protected boolean typeConnection(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI) return true;
        else return false;
    }

}
