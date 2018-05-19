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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import android.widget.SeekBar;
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
import com.appyvet.materialrangebar.RangeBar;
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
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class PantallaLozoya extends AppCompatActivity {

    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;
    String email,edad;
    float percent;
    SeekBar seekBar;
    TextView textValue,textSV;
    FloatingActionButton btnRefresh;
    SharedPreferences sp;



    //Fatiga
    JSONArray jsonArrayF;
    private String f,timeF,varF,dateF,valueF;
    ArrayList<String> mylistF = new ArrayList<String>();
    ArrayList<String> mylistFP = new ArrayList<String>();
    ArrayList<String> mylistDatesF = new ArrayList<String>();
    ArrayList<String> mylistVarF = new ArrayList<String>();
    ArrayList<String> mylistTimeF = new ArrayList<String>();
    int vCalmF,vTiredF,vNormalF;

    //Mood
    JSONArray jsonArrayM;
    private String m,timeM,varM,dateM,valueM;
    ArrayList<String> mylistM = new ArrayList<String>();
    ArrayList<String> mylistP = new ArrayList<String>();
    ArrayList<String> mylistDatesM = new ArrayList<String>();
    ArrayList<String> mylistVarM = new ArrayList<String>();
    ArrayList<String> mylistTimeM = new ArrayList<String>();
    int vCalmM,vTiredM,vNormalM;

    //HR
    JSONArray jsonArrayHR;
    private String hr,timeHR,varHR,dateHR,valueHR;
    ArrayList<String> mylistHR = new ArrayList<String>();
    ArrayList<String> mylistHRP = new ArrayList<String>();
    ArrayList<String> mylistDatesHR = new ArrayList<String>();
    ArrayList<String> mylistVarHR = new ArrayList<String>();
    ArrayList<String> mylistTimeHR = new ArrayList<String>();
    int ValorN,Bradi,Taqui,LigTaqui,LigBradi;

    //BR
    JSONArray jsonArrayBR;
    private String br,timeBR,varBR,dateBR,valueBR;
    ArrayList<String> mylistBR = new ArrayList<String>();
    ArrayList<String> mylistBRP = new ArrayList<String>();
    ArrayList<String> mylistDatesBR = new ArrayList<String>();
    ArrayList<String> mylistVarBR = new ArrayList<String>();
    ArrayList<String> mylistTimeBR = new ArrayList<String>();
    int frecN,frecAN,frecBN,taquiL,taquiS,bradiL,bradiS;

    //BP
    JSONArray jsonArrayBP;
    private String bp,timeBP,varBP,dateBP,valueBP;
    ArrayList<String> mylistBP = new ArrayList<String>();
    ArrayList<String> mylistBPP = new ArrayList<String>();
    ArrayList<String> mylistDatesBP = new ArrayList<String>();
    ArrayList<String> mylistVarBP = new ArrayList<String>();
    ArrayList<String> mylistTimeBP = new ArrayList<String>();
    int APN,PN,BPN,PPB,PMB,LPB,HSA1,HSA2,HSA3,HSA4,LHSA,HSAMB,HSP,HN1,HN2,HN3;


    private PieChart pChartBP,pChartHR,pChartBR;
    TableView<String[]> tableViewF,tableViewM;
    private static final String[] TABLE_HEADERS = {"Valor", "Fecha", "Hora" };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_lozoya);

        menu();
        dias();

        sp= getSharedPreferences("login", MODE_PRIVATE);


        Toast.makeText(getApplicationContext(),String.valueOf("edad: "+age()),Toast.LENGTH_SHORT).show();

        email = sp.getString("email","no email");
        edad = sp.getString("birth","birth");

        seekBar = (SeekBar)findViewById(R.id.seek);
        btnRefresh = (FloatingActionButton) findViewById(R.id.btnSend);
        pChartBP = (PieChart)findViewById(R.id.piechart);
        pChartHR = (PieChart)findViewById(R.id.piechart3);
        pChartBR = (PieChart)findViewById(R.id.piechart4);



        textValue = (TextView)findViewById(R.id.textValue);
        textSV = (TextView)findViewById(R.id.textSV);

        tableViewF = (TableView) findViewById(R.id.tableView);
        tableViewM = (TableView) findViewById(R.id.tableView2);


        percent=1;
        tableViewF.setColumnCount(3);
        tableViewM.setColumnCount(3);

        tableViewF.setVerticalScrollBarEnabled(true);
        tableViewF.setHorizontalScrollBarEnabled(true);
        tableViewM.setVerticalScrollBarEnabled(true);
        tableViewM.setHorizontalScrollBarEnabled(true);

        tableViewF.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                Toast.makeText(getApplicationContext(),"Valor: "+clickedData[0]+"\nFecha: "+clickedData[1]+"\nHora: "+clickedData[2],
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),edad, Toast.LENGTH_LONG).show();

            }
        });

        tableViewM.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                Toast.makeText(getApplicationContext(),"Valor: "+clickedData[0]+"\nFecha: "+clickedData[1]+"\nHora: "+clickedData[2],
                        Toast.LENGTH_LONG).show();
            }
        });


        TableColumnWeightModel columnModel = new TableColumnWeightModel(3);
        columnModel.setColumnWeight(0, 1);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 1);
        tableViewF.setColumnModel(columnModel);
        tableViewM.setColumnModel(columnModel);

        tableViewF.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        tableViewM.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percent = (float)progress/100;
                textValue.setText(percent*100+" %");
                btnRefresh.setVisibility(View.VISIBLE);
            }
