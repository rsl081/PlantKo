package com.example.plantkoapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.ViewHolder> {

    private ArrayList<Plant> plantArrayList;
    private Context mContext;
    private AccountListAdapter.OnClickListener listener;
    private AccountListAdapter.OnClickListener listener2;

    /**
     * Holds variables in a View
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPic;
        TextView plantNameTextView;
        TextView descriptionTextView;
        TextView dateTextView;
        TextView timeTextView;
        Button editPlantBtn;
        Button deletePlantBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPic = (ImageView) itemView.findViewById(R.id.profilepic_account);
            this.plantNameTextView = (TextView) itemView.findViewById(R.id.plant_adapter_name);
            this.descriptionTextView = (TextView) itemView.findViewById(R.id.plant_adapter_description);
            this.dateTextView = (TextView) itemView.findViewById(R.id.account_adapter_date);
            this.timeTextView = (TextView) itemView.findViewById(R.id.account_adapter_time);
            this.editPlantBtn = (Button) itemView.findViewById(R.id.setting_btn);
            this.deletePlantBtn = (Button) itemView.findViewById(R.id.logout_btn);

            this.deletePlantBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener2 != null){
                        listener2.OnClickListener(position);
                    }
                }
            });

            this.editPlantBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.OnClickListener(position);
                    }
                }
            });
        }
    }

    public PlantListAdapter(Context context, ArrayList<Plant> objects) {
        this.mContext = context;
        this.plantArrayList = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_list_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plant plant = this.plantArrayList.get(position);
        if(plant != null) {
            byte[] images = plant.getPlantbyteProfilePic();
            Bitmap bitmapImages = BitmapFactory.decodeByteArray(images, 0, images.length);
            holder.imageViewPic.setImageBitmap(bitmapImages);

            holder.plantNameTextView.setText("Plant Name: "+plant.getPlantName());
            holder.descriptionTextView.setText("Description: "+plant.getPlantDescription());
            holder.dateTextView.setText("Date: "+plant.getPlantDate());
            holder.timeTextView.setText("Time: "+plant.getPlantTime());
        }

    }

    @Override
    public int getItemCount() {
        return this.plantArrayList.size();
    }

    public ArrayList<Plant> getItems() {
        return plantArrayList;
    }

    public void setItems(ArrayList<Plant> mItems) {
        this.plantArrayList = mItems;
    }

    public void setOnClickListener(AccountListAdapter.OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnClickListener2(AccountListAdapter.OnClickListener listener2) {
        this.listener2 = listener2;
    }

}