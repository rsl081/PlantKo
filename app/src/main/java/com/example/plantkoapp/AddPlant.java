package com.example.plantkoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AddPlant extends AppCompatActivity implements View.OnClickListener {

    SpinnerActivity mySpinners = new SpinnerActivity(this);

    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView imageViewCapture;

    //Add Plant EdiText
    EditText editTextPlantName;
    EditText editTextDescription;


    //Add Plant Buttons
    Button addPlantBtn;

    //References
    private byte[] selectedImage;
    String plantName;
    String catergory;
    String description;
    String date;
    String time;

    //SQLite
    private PlantDb plantDb;
    Intent intentUpdateList;
    Plant plant;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_plant);

        Init();
    }

    private void Init()
    {
        this.plantDb = new PlantDb(this);

        try {
            Uri uri = Uri.parse("android.resource://com.example.plantkoapp/drawable/unisex");
            InputStream stream = getContentResolver().openInputStream(uri);
            selectedImage = GetBytes(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        editTextPlantName = findViewById(R.id.plantname_editext_register);
        mySpinners.MySpinner();
        editTextDescription = findViewById(R.id.email_editext);


        //ImageView
        imageViewCapture = findViewById(R.id.register_profile_imageview);

        //Button
        addPlantBtn = findViewById(R.id.addplant_button);

        Listeners();
    }


    public void Listeners()
    {
        imageViewCapture.setOnClickListener(this);
        addPlantBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.addplant_button:
                Validation();
            break;
            case R.id.register_profile_imageview:
                CaptureImage();
            break;
        }
    }

    private void Validation() {
        plantName = editTextPlantName.getText().toString().trim();
        catergory = mySpinners.PlantCategoryMethod();
        description = editTextDescription.getText().toString().trim();


       if (IsError()) {
            ShowErrorDialog();
        } else {
            AddList();
        }
    }

    boolean IsError()
    {
        if(plantName.equals("") || description.equals(""))
        {
            return true;
        }else{
            return false;
        }
    }

    public void ShowErrorDialog(){
        String str_plantname = "Plant Name is Missing!\n";
        String str_description = "\nDescription is Missing!\n";

        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle("Missing In Action!");
        error.setMessage((this.plantName.matches("") ? str_plantname : str_plantname.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.description.matches("") ? str_description : str_description.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")));

        error.setPositiveButton("Okay",null);
        error.show();
    }

    private void AddList()
    {
        intentUpdateList = new Intent();
        String getPlantName = editTextPlantName.getText().toString();
        //String getCategory = mySpinners.PlantCategoryMethod();
        String getDescription = editTextDescription.getText().toString();

        Bundle bundle = this.getIntent().getExtras();
        long accountID = bundle.getLong("add_plant",0);

        plant = plantDb.createdPlant(selectedImage,getPlantName,catergory,getDescription,"7/7/2022", "6:30 PM",accountID);

        intentUpdateList.putExtra("new_plant", plant);
        setResult(RESULT_OK, intentUpdateList);
        finish();

    }

    //============================================IMG SECTION========================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    selectedImage = stream.toByteArray();
                    imageViewCapture.setImageBitmap(photo);
                    try {
                        SaveImage(photo, "happy");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        InputStream iStream =   getContentResolver().openInputStream(uri);
                        selectedImage = GetBytes(iStream);
                        imageViewCapture.setImageURI(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }//switch
    }// End of ActivityResult Curly Braces

    private void SaveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "PlantKo");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "PlantKo";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }
        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }

    public byte[] GetBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void CaptureImage()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            DialogForImages();
        }
    }

    private void DialogForImages()
    {
        String[] items = {"Take Photo", "Choose from gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Take Photo")){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }else if(items[which].equals("Choose from gallery")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , GALLERY_REQUEST);
                }else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    //===========================================END Of IMG SECTION==================================
}