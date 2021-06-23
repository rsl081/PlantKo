package com.example.plantkoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class OutdoorFragment extends Fragment {

    View view;

    private RecyclerView recyclerViewPlant;
    private RecyclerView.LayoutManager myLayoutManagerPlant;

    //Add the Person objects to an ArrayList
    ArrayList<Plant> plantList = new ArrayList<>();
    PlantListAdapter plantListAdapter;

    private PlantDb plantDb;


    ArrayList<Plant> plantList2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_outdoor, container, false);

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
            if(plant.getPlantCategory().equals("Outdoor"))
            {
                plantList2.add(plant);
            }
        }


        recyclerViewPlant = view.findViewById(R.id.recycleView_outdoor);
        recyclerViewPlant.setHasFixedSize(true);
        myLayoutManagerPlant = new LinearLayoutManager(getContext());
        plantListAdapter = new PlantListAdapter(getContext(), plantList2);
        recyclerViewPlant.setLayoutManager(myLayoutManagerPlant);
        recyclerViewPlant.setAdapter(plantListAdapter);
        plantListAdapter.setOnClickListener2(new AccountListAdapter.OnClickListener() {
            @Override
            public void OnClickListener(int position) {
                DeleteList(position);
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

            if(plant.getPlantCategory().equals("Outdoor"))
            {
                plantList2.add(plant);
            }

            plantListAdapter.notifyDataSetChanged();
        }//end of if requestCode 2
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
}