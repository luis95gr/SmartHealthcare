package com.example.luisguzmn.healthcare40;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.graphics.Color;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


public class MainScreen extends MenuToolbar{//MenuToolbar extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //VARIABLES
    BluetoothAdapter bluetoothAdapter;
    //AGREGAR ESTAS VARIABALES
    int headphones = 0;
    int smartwatch = 0;
    int tipoDisp = 0;
    File image;
    //
    private ImageView imageView;
    ImageButton imageButtonSS,imageButtonSH,imageButtonSW;
    Button buttonStart;
    TextView text_monday,text_tuesday,text_wednesday,text_thursday,text_friday,text_saturday,text_sunday;
    private static final int REQUEST_ENABLED = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE = 0;
    //
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    private File file = new File(ruta_fotos);
    String path;
    File fileImagen;
    Bitmap imageBitmap;
    //
    //UPLOAD IMAGES
    //Image request code
    private int PICK_IMAGE_REQUEST = 0;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;
    String mCurrentPhotoPath;
    boolean booleanTake;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        requestStoragePermission();
        //
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //
        //CAST
        imageView = (ImageView) findViewById(R.id.image_profile);
        TextView textNameMain = (TextView) findViewById(R.id.textNameMain);
        text_monday = (TextView) findViewById(R.id.text_monday);
        text_tuesday = (TextView) findViewById(R.id.text_tuesday);
        text_wednesday = (TextView) findViewById(R.id.text_wednesday);
        text_thursday = (TextView) findViewById(R.id.text_thursday);
        text_friday = (TextView) findViewById(R.id.text_friday);
        text_saturday = (TextView) findViewById(R.id.text_saturday);
        text_sunday = (TextView) findViewById(R.id.text_sunday);
        imageButtonSW = (ImageButton) findViewById(R.id.imageButtonSW);
        imageButtonSH = (ImageButton) findViewById(R.id.imageButtonSH);
        imageButtonSS = (ImageButton) findViewById(R.id.imageButtonSS);
        buttonStart = (Button)findViewById(R.id.buttonStart);

        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarMain);
//        MenuToolbar menu = new MenuToolbar(toolbar,"Main",this);
        this.setToolbar(toolbar);
        this.setContext(this);
        this.setTitleToolbar("Main");
        this.loadToolbar();

        //-------------------------------------------------------------------------------------------
        //MENU

        //MAIN SCREEN
        LottieAnimationView animationView_nivel = (LottieAnimationView) findViewById(R.id.animation_view_nivel);
        LottieAnimationView animationView_nivel_rojo = (LottieAnimationView) findViewById(R.id.animation_view_nivel_rojo);
        Picasso.with(this).load("http://meddata.sytes.net/newuser/profileImg/" + sp.getString("imagen", "No Image"))
                .resize(250, 250).centerCrop().into(imageView);
        Toast.makeText(this, sp.getString("imagen", "No Image"), Toast.LENGTH_LONG).show();
        animationView_nivel.setSpeed(10f);
        animationView_nivel_rojo.setSpeed(100f);
        animationView_nivel.playAnimation(0, 10);
        animationView_nivel_rojo.playAnimation(0, 30);
        textNameMain.setText(sp.getString("name", "No name"));


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
        //////////////////////////////////////////////////////////////////////////////////////////////////
        //BOTONES
        imageButtonSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageButtonSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        imageButtonSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, Bluetooth.class);
                startActivity(intent);
            }
        });
        ///////////////////////////////////////////////////////////////////////
        ///AQUI ES LA PARTE DEL BLUETOOTH DENTRO DEL ON CREATE
        //BLUETOOTH/////////
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices;

        if(bluetoothAdapter != null) { // check this condition DUE to emulator without BT
            pairedDevices = bluetoothAdapter.getBondedDevices();



            if (pairedDevices.size() > 0) {
                //Toast.makeText(getApplicationContext(), pairedDevices.size() + " bLUETOOTH DEVICEs paired.", Toast.LENGTH_SHORT).show();
                for (BluetoothDevice device : pairedDevices) {

                    BluetoothClass bluetoothClass = device.getBluetoothClass();
                    int estado = device.getBondState();

                    if (estado == BluetoothDevice.BOND_BONDED) {
                        int tipoDeDispositivo = bluetoothClass.getDeviceClass();
                        if (tipoDeDispositivo == 1028 || tipoDeDispositivo==7936) {
                            headphones = headphones + 1;
                            imageButtonSH.getBackground().setTint(Color.parseColor("#ADFF2F"));
                        }

                        if (tipoDeDispositivo == 268 || tipoDeDispositivo==7936) {
                            imageButtonSW.getBackground().setTint(Color.parseColor("#ADFF2F"));
                            smartwatch = smartwatch + 1;
                        }
                        if(tipoDeDispositivo == 1032){
                            //imageButtonSS.getBackground().setTint(Color.parseColor("#ADFF2F"));
                        }
                    }else{
                        imageButtonSH.getBackground().setTint(Color.parseColor("#b8ddcd"));
                        imageButtonSW.getBackground().setTint(Color.parseColor("#b8ddcd"));
                        imageButtonSS.getBackground().setTint(Color.parseColor("#b8ddcd"));
                    }


                }
            }

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Bluetooth not found in Device", Toast.LENGTH_SHORT).show();






        //Toast.makeText(getApplicationContext(), smartwatch + " smartwatch paired.", Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(), headphones + " headphones paired.", Toast.LENGTH_SHORT).show();


        // END BLUETOOTH
        //set image profile
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    public void uploadMultipart() {
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //getting user email
        String email = sp.getString("email","no email");
        //getting the actual path of the image
        String path = getPath(filePath);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId,"http://meddata.sytes.net/newuser/update.php")
                    .addFileToUpload(path, "photo") //Adding file
                    .addParameter("email", email) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public void ShowDialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("CAMBIAR IMAGEN DE PERFIL");
        //builder.setMessage("");
        builder.setPositiveButton("BUSCAR EN EL DISPOSITIVO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showFileChooser();
            }
        })
        .setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setNegativeButton("TOMAR FOTO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File

                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(MainScreen.this,
                                        "com.example.android.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, 1);
                            }
                        }


                    }
                });
        builder.create().show();
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadMultipart();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            uploadMultipart2();
        }
    }

    public void uploadMultipart2() {
        //getting user email
        String email = "c";
        //getting the actual path of the image
        String path = mCurrentPhotoPath;

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId,"http://meddata.sytes.net/newuser/update.php")
                    .addFileToUpload(path, "photo") //Adding file
                    .addParameter("email", email) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }




    //ELIMINAR BACK PRESS
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK ? true : super.onKeyDown(keyCode, event));
    }
    //MENU 3 DOTS
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(MainScreen.this,MainScreen.class));
                return true;

            case R.id.logout:
                SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.apply();
                SharedPreferences spLogin = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor spLoginEditor = spLogin.edit();
                spLoginEditor.putBoolean("success",false);
                spLoginEditor.apply();
                startActivity(new Intent(MainScreen.this,MainActivity.class));
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }


    }
    //END MENU 3 DOTS

}


