package com.example.luisguzmn.healthcare40.Helo;

import android.Manifest;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguzmn.healthcare40.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.worldgn.connector.Connector;
import com.worldgn.connector.DeviceItem;

import com.worldgn.connector.ScanCallBack;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class BleDevice extends AppCompatActivity implements ScanCallBack {

    public final String BROADCAST_ACTION_BP_MEASUREMENT = "com.worldgn.connector.BP_MEASUREMENT";
    public final String BROADCAST_ACTION_ECG_MEASUREMENT = "com.worldgn.connector.ECG_MEASUREMENT";
    public final String BROADCAST_ACTION_APPVERSION_MEASUREMENT = "com.worldgn.connector.APPVERSION_MEASUREMENT";
    public final String BROADCAST_ACTION_HELO_DEVICE_RESET = "com.worldgn.connector.ACTION_HELO_DEVICE_RESET";
    public final String BROADCAST_ACTION_HELO_CONNECTED = "com.worldgn.connector.ACTION_HELO_CONNECTED";
    public final String BROADCAST_ACTION_HELO_DISCONNECTED = "com.worldgn.connector.ACTION_HELO_DISCONNECTED";
    public final String BROADCAST_ACTION_HELO_BONDED = "com.worldgn.connector.ACTION_HELO_BONDED";
    public final String BROADCAST_ACTION_HELO_UNBONDED = "com.worldgn.connector.ACTION_HELO_UNBONDED";
    public final String BROADCAST_ACTION_HELO_FIRMWARE = "com.worldgn.connector.ACTION_HELO_FIRMWARE";
    public final String BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE = "com.worldgn.connector.MEASURE_WRITE_FAILURE";
    public final String BROADCAST_ACTION_BATTERY = "com.worldgn.connector.BATTERY";
    public final String BROADCAST_ACTION_HR_MEASUREMENT = "com.worldgn.connector.HR_MEASUREMENT";
    public final String BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT = "com.worldgn.connector.DYNAMIC_HR_MEASUREMENT";
    public final String BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT = "com.worldgn.connector.DYNAMIC_STEPS_MEASUREMENT";
    public final String BROADCAST_ACTION_BR_MEASUREMENT = "com.worldgn.connector.BR_MEASUREMENT";
    public final String BROADCAST_ACTION_SLEEP = "com.worldgn.connector.SLEEP";
    public final String BROADCAST_ACTION_FATIGUE_MEASUREMENT = "com.worldgn.connector.FATIGUE_MEASUREMENT";
    public final String BROADCAST_ACTION_MOOD_MEASUREMENT = "com.worldgn.connector.MOOD_MEASUREMENT";
    public final String BROADCAST_ACTION_STEPS_MEASUREMENT = "com.worldgn.connector.STEPS_MEASUREMENT";

    public  final String INTENT_KEY_HR_MEASUREMENT = "HR_MEASUREMENT";
    public  final String INTENT_KEY_BR_MEASUREMENT = "BR_MEASUREMENT";
    public  final String INTENT_KEY_BATTERY = "BATTERY";
    public  final String INTENT_KEY_BP_MEASUREMENT_MAX = "BP_MEASUREMENT_MAX";
    public  final String INTENT_KEY_BP_MEASUREMENT_MIN = "BP_MEASUREMENT_MIN";
    public  final String INTENT_KEY_MOOD_MEASUREMENT = "MOOD_MEASUREMENT";
    public  final String INTENT_KEY_FATIGUE_MEASUREMENT = "FATIGUE_MEASUREMENT";
    public  final String INTENT_KEY_STEPS_MEASUREMENT = "STEPS_MEASUREMENT";
    public  final String INTENT_KEY_ECG_MEASUREMENT = "ECG_MEASUREMENT";
    public  final String INTENT_KEY_FIRMWARE = "HELO_FIRMWARE";
    public  final String INTENT_KEY_MAC = "HELO_MAC";
    public  final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";


    TextView battery, conn_status, bond_status, measure_type, measure_value, steps_val,dynamic_steps_val,dynamic_heartrate_val,firmware_version;
    TextView bp_val, hr_val, ecg_val, br_val, mood_val, fatigue_val, sleep_val, mac;
    TextView lastsynctime;
    Button scan, unpair;
    MeasurementReceiver heloMeasurementReceiver;
    IntentFilter intentFilter;
    private AlertDialog alertDialog;
    private final String TAG = BleDevice.class.getSimpleName();
    private LinearLayout dynamic_measure_layout;

    DeviceItem deviceItem = null;
    String mSteps="";
    String mHeartRate="";
    int firmwareVersion;
    boolean startStepsHeartRate=false;
    PermissionListener permissionlistener;

//    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_device);

        scan = (Button) findViewById(R.id.scan);
        unpair = (Button) findViewById(R.id.unpair);
        lastsynctime = (TextView)findViewById(R.id.lastsynctime);
        mac = (TextView)findViewById(R.id.mac);
        dynamic_measure_layout = (LinearLayout) findViewById(R.id.dynamic_measure_layout);
//        unpair.setVisibility(View.GONE);
        heloMeasurementReceiver = new MeasurementReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_BP_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_APPVERSION_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BATTERY);
        intentFilter.addAction(BROADCAST_ACTION_ECG_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_FATIGUE_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_MOOD_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_STEPS_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_HR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_SLEEP);
        intentFilter.addAction(BROADCAST_ACTION_HELO_CONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DISCONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_HELO_BONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_UNBONDED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_FIRMWARE);
        intentFilter.addAction(BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DEVICE_RESET);
        intentFilter.addAction(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE);



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

        battery = (TextView) findViewById(R.id.battery);
        conn_status = (TextView) findViewById(R.id.conn_status);
        bond_status = (TextView) findViewById(R.id.bond_status);
        measure_type = (TextView) findViewById(R.id.measure_type);
        measure_value = (TextView) findViewById(R.id.measure_value);
        bp_val  = (TextView) findViewById(R.id.bp_val);
        hr_val  = (TextView) findViewById(R.id.hr_val);
        ecg_val = (TextView) findViewById(R.id.ecg_val);
        br_val = (TextView) findViewById(R.id.br_val);
        mood_val = (TextView) findViewById(R.id.mood_val);
        fatigue_val = (TextView) findViewById(R.id.fatigue_val);
        steps_val = (TextView) findViewById(R.id.steps_val);
        sleep_val = (TextView) findViewById(R.id.sleep_val);
        dynamic_steps_val = (TextView) findViewById(R.id.dynamic_steps_val);
        dynamic_heartrate_val = (TextView) findViewById(R.id.dynamic_heartrate_val);
        firmware_version= (TextView) findViewById(R.id.firmware_version);
