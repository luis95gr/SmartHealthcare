package com.example.luisguzmn.healthcare40.Helo;

import android.Manifest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguzmn.healthcare40.PrincipalDashboard;
import com.example.luisguzmn.healthcare40.R;
import com.example.luisguzmn.healthcare40.crearCuentaHelo;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.worldgn.connector.Connector;
import com.worldgn.connector.DeviceItem;

import com.worldgn.connector.ScanCallBack;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class HeloConnection extends AppCompatActivity implements ScanCallBack {

    //VARIABLES
    TextView battery, conn_status, bond_status,firmware_version,mac,lastsynctime,textScanning;
    Button scan, unpair,clear;
    MeasurementReceiver heloMeasurementReceiver;
    IntentFilter intentFilter;
    private AlertDialog alertDialog;
    private final String TAG = HeloConnection.class.getSimpleName();
    private LinearLayout dynamic_measure_layout;
    DeviceItem deviceItem = null;
    int firmwareVersion;
    PermissionListener permissionlistener;
    ProgressBar progressBar;
    SharedPreferences spConnectionHelo;
    //
    public final String BROADCAST_ACTION_APPVERSION_MEASUREMENT = "com.worldgn.connector.APPVERSION_MEASUREMENT";
    public final String BROADCAST_ACTION_HELO_DEVICE_RESET = "com.worldgn.connector.ACTION_HELO_DEVICE_RESET";
    public final String BROADCAST_ACTION_HELO_CONNECTED = "com.worldgn.connector.ACTION_HELO_CONNECTED";
    public final String BROADCAST_ACTION_HELO_DISCONNECTED = "com.worldgn.connector.ACTION_HELO_DISCONNECTED";
    public final String BROADCAST_ACTION_HELO_BONDED = "com.worldgn.connector.ACTION_HELO_BONDED";
    public final String BROADCAST_ACTION_HELO_UNBONDED = "com.worldgn.connector.ACTION_HELO_UNBONDED";
    public final String BROADCAST_ACTION_HELO_FIRMWARE = "com.worldgn.connector.ACTION_HELO_FIRMWARE";
    public final String BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE = "com.worldgn.connector.MEASURE_WRITE_FAILURE";
    public final String BROADCAST_ACTION_BATTERY = "com.worldgn.connector.BATTERY";
    public  final String INTENT_KEY_BATTERY = "BATTERY";
    public  final String INTENT_KEY_FIRMWARE = "HELO_FIRMWARE";
    public  final String INTENT_KEY_MAC = "HELO_MAC";
    public  final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";
    //


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helo_connection);

        spConnectionHelo = PreferenceManager.getDefaultSharedPreferences(this);
        //CAST
        textScanning = (TextView)findViewById(R.id.textScanning);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        scan = (Button) findViewById(R.id.scan);
        unpair = (Button) findViewById(R.id.unpair);
        clear =(Button)findViewById(R.id.cleardata);
        lastsynctime = (TextView)findViewById(R.id.lastsynctime);
        mac = (TextView)findViewById(R.id.mac);
        dynamic_measure_layout = (LinearLayout) findViewById(R.id.buttons);
        battery = (TextView) findViewById(R.id.battery);
        conn_status = (TextView) findViewById(R.id.conn_status);
        bond_status = (TextView) findViewById(R.id.bond_status);
        firmware_version= (TextView) findViewById(R.id.firmware_version);
        heloMeasurementReceiver = new MeasurementReceiver();
        //
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_APPVERSION_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BATTERY);
        intentFilter.addAction(BROADCAST_ACTION_HELO_CONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DISCONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_HELO_BONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_UNBONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_FIRMWARE);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE);
        //

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conn_status.getText().equals("Connected") || bond_status.getText().equals("Bonded")) {
                    startActivity(new Intent(HeloConnection.this, PrincipalDashboard.class));
                }else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.helo_connection),"Helo LX not connected",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to connect to this BleDevcie ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        connectBleDevice();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        alertDialog = builder.create();


        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(HeloConnection.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(HeloConnection.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                        .show();
            }


        };
        checkBTenabled();
        if(isGpsEnabled()) {
            checkLocationPermission();
        } else {
            buildAlertMessageNoGps();
        }

        //AUTOMATIC STATUSES
        if(spConnectionHelo.getBoolean("connected",false)){
            conn_status.setText("Connected");
            bond_status.setText("Bonded");
            mac.setText(spConnectionHelo.getString("mac","Not Available"));
            battery.setText(spConnectionHelo.getString("battery","Not Available"));
            firmware_version.setText(spConnectionHelo.getString("firmware","Not Available"));
        }

        //

    }

    private void checkLocationPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_message)
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("bla bla")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }

    private boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {

            return true;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Location seems to be disabled, Please enable Location")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 102);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        Toast.makeText(HeloConnection.this, "Connector sample need Location to turn on. Please enable Location.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void checkBTenabled() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "This device doesnot support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                Toast.makeText(this, "Bluetooth off", Toast.LENGTH_LONG).show();
                mBluetoothAdapter.enable();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(heloMeasurementReceiver, intentFilter);
    }


    @Override
    public void onScanStarted() {
        progressBar.setVisibility(View.VISIBLE);
        textScanning.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScanFinished() {
        progressBar.setVisibility(View.INVISIBLE);
        textScanning.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLedeviceFound(DeviceItem deviceItem) {
        progressBar.setVisibility(View.INVISIBLE);
        textScanning.setVisibility(View.INVISIBLE);
        this.deviceItem = deviceItem;
        alertDialog.show();
    }


    @Override
    public void onPairedDeviceNotFound() {
        Toast.makeText(this, "Paired device not found", Toast.LENGTH_LONG).show();
    }

    public class MeasurementReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {

                if (intent.getAction().equals(BROADCAST_ACTION_BATTERY)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int batteryVal = intent.getIntExtra(INTENT_KEY_BATTERY, -1);
                            battery.setText(Integer.toString(batteryVal));
                            //
                            SharedPreferences.Editor spConnectionHeloEditor = spConnectionHelo.edit();
                            spConnectionHeloEditor.putString("battery",Integer.toString(batteryVal));
                            spConnectionHeloEditor.apply();
                            //
                        }
                    });

                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_DISCONNECTED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conn_status.setText("Disconnected");
                            mac.setText("");
                            battery.setText("");
                            SharedPreferences.Editor spConnectionHeloEditor = spConnectionHelo.edit();
                            spConnectionHeloEditor.putBoolean("connected",false);
                            spConnectionHeloEditor.apply();
                        }
                    });
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_CONNECTED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conn_status.setText("Connected");
                            mac.setText(intent.getStringExtra(INTENT_KEY_MAC));
                            SharedPreferences.Editor spConnectionHeloEditor = spConnectionHelo.edit();
                            spConnectionHeloEditor.putString("mac",mac.getText().toString());
                            spConnectionHeloEditor.putBoolean("connected",true);
                            spConnectionHeloEditor.apply();

                        }
                    });
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_DEVICE_RESET)) {
                    Toast.makeText(HeloConnection.this, "Restart Helo device by pressing 8 seconds", Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_BONDED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bond_status.setText("Bonded");

                        }
                    });
                }else if (intent.getAction().equals(BROADCAST_ACTION_HELO_UNBONDED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bond_status.setText("Unbonded");
                        }
                    });
                }else if (intent.getAction().equals(BROADCAST_ACTION_HELO_FIRMWARE)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            firmwareVersion = intent.getIntExtra(INTENT_KEY_FIRMWARE, -1);
                            firmware_version.setText(Integer.toString(firmwareVersion));
                            //
                            SharedPreferences.Editor spConnectionHeloEditor = spConnectionHelo.edit();
                            spConnectionHeloEditor.putString("firmware",Integer.toString(firmwareVersion));
                            spConnectionHeloEditor.apply();
                            //
                            if(firmwareVersion >= 5014) {
                                dynamic_measure_layout.setVisibility(View.VISIBLE);
                            } else {
                                dynamic_measure_layout.setVisibility(View.GONE);
                            }
                        }
                    });
                } else if(intent.getAction().equals(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE)) {
                    String message = intent.getStringExtra(INTENT_MEASUREMENT_WRITE_FAILURE);
                    Toast.makeText(HeloConnection.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    private String getAppVersion() {
        PackageManager manager;
        PackageInfo info = null;
        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String str = info.versionCode + "";
        Log.i(TAG, "OldVersionCore1 = " + str);
        String result = str.substring(2, 8);
        Log.i(TAG, "OldVersionCore2 = " + result);
        return result;
    }


    public void scan(View view) {
        Connector.getInstance().scan(this);
    }

    private void connectBleDevice() {
        Connector.getInstance().connect(deviceItem);
    }

    public void unpair(View view) {
        Connector.getInstance().unbindDevice();
        progressBar.setVisibility(View.INVISIBLE);
        textScanning.setVisibility(View.INVISIBLE);
        bond_status.setText("Unbonded");
        conn_status.setText("Disconnected");
        battery.setText("");
        firmware_version.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:
                if(isGpsEnabled()) {
                    checkLocationPermission();
                } else {
                    Toast.makeText(HeloConnection.this, "Please turn on GPS", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

