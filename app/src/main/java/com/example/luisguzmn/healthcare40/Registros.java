package com.example.luisguzmn.healthcare40;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Registros extends AppCompatActivity {
private Button choose,upload;

    //los archivos que se suban estaran en la carpeta de /dataVar/ecgdb en el server
    //yo incluí la opcion de que te dejara seleccionar que querias subir, pero esto se elimina teniendo
    //el path del archivo a subir
    //si el archivo esta almacenado en la memoria externa, entonces se debe dar permiso para acceder a ellos

    //este string es para indicar la ruta del archivo a subir (.txt)
    private String pathF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        choose = (Button) findViewById(R.id.choose);
        upload = (Button)findViewById(R.id.upload);

        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);

      final  Date todays = Calendar.getInstance().getTime();
      final  SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");
     final   SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

     //esta seccion de codigo es para seleccionar el archivo que se va a subir, ya teniendo el path,
        //esto se elimina
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize Builder
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(Registros.this)
                        .withFragmentManager(getFragmentManager())
                        .withMemoryBar(true)
                        .allowCustomPath(true)
                        .setType(StorageChooser.FILE_PICKER)
                        .build();
            // Show dialog whenever you want by
                chooser.show();
                // get path that the user has chosen
                chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
                    @Override
                    public void onSelect(String path) {
                        Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
                        pathF=path;
                    }
                });

            }
        });


        //codigo para cargar el archivo al server
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdate = date.format(todays);
                String stime = time.format(todays);

                // estos string se cambian por las variables que estan en shared preferences
                String email = "emilio.perez@udem.edu";
                String pass = "kkk";

                //Uploading code
                try {
                    String uploadId = UUID.randomUUID().toString();
                    //Creating a multi part request
                    new MultipartUploadRequest(getApplicationContext(), uploadId,"http://meddata.sytes.net/dataVar/registerECG.php")
                            .addFileToUpload(pathF, "value") //Adding file
                            .addParameter("email", email) //Adding text parameter to the request
                            .addParameter("pass",pass)
                            .addParameter("var","ecg")
                            .addParameter("date",sdate)
                            .addParameter("time",stime)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } catch (Exception exc) {
                    Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