//        conn_status.setText("Connection status : Connected");
//        bond_status.setText("Bond status : Bonded");
        //PrefHelper prefHelper = new PrefHelper(BleDevice.this);
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(BleDevice.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(BleDevice.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                        .show();
            }


        };
        checkBTenabled();
        if(isGpsEnabled()) {
            checkLocationPermission();
        } else {
            buildAlertMessageNoGps();
        }
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
                        Toast.makeText(BleDevice.this, "Connector sample need Location to turn on. Please enable Location.", Toast.LENGTH_SHORT)
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(heloMeasurementReceiver);
    }

    public void measureBP(View view) {

        Connector.getInstance().measureBP();
    }

    public void measureHR(View view) {

        Connector.getInstance().measureHR();
    }

    public void measureEcg(View view) {
        Connector.getInstance().measureECG();
    }

    public void measureBR(View view) {
        Connector.getInstance().measureBr();
    }

    public void measureMF(View view) {
        Connector.getInstance().measureMF();
    }

    public void measurePW(View view) {
//        Connector.getInstance().sendBpData(getApplicationContext());

    }

    public void setSyncInterval(View view) {
//        Connector.getInstance().sendReviseAutoTimeData(2);
    }

    @Override
    public void onScanStarted() {

    }

    @Override
    public void onScanFinished() {

    }

    @Override
    public void onLedeviceFound(DeviceItem deviceItem) {

        this.deviceItem = deviceItem;

        alertDialog.show();
    }

    @Override
    public void onPairedDeviceNotFound() {
        Toast.makeText(this, "Paired device not found", Toast.LENGTH_LONG).show();
    }

    /*public void startDynamicStepHeartRate(View view) {
        if(null!=firmwareVersion && !firmwareVersion.isEmpty()&& firmwareVersion.equals("5014")){
            Connector.getInstance().startStepsHRDynamicMeasurement();
            startStepsHeartRate=true;
        }else {
            Toast.makeText(this,"Please update firmware to 5014",Toast.LENGTH_LONG).show();
        }
    }

    public void stopDynamicStepHeartRate(View view) {
        if(null!=firmwareVersion && !firmwareVersion.isEmpty()&& firmwareVersion.equals("5014")) {
            Connector.getInstance().stopStepsHRDynamicMeasurement();
            startStepsHeartRate = false;
        }else {
            Toast.makeText(this,"Please update firmware to 5014",Toast.LENGTH_LONG).show();
        }
    }*/

    public void startDynamicStepHeartRate(View view) {
        Connector.getInstance().startStepsHRDynamicMeasurement();
        startStepsHeartRate=true;
    }

    public void stopDynamicStepHeartRate(View view) {
        Connector.getInstance().stopStepsHRDynamicMeasurement();
        startStepsHeartRate = false;
    }
    public class MeasurementReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {

                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    String max = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    String min = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    updateUI(intent, "Bp", INTENT_KEY_BP_MEASUREMENT_MAX, bp_val);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    if(lastsynctime.getText().toString().equals("")) {
                        lastsynctime.setText(sdf.format(date));
                    }
//                    updateUI(intent, "Bp", Constants.INTENT_KEY_BP_MEASUREMENT_MIN, bp_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_APPVERSION_MEASUREMENT)) {


                } else if (intent.getAction().equals(BROADCAST_ACTION_BATTERY)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int batteryVal = intent.getIntExtra(INTENT_KEY_BATTERY, -1);
                            battery.setText(Integer.toString(batteryVal));
                        }
                    });

                } else if (intent.getAction().equals(BROADCAST_ACTION_ECG_MEASUREMENT)) {
                    updateUI(intent, "ECG", INTENT_KEY_ECG_MEASUREMENT, ecg_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {

                    updateUI(intent, "BR", INTENT_KEY_BR_MEASUREMENT, br_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_FATIGUE_MEASUREMENT)) {
                    updateUI(intent, "FATIGUE", INTENT_KEY_FATIGUE_MEASUREMENT, fatigue_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_MOOD_MEASUREMENT)) {
                    updateUI(intent, "MOOD", INTENT_KEY_MOOD_MEASUREMENT, mood_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_STEPS_MEASUREMENT)) {
                    updateUI(intent, "STEPS", INTENT_KEY_STEPS_MEASUREMENT, steps_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_HR_MEASUREMENT)) {
                    updateUI(intent, "HR", INTENT_KEY_HR_MEASUREMENT, hr_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_SLEEP)) {
                    updateUI(intent, "SLEEP", "com.wgn.SLEEP_ALL_DATA", sleep_val);
//                    updateUI(intent, "SLEEP", Constants.INTENT_KEY_SLEEP, sleep_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_DYNAMIC_HR_MEASUREMENT)) {
                    updateUI(intent, "HR", INTENT_KEY_HR_MEASUREMENT, dynamic_heartrate_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_DYNAMIC_STEPS_MEASUREMENT)) {
                    updateUI(intent, "HR", INTENT_KEY_STEPS_MEASUREMENT, dynamic_steps_val);
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_DISCONNECTED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conn_status.setText("Connection status : Disconnected");
                            mac.setText("");
                        }
                    });
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_CONNECTED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conn_status.setText("Connection status : Connected");
                            mac.setText(intent.getStringExtra(INTENT_KEY_MAC));
                        }
                    });
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_DEVICE_RESET)) {
                    Toast.makeText(BleDevice.this, "Restart Helo device by pressing 8 seconds", Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_BONDED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bond_status.setText("Bind status : Bonded");
                        }
                    });
                }else if (intent.getAction().equals(BROADCAST_ACTION_HELO_UNBONDED)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bond_status.setText("Bind status : UnBonded");
                        }
                    });
                }else if (intent.getAction().equals(BROADCAST_ACTION_HELO_FIRMWARE)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            firmwareVersion = intent.getIntExtra(INTENT_KEY_FIRMWARE, -1);
                            firmware_version.setText(Integer.toString(firmwareVersion));
                            if(firmwareVersion >= 5014) {
                                dynamic_measure_layout.setVisibility(View.VISIBLE);
                            } else {
                                dynamic_measure_layout.setVisibility(View.GONE);
                            }
                        }
                    });
                } else if(intent.getAction().equals(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE)) {
                    String message = intent.getStringExtra(INTENT_MEASUREMENT_WRITE_FAILURE);
                    Toast.makeText(BleDevice.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    private void updateUI(final Intent intent, final String measuretype, final String key_measurement, final TextView tv) {
        BleDevice.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(measuretype.equals("Bp")) {
                    tv.setText("max : "+intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX)+" min : "+intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN));
                }/*else if(startStepsHeartRate && null!=mSteps && null!=mHeartRate){
                    steps_hr_val.setText(mSteps +" / " + mHeartRate);
                }*/ else {
                    tv.setText(intent.getStringExtra(key_measurement));
                }

            }
        });
    }
    private void updateDynamicStepsUI(final String steps) {
        BleDevice.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(startStepsHeartRate && null!=steps){
                    dynamic_steps_val.setText(steps);

                }
            }
        });
    }

    private void updateDynamicHeartRateUI(final String heartRate) {
        BleDevice.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(startStepsHeartRate && null!=heartRate){
                    dynamic_heartrate_val.setText(heartRate);
                }
            }
        });
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

    public void stepcount(View view) {
        Connector.getInstance().getStepsData();
    }

    public void sleepcount(View view) {
//        Connector.getInstance().sleepTime();
//        Connector.getInstance().updateNewTime();
//        Connector.getInstance().sendBpData(getApplicationContext());
//        Connector.getInstance().sendReviseAutoTimeData();
//        Connector.getInstance().sendReviseAutoTimeData(1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Connector.getInstance().disconnect();
    }

    public void scan(View view) {
        Connector.getInstance().scan(this);
    }

    private void connectBleDevice() {
        Connector.getInstance().connect(deviceItem);
    }

    public void cleardata(View view) {
        bp_val.setText("");
        hr_val.setText("");
        ecg_val.setText("");
        br_val.setText("");
        mood_val.setText("");
        fatigue_val.setText("");
        sleep_val.setText("");
        dynamic_steps_val.setText("");
        dynamic_heartrate_val.setText("");
        steps_val.setText("");
//        firmware_version.setText("");
    }


    public void unpair(View view) {
        Connector.getInstance().unbindDevice();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:
                if(isGpsEnabled()) {
                    checkLocationPermission();
                } else {
                    Toast.makeText(BleDevice.this, "Please turn on GPS", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}