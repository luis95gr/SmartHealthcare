package com.example.luisguzmn.healthcare40;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.UUID;

public class Registros extends AppCompatActivity {
private Button choose,upload;
private String pathF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        choose = (Button) findViewById(R.id.choose);
        upload = (Button)findViewById(R.id.upload);



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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting user email
                String email = "emilio.perez@udem.edu";
                //getting the actual path of the image

                //Uploading code
                try {
                    String uploadId = UUID.randomUUID().toString();

                    //Creating a multi part request
                    new MultipartUploadRequest(getApplicationContext(), uploadId,"http://meddata.sytes.net/ecgbddata/setecg.php")
                            .addFileToUpload(pathF, "ecgName") //Adding file
                            .addParameter("email", email) //Adding text parameter to the request
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
