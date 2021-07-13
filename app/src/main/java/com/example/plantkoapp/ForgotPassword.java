package com.example.plantkoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {


    public static final String EXTRA_ADDED_ACCOUNT_FORGOT = "extra_key_added_account_forgot";

    EditText newpasswordEditext;
    EditText confirmpasswordEditext;

    //Button
    Button updatePasswordBtn;
    //Database
    private AccountDb accountDb;
    Account account;
    int positionOfAccount;

    //References
    String newpassword;
    String confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        Init();
    }

    private void Init()
    {
        this.accountDb = new AccountDb(this);
        newpasswordEditext = findViewById(R.id.editTextTextPersonName2);
        confirmpasswordEditext = findViewById(R.id.editTextTextPersonName3);

        updatePasswordBtn = findViewById(R.id.button);
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });
    }

    private void Validation() {
        Intent intent = getIntent();
        account = intent.getParcelableExtra(EXTRA_ADDED_ACCOUNT_FORGOT);

        byte[] newProfile = account.getByteUserPofilePic();
        String name = account.getFullname();
        String username = account.getUsername();
        String email = account.getEmail();
        //String password = account.getPassword();
        String location = account.getLocation();

        newpassword = newpasswordEditext.getText().toString().trim();
        confirmpassword = confirmpasswordEditext.getText().toString().trim();


        if (!newpassword.equals(confirmpassword)) {
            ShowPasswordDialog();
        }else{
            //ShowSuccessDialog();
            Toast.makeText(getApplicationContext(), "Password is now Update!", Toast.LENGTH_SHORT).show();
            long accountId = accountDb.Sender(username);
            accountDb.UpdateAccount(accountId,newProfile,name,username,email,newpassword,location);
            intent = new Intent(ForgotPassword.this, LoginActivity.class);
            startActivity(intent);
            finish();
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
}