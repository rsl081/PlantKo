package com.example.plantkoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    private Toast popUp;
    private String name, pass;
    private ArrayList<Account> accountArrayList = new ArrayList<>();
    Intent myIntent;

    //SQLite
    private AccountDb accountDb;
    private PlantDb plantDb;
    private DBHelper dbHelper;


    //Login Editext
    private EditText usernameEdit;
    private EditText passwordEdit;

    //Login Btn
    Button loginBtn;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Init();
    }

    private void Init()
    {
        this.accountDb = new AccountDb(this);
        this.plantDb = new PlantDb(this);
        dbHelper = new DBHelper(this);

        usernameEdit = findViewById(R.id.plantname_editext_register);
        passwordEdit = findViewById(R.id.editext_password);

        //Btn
        loginBtn = findViewById(R.id.login_button);
        registerBtn = findViewById(R.id.register_button);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        name = usernameEdit.getText().toString().trim();
        pass = passwordEdit.getText().toString().trim();
        switch (view.getId())
        {
            case R.id.login_button:
                showToast();
            break;
            case R.id.register_button:
                Registration();
            break;
        }
    }

    public void showToast() {
        if (name.matches("") || pass.matches("")) {
            if (name.matches("")) {
                Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                usernameEdit.setHint("Put your username here!");
            }

            if (pass.matches("")) {
                Toast.makeText(this, "You did not enter a password", Toast.LENGTH_SHORT).show();
                passwordEdit.setHint("Put your password here!");
            }
        } else {
            if (accountDb.CheckUser(name,pass)) {
                CustomToast();
                myIntent = new Intent(this, Home.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Login();
                startActivity(myIntent);
                finish();
            } else {
                AlertDialag();
            }
        }
    }//end of showToast CURLY BRACES

    public void CustomToast()
    {
        Toast.makeText(getApplicationContext(), "Login is succesful", Toast.LENGTH_LONG).show();
    }//end of CustomToast CURLY BRACES

    public void AlertDialag() {
        AlertDialog.Builder bldg = new AlertDialog.Builder(this);
        bldg.setTitle("Credential does not match");
        bldg.setMessage("Username or Password is Incorrect!");
        bldg.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usernameEdit.setText("");
                passwordEdit.setText("");
            }
        });
        bldg.show();
    }//end of AlertDialag CURLY BRACES


    public void Login()
    {
        myIntent.putExtra("username", name);
        myIntent.putExtra("password", pass);
        long sendUsername = accountDb.Sender(name);
        myIntent.putExtra(Home.EXTRA_ADDED_ACCOUNT, (Parcelable) accountDb.GetAccountId(sendUsername));
        myIntent.putExtra(Home.EXTRA_ADDED_PLANT, sendUsername);

        //myIntent.putExtra(Home.EXTRA_ADDED_ACCOUNT_PLANT, (Parcelable) plantDb.GetPLantId(sendUsername));
    }



    private void Registration()
    {
        Intent intent = new Intent(LoginActivity.this, Register.class);
        startActivity(intent);
        finish();
    }


}