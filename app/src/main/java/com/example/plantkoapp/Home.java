package com.example.plantkoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener{

    //Recycler
    private RecyclerView recyclerViewPlant;
    private RecyclerView.LayoutManager myLayoutManagerPlant;

    private RecyclerView recyclerViewAccount;
    private RecyclerView.LayoutManager myLayoutManagerAccount;

    public static final String POSITION = "com.example.plantkoapp.POSITION";
    public static final String ACCOUNT_LIST = "com.example.plantkoapp.PEOPLE_LIST";

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
    private PlantDb plantDb;
    long createdPlant;
    long sendRegister;
//    Person person;

    //Button
    Button addPlantBtn;

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
            Account createdAccount = intent.getParcelableExtra(EXTRA_ADDED_ACCOUNT);
            createdPlant = intent.getLongExtra(EXTRA_ADDED_PLANT,0);

            if(intent.hasExtra("RegisteredUser"))
            {
                String registeredUsername = intent.getStringExtra("RegisteredUser");
                sendRegister = accountDb.Sender(registeredUsername);
            }

            if(plantList != null){
                plantList = (ArrayList<Plant>) plantDb.getAllPeople(createdPlant);
            }

            accountList.add(createdAccount);


            recyclerViewPlant = findViewById(R.id.recycleView_plant);
            recyclerViewPlant.setHasFixedSize(true);
            myLayoutManagerPlant = new LinearLayoutManager(this);
            plantListAdapter = new PlantListAdapter(this, plantList);
            recyclerViewPlant.setLayoutManager(myLayoutManagerPlant);
            recyclerViewPlant.setAdapter(plantListAdapter);

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

        //addPlantBtn = findViewById(R.id.btn_add_new_entry);

        //addPlantBtn.setOnClickListener(this);
    }//End of Init

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("update_list", 0);
                Account edit_person = data.getParcelableExtra("edit_account");

                accountList.set(position, edit_person);
                accountListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                if(data.getExtras() != null){
                    Plant plant = data.getParcelableExtra("new_plant");
                    plantList.add(0,plant);
                    plantListAdapter.notifyDataSetChanged();
                }
            }
        }//end of if requestCode 2
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
        switch (view.getId()){
//            case R.id.btn_add_new_entry:
//                AddNewPlant();
//                break;
        }
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

}