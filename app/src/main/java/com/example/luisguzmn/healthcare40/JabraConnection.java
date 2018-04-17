package com.example.luisguzmn.healthcare40;

import android.bluetooth.BluetoothAdapter;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jabra.sdk.api.Callback;
import com.jabra.sdk.api.DefaultListener;
import com.jabra.sdk.api.JabraDevice;
import com.jabra.sdk.api.JabraError;
import com.jabra.sdk.api.Listener;
import com.jabra.sdk.api.basic.BatteryStatus;

import org.w3c.dom.Text;

public class JabraConnection extends AppCompatActivity {

    private Button btnRefresh;
    private TextView txtName;
    private TextView txtConn;
    private DeviceConnector mDeviceConnector;
    private Button btnUpload;
    private boolean mConnectedToJabra;
    private ProgressBar prgbStatus;
    private TextView txtTransport;
    private TextView txtSerial;
    private TextView txtVersion;
    private TextView txtBatt;
    private TextView txtID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jabra_connection);
        mDeviceConnector = DeviceConnector.getInstance(this);
        btnRefresh = (Button)findViewById(R.id.btnRefresh);
        btnUpload = (Button)findViewById(R.id.btnUpload);
        txtConn = (TextView)findViewById(R.id.txtConn);
        txtName = (TextView)findViewById(R.id.txtName);
        prgbStatus = (ProgressBar)findViewById(R.id.prgbStatus);
        txtTransport = (TextView)findViewById(R.id.txtTransp);
        txtSerial = (TextView)findViewById(R.id.txtSerial);
        txtVersion = (TextView)findViewById(R.id.txtVersion);
        txtBatt = (TextView)findViewById(R.id.txtBat);
        txtID = (TextView)findViewById(R.id.txtID);
        setupRefreshButton();

        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.disable();
            }

        });

    }


    private void setupRefreshButton() {

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(JabraConnection.this, "Long Press To Scan Devices", Toast.LENGTH_SHORT).show();
            }
        });
        btnRefresh.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                btnUpload.setEnabled(true);
                mDeviceConnector.findConnectedDevice();
                prgbStatus.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDeviceConnector.registerPresenter(mPresenter);
        mDeviceConnector.registerConnectionStateListener(mReceiver);

        JabraDevice device = mDeviceConnector.getConnectedDevice();
        if (device == null) {
            mDeviceConnector.findConnectedDevice();
        } else {
            if (device.isConnected()) {
                mPresenter.updateConnectionStatus(true);
            } else {
                mPresenter.updateConnectionStatus(false);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDeviceConnector.unregisterPresenter(mPresenter);
        mDeviceConnector.unregisterConnectionStateListener(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDeviceConnector.unregisterConnectionStateListener(mReceiver);
        // note that this is not a good place to call mDeviceConnector.destroy() (or, put another way: do not tie the lifecycle of that singleton to the UI)
    }

    @Override
    public void onBackPressed() {
        // Be sure to close connection, when the application finished / closes down.
        // Alternatively, you could extend DeviceConnector to keep track of its clients, and start a short timer when the last client leaves. When the timer expires, self-destruct.
        mDeviceConnector.destroy();
        super.onBackPressed();
    }

    private DeviceConnector.Presenter2 mPresenter = new DeviceConnector.Presenter2() {
        @Override
        public void showMessage(String message, boolean loading) {
            JabraConnection.this.showLoading(message, loading);
        }

        @Override
        public void noDevice() {
            btnUpload.setEnabled(false);
            txtName.setText("??");
            txtTransport.setText("??");
            txtID.setText("??");
            txtBatt.setText("??");
            txtSerial.setText("??");
            txtVersion.setText("??");
        }

        @Override
        public void updateConnectionStatus(boolean connected) {
            JabraDevice device = mDeviceConnector.getConnectedDevice();

            mConnectedToJabra = connected;
            if (connected) {
                hideLoading();
                btnUpload.setEnabled(true);
                txtConn.setText("Connected");
                getDeviceInfo(device);
            } else {
                btnUpload.setEnabled(false);
                JabraConnection.this.showLoading("Connection lost!", false);
                txtConn.setText("Disconnected");
            }
        }
    };


    private Listener<Boolean> mReceiver = new DefaultListener<Boolean>() {
        @Override
        public void onProvided(Boolean connected) {
            if (connected && !mConnectedToJabra) {
                simpleSnackbarLong("Creating Connection",findViewById(R.id.lytMain));
                mDeviceConnector.findConnectedDevice();
            }
        }
    };
    public void simpleSnackbarLong(String string,View view){
        Snackbar.make(view, string, Snackbar.LENGTH_LONG).show();
    }

    private void showLoading(String text, boolean loading) { //NOSONAR - ok to have this method at this level and not in the presenter, could be useful here, too
        simpleSnackbarLong(text,findViewById(R.id.lytMain));
    }

    private void hideLoading() { //NOSONAR - ok to have this method at this level and not in the presenter, could be useful here, too
        prgbStatus.setVisibility(View.GONE);
    }

    private void getDeviceInfo(JabraDevice device) {
        txtTransport.setText(device.getNameFromTransport());
        txtID.setText(device.getId());

        device.getName(new Callback<String>() {
            @Override
            public void onProvided(String value) {
                txtName.setText(value);
            }

            @Override
            public void onError(JabraError error, Bundle params) {
                txtName.setText("??");
            }
        });
        device.getSerialNumber(new Callback<String>() {
            @Override
            public void onProvided(String value) {
                txtSerial.setText(value);
            }

            @Override
            public void onError(JabraError error, Bundle params) {
                txtSerial.setText("??");
            }
        });
        device.getVersion(new Callback<String>() {
            @Override
            public void onProvided(String value) {
                txtVersion.setText(value);
            }

            @Override
            public void onError(JabraError error, Bundle params) {
                txtVersion.setText("??");
            }
        });
        device.getBatteryStatus(1, new Listener<BatteryStatus>() {
            @Override
            public void onProvided(BatteryStatus value) {
                txtBatt.setText(value.getLevel() + "%" + (value.isCharging() ? " CHG " : "") + (value.isLow() ? " LOW " : ""));
            }

            @Override
            public void onError(JabraError error, Bundle params) {
                txtBatt.setText("??");
            }
        });
    }
}