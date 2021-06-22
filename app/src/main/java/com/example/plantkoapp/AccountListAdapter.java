package com.example.plantkoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    private ArrayList<Account> accountsArrayList;
    private Context mContext;
    private OnClickListener listener1;
    private OnClickListener listener2;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //private PersonListAdapter.OnClickListener listener;

    /**
     * Holds variables in a View
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPic;
        TextView accountName;
        TextView accountTemp;
        Button settingBtn;
        Button logoutBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPic = (ImageView) itemView.findViewById(R.id.profilepic_account);
            this.accountName = (TextView) itemView.findViewById(R.id.account_adapter_name);
            this.accountTemp = (TextView) itemView.findViewById(R.id.account_adapter_temp);
            this.settingBtn = (Button) itemView.findViewById(R.id.setting_btn);
            this.logoutBtn = (Button) itemView.findViewById(R.id.logout_btn);

            this.settingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener2 != null){
                        listener2.OnClickListener(position);
                    }
                }
            });

            this.logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener1 != null){
                        listener1.OnClickListener(position);
                    }
                }
            });
        }//End of Constructor
    }

    public AccountListAdapter(Context context, ArrayList<Account> objects) {
        this.mContext = context;
        this.accountsArrayList = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accout_list_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account accounts = this.accountsArrayList.get(position);
        if(accounts != null) {
            byte[] images = accounts.getByteUserPofilePic();
            Bitmap bitmapImages = BitmapFactory.decodeByteArray(images, 0, images.length);
            holder.imageViewPic.setImageBitmap(bitmapImages);
            holder.accountName.setText(accounts.getUsername());
        }
        String apikey = "bc292c9cf76e689045c05c8fdb195f81";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+accounts.getLocation()+"&appid="+apikey+"\n";
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                //Json(response);
                try {
                    JSONObject object1 = response.getJSONObject("main");
                    String temperature = object1.getString("temp");
                    Double tempeConversion = Double.parseDouble(temperature)-273.15f;
                    holder.accountTemp.setText("Temp: "+tempeConversion.toString().substring(0,5) + " C");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Register.this, "Incorrect City!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);


    }

    @Override
    public int getItemCount() {
        return this.accountsArrayList.size();
    }

    public ArrayList<Account> getItems() {
        return accountsArrayList;
    }

    public void setItems(ArrayList<Account> mItems) {
        this.accountsArrayList = mItems;
    }

//    public void setOnClickListener(PersonListAdapter.OnClickListener listener) {
//        this.listener = listener;
//    }

    public interface OnClickListener
    {
        void OnClickListener(int position);
    }

    public void setOnClickListener(OnClickListener listener1) {
        this.listener1 = listener1;
    }
    public void setOnClickListener2(OnClickListener listener2) {
        this.listener2 = listener2;
    }

}
