package com.example.unit_converter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

private Spinner fromUnitSpinner;
private Spinner toUnitSpinner;
private EditText editTextNumber;
private TextView textView4;
private boolean isUpdating = false;
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.unitConverter,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnitSpinner.setAdapter(adapter);


        public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener{
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

            }
            public void onNothingSelected(AdapterView<?> parent){}


        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

    }

}