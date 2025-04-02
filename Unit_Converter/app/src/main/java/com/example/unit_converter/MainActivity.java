package com.example.unit_converter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private EditText editTextNumber;
    private TextView textView4;
    private Button convertButton;
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

        editTextNumber = findViewById(R.id.editTextNumber);
        textView4 = findViewById(R.id.textView4);
        fromUnitSpinner = findViewById(R.id.fromUnit);
        toUnitSpinner = findViewById(R.id.toUnit);
        convertButton = findViewById(R.id.button);

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