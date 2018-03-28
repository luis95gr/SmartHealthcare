package com.example.luisguzmn.healthcare40;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.snatik.storage.Storage;
import com.snatik.storage.helpers.OrderType;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Dashboard_helo extends AppCompatActivity {

    //VARIABLES
    GraphView graph;
    double x=2;
    String s;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_helo);

        //CAST
        graph = (GraphView)findViewById(R.id.graph);
        texto = (TextView)findViewById(R.id.textView);
        //
        //

        //READ FILE
        String text = null;
        try {
            InputStream is = getAssets().open("a2018-03-05 13_26_00.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);

            //Toast.makeText(getApplicationContext(), "" +tam , Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //EJE Y
        String[] datos = text.split(",",-2);
        int sizeDatos = datos.length;

        double[] datosD = new double[sizeDatos];

        int valor=0;
        int loop=1;
        int h=0;
            while(valor<sizeDatos*0.985){
                datosD[h+valor] = Double.parseDouble(datos[h+valor]);
                h++;
                if(h==254){
                    h=0;
                    valor =255*loop;
                    loop++;
                }
            }
        //
        //EJE X
        float fs = (sizeDatos/40);
        float inter = 1/fs;
        float x[]= new float[sizeDatos];
        for(int j=1;j<sizeDatos;j++){
            x[j]=j*inter;
        }
        //

        //Toast.makeText(getApplicationContext(), ""+ datosD[0] , Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), ""+ datos[0], Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), ""+ sizeDatos, Toast.LENGTH_SHORT).show();

        //GRAFICA
        //double[] parsed = new double[datos.length];
        //for (int i = 0; i<datos.length; i++) parsed[i] = Double.valueOf(datos[i]);

        DataPoint[] points = new DataPoint[sizeDatos];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(x[i], datosD[i]);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        // set manual X bounds
        //
        double i, max, min, suma;

        min = max = datosD[0];

        for(int c = 0; c < datosD.length; c++)
        {
            if(min>datosD[c]) {
                min=datosD[c];
            }
            if(max<datosD[c]) {
                max=datosD[c];
            }
        }
        texto.setText("" +max);
        //
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(max-100000);
        graph.getViewport().setMaxY(max);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(20);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        //

    }
}
