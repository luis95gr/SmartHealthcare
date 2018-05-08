package com.example.luisguzmn.healthcare40.Cowtech54;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.luisguzmn.healthcare40.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.UUID;

public class CowService extends Service {
    private final String TAG = CowService.class.getSimpleName();

    // Binder given to clients
    private final IBinder cBinder = new CowBinder();

    private static Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread cConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private BluetoothAdapter mBTAdapter;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    //BtMessageManager
    public BtMessageManager btMessageManager;

    //Initilizing CSV File
    public BtMessageFiles fileCSV;

    //Preferences
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    private String deviceMac;
    private String deviceName;
    private boolean deviceConnected = false;


    public CowService() {
    }

    public boolean isDeviceConnected() {
        return deviceConnected;
    }

    public BtMessageFiles getFileCSV() {
        return fileCSV;
    }

    public void setFileCSV(BtMessageFiles fileCSV) {
        this.fileCSV = fileCSV;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Cow Service started");
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        //Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //Create BtMessageManager object
        btMessageManager = new BtMessageManager();

        //Init filcsv
        initBtMessageFiles();

        //Update the paired device textview
        deviceMac = sharedPref.getString("cow_paired_mac", "Not synced");
        deviceName = sharedPref.getString("cow_paired_name", "Not synced");

        //Load device and connect
        bluetoothConnectThread(deviceMac,  deviceName);
//        bluetoothConnectThread("98:D3:32:20:E2:08",  "COW");


        mHandler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    //readMessage = readMessage.replaceAll("�","");
                    //Delete first line if it does not begin with '['
                    /*char[] firstChar = {'c'}; //new char[1];
                    readMessage.getChars(0,1,firstChar,0);
                    if(firstChar[0]!='['){
                        recIDsText.setText(Character.toString(firstChar[0]));
                        readMessage = readMessage.replaceFirst(".*\n","");

                    }*/

                    //TODO string. substring( string.length()+1)   if equals ] then it is ok
                    btMessageManager.addNewMessage(readMessage);

//                    lostIDsText.setText(String.valueOf(btMessageManager.getLostIDs()));
//                    recIDsText.setText(String.valueOf(btMessageManager.getRecIDs()));
                    //recIDsText.setText(Character.toString(firstChar[0]));

                    //mReadBuffer.setText(btMessageManager.getMessagePurged());
                    //Using Copy to avoid conflict while saving in file
                    if(fileCSV!=null)
                        fileCSV.writeInFile(btMessageManager.getMessagePurgedCopy());
                    //mReadBuffer.setText(readMessage);
                }

                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1)
                        Log.d(TAG,"Connected to device: " + (String)(msg.obj));
                        // mBluetoothStatus.setText("Connected to Device: " + (String)(msg.obj));
                    else
                        Log.d(TAG,"Connection Fialed");
                        //mBluetoothStatus.setText("Connection Failed");
                }
            }
        };

    }

    public void initBtMessageFiles(){
        //Initilizing CSV File with stored preferences
        String extStore = System.getenv("EXTERNAL_STORAGE");
        File f_exts = new File(extStore);

        String dirStr = f_exts.getAbsolutePath() + File.separator + getResources().getString(R.string.app_name);

        /*String defaultString = getResources().getString(R.string.folder_string_default);
        String folderStr = sharedPref.getString(getString(R.string.folder_string), defaultString);

        defaultString = getResources().getString(R.string.prefix_string_default);
        String prefixStr = sharedPref.getString(getString(R.string.prefix_string), defaultString);

        int defaultInt = 5; //5 min of def
        int timeFileGenerator = sharedPref.getInt(getString(R.string.update_time_file), defaultInt);
       */

        String prefixStr;
        String folderStr;
        int timeFileGenerator;

        prefixStr = sharedPref.getString("cow_file_prefix", "WPQ");
        folderStr = sharedPref.getString("cow_folder","CTLogsWPQ");
        timeFileGenerator = Integer.valueOf(sharedPref.getString("cow_period_files", "3" ));

        fileCSV = new BtMessageFiles(prefixStr, folderStr);
        fileCSV.setParentDirStr(dirStr);
        //fileCSV.setFilePathTextView(filePathTxt);
        fileCSV.initTimerGenerator(timeFileGenerator);

        //Upd UI
        /*newTimeStatusTxt.setText(Integer.toString(timeFileGenerator));
        updateTimeTxt.setText(Integer.toString(timeFileGenerator));
        folderTxt.setText(folderStr);
        prefixTxt.setText(prefixStr);*/
    }

    public void bluetoothConnectThread(final String address, final String name){
        if(mBTAdapter.isEnabled()) {
            new Thread()
            {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    Log.d("BluetoothW", "Spawn threading");

                    try {
                        mBTSocket = createBluetoothSocket(device);

                        Log.d("BluetoothW", "bt socket created");
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        Log.d("BluetoothW", "bt socket TRYING TO connect!");
                        mBTSocket.connect();
                        //mBTSocket.connect();
                        Log.d("BluetoothW", "bt socket deviceConnected!");
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();

                            Log.d("BluetoothW", "bt socket TRYed, now Closed");
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!fail) {
                        cConnectedThread = new ConnectedThread(mBTSocket);
                        cConnectedThread.start();

                        //Send mode messages
                        //modeSend("BT", true);

                        try{
                            Thread.sleep(2000); //Wait to send the 2nd message
                        }catch(InterruptedException e){

                        }
                        //modeSend("SD", true);

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer; // = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            deviceConnected = true;
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        buffer = new byte[51200]; //supossing a 300Hz freq of IDs //byte[1024];
                        SystemClock.sleep(1000); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    deviceConnected = false;
                    break;
                }
            }
            deviceConnected = false;
        }

        //    Call this from the main activity to send data to the remote device
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
                Thread.sleep(500);
                Log.d("BluetoothW", "Sending String as Byte");
            } catch (IOException | InterruptedException ie) { }
        }

        // Call this from the main activity to shutdown the connection
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Cow Service Destroyed and STOPPED");
        if(cConnectedThread!=null) //If there is a Connected Thread initilized
            cConnectedThread.cancel();
        mHandler.removeCallbacks(cConnectedThread); //Remove possible conflicts
        //deviceConnected = false; //make sure it is false
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class CowBinder extends Binder {
        CowService getService() {
            // Return this instance of LocalService so clients can call public methods
            return CowService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return cBinder;
    }
}
