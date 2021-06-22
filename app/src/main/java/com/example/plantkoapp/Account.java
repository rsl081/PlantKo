package com.example.plantkoapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable
{
    private Uri uriImageProfile;
    private Bitmap bitmapImageProfile;
    private int intImageProfile;
    private String accountName;
    Plant[] plant;
    //Database
    private long accountId;
    private byte[] byteUserPofilePic;
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String location;

    public Account() {}

    public Account(byte[] userPofilePic, String fullname, String username, String email,
                   String password, String location){
        this.byteUserPofilePic = userPofilePic;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public Account(String username, String password, String accountName, Plant[] plant){
        this.setUsername(username);
        this.setPassword(password);
        this.setAccountName(accountName);
        this.plant = plant;
    }

    public Account(String accountName, int intImageProfile){
        this.accountName = accountName;
        this.intImageProfile = intImageProfile;
    }

    public Account(String accountName, Bitmap profilePic){
        this.accountName = accountName;
        this.bitmapImageProfile = profilePic;
    }

    public Account(String accountName, Uri profilePic){
        this.accountName = accountName;
        this.uriImageProfile = profilePic;
    }

    public Account(String accountName, byte[] profilePic){
        this.accountName = accountName;
        this.byteUserPofilePic = profilePic;
    }

    public void setbyteUserPofilePic(byte[] bitmapImageProfile) { this.byteUserPofilePic = bitmapImageProfile;}
    public void setIntImageProfile(int intImageProfile) {
        this.intImageProfile = intImageProfile;
    }
    public void setFullName(String fullname) { this.fullname = fullname; }
    public void setUsername(String username){ this.username = username;}
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password){ this.password = password;}
    public void setLocation(String location){ this.location = location; }
    public void setAccountName(String accountName){ this.accountName = accountName;}
    public void setUriImageProfile(Uri uriImageProfile) {
        this.uriImageProfile = uriImageProfile;
    }
    public void setPlant(Plant[] plant) {
        this.plant = plant;
    }
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
    public void setByteUserPofilePic(byte[] byteUserPofilePic) { this.byteUserPofilePic = byteUserPofilePic; }


    //=======================================GETTERS========================================
    public Uri getUriImageProfile() { return getUriImageProfile(); }
    public Bitmap getBitmapImageProfile() { return bitmapImageProfile; }
    public int getIntImageProfile() { return intImageProfile; }
    public String getFullname() { return fullname; }
    public String getUsername(){ return  username; }
    public String getEmail() { return email; }
    public String getPassword(){ return  password; }
    public String getLocation() { return location; }
    public long getAccountId() { return accountId; }
    public byte[] getByteUserPofilePic() { return byteUserPofilePic; }

    protected Account(Parcel in)
    {
        this.accountId = in.readLong();
        this.byteUserPofilePic = in.createByteArray();
        this.uriImageProfile = (Uri) in.readValue(Uri.class.getClassLoader());
        this.bitmapImageProfile = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        this.intImageProfile = in.readInt();
        this.fullname = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.location = in.readString();
        this.accountName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(accountId);
        dest.writeByteArray(byteUserPofilePic);
        dest.writeValue(uriImageProfile);
        dest.writeValue(bitmapImageProfile);
        dest.writeInt(intImageProfile);
        dest.writeString(fullname);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(location);
        dest.writeString(accountName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };


}
