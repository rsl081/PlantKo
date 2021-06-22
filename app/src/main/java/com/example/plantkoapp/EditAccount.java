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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EditAccount extends AppCompatActivity implements View.OnClickListener {

    //Camera
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView imageViewCapture;

    //Register EditText
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmpassEditText;
    private EditText locationEditText;

    //Register Btn
    Button signupBtn;

    //References
    private long mId;
    private byte[] selectedImage = null;
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String confirmpass;
    private String location;
    private DBHelper dbHelper;
    //Database
    private AccountDb accountDb;
    private Account account;
    int positionOfAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);
        Init();
    }

    private void Init()
    {
        this.accountDb = new AccountDb(this);
        try {
            Uri uri = Uri.parse("android.resource://com.example.plantkoapp/drawable/unisex");
            InputStream stream = getContentResolver().openInputStream(uri);
            selectedImage = GetBytes(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        positionOfAccount = intent.getIntExtra(Home.POSITION, 0);

        account = intent.getParcelableExtra(Home.ACCOUNT_LIST);

        //Get Information
        byte[] newProfile = account.getByteUserPofilePic();
        String name = account.getFullname();
        String username = account.getUsername();
        String email = account.getEmail();
        String password = account.getPassword();
        String location = account.getLocation();

        imageViewCapture = findViewById(R.id.register_profile_imageview);
        nameEditText = findViewById(R.id.plantname_editext_register);
        usernameEditText = findViewById(R.id.username_register);
        emailEditText = findViewById(R.id.email_editext);
        passwordEditText = findViewById(R.id.editext_password);
        confirmpassEditText = findViewById(R.id.editext_confirmpass);
        locationEditText = findViewById(R.id.editext_location);

        Bitmap bitmapImages = BitmapFactory.decodeByteArray(newProfile, 0, newProfile.length);
        imageViewCapture.setImageBitmap(bitmapImages);
        nameEditText.setText(name);
        usernameEditText.setText(username);
        emailEditText.setText(email);
        passwordEditText.setText(password);
        locationEditText.setText(location);

        //Button
        signupBtn = findViewById(R.id.addplant_button);
        //Listeners
        imageViewCapture.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.addplant_button:
                SaveChanges();
                break;
            case R.id.register_profile_imageview:
                CaptureImage();
                break;
        }
    }

    private void Validation() {
        fullname = nameEditText.getText().toString().trim();
        username = usernameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        confirmpass = confirmpassEditText.getText().toString().trim();
        location = locationEditText.getText().toString().trim();

        if (!password.equals(confirmpass)) {
            ShowPasswordDialog();
        } else if (IsError()) {
            ShowErrorDialog();
        } else {
            ShowTempInformation();
        }
    }

    public void ShowPasswordDialog()
    {
        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle("Password Error!");
        error.setMessage("Password and Confirm Password did not match");
        error.setPositiveButton("Okay",null);
        error.show();
    }

    boolean IsError()
    {
        if(fullname.equals("") || username.equals("") ||
                email.equals("") || password.equals("") ||
                confirmpass.equals("") || location.equals(""))
        {
            return true;
        }else{
            return false;
        }
    }

    public void ShowErrorDialog()
    {
        String name = "Name is Missing!\n";
        String username = "\nUsername is Missing!\n";
        String email = "\nEmail is Missing!\n";
        String password = "\nPassword is Missing!\n";
        String confirmpass = "\nConfirm Password is Missing!\n";
        String location = "\nLocation is Missing!\n";

        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle("Error/s!");
        error.setMessage((this.fullname.matches("") ? name : name.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.username.matches("") ? username : username.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.email.matches("") ? email : email.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.password.matches("") ? password : password.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.confirmpass.matches("") ? confirmpass : confirmpass.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")) +
                (this.location.matches("") ? location : location.replaceAll("[\n]", "").replaceAll("([a-z])", "").replaceAll("([A-Z])", "").replaceAll("!", "")));

        error.setPositiveButton("Okay",null);
        error.show();
    }

    public void ShowSuccessDialog()
    {
        AlertDialog.Builder error = new AlertDialog.Builder(this);
        error.setTitle("Success!");
        error.setMessage("Name: \n" + fullname + "\n" +
                "\nUsername: \n" + username + "\n" +
                "\nEmail: \n" + email + "\n" +
                "\nPassword: \n" + password + "\n" +
                "\nLocation: \n" + location);
        error.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdateList();
                Toast.makeText(getApplicationContext(), "Registration Was Successful!", Toast.LENGTH_SHORT).show();
            }
        });
        error.show();
    }// end of ShowSuccessDialog


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

    //==========================================WEATHER API==========================================
    private void ShowTempInformation()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlOFOpenWeatherMap(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                ShowSuccessDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditAccount.this, "Incorrect City!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }//End of ShowInformation

    private void UpdateList()
    {
        Bitmap bitmap = ((BitmapDrawable) imageViewCapture.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        String name = nameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        account.setbyteUserPofilePic(imageInByte);
        account.setFullName(name);
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(password);
        account.setLocation(location);

        Intent intentUpdateList = new Intent();
        Bundle bundle = this.getIntent().getExtras();
        long accountId = bundle.getLong("account_id");
        accountDb.UpdateAccount(accountId,imageInByte,name,username,email,password,location);

        intentUpdateList.putExtra("update_list", positionOfAccount);
        intentUpdateList.putExtra("edit_account", account);
        setResult(RESULT_OK, intentUpdateList);
        finish();
    }

    private String UrlOFOpenWeatherMap()
    {
        //Apikey nyo dapat ilagay dito pag nag error
        String apikey = "bc292c9cf76e689045c05c8fdb195f81";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+location+"&appid="+apikey+"\n";
        return url;
    }
    //===========================================END OF SECTION WEATHER==============================
    private void SaveChanges()
    {
        Validation();
    }

    @Override
    public void onBackPressed()
    {
        Intent i= new Intent(EditAccount.this,Home.class);
        startActivity(i);
        finish();
    }
}