package com.example.luisguzmn.healthcare40;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisguzmn.healthcare40.HealthcareInfo.MenuHinfo;
import com.github.mikephil.charting.charts.LineChart;
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

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class RegistrosEmty extends AppCompatActivity {


    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;

    TextView txtBR;
    JSONArray jsonArray;
    private String a;
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> mylistDates = new ArrayList<String>();
    ArrayList<String> mylistTime = new ArrayList<String>();
    ArrayList<String> mylistVar = new ArrayList<String>();

    float[] floatValues;
    String var,date,time,email,variable;
    TableView<String[]> tableView;
    final Handler handler = new Handler();




    //private static final String[][] DATA_TO_SHOW = { { "b","a", "a", "test" },
      //      { "and", "a", "second", "test" } };

    private static final String[] TABLE_HEADERS = { "Var", "Valor", "Fecha", "Hora" };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros2);
        menu();
        dias();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            variable = extras.getString("key");
            //The key argument here must match that used in the other activity
        }

        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

        email = sp.getString("email", "no email");



        tableView = (TableView) findViewById(R.id.tableView);
        tableView.setColumnCount(4);

        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(int rowIndex, String[] clickedData) {
                    Toast.makeText(getApplicationContext(),"Var: "+clickedData[0]
                            +"\nValor: "+clickedData[1]+"\nFecha: "+clickedData[2]+"\nHora: "+clickedData[3],
                            Toast.LENGTH_LONG).show();
            }
        });

        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 1);
        columnModel.setColumnWeight(3, 1);
        columnModel.setColumnWeight(4, 1);



        tableView.setColumnModel(columnModel);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        Toast.makeText(getApplicationContext(),"Obteniendo Información",Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                VolleyPetition("http://meddata.sytes.net/grafico/dataShow.php?email="+email+"&var="+variable);
            }
        }, 500);





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
                        if(variable.equals("Mood") | variable.equals("ecg") | variable.equals("fatigue")) {

                        }else {
                            floatValues = new float[mylist.size()];
                            for (int i = 0; i < mylist.size(); i++) {
                                floatValues[i] = Float.parseFloat(mylist.get(i));
                            }
                        }

                        String[][] DATA_TO_SHOW = new String[mylist.size()][4];

                        for (int i = 0; i < mylist.size(); i++) {
                            DATA_TO_SHOW[i][0] = mylistVar.get(i).toString();
                            DATA_TO_SHOW[i][1] = mylist.get(i).toString();
                            DATA_TO_SHOW[i][2] = mylistDates.get(i).toString();
                            DATA_TO_SHOW[i][3] = mylistTime.get(i).toString();
                        }
                        tableView.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), DATA_TO_SHOW));

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String[][] DATA_TO_SHOW = new String[0][3];
                DATA_TO_SHOW[0][0] = "ERROR";
                DATA_TO_SHOW[0][1] = "ERROR";
                DATA_TO_SHOW[0][2] = "ERROR";
                DATA_TO_SHOW[0][3] = "ERROR";
                tableView.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), DATA_TO_SHOW));

            }
        });
        queue.add(stringRequest);
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void menu(){
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Registros");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
