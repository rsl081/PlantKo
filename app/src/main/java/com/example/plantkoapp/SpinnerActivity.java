package com.example.plantkoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerActivity implements AdapterView.OnItemSelectedListener
{
    public Activity activity;
    private String plantCatergoryQ;


    public SpinnerActivity(Activity _activity){
        this.activity = _activity;
    }
    public void MySpinner()
    {
        Spinner spinnerCategory = this.activity.findViewById(R.id.username_register);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.activity,
                R.array.platcategrory_array, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter1);
        spinnerCategory.setOnItemSelectedListener(this);

    }

    public String PlantCategoryMethod() { return plantCatergoryQ; }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String select = parent.getItemAtPosition(position).toString();
        boolean sameSelected = position == parent.getSelectedItemPosition();

        switch (parent.getId()){
            case R.id.username_register:
                    plantCatergoryQ = select;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void SecurityQuestionError(){
        AlertDialog.Builder error = new AlertDialog.Builder(this.activity);
        error.setTitle("Error/s!");
        error.setMessage("The selection is invalid ❌");
        error.setPositiveButton("Okay",null);
        error.show();
    }

}
