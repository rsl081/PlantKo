package com.example.plantkoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener{

    //Recycler
//    private RecyclerView recyclerViewPlant;
//    private RecyclerView.LayoutManager myLayoutManagerPlant;

    private RecyclerView recyclerViewAccount;
    private RecyclerView.LayoutManager myLayoutManagerAccount;

    public static final String POSITION = "com.example.plantkoapp.POSITION";
    public static final String ACCOUNT_LIST = "com.example.plantkoapp.ACCOUNT_LIST";

    //Add the Person objects to an ArrayList
    ArrayList<Plant> plantList = new ArrayList<>();
    PlantListAdapter plantListAdapter;

    //Add the Accounts objects to an ArrayList
    ArrayList<Account> accountList = new ArrayList<>();
    AccountListAdapter accountListAdapter;
    public static final String EXTRA_ADDED_ACCOUNT = "extra_key_added_account";
    public static final String EXTRA_ADDED_PLANT = "extra_key_added_person";

    //Database
    private AccountDb accountDb;
    Account account;
    //Plant plant;
    private PlantDb plantDb;
    long createdPlant;
    long sendRegister;
    //Button
    Button addPlantBtn;
    Button indoorPlantBtn;
    Button outdoorPlantBtn;

   //SharePrefs
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String UsernameSharePrefs = "text";
    public static final String TimeToWaterSharePrefs = "timetoplant";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Init();
    }

    private void Init()
    {
        this.accountDb = new AccountDb(this);
        this.plantDb = new PlantDb(this);

        Intent intent = getIntent();

        if(intent != null)
        {
            account = intent.getParcelableExtra(EXTRA_ADDED_ACCOUNT);
            createdPlant = intent.getLongExtra(EXTRA_ADDED_PLANT,0);

            saveData();
            if(intent.hasExtra("RegisteredUser"))
            {
                String registeredUsername = intent.getStringExtra("RegisteredUser");
                sendRegister = accountDb.Sender(registeredUsername);
            }


            accountList.add(account);

            recyclerViewAccount = findViewById(R.id.recycleView_account);
            recyclerViewAccount.setHasFixedSize(true);
            myLayoutManagerAccount = new LinearLayoutManager(this);
            accountListAdapter = new AccountListAdapter(this, accountList);
            recyclerViewAccount.setLayoutManager(myLayoutManagerAccount);
            recyclerViewAccount.setAdapter(accountListAdapter);
            accountListAdapter.setOnClickListener(new AccountListAdapter.OnClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Logout();
                }
            });

        accountListAdapter.setOnClickListener2(new AccountListAdapter.OnClickListener() {
            @Override
            public void OnClickListener(int position)
            {
                String accountName = accountList.get(position).getFullname();
                long getAccountId = accountDb.Sender(accountName);
                Intent intentToEditEntryAct = new Intent(Home.this, EditAccount.class);
                intentToEditEntryAct.putExtra(POSITION, position);
                intentToEditEntryAct.putExtra("account_id", getAccountId);
                intentToEditEntryAct.putExtra(ACCOUNT_LIST, accountList.get(position));
                startActivityForResult(intentToEditEntryAct, 1);
            }
        });

        }

        addPlantBtn = findViewById(R.id.btn_add_new_entry);
        indoorPlantBtn = findViewById(R.id.indoorBtn);
        outdoorPlantBtn = findViewById(R.id.outdoorBtn);

        indoorPlantBtn.setOnClickListener(this);
        outdoorPlantBtn.setOnClickListener(this);
        addPlantBtn.setOnClickListener(this);
    }//End of Init

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("update_list", 0);
                Account edit_account = data.getParcelableExtra("edit_account");

                accountList.set(position, edit_account);
                accountListAdapter.notifyDataSetChanged();
            }
        }
//        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(new IndoorFragment()));
//        if (fragment != null) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
//        if (requestCode == 2)
//        {
//            if(resultCode == RESULT_OK)
//            {
//                int ctrpending = data.getIntExtra("alarm_pending",0);
//                Log.v("HAPPY", String.valueOf(ctrpending));
//            }
//        }//end of if requestCode
    }

    public void Logout() {
        AlertDialog.Builder bldg = new AlertDialog.Builder(this);
        bldg.setTitle("Are you sure you want to logout?");
        bldg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Home.this, LoginActivity.class));
            }
        });
        bldg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Home.this, "Cancelled so sad!", Toast.LENGTH_LONG).show();
            }
        });
        bldg.show();
    }//end of Logout CURLY BRACES

    @Override
    public void onClick(View view) {
        boolean isValidColor = false;
        switch (view.getId()){
            case R.id.btn_add_new_entry:
                AddNewPlant();
            break;

            case R.id.indoorBtn:
                outdoorPlantBtn.setAlpha(1f);
                indoorPlantBtn.setAlpha(0.5f);
                ReplaceFragment(new IndoorFragment());
            break;

            case R.id.outdoorBtn:
                indoorPlantBtn.setAlpha(1f);
                outdoorPlantBtn.setAlpha(0.5f);
                ReplaceFragment(new OutdoorFragment());
            break;
        }
    }

    private void ReplaceFragment(Fragment fragment)
    {
        Bundle data = new Bundle();
        data.putLong("myData", createdPlant);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragment.setArguments(data);
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    public void AddNewPlant(){
        Intent addEntryIntent = new Intent(Home.this, AddPlant.class);
        if(sendRegister == 0){
            addEntryIntent.putExtra("add_plant", createdPlant);
        }else{
            addEntryIntent.putExtra("add_plant", sendRegister);
        }
        startActivityForResult(addEntryIntent, 2);
    }

    //=======================SharePreferences========================================================
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(editor != null)
        {
            editor.putString(UsernameSharePrefs, account.getUsername());
           // editor.putString()

            editor.apply();
        }
    }

}