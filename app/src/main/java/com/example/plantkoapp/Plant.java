package com.example.plantkoapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Plant implements Parcelable
{
    private long idplant;
    private byte[] plantbyteProfilePic;
    private int plantProfilePic;
    private Bitmap plantbitmapImage;
    private String plantName;
    private String plantCategory;
    private String plantDescription;
    private String plantDate;
    private String plantTime;
    private int alarmtime;

    Plant(){}

    //=====================SETTERS==================================================
    public void setIdplant(long idplant) { this.idplant = idplant; }
    public void setPlantbyteProfilePic(byte[] plantbyteProfilePic) { this.plantbyteProfilePic = plantbyteProfilePic; }
    public void setPlantProfilePic(int plantProfilePic) { this.plantProfilePic = plantProfilePic; }
    public void setPlantbitmapImage(Bitmap plantbitmapImage) {
        this.plantbitmapImage = plantbitmapImage;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setPlantCategory(String plantCategory) {
        this.plantCategory = plantCategory;
    }

    public void setPlantDescription(String plantDescription) {
        this.plantDescription = plantDescription;
    }

    public void setPlantTime(String plantTime) {
        this.plantTime = plantTime;
    }

    public void setPlantDate(String plantDate) {
        this.plantDate = plantDate;
    }
    public void setAlarmtime(int alarmtime) {
        this.alarmtime = alarmtime;
    }


    //=====================GETTERS==================================================
    public long getIdplant() {
        return idplant;
    }

    public byte[] getPlantbyteProfilePic() {
        return plantbyteProfilePic;
    }

    public int getPlantProfilePic() {
        return plantProfilePic;
    }

    public Bitmap getPlantbitmapImage() {
        return plantbitmapImage;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getPlantCategory() {
        return plantCategory;
    }

    public String getPlantDescription() {
        return plantDescription;
    }

    public String getPlantTime() {
        return plantTime;
    }

    public String getPlantDate() {
        return plantDate;
    }
    public int getAlarmtime() {
        return alarmtime;
    }


    public Plant(Parcel in)
    {
        this.idplant = in.readLong();
        this.plantbyteProfilePic = in.createByteArray();
        this.plantName = in.readString();
        this.plantCategory = in.readString();
        this.plantDescription = in.readString();
        this.plantDate = in.readString();
        this.plantTime = in.readString();
        this.alarmtime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeLong(this.idplant);
        parcel.writeByteArray(this.plantbyteProfilePic);
        parcel.writeString(this.plantName);
        parcel.writeString(this.plantCategory);
        parcel.writeString(this.plantDescription);
        parcel.writeString(this.plantDate);
        parcel.writeString(this.plantTime);
        parcel.writeInt(this.alarmtime);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Plant createFromParcel(Parcel in) {
            return new Plant(in);
        }

        public Plant[] newArray(int size) {
            return new Plant[size];
        }
    };

}//End of Plant
