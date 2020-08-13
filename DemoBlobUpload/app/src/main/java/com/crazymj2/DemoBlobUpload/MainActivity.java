package com.crazymj2.DemoBlobUpload;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    String storageConnectionString = "Add Connection String Here";
    String storageContainerName = "Add Container Name Here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.click_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To open up a gallery browser
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri selectImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                ContentResolver contentResolver = getContentResolver();
                if (contentResolver != null && selectImage != null) {
                    Cursor cursor = contentResolver.query(selectImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        if (picturePath != null) {
                            logText("picturePath******:" + picturePath);
                            uploadFile(picturePath);
                        }
                        cursor.close();
                    }
                }
            }
        }
    }

    private void uploadFile(final String path) {
        logText("uploadFile******:" + path);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //Here starts the code for Azure Storage Blob
                try {
                    CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

                    // Create the blob client
                    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

                    // Get a reference to a container
                    // The container name must be lower case

                    CloudBlobContainer container = blobClient.getContainerReference(storageContainerName);

                    // Create the container if it does not exist
                    container.createIfNotExists();

                    // Create a permissions object
                    BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

                    // Include public access in the permissions object
                    containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

                    // Set the permissions on the container
                    container.uploadPermissions(containerPermissions);

                    // Create or overwrite the "myimage.jpg" blob with contents from a local file
                    File source = new File(path);
                    CloudBlockBlob blob = container.getBlockBlobReference(source.getName());
                    blob.getProperties().setContentType("image/jpeg");
                    blob.upload(new FileInputStream(source), source.length());


                    String uploadUrl = blob.getUri().toString();
                    logText("uploadUrl******:" + uploadUrl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void logText(String message) {
        Log.d(BuildConfig.APPLICATION_ID, message);
    }


}