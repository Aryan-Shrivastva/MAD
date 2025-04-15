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

    // UI elements
    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private EditText editTextNumber;
    private TextView textView4;
    private Button convertButton;
    private Switch themeSwitch;
    private  LottieAnimationView lottie;

    private DecimalFormat df = new DecimalFormat("#.####"); //date formatter


    //using conversion constants to convert everything to meters first
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


        //Lottie animation on app launch
        lottie = findViewById(R.id.Lottie);
        lottie.playAnimation();


        //Setting up toolbar
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);


        // Creating adapter for both spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.unitConverter,
                android.R.layout.simple_spinner_item
        );


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

    //Settings menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    //Handling toolbar menu item click-> this opens settings screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // These two methods are required by the spinner interface, but we are not using them here
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //This will handle the logic of converting values between selected units
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


    //first we will convert input to meters, then will convert from meter to the target unit
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








