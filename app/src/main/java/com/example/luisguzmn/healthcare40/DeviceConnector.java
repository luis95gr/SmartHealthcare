package com.example.luisguzmn.healthcare40;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.jabra.sdk.api.DefaultListener;
import com.jabra.sdk.api.JabraConnectionManager;
import com.jabra.sdk.api.JabraDevice;
import com.jabra.sdk.api.JabraError;
import com.jabra.sdk.api.JabraHelper;
import com.jabra.sdk.api.Listener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by JeppeLeth on 03/09/2017.
 */
public class DeviceConnector {

    private static final String TAG = "DeviceConnector";

    public interface Presenter2 {
        void showMessage(String message, boolean loading);

        void noDevice();

        void updateConnectionStatus(boolean connected);
    }

    private static DeviceConnector sInstance;

    private JabraConnectionManager mConnectionManager;
    private JabraDevice mConnectedDevice;
    private final Handler mUiHandler = new Handler();
    private Context mContext;
    private Set<Presenter2> mPresenters = new HashSet<>(2);
    private Set<Listener<Boolean>> mConnectionStateListeners = new HashSet<>(2);
    private Listener<Boolean> mConnectionStateListener = new DefaultListener<Boolean>() {
        @Override
        public void onProvided(Boolean aBoolean) {
            for (Listener<Boolean> client : mConnectionStateListeners) {
                client.onProvided(aBoolean);
            }
        }
    };

    public static DeviceConnector getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DeviceConnector(context);
        }
        return sInstance;
    }

    private DeviceConnector(Context context) {
        mContext = context.getApplicationContext();
        reInitializeConnectionManager();
    }

    private void reInitializeConnectionManager() {
        JabraHelper.getJabraConnectionManagerInstance(mContext, new Listener<JabraConnectionManager>() {
            @Override
            public void onProvided(JabraConnectionManager value) {
                mConnectionManager = value;
                DeviceConnector.this.initJabraManager();
            }

            @Override
            public void onError(JabraError e, Bundle params) {
                mConnectionManager = null;
                Toast.makeText(mContext, "Service issue. " + e, Toast.LENGTH_LONG).show();
            }

        }, mUiHandler);
    }

    public void registerPresenter(Presenter2 presenter) {
        mPresenters.add(presenter);
    }

    public boolean unregisterPresenter(Presenter2 presenter) {
        return mPresenters.remove(presenter);
    }


    public void registerConnectionStateListener(Listener<Boolean> listener) {
        mConnectionStateListeners.add(listener);
    }

    public void unregisterConnectionStateListener(Listener<Boolean> listener) {
        mConnectionStateListeners.remove(listener);
    }

    private void initJabraManager() {
        mConnectionManager.addConnectionStateListener(mConnectionStateListener);
        lookForTheVoiceDevice();
    }

    public void findConnectedDevice() {
        lookForTheVoiceDevice();
    }

    private void lookForTheVoiceDevice() {
        if (mConnectionManager != null) {
            postShowMessage("Looking for devices", true);
            boolean found = false;
            List<JabraDevice> devices = mConnectionManager.getJabraDevices();
            for (JabraDevice device : devices) {
                if (device.isVoiceAudioConnected()) {
                    connectToDevice(device);
                    found = true;
                    break;
                }
            }
            if (!found) {
                postNoDevice();
                postShowMessage("No devices found", false);
            }
        } else {
            reInitializeConnectionManager();
        }
    }

    private void connectToDevice(final JabraDevice device) {
        postShowMessage("Connect to device\n" + device.getNameFromTransport(), true);
        device.connect(new Listener<Boolean>() {
            @Override
            public void onProvided(Boolean value) {
                Log.d(TAG, "onProvided() called with: value = [" + value + "]");
                if (value) {
                    postUpdateConnectionStatus(true);
                    deviceConnected(device);
                } else {
                    postUpdateConnectionStatus(false);
                    Log.e(TAG, "connectToDevice: failed");
                }
            }

            @Override
            public void onError(JabraError error, Bundle params) {
                deviceConnected(null);
                postNoDevice();
                postShowMessage("Connect error, " + error, false);
                Log.e(TAG, "onError() called with: error = [" + error + "], params = [" + params + "]");
            }
        });
    }

    @Nullable
    public JabraDevice getConnectedDevice() {
        return mConnectedDevice;
    }

    private void postUpdateConnectionStatus(final boolean connected) {
        for (final Presenter2 presenter : mPresenters) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    presenter.updateConnectionStatus(connected);
                }
            });
        }
    }

    private void postNoDevice() {
        for (final Presenter2 presenter : mPresenters) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    presenter.noDevice();
                }
            });
        }
    }

    private void postShowMessage(final String message, final boolean loading) {
        for (final Presenter2 presenter : mPresenters) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    presenter.showMessage(message, loading);
                }
            });
        }
    }

    private void deviceConnected(final JabraDevice device) {
        mConnectedDevice = device;
    }

    public void destroy() {
        mUiHandler.removeCallbacksAndMessages(null);
        mPresenters.clear();
        if (mConnectionManager != null) {
            mConnectionStateListeners.clear();
            mConnectionManager.removeConnectionStateListener(mConnectionStateListener);
            mConnectionManager.close();
            mConnectedDevice = null;
            mConnectionManager = null;
        }
    }

}