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
    private String month;
    private String day;
    private String year;
    private Spinner spinnerPlantMonth;
    private Spinner spinnerPlantDay;
    private Spinner spinnerPlantYear;
    private boolean isSpinnerTouchMonth = false;
    private boolean isSpinnerTouchDay = false;
    private boolean isSpinnerTouchYear = false;



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

        spinnerPlantMonth = this.activity.findViewById(R.id.month_addplant_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this.activity,
                R.array.plant_month_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantMonth.setAdapter(adapter2);
        spinnerPlantMonth.setOnItemSelectedListener(this);

        spinnerPlantDay = this.activity.findViewById(R.id.day_plant_spinnner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this.activity,
                R.array.plant_day_array, android.R.layout.simple_spinner_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantDay.setAdapter(adapter3);
        spinnerPlantDay.setOnItemSelectedListener(this);

        spinnerPlantYear = this.activity.findViewById(R.id.year_addplant_spinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this.activity,
                R.array.plant_year_array, android.R.layout.simple_spinner_item);

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantYear.setAdapter(adapter4);
        spinnerPlantYear.setOnItemSelectedListener(this);


        spinnerPlantMonth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSpinnerTouchMonth = true;
                return false;
            }
        });

        spinnerPlantDay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSpinnerTouchDay = true;
                return false;
            }
        });

        spinnerPlantYear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSpinnerTouchYear = true;
                return false;
            }
        });


    }

    public String PlantCategoryMethod() { return plantCatergoryQ; }
    public String PlantMonthMethod() { return month; }
    public String PlantDayMethod() { return day; }
    public String PlantYearMethod() { return year; }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String select = parent.getItemAtPosition(position).toString();
        boolean sameSelected = position == parent.getSelectedItemPosition();

        switch (parent.getId()){
            case R.id.username_register:
                    plantCatergoryQ = select;
                break;
            case R.id.month_addplant_spinner:
                    month = select;
                    if (select.equals("Month") && isSpinnerTouchMonth){
                        SecurityQuestionError();
                        spinnerPlantMonth.setSelection(1);
                    }
                break;
            case R.id.day_plant_spinnner:
                    day = select;
                    if (select.equals("Day") && isSpinnerTouchDay){
                        SecurityQuestionError();
                        spinnerPlantDay.setSelection(1);
                    }
                break;
            case R.id.year_addplant_spinner:
                    year = select;
                    if (select.equals("Year") && isSpinnerTouchYear){
                        SecurityQuestionError();
                        spinnerPlantYear.setSelection(1);
                    }
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