//
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                Toast.makeText(getApplicationContext(),"Datos Actualizados",Toast.LENGTH_SHORT).show();
                pChartHR.clear();
                pChartBP.clear();
                pChartBR.clear();
                VolleyPetitionFatigue("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=Fatigue");
                VolleyPetitionMood("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=mood");
                VolleyPetitionHR("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=HR");
                VolleyPetitionBR("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=BR");
                VolleyPetitionBP("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=BP");

            }
        });



        //Dias
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);


        VolleyPetitionFatigue("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=Fatigue");
        VolleyPetitionMood("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=mood");
        VolleyPetitionHR("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=HR");
        VolleyPetitionBR("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=BR");
        VolleyPetitionBP("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var=BP");

        pChartBR.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                pe.getLabel();
                Toast.makeText(getApplicationContext(),pe.getLabel(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });
        pChartBP.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                pe.getLabel();
                Toast.makeText(getApplicationContext(),pe.getLabel(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });
        pChartHR.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                pe.getLabel();
                Toast.makeText(getApplicationContext(),pe.getLabel(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });


    }
    public void VolleyPetitionFatigue(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> mylistF = new ArrayList<String>();
                    ArrayList<String> mylistFP = new ArrayList<String>();
                    ArrayList<String> mylistDatesF = new ArrayList<String>();
                    ArrayList<String> mylistVarF = new ArrayList<String>();
                    ArrayList<String> mylistTimeF = new ArrayList<String>();
                    jsonArrayF = new JSONArray(response);
                    for (int i = 0; i < jsonArrayF.length(); i++) {
                        f = jsonArrayF.getString(i);
                        JSONObject jsonObj = new JSONObject(f);
                        valueF = jsonObj.getString("value");
                        dateF = jsonObj.getString("date");
                        timeF = jsonObj.getString("time");
                        varF = jsonObj.getString("var");
                        mylistF.add(valueF);
                        mylistDatesF.add(dateF);
                        mylistTimeF.add(timeF);
                        mylistVarF.add(varF);
                    }
                    for(int i = 0; i<((int)(mylistF.size()*percent));i++) {
                        mylistFP.add(mylistF.get(i));
                    }
                    for(int i = 0; i<mylistFP.size();i++) {
                        if(mylistFP.get(i).equals("Calm")){
                            vCalmF++;
                        } else if(mylistFP.get(i).equals("Tired")){
                            vTiredF++;
                        } else if(mylistFP.get(i).equals("Normal")){
                            vNormalF++;
                        }
                    }
                    String[][] DATA_TO_SHOW = new String[mylistFP.size()][4];

                    for (int i = 0; i < mylistFP.size(); i++) {
                        DATA_TO_SHOW[i][0] = mylistFP.get(i).toString();
                        DATA_TO_SHOW[i][1] = mylistDatesF.get(i).toString();
                        DATA_TO_SHOW[i][2] = mylistTimeF.get(i).toString();
                    }


                    tableViewF.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), DATA_TO_SHOW));

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
    public void VolleyPetitionHR(String URL) {
        Log.i("url", "" + URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> mylistHR = new ArrayList<String>();
                    ArrayList<String> mylistHRP = new ArrayList<String>();
                    ArrayList<String> mylistDatesHR = new ArrayList<String>();
                    ArrayList<String> mylistVarHR = new ArrayList<String>();
                    ArrayList<String> mylistTimeHR = new ArrayList<String>();
                    jsonArrayHR   = new JSONArray(response);
                    for (int i = 0; i < jsonArrayHR.length(); i++) {
                        hr = jsonArrayHR.getString(i);
                        JSONObject jsonObj = new JSONObject(hr);
                        valueHR = jsonObj.getString("diagnostic");
                        dateHR = jsonObj.getString("date");
                        timeHR = jsonObj.getString("time");
                        varHR = jsonObj.getString("var");
                        mylistHR.add(valueHR);
                        mylistDatesHR.add(dateHR);
                        mylistTimeHR.add(timeHR);
                        mylistVarHR.add(varHR);
                    }
                    for(int i = 0; i<((int)(mylistHR.size()*percent));i++) {
                        mylistHRP.add(mylistHR.get(i));
                    }
                    for(int i = 0; i<mylistHRP.size();i++) {
                        if(mylistHRP.get(i).equals("VALOR NORMAL")){
                            ValorN++;
                        } else if(mylistHRP.get(i).equals("BRADICARDIA")){
                            Bradi++;
                        } else if(mylistHRP.get(i).equals("TAQUICARDIA")){
                            Taqui++;
                        } else if(mylistHRP.get(i).equals("LIGERA TAQUICARDIA")){
                            LigTaqui++;
                        } else if(mylistHRP.get(i).equals("LIGERA BRADICARDIA")){
                            LigBradi++;
                        }
                    }
                    PsetDataHR();
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
    public void VolleyPetitionBR(String URL) {
        Log.i("url", "" + URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> mylistBR = new ArrayList<String>();
                    ArrayList<String> mylistBRP = new ArrayList<String>();
                    ArrayList<String> mylistDatesBR = new ArrayList<String>();
                    ArrayList<String> mylistVarBR = new ArrayList<String>();
                    ArrayList<String> mylistTimeBR = new ArrayList<String>();
                    jsonArrayBR   = new JSONArray(response);
                    for (int i = 0; i < jsonArrayBR.length(); i++) {
                        br = jsonArrayBR.getString(i);
                        JSONObject jsonObj = new JSONObject(br);
                        valueBR = jsonObj.getString("diagnostic");
                        dateBR = jsonObj.getString("date");
                        timeBR = jsonObj.getString("time");
                        varBR = jsonObj.getString("var");
                        mylistBR.add(valueBR);
                        mylistDatesBR.add(dateBR);
                        mylistTimeBR.add(timeBR);
                        mylistVarBR.add(varBR);
                    }
                    for(int i = 0; i<((int)(mylistBR.size()*percent));i++) {
                        mylistBRP.add(mylistBR.get(i));
                    }
                    for(int i = 0; i<mylistBRP.size();i++) {
                        if(mylistBRP.get(i).equals("FRECUENCIA RESPIRATORIA NORMAL ALTA")){
                            frecAN++;
                        } else if(mylistBRP.get(i).equals("FRECUENCIA RESPIRATORIA NORMAL BAJA")){
                            frecBN++;
                        } else if(mylistBRP.get(i).equals("FRECUENCIA RESPIRATORIA NORMAL")){
                            frecN++;
                        } else if(mylistBRP.get(i).equals("TAQUIPNEA LIGERA")){
                            taquiL++;
                        } else if(mylistBRP.get(i).equals("BRADIPNEA LIGERA")){
                            bradiL++;
                        } else if(mylistBRP.get(i).equals("TAQUIPNEA SEVERA")){
                            taquiS++;
                        } else if(mylistBRP.get(i).equals("BRADIPNEA SEVERA")){
                            bradiS++;
                        }
                    }
                    PsetDataBR();
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
                    ArrayList<String> mylistM = new ArrayList<String>();
                    ArrayList<String> mylistP = new ArrayList<String>();
                    ArrayList<String> mylistDatesM = new ArrayList<String>();
                    ArrayList<String> mylistVarM = new ArrayList<String>();
                    ArrayList<String> mylistTimeM = new ArrayList<String>();
                    jsonArrayM   = new JSONArray(response);
                    for (int i = 0; i < jsonArrayM.length(); i++) {
                        m = jsonArrayM.getString(i);
                        JSONObject jsonObj = new JSONObject(m);
                        valueM = jsonObj.getString("value");
                        dateM = jsonObj.getString("date");
                        timeM = jsonObj.getString("time");
                        varM = jsonObj.getString("var");
                        mylistM.add(valueM);
                        mylistDatesM.add(dateM);
                        mylistTimeM.add(timeM);
                        mylistVarM.add(varM);
                    }for(int i = 0; i<((int)(mylistM.size()*percent));i++) {
                        mylistP.add(mylistM.get(i));
                    }
                    for(int i = 0; i<mylistP.size();i++) {
                        if(mylistP.get(i).equals("Calm")){
                            vCalmM++;
                        } else if(mylistP.get(i).equals("Depression")){
                            vTiredM++;
                        } else if(mylistP.get(i).equals("Normal")){
                            vNormalM++;
                        }
                    }
                    String[][] DATA_TO_SHOW2 = new String[mylistP.size()][4];

                    for (int i = 0; i < mylistP.size(); i++) {
                        DATA_TO_SHOW2[i][0] = mylistP.get(i).toString();
                        DATA_TO_SHOW2[i][1] = mylistDatesM.get(i).toString();
                        DATA_TO_SHOW2[i][2] = mylistTimeM.get(i).toString();
                    }

                    tableViewM.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), DATA_TO_SHOW2));

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
    public void VolleyPetitionBP(String URL) {
        Log.i("url", "" + URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> mylistBP = new ArrayList<String>();
                    ArrayList<String> mylistBPP = new ArrayList<String>();
                    ArrayList<String> mylistDatesBP = new ArrayList<String>();
                    ArrayList<String> mylistVarBP = new ArrayList<String>();
                    ArrayList<String> mylistTimeBP = new ArrayList<String>();
                    jsonArrayBP   = new JSONArray(response);
                    for (int i = 0; i < jsonArrayBP.length(); i++) {
                        bp = jsonArrayBP.getString(i);
                        JSONObject jsonObj = new JSONObject(bp);
                        valueBP = jsonObj.getString("diagnostic");
                        dateBP = jsonObj.getString("date");
                        timeBP = jsonObj.getString("time");
                        varBP = jsonObj.getString("var");
                        mylistBP.add(valueBP);
                        mylistDatesBP.add(dateBP);
                        mylistTimeBP.add(timeBP);
                        mylistVarBP.add(varBP);
                    }
                    for(int i = 0; i<((int)(mylistBP.size()*percent));i++) {
                        mylistBPP.add(mylistBP.get(i));
                    }
                    for(int i = 0; i<mylistBPP.size();i++) {
                        if(mylistBPP.get(i).equals("ALTA PRESION NORMAL")){
                            APN++;
                        } else if(mylistBPP.get(i).equals("PRESION NORMAL")){
                            PN++;
                        } else if(mylistBPP.get(i).equals("BAJA PRESION NORMAL")){
                            BPN++;
                        } else if(mylistBPP.get(i).equals("PRESION PELIGROSAMENTE BAJA")){
                            PPB++;
                        } else if(mylistBPP.get(i).equals("PRESION MUY BAJA")){
                            PMB++;
                        } else if(mylistBPP.get(i).equals("LIMITE DE PRESION BAJA")){
                            LPB++;
                        } else if(mylistBPP.get(i).equals("HIPERTENSION: NIVEL 1")){
                            HN1++;
                        } else if(mylistBPP.get(i).equals("HIPERTENSION: NIVEL 2")){
                            HN2++;
                        } else if(mylistBPP.get(i).equals("HIPERTENSION: NIVEL 3")){
                            HN3++;
                        } else if(mylistBPP.get(i).equals("HIPERTENSION SISTOLICA AISLADA NIVEL 1")){
                            HSA1++;
                        } else if(mylistBPP.get(i).equals("HIPERTENSION SISTOLICA AISLADA NIVEL 2")){
                            HSA2++;
                        } else if(mylistBPP.get(i).equals("HIPERTENSION SISTOLICA AISLADA NIVEL 3")){
                            HSA3++;
                        } else if(mylistBPP.get(i).equals("HIPERTENSION SISTOLICA AISLADA NIVEL 4")){
                            HSA4++;
                        } else if(mylistBPP.get(i).equals("LIMITE DE HIPOTENSION SISTOLICA AISLADA")){
                            LHSA++;
                        } else if(mylistBPP.get(i).equals("HIPOTENSION SISTOLICA AISLADA MUY BAJA")){
                            HSAMB++;
                        } else if(mylistBPP.get(i).equals("HIPOTENSION SISTOLICA PELIGROSA")){
                            HSP++;
                        }
                    }
                    PsetDataBP();
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
    private void PsetDataHR() {


        pChartHR.setUsePercentValues(true);
        pChartHR.getDescription().setEnabled(false);
        pChartHR.setExtraOffsets(0, 0, 0, 0);
        pChartHR.setDragDecelerationFrictionCoef(0f);
        pChartHR.setRotationAngle(0);
        pChartHR.setRotationEnabled(false);
        pChartHR.setHighlightPerTapEnabled(true);
        pChartHR.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        pChartHR.setDrawEntryLabels(false);
        pChartHR.setEntryLabelColor(Color.WHITE);
        pChartHR.setEntryLabelTextSize(8f);

        pChartHR.getLegend().setWordWrapEnabled(true);
        pChartHR.getLegend().setTextSize(7f);



        pChartHR.setDrawHoleEnabled(true);
        pChartHR.setHoleRadius(28f);
        pChartHR.setTransparentCircleRadius(31f);
        pChartHR.setTransparentCircleColor(Color.BLACK);
        pChartHR.setTransparentCircleAlpha(50);
        pChartHR.setHoleColor(Color.WHITE);
        pChartHR.setDrawCenterText(true);
        pChartHR.setCenterText("HR");
        pChartHR.setCenterTextSize(8f);
        pChartHR.setCenterTextColor(Color.RED);

        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#80e5ff"));
        colors.add(Color.parseColor("#b3d9ff"));
        colors.add(Color.parseColor("#0080ff"));
        colors.add(Color.parseColor("#33ffd6"));
        colors.add(Color.parseColor("#00b38f"));

        PieEntry valorN = new PieEntry(ValorN, "FRECUENCIA CARDIACA NORMAL");
        PieEntry bradi = new PieEntry(Bradi, "BRADICARDIA");
        PieEntry taqui = new PieEntry(Taqui, "TAQUICARDIA");
        PieEntry ligBrad = new PieEntry(LigBradi, "LIGERA BRADICARDIA");
        PieEntry ligTaqui = new PieEntry(LigTaqui,"LIGERA TAQUICARDIA");
        pieEntryList.add(valorN);
        pieEntryList.add(bradi);
        pieEntryList.add(taqui);
        pieEntryList.add(ligBrad);
        pieEntryList.add(ligTaqui);

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(10f);
        pieDataSet.setColors(colors);


        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.BLUE);
        pieData.setValueTextSize(10f);
        pieData.setValueFormatter(new PercentFormatter());
        pChartHR.setData(pieData);
        pChartHR.highlightValues(null);
        pChartHR.invalidate();
    }
    private void PsetDataBR() {


        pChartBR.setUsePercentValues(true);
        pChartBR.getDescription().setEnabled(false);
        pChartBR.setExtraOffsets(0, 0, 0, 0);
        pChartBR.setDragDecelerationFrictionCoef(0f);
        pChartBR.setRotationAngle(0);
        pChartBR.setRotationEnabled(false);
        pChartBR.setHighlightPerTapEnabled(true);
        pChartBR.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        pChartBR.setDrawEntryLabels(false);
        pChartBR.setEntryLabelColor(Color.WHITE);
        pChartBR.setEntryLabelTextSize(8f);

       // pChartBR.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        //pChartBR.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pChartBR.getLegend().setWordWrapEnabled(true);
        pChartBR.getLegend().setTextSize(7f);

        pChartBR.setDrawHoleEnabled(true);
        pChartBR.setHoleRadius(28f);
        pChartBR.setTransparentCircleRadius(31f);
        pChartBR.setTransparentCircleColor(Color.BLACK);
        pChartBR.setTransparentCircleAlpha(50);
        pChartBR.setHoleColor(Color.WHITE);
        pChartBR.setDrawCenterText(true);
        pChartBR.setCenterText("BR");
        pChartBR.setCenterTextSize(8f);
        pChartBR.setCenterTextColor(Color.RED);

        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#80e5ff"));
        colors.add(Color.parseColor("#b3d9ff"));
        colors.add(Color.parseColor("#0080ff"));
        colors.add(Color.parseColor("#33ffd6"));
        colors.add(Color.parseColor("#00b38f"));
        colors.add(Color.parseColor("#80ff80"));
        colors.add(Color.parseColor("#00cc00"));

        PieEntry vfrecN = new PieEntry(frecN, "FRECUENCIA RESPIRATORIA NORMAL");
        PieEntry vfrecAN = new PieEntry(frecAN, "FRECUENCIA RESPIRATORIA ALTA NORMAL");
        PieEntry vfrecBN = new PieEntry(frecBN, "FRECUENCIA RESPIRATORIA BAJA NORMAL");
        PieEntry vtaquiL = new PieEntry(taquiL, "TAQUIPNEA LIGERA");
        PieEntry vtaquiS = new PieEntry(taquiS,"TAQUIPNEA SEVERA");
        PieEntry vbradiL = new PieEntry(bradiL, "BRADIPNEA LIGERA");
        PieEntry vbradiS = new PieEntry(bradiS,"BRADIPNEA SEVERA");
        pieEntryList.add(vfrecN);
        pieEntryList.add(vfrecAN);
        pieEntryList.add(vfrecBN);
        pieEntryList.add(vtaquiL);
        pieEntryList.add(vtaquiS);
        pieEntryList.add(vbradiL);
        pieEntryList.add(vbradiS);

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(10f);
        pieDataSet.setColors(colors);


        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.BLUE);
        pieData.setValueTextSize(10f);
        pieData.setValueFormatter(new PercentFormatter());
        pChartBR.setData(pieData);
        pChartBR.highlightValues(null);
        pChartBR.invalidate();
    }
    private void PsetDataBP() {


        pChartBP.setUsePercentValues(true);
        pChartBP.getDescription().setEnabled(false);
        pChartBP.setExtraOffsets(0, 0, 0, 0);
        pChartBP.setDragDecelerationFrictionCoef(0f);
        pChartBP.setRotationAngle(0);
        pChartBP.setRotationEnabled(false);
        pChartBP.setHighlightPerTapEnabled(true);
        pChartBP.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        pChartBP.setDrawEntryLabels(false);
        pChartBP.setEntryLabelColor(Color.WHITE);
        pChartBP.setEntryLabelTextSize(8f);

        // pChartBR.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        //pChartBR.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pChartBP.getLegend().setWordWrapEnabled(true);
        pChartBP.getLegend().setTextSize(7f);

        pChartBP.setDrawHoleEnabled(true);
        pChartBP.setHoleRadius(28f);
        pChartBP.setTransparentCircleRadius(31f);
        pChartBP.setTransparentCircleColor(Color.BLACK);
        pChartBP.setTransparentCircleAlpha(50);
        pChartBP.setHoleColor(Color.WHITE);
        pChartBP.setDrawCenterText(true);
        pChartBP.setCenterText("BP");
        pChartBP.setCenterTextSize(8f);
        pChartBP.setCenterTextColor(Color.RED);

        ArrayList<PieEntry> pieEntryList = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#b3d9ff"));
        colors.add(Color.parseColor("#0080ff"));
        colors.add(Color.parseColor("#80e5ff"));
        colors.add(Color.parseColor("#33ffd6"));
        colors.add(Color.parseColor("#00b38f"));
        colors.add(Color.parseColor("#80ff80"));
        colors.add(Color.parseColor("#00cc00"));
        colors.add(Color.parseColor("#bbff33"));
        colors.add(Color.parseColor("#ffff80"));
        colors.add(Color.parseColor("#ffb366"));
        colors.add(Color.parseColor("#8080ff"));
        colors.add(Color.parseColor("#ff99ff"));
        colors.add(Color.parseColor("#ff4d88"));
        colors.add(Color.parseColor("#ff9999"));
        colors.add(Color.parseColor("#d6d6c2"));
        colors.add(Color.parseColor("#b1b1cd"));

        PieEntry vAPN = new PieEntry(APN, "ALTA PRESIÓN NORMAL");
        PieEntry vPN = new PieEntry(PN, "PRESION NORMAL");
        PieEntry vBPN = new PieEntry(BPN, "BAJA PRESIÓN NORMAL");
        PieEntry vPPB = new PieEntry(PPB, "PRESIÓN PPELIGROSAMENTE BAJA");
        PieEntry vPMB = new PieEntry(PMB,"PRESIÓN MUY BAJA");
        PieEntry vLPB = new PieEntry(LPB, "LIMITE DE PRESIÓN BAJA");
        PieEntry vHSA1 = new PieEntry(HSA1,"HIPERTENSIóN SISTOLICA AISLADA NIVEL 1");
        PieEntry vHSA2 = new PieEntry(HSA2,"HIPERTENSIóN SISTOLICA AISLADA NIVEL 2");
        PieEntry vHSA3 = new PieEntry(HSA3,"HIPERTENSIóN SISTOLICA AISLADA NIVEL 3");
        PieEntry vHSA4 = new PieEntry(HSA4,"HIPERTENSIóN SISTOLICA AISLADA NIVEL 4");
        PieEntry vLHSA = new PieEntry(LHSA,"LIMITE DE HIPOTENSIÓN SISTOLICA AISLADA");
        PieEntry vHSAMB = new PieEntry(HSAMB,"HIPOTENSIÓN SISTOLICA AISLADA MUY BAJA");
        PieEntry vHSP = new PieEntry(HSP,"HIPOTENSIÓN SISTOLICA PELIGROSA");
        PieEntry vHN1 = new PieEntry(HN1,"HIPERTENSIÓN: NIVEL 1");
        PieEntry vHN2 = new PieEntry(HN2,"HIPERTENSIÓN: NIVEL 2");
        PieEntry vHN3 = new PieEntry(HN3,"HIPERTENSIÓN: NIVEL 3");



        pieEntryList.add(vAPN);
        pieEntryList.add(vPN);
        pieEntryList.add(vBPN);
        pieEntryList.add(vPPB);
        pieEntryList.add(vPMB);
        pieEntryList.add(vLPB);
        pieEntryList.add(vHSA1);
        pieEntryList.add(vHSA2);
        pieEntryList.add(vHSA3);
        pieEntryList.add(vHSA4);
        pieEntryList.add(vLHSA);
        pieEntryList.add(vHSAMB);
        pieEntryList.add(vHSP);
        pieEntryList.add(vHN1);
        pieEntryList.add(vHN2);
        pieEntryList.add(vHN3);


        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(10f);
        pieDataSet.setColors(colors);


        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.BLUE);
        pieData.setValueTextSize(10f);
        pieData.setValueFormatter(new PercentFormatter());
        pChartBP.setData(pieData);
        pChartBP.highlightValues(null);
        pChartBP.invalidate();
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
                        if (position == 2) {
                            Intent intent = new Intent(PantallaLozoya.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 3) {
                            Intent intent = new Intent(PantallaLozoya.this, Registros.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 4){
                            Intent intent = new Intent(PantallaLozoya.this, Statistics.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 5){
                            Intent intent = new Intent(PantallaLozoya.this, MenuHinfo.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 7){
                            Intent intent = new Intent(PantallaLozoya.this, configuracion.class);
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
    public void clear(){
        ValorN=0;
        Bradi=0;
        Taqui=0;
        LigBradi=0;
        LigTaqui=0;

        vCalmF=0;
        vTiredF=0;
        vNormalF=0;

        vCalmM=0;
        vTiredM=0;
        vNormalM=0;

        frecN=0;
        frecAN=0;
        frecBN=0;
        taquiL=0;
        taquiS=0;
        bradiL=0;
        bradiS=0;

        APN=0;
        PN=0;
        BPN=0;
        PPB=0;
        PMB=0;
        LPB=0;
        HSA1=0;
        HSA2=0;
        HSA3=0;
        HSA4=0;
        LHSA=0;
        HSAMB=0;
        HSP=0;
        HN1=0;
        HN2=0;
        HN3=0;


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

}
