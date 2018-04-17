package com.example.luisguzmn.healthcare40;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class serviceInternet extends Service {

    //VARIABLES
    StringTokenizer tokenDataBpmax = null;
    StringTokenizer tokenDataBpmin = null;
    StringTokenizer tokenDataBr = null;
    StringTokenizer tokenDataMood = null;
    StringTokenizer tokenDataFatigue = null;
    //
    String[] stringTokenBpmax = new String[20];
    String[] stringTokenBpmin = new String[20];
    String[] stringTokenBr = new String[20];
    String[] stringTokenMood = new String[20];
    String[] stringTokenFatigue = new String[20];
    //
    String[] stringBpmaxSaved = new String[20];
    String[] stringBpminSaved = new String[20];
    String[] stringBrSaved = new String[20];
    String[] stringMoodSaved = new String[20];
    String[] stringFatigueSaved = new String[20];
    //
    String stringEmail;
    String stringPass;
    SharedPreferences spMeasuresSaved;
    SharedPreferences spTaskFinished;
    int contBp;
    int contBr;
    int contMood;
    int contFatigue;
    int contTotal =0;
    Timer timer = new Timer();
    NotificationManager notificationManager;
    NotificationCompat.Builder builderStart;
    NotificationCompat.Builder builderDone;
    //

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //START NOTIFICATION
        builderStart = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setContentTitle("Uploader Manager")
                .setContentText("Waiting for Internet connection to upload data")
                .setAutoCancel(true)
                .setColor(Color.parseColor("#b8ddcd"));
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,builderStart.build());
        // DONE NOTIFICATION
        builderDone = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setContentTitle("Uploader Manager")
                .setContentText("Data uploaded succesfully")
                .setAutoCancel(true)
                .setColor(Color.parseColor("#b8ddcd"));

        //
        //SharedPreferences.Editor spTaskFinishedEditor = spTaskFinished.edit();
        //SharedPreferences.Editor spMeasuresSavedEditor = spMeasuresSaved.edit();
        //
        spMeasuresSaved = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences spLogin = getSharedPreferences("login", MODE_PRIVATE);
        final String ip = "meddata.sytes.net";
        stringEmail = spLogin.getString("email",null);
        stringPass = spLogin.getString("pass",null);

        contBp = spMeasuresSaved.getInt("contBp", 0);
        contBr = spMeasuresSaved.getInt("contBr",0);
        contMood = spMeasuresSaved.getInt("contMood",0);
        contFatigue = spMeasuresSaved.getInt("contFatigue",0);

        //contTotal = contBp + contBr + contMood + contFatigue;

        //RECEIVE DATA
        ///////BP//////
        for (int i = 1; i <= contBp; i++) {
            stringBpmaxSaved[i] = spMeasuresSaved.getString("stringBpmaxSaved" + i, "0");
            stringBpminSaved[i] = spMeasuresSaved.getString("stringBpminSaved" + i, "0");
        }
        ///////BR//////
        for (int j = 1; j <= contBr; j++) {
            stringBrSaved[j] = spMeasuresSaved.getString("stringBrSaved" + j, "0");
        }
        ///////MOOD//////
        for (int k = 1; k <= contMood; k++) {
            stringMoodSaved[k] = spMeasuresSaved.getString("stringMoodSaved" + k, "0");
        }
        ///////FATIGUE//////
        for (int m = 1; m <= contFatigue; m++) {
            stringFatigueSaved[m] = spMeasuresSaved.getString("stringFatigueSaved" + m, "0");
        }
        Toast.makeText(serviceInternet.this, ""+contBr , Toast.LENGTH_LONG).show();
        Toast.makeText(serviceInternet.this, stringBrSaved[1] , Toast.LENGTH_LONG).show();
        Toast.makeText(serviceInternet.this, stringBrSaved[2] , Toast.LENGTH_LONG).show();

        //SPLIT CONTENT
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (internet()) {
                    for (int q = 1; q <= contBp; q++) {
                        tokenDataBpmax = new StringTokenizer(stringBpmaxSaved[q], ",");
                        tokenDataBpmin = new StringTokenizer(stringBpminSaved[q], ",");
                        //
                        stringTokenBpmax[1] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[2] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[3] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[4] = tokenDataBpmax.nextToken();
                        VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                                "&var=" + stringTokenBpmax[1] + "&value=" + stringTokenBpmax[2] + "&date=" + stringTokenBpmax[3] + "&time=" + stringTokenBpmax[4]);
                        //
                        stringTokenBpmin[1] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[2] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[3] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[4] = tokenDataBpmin.nextToken();
                        VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                                "&var=" + stringTokenBpmin[1] + "&value=" + stringTokenBpmin[2] + "&date=" + stringTokenBpmin[3] + "&time=" + stringTokenBpmin[4]);
                        //
                    }
                    //
                    for (int s = 1; s <= contBr; s++) {
                        tokenDataBr = new StringTokenizer(stringBrSaved[s], ",");
                        //
                        stringTokenBr[1] = tokenDataBr.nextToken();
                        stringTokenBr[2] = tokenDataBr.nextToken();
                        stringTokenBr[3] = tokenDataBr.nextToken();
                        stringTokenBr[4] = tokenDataBr.nextToken();
                        VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                                "&var=" + stringTokenBr[1] + "&value=" + stringTokenBr[2] + "&date=" + stringTokenBr[3] + "&time=" + stringTokenBr[4]);
                    }
                    //
                    for (int d = 1; d <= contMood; d++) {
                        tokenDataMood = new StringTokenizer(stringMoodSaved[d], ",");
                        //
                        stringTokenMood[1] = tokenDataMood.nextToken();
                        stringTokenMood[2] = tokenDataMood.nextToken();
                        stringTokenMood[3] = tokenDataMood.nextToken();
                        stringTokenMood[4] = tokenDataMood.nextToken();
                        VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                                "&var=" + stringTokenMood[1] + "&value=" + stringTokenMood[2] + "&date=" + stringTokenMood[3] + "&time=" + stringTokenMood[4]);
                    }
                    //
                    for (int f = 1; f <= contFatigue; f++) {
                        tokenDataFatigue = new StringTokenizer(stringFatigueSaved[f], ",");
                        //
                        stringTokenFatigue[1] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[2] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[3] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[4] = tokenDataFatigue.nextToken();
                        VolleyPetition("http://"+ ip +"/dataVar/register.php?email=" + stringEmail + "&pass=" + stringPass +
                                "&var=" + stringTokenFatigue[1] + "&value=" + stringTokenFatigue[2] + "&date=" + stringTokenFatigue[3] + "&time=" + stringTokenFatigue[4]);
                    }
                }
            }
        },0,5000);

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        notificationManager.notify(1,builderDone.build());
    }

    private boolean internet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    //BP VOLLEY
    private void VolleyPetition(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //DATA SENT
                contTotal++;
                if (contTotal == contBp*2 + contBr + contMood + contFatigue){
                    stopSelf();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error!" , Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }




}
