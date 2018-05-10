package com.example.luisguzmn.healthcare40;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguzmn.healthcare40.Helo.CountrySpinnerItem;
import com.example.luisguzmn.healthcare40.Helo.HeloConnection;
import com.example.luisguzmn.healthcare40.Helo.PinActivity;
import com.worldgn.connector.Connector;
import com.worldgn.connector.ICallbackPin;
import com.worldgn.connector.ILoginCallback;
import com.worldgn.connector.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class crearCuentaHelo extends Activity {

    //VARIABLES
    EditText editTextEmail, editTextPhone, app_key, app_token; //
    AppCompatSpinner countrySpinner;
    TextView text_1;
    private List<CountrySpinnerItem> countrySpinnerItems;
    private String prefix = "",countryName;
    private final static String TAG = crearCuentaHelo.class.getSimpleName();
    ProgressDialog progressDialog;
    SharedPreferences spLogin;
    //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta_helo);
        spLogin = PreferenceManager.getDefaultSharedPreferences(this);
        if (Connector.getInstance().isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        //CAST
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhone = (EditText) findViewById(R.id.phonenumber);
        app_key = (EditText) findViewById(R.id.app_key);
        app_token = (EditText) findViewById(R.id.app_token);
        countrySpinner= (AppCompatSpinner) findViewById(R.id.countrySpinner);
        text_1=(TextView) findViewById(R.id.text_1);
        countrySpinnerItems=getCountries(this);
        ArrayAdapter<CountrySpinnerItem> adapterCountry = new ArrayAdapter<CountrySpinnerItem>(
                this, R.layout.spinner_item, countrySpinnerItems);
        adapterCountry.setDropDownViewResource(R.layout.spinner_item_dropdown);
        countrySpinner.setAdapter(adapterCountry);
        countrySpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        //
        editTextEmail.setText("healthcare4pef@gmail.com");
        editTextPhone.setText(spLogin.getString("phone","no phone"));
        app_key.setText("152384277176254522");
        app_token.setText("B9B2E74B829AC1FCE45FFF44BE772CE8C5DD2200");

        /////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////LOGIN AUTOMATICO/////////////////////////////////////////////////////

        if(!TextUtils.isEmpty(app_key.getText().toString()) && !TextUtils.isEmpty(app_token.getText().toString())) {
            Connector.getInstance().initialize(this,app_key.getText().toString(), app_token.getText().toString());
            if (Util.isvalidEmail(editTextEmail.getText().toString()) ) {
                editTextPhone.getText().clear();//to clear phone number to avoid
                final String email = editTextEmail.getText().toString();
                //prefHelper.setString(Constants.PREF_DEV_ENVIRONMENT,"0");
                progressDialog.show();
                Connector.getInstance().login(email, new ILoginCallback() {

                    @Override
                    public void onSuccess(long heloUserId) {
                        SharedPreferences.Editor spLoginEditor = spLogin.edit();
                        spLoginEditor.putBoolean("success",true);
                        spLoginEditor.apply();
                        progressDialog.cancel();
                        startActivity(new Intent(crearCuentaHelo.this, HeloConnection.class));
                        finish();
                    }

                    @Override
                    public void onPinverification() {
                        progressDialog.cancel();
                        Intent intent = new Intent(crearCuentaHelo.this, PinActivity.class);
                        intent.putExtra(PinActivity.ACTION_TYPE, PinActivity.ACTION_EMAIL);
                        intent.putExtra(PinActivity.ACTION_EMAIL, email);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String description) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void accountVerification() {

                    }
                });
            } else if (Util.isvalidPhone(editTextPhone.getText().toString())) {
                editTextEmail.getText().clear();
                final String phone = editTextPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "Enter phone address", Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog.show();
                Connector.getInstance().login(text_1.getText().toString(), phone, new ILoginCallback() {

                    @Override
                    public void onSuccess(long heloUserId) {
                        SharedPreferences.Editor spLoginEditor = spLogin.edit();
                        spLoginEditor.putBoolean("success",true);
                        spLoginEditor.apply();
                        progressDialog.cancel();
                        startActivity(new Intent(crearCuentaHelo.this, HeloConnection.class));
                    }

                    @Override
                    public void onPinverification() {
                        progressDialog.cancel();
                        Intent intent = new Intent(crearCuentaHelo.this, PinActivity.class);
                        intent.putExtra(PinActivity.ACTION_TYPE, PinActivity.ACTION_PHONE);
                        intent.putExtra(PinActivity.ACTION_PHONE, phone);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String description) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void accountVerification() {

                    }
                });
            }
        } else {
            Toast.makeText(this, "Please fill APP KEY and APP TOKEN", Toast.LENGTH_LONG).show();
        }

        /////////////////////////////////////////////////////////////////////////////////////////////

    }

    public void onClick(View view) {

        /*if (view.getId() == R.id.login) {
            if(!TextUtils.isEmpty(app_key.getText().toString()) && !TextUtils.isEmpty(app_token.getText().toString())) {
                Connector.getInstance().initialize(this,app_key.getText().toString(), app_token.getText().toString());
                if (Util.isvalidEmail(editTextEmail.getText().toString()) ) {
                    editTextPhone.getText().clear();//to clear phone number to avoid
                    final String email = editTextEmail.getText().toString();
                    //prefHelper.setString(Constants.PREF_DEV_ENVIRONMENT,"0");
                    progressDialog.show();
                    Connector.getInstance().login(email, new ILoginCallback() {

                        @Override
                        public void onSuccess(long heloUserId) {
                            progressDialog.cancel();
                            startActivity(new Intent(crearCuentaHelo.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onPinverification() {
                            progressDialog.cancel();
                            Intent intent = new Intent(crearCuentaHelo.this, PinActivity.class);
                            intent.putExtra(PinActivity.ACTION_TYPE, PinActivity.ACTION_EMAIL);
                            intent.putExtra(PinActivity.ACTION_EMAIL, email);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String description) {
                            progressDialog.cancel();
                            Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void accountVerification() {

                        }
                    });
                } else if (Util.isvalidPhone(editTextPhone.getText().toString())) {
                    editTextEmail.getText().clear();
                    final String phone = editTextPhone.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(getApplicationContext(), "Enter phone address", Toast.LENGTH_LONG).show();
                        return;
                    }
                    progressDialog.show();
                    Connector.getInstance().login(text_1.getText().toString(), phone, new ILoginCallback() {

                        @Override
                        public void onSuccess(long heloUserId) {
                            progressDialog.cancel();
                            startActivity(new Intent(crearCuentaHelo.this, MainActivity.class));
                        }

                        @Override
                        public void onPinverification() {
                            progressDialog.cancel();
                            Intent intent = new Intent(crearCuentaHelo.this, PinActivity.class);
                            intent.putExtra(PinActivity.ACTION_TYPE, PinActivity.ACTION_PHONE);
                            intent.putExtra(PinActivity.ACTION_PHONE, phone);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String description) {
                            progressDialog.cancel();
                            Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void accountVerification() {

                        }
                    });
                }
            } else {
                Toast.makeText(this, "Please fill APP KEY and APP TOKEN", Toast.LENGTH_LONG).show();
            }

        }else if(view.getId() == R.id.CallBack_Phone_Pin){
            if(Util.isvalidPhone(editTextPhone.getText().toString())){
                final String phone = editTextPhone.getText().toString();
                final String prefix=text_1.getText().toString();
                progressDialog.show();
                Connector.getInstance().CallBack_Phone(false,prefix, phone, new ICallbackPin() {

                    @Override
                    public void onPinverification() {
                        progressDialog.cancel();
                        Intent intent = new Intent(crearCuentaHelo.this, PinActivity.class);
                        intent.putExtra(PinActivity.ACTION_TYPE, PinActivity.ACTION_PHONE);
                        intent.putExtra(PinActivity.ACTION_PHONE, phone);
                        startActivity(intent);
                        Toast.makeText(crearCuentaHelo.this,"onPinverification",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String description) {
                        progressDialog.cancel();
                        Toast.makeText(crearCuentaHelo.this,"onFailure",Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Toast.makeText(this,"not valid number",Toast.LENGTH_LONG).show();
            }
        }else if(view.getId() == R.id.CallBack_Email_Pin){
            if(Util.isvalidEmail(editTextEmail.getText().toString())){
                final String email = editTextEmail.getText().toString();
                progressDialog.show();
                Connector.getInstance().CallBack_Phone(true, "00", email, new ICallbackPin() {

                    @Override
                    public void onPinverification() {
                        progressDialog.cancel();
                        Intent intent = new Intent(crearCuentaHelo.this, PinActivity.class);
                        intent.putExtra(PinActivity.ACTION_TYPE, PinActivity.ACTION_EMAIL);
                        intent.putExtra(PinActivity.ACTION_EMAIL, email);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String description) {
                        progressDialog.cancel();
                        Toast.makeText(crearCuentaHelo.this,"onFailure",Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Toast.makeText(this,"not valid Email",Toast.LENGTH_LONG).show();
            }
        }*/
    }

    private List<CountrySpinnerItem> getCountries(Context context) {
        List<CountrySpinnerItem> countrySpinnerItems = new ArrayList<CountrySpinnerItem>();
        try {
            InputStream in = context.getAssets().open("PrefissIntern.txt");
            BufferedReader re = new BufferedReader(new InputStreamReader(in));
            String temp;
            while ((temp = re.readLine()) != null) {
                String[] tempArr = temp.split(";");
                if (tempArr.length == 2) {
                    countrySpinnerItems.add(new CountrySpinnerItem(tempArr[0]
                            .trim(), tempArr[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countrySpinnerItems;
    }

    private class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Log.i(TAG, "arg2 = " + pos + "   position = " );
            Object item = parent.getItemAtPosition(pos);
            if (item != null) {
                //Toast.makeText(crearCuentaHelo.this, item.toString(),
                        //Toast.LENGTH_SHORT).show();
            }
            prefix = ((CountrySpinnerItem) countrySpinner.getSelectedItem()).getCode();
            countryName = ((CountrySpinnerItem) countrySpinner.getSelectedItem()).getName();
            if (!prefix.equals("-1")) {
                Log.i(TAG, "prefix = " + prefix);
                text_1.setText(prefix);
            }
            Log.d(TAG, "onItemSelected and the prefix is " + prefix);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.i(TAG, "onNothingSelected : position = " + "onNothingSelected");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}