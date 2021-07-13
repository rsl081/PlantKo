package com.example.plantkoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditPlant extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener
{
    SpinnerActivity mySpinners = new SpinnerActivity(this);

    //ImageView
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView imageViewPic;

    //Edit Text
    private EditText plantNameEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;

    //SpinnerForCategory
    private Spinner spinnerCategory;

    //TextView
    private TextView timeTextView;
    //Button
    private Button addTimeBtn;
    private Button addPlantBtn;

    //References
    private byte[] selectedImage;
    private String plantName;
    private String catergory;
    private String description;
    private String mDate;
    private String mTime;
    int positionPlantListIndoor;
    int positionPlantListOutdoor;
    DatePickerDialog dateDialog;
    int mHour, mMinute;
    int mYear = 0, mMonth = 0, mDayOfMonth = 0;
    final int alarmtime = (int) System.currentTimeMillis();
    int ctrpending;

    //Database
    PlantDb plantDb;
    Plant plant_indoor;
    Plant plant_outdoor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_plant);

        Init();
    }

    private void Init()
    {
        Intent intent = getIntent();
        this. plantDb = new PlantDb(this);

        if(intent.hasExtra("cancel_alarm_indoor"))
        {
            ctrpending = intent.getIntExtra("cancel_alarm_indoor",0);
        }else{
            ctrpending = intent.getIntExtra("cancel_alarm_outdoor",0);
        }
        Log.v("HAPPY", "pending " + String.valueOf(ctrpending));

        Log.v("HAPPY", String.valueOf(alarmtime));
        //CancelAlarm();

        imageViewPic = findViewById(R.id.register_profile_imageview);
        plantNameEditText = findViewById(R.id.plantname_editext_register);
        spinnerCategory = findViewById(R.id.username_register);
        descriptionEditText = findViewById(R.id.email_editext);
        dateEditText = findViewById(R.id.editext_date_addplant);
        timeTextView = findViewById(R.id.time_addplant);
        //Buttons
        addTimeBtn = findViewById(R.id.time_button_addplant);
        addPlantBtn = findViewById(R.id.addplant_button);

        if(intent.hasExtra("com.example.plantkoapp.POSITIONPLANT_OUTDOOR") && intent.hasExtra("com.example.plantkoapp.PLANT_LIST_OUTDOOR"))
        {
            positionPlantListOutdoor = intent.getIntExtra(OutdoorFragment.POSITIONPLANT_OUTDOOR, 0);
            plant_outdoor = intent.getParcelableExtra(OutdoorFragment.PLANT_LIST_OUTDOOR);
            byte[] init_newPicPlant = plant_outdoor.getPlantbyteProfilePic();
            String init_plantName = plant_outdoor.getPlantName();
            String init_category = plant_outdoor.getPlantCategory();
            String init_description = plant_outdoor.getPlantDescription();
            String init_strDate = plant_outdoor.getPlantDate();
            String init_strTime = plant_outdoor.getPlantTime();

            Bitmap bitmapImages = BitmapFactory.decodeByteArray(init_newPicPlant, 0, init_newPicPlant.length);
            imageViewPic.setImageBitmap(bitmapImages);
            plantNameEditText.setText(init_plantName);
            int selectedPos;
            if(init_category.equals("Indoor"))
            {
                selectedPos = 0;
            }else{
                selectedPos = 1;
            }
            spinnerCategory.setSelection(selectedPos);
            descriptionEditText.setText(init_description);
            //dateEditText.setText(init_strDate);
            timeTextView.setText(init_strTime);
        }else{
            positionPlantListIndoor = intent.getIntExtra(IndoorFragment.POSITIONPLANT, 0);
            plant_indoor = intent.getParcelableExtra(IndoorFragment.PLANT_LIST);

            byte[] init_newPicPlant = plant_indoor.getPlantbyteProfilePic();
            String init_plantName = plant_indoor.getPlantName();
            String init_category = plant_indoor.getPlantCategory();
            String init_description = plant_indoor.getPlantDescription();
            String init_strDate = plant_indoor.getPlantDate();
            String init_strTime = plant_indoor.getPlantTime();

            Bitmap bitmapImages = BitmapFactory.decodeByteArray(init_newPicPlant, 0, init_newPicPlant.length);
            imageViewPic.setImageBitmap(bitmapImages);
            plantNameEditText.setText(init_plantName);
            int selectedPos;
            if(init_category.equals("Indoor"))
            {
                selectedPos = 0;
            }else{
                selectedPos = 1;
            }
            spinnerCategory.setSelection(selectedPos);
            descriptionEditText.setText(init_description);
            //dateEditText.setText(init_strDate);
            timeTextView.setText(init_strTime);
        }

        Listeners();
    }//End of Init

    public void Validation(){

        plantName = plantNameEditText.getText().toString();
        catergory = spinnerCategory.getSelectedItem().toString();
        description = descriptionEditText.getText().toString();
        mDate = dateEditText.getText().toString();
        mTime = timeTextView.getText().toString();

        if(IsError()){
            ShowErrorDialog();
        }else{
            UpdateList();
            StartAlarm();
        }
    }

    boolean IsError()
    {
        if(plantName.equals("") || description.equals("") ||
                mDate.equals("") || mTime.equals(""))
        {
            return true;
        }else{
            return false;
        }
    }

    private void Listeners()
    {
        dateEditText.setOnClickListener(this);
        imageViewPic.setOnClickListener(this);
        addTimeBtn.setOnClickListener(this);
        addPlantBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.addplant_button:
                CancelAlarm();
                Validation();
            break;
            case R.id.time_button_addplant:
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            break;
            case R.id.editext_date_addplant:
                Datepickerdialog();
            break;
            case R.id.register_profile_imageview:
                CaptureImage();
            break;
        }
    }//End of OnClick

    public void Datepickerdialog(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                mMonth = month;
                mDayOfMonth = dayOfMonth;
                mYear = year;
                dateEditText.setText(month+1+"/"+dayOfMonth+"/"+year);
                mDate = month+1+"/"+dayOfMonth+"/"+year;
            }
        }, year, month,day);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.show();
            }
        });

    }


    public void UpdateList(){
        Bitmap bitmap = ((BitmapDrawable) imageViewPic.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        String getPlantName = plantNameEditText.getText().toString();
        catergory = spinnerCategory.getSelectedItem().toString();
        String getDescription = descriptionEditText.getText().toString();

        Intent intentUpdateList = new Intent();
        Bundle bundle = this.getIntent().getExtras();
        if(catergory.equals("Outdoor"))
        {

            long plantId_indoor = bundle.getLong("plant_id_indoor");
            plantDb.UpdatePlant(plantId_indoor,imageInByte,getPlantName,catergory,getDescription,mDate,mTime, alarmtime);

            intentUpdateList.putExtra("update_list_indoor", positionPlantListIndoor);
            intentUpdateList.putExtra("edit_plant_indoor", plant_indoor);



            long plantId_indoor2 = bundle.getLong("plant_id_outdoor");
            plantDb.UpdatePlant(plantId_indoor2,imageInByte,getPlantName,catergory,getDescription,mDate,mTime, alarmtime);

            intentUpdateList.putExtra("update_list_outdoor", positionPlantListOutdoor);
            intentUpdateList.putExtra("edit_plant_outdoor", plant_outdoor);
        }
        if(catergory.equals("Indoor")){

            long plantId_indoor = bundle.getLong("plant_id_outdoor");
            plantDb.UpdatePlant(plantId_indoor,imageInByte,getPlantName,catergory,getDescription,mDate,mTime, alarmtime);

            intentUpdateList.putExtra("update_list_outdoor", positionPlantListOutdoor);
            intentUpdateList.putExtra("edit_plant_outdoor", plant_outdoor);

            long plantId_indoor2 = bundle.getLong("plant_id_indoor");
            plantDb.UpdatePlant(plantId_indoor2,imageInByte,getPlantName,catergory,getDescription,mDate,mTime, alarmtime);

            intentUpdateList.putExtra("update_list_indoor", positionPlantListIndoor);
            intentUpdateList.putExtra("edit_plant_indoor", plant_indoor);

        }
        setResult(RESULT_OK, intentUpdateList);
        finish();
    }

    public void ShowErrorDialog(){
        String str_plantname = "Plant Name is Missing!\n";
        String str_description = "\nDescription is Missing!\n";
        String str_date = "\nDate is Missing!\n";

        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle("Missing In Action!");
        error.setMessage((this.plantName.matches("") ? str_plantname : str_plantname.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.description.matches("") ? str_description : str_description.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.mDate.matches("") ? str_date : str_date.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")));
        error.setPositiveButton("Okay",null);
        error.show();
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
                    imageViewPic.setImageBitmap(photo);
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
                        imageViewPic.setImageURI(uri);
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

    //===========================================ALARM MANAGER=======================================
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        mHour = hourOfDay;
        mMinute = minute;
        updateTimeText(c);
    }

    private void updateTimeText(Calendar c)
    {
        mTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        timeTextView.setText(mTime);
    }

    private void StartAlarm()
    {
        mMonth++;
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDayOfMonth);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendar.getTimeInMillis();

        AlarmManager mgrAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("thetime", mTime);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmtime, intent, 0);
        mgrAlarm.setExact(AlarmManager.RTC_WAKEUP, selectedTimestamp, pendingIntent);
//        mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, selectedTimestamp,
//                24*60*60*1000 , pendingIntent);
    }

    private void CancelAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ctrpending, intent, 0);
//        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

}