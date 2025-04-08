package com.example.myapplication;

import androidx.appcompat.app.AppCompatDelegate;

import static com.example.myapplication.Settings.pref_name;
import static com.example.myapplication.Settings.theme_key;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private EditText editTextNumber;
    private TextView textView4;
    private Button convertButton;
    private Switch themeSwitch;
    private  LottieAnimationView lottie;

    private DecimalFormat df = new DecimalFormat("#.####");

    private final double feet_to_meter = 0.3048;
    private final double inch_to_meter = 0.0254;
    private final double cm_to_meter = 0.01;
    private final double yards_to_meter = 0.9144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        applyTheme(); // applying theme before setting content view

        editTextNumber = findViewById(R.id.editTextNumber);
        textView4 = findViewById(R.id.textView4);
        fromUnitSpinner = findViewById(R.id.fromUnit);
        toUnitSpinner = findViewById(R.id.toUnit);
        convertButton = findViewById(R.id.button);

        lottie = findViewById(R.id.Lottie);

        lottie.playAnimation();


        //For toolbar
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);


        // Creating adapter for both spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.unitConverter,
                android.R.layout.simple_spinner_item
        );



//        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->{
//            if(isChecked){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }else{
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            }
//
//            SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
//            editor.putBoolean("dark_mode", isChecked);
//            editor.apply();
//        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //adapter for both spinners
        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        //listeners for both spinners
        fromUnitSpinner.setOnItemSelectedListener(this);
        toUnitSpinner.setOnItemSelectedListener(this);

        //default selections
        fromUnitSpinner.setSelection(0); // Set to first item, Feet
        toUnitSpinner.setSelection(1); // Set to second item, Inches

        //default value in TextView
        textView4.setText("0");

        //click listener to convert button
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();
            }
        });
    }



    private void applyTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(pref_name, MODE_PRIVATE);
        int savedTheme = sharedPreferences.getInt(theme_key, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedTheme);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






    //Lottie














    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void convertUnits() {
        //input values
        String inputString = editTextNumber.getText().toString();
        if (inputString.isEmpty()) {
            textView4.setText("0");
            return;
        }

        try {
            double value = Double.parseDouble(inputString);
            String fromUnit = fromUnitSpinner.getSelectedItem().toString();
            String toUnit = toUnitSpinner.getSelectedItem().toString();

            double result = convert(value, fromUnit, toUnit);
            textView4.setText(df.format(result));
        } catch (NumberFormatException e) {
            textView4.setText("Invalid input");
        }
    }

    private double convert(double value, String fromUnit, String toUnit) {
        // Converting unit to meters
        double valueInMeters = 0;

        switch (fromUnit) {
            case "Feet":
                valueInMeters = value * feet_to_meter;
                break;
            case "Inches":
                valueInMeters = value * inch_to_meter;
                break;
            case "Centimeters":
                valueInMeters = value * cm_to_meter;
                break;
            case "Meters":
                valueInMeters = value;
                break;
            case "Yards":
                valueInMeters = value * yards_to_meter;
                break;
        }

        // Converting from meters to unit
        double result = 0;

        switch (toUnit) {
            case "Feet":
                result = valueInMeters / feet_to_meter;
                break;
            case "Inches":
                result = valueInMeters / inch_to_meter;
                break;
            case "Centimeters":
                result = valueInMeters / cm_to_meter;
                break;
            case "Meters":
                result = valueInMeters;
                break;
            case "Yards":
                result = valueInMeters / yards_to_meter;
                break;
        }

        return result;
    }
}








