package com.example.plantkoapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class IndoorFragment extends Fragment {

    View view;
    private RecyclerView recyclerViewPlant;
    private RecyclerView.LayoutManager myLayoutManagerPlant;

    public static final String POSITIONPLANT = "com.example.plantkoapp.POSITIONPLANT";
    public static final String PLANT_LIST = "com.example.plantkoapp.PLANT_LIST";

    //Add the Person objects to an ArrayList
    ArrayList<Plant> plantList = new ArrayList<>();
    PlantListAdapter plantListAdapter;

    private PlantDb plantDb;


    ArrayList<Plant> plantList2 = new ArrayList<>();

    //References
    int ctrpending;

    //SharePrefs
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TimeToWaterSharePrefs = "timetoplant";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_indoor, container, false);

        this.plantDb = new PlantDb(getContext());
        //Fuck Fragment
        Bundle data = getArguments();

        long createdPlant = data.getLong("myData");
        if(plantList != null)
        {
            plantList = (ArrayList<Plant>) plantDb.GetAllPlant(createdPlant);
        }
        for(Plant plant : plantList)
        {
            if(plant.getPlantCategory().equals("Indoor"))
            {
                plantList2.add(plant);
            }
        }


        recyclerViewPlant = view.findViewById(R.id.recycleView_indoor);
        recyclerViewPlant.setHasFixedSize(true);
        myLayoutManagerPlant = new LinearLayoutManager(getContext());
        plantListAdapter = new PlantListAdapter(getContext(), plantList2);
        recyclerViewPlant.setLayoutManager(myLayoutManagerPlant);
        recyclerViewPlant.setAdapter(plantListAdapter);
        plantListAdapter.setOnClickListener2(new AccountListAdapter.OnClickListener() {
            @Override
            public void OnClickListener(int position) {
                DeleteList(position);
                CancelAlarm();
            }
        });

        plantListAdapter.setOnClickListener(new AccountListAdapter.OnClickListener() {
            @Override
            public void OnClickListener(int position)
            {
                String plantName = plantList2.get(position).getPlantName();
                long getPlantId = plantDb.SenderPlant(plantName);
                int getAlarmNo  = plantDb.AlarmSenderPlant(plantName);
                Intent intentToEditEntryAct = new Intent(getContext(), EditPlant.class);
                intentToEditEntryAct.putExtra(POSITIONPLANT, position);
                intentToEditEntryAct.putExtra("plant_id_indoor", getPlantId);
                intentToEditEntryAct.putExtra(PLANT_LIST, plantList2.get(position));
                intentToEditEntryAct.putExtra("cancel_alarm_indoor", getAlarmNo);
                getActivity().startActivityForResult(intentToEditEntryAct, 3);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
        {
            Plant plant = data.getParcelableExtra("new_plant");
            ctrpending = data.getIntExtra("alarm_pending",0);

            if(plant.getPlantCategory().equals("Indoor"))
            {
                plantList2.add(plant);;
            }
            plantListAdapter.notifyDataSetChanged();
        }//end of if requestCode 2

        if (requestCode == 3)
        {
            int position = data.getIntExtra("update_list_indoor", 0);
            Plant edit_plant = data.getParcelableExtra("edit_plant_indoor");
            Log.v("HW", String.valueOf(position));
            if(edit_plant != null)
            {
                if(edit_plant.equals("Indoor"))
                {
                    //plantList2.set(position, edit_plant);
                }else{
                    plantList2.remove(position);
                }
            }else{
                plantList2.remove(position);
            }
            plantListAdapter.notifyDataSetChanged();
        }
    }

    public void DeleteList(int position) {
        AlertDialog.Builder bldg = new AlertDialog.Builder(getContext());
        bldg.setTitle("Are you sure you want to delete?");
        bldg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String plantName = plantList2.get(position).getPlantName();
                long getPersonId = plantDb.SenderPlant(plantName);
                plantDb.DeletePlant(getPersonId);
                plantList2.remove(position);
                plantListAdapter.notifyDataSetChanged();
            }
        });
        bldg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancel!", Toast.LENGTH_LONG).show();
            }
        });
        bldg.show();
    }//end of Delete CURLY BRACES

    private void CancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), ctrpending, intent, 0);
        alarmManager.cancel(pendingIntent);
    }


}