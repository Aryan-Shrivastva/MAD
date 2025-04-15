package com.example.myapplication;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class Settings extends AppCompatActivity {

    private RadioGroup themeRadioGroup;
    private RadioButton lightThemeRadio;
    private RadioButton darkThemeRadio;
    public SharedPreferences sharedPreferences; //this will save user's theme preferences
    public static final String pref_name = "unit_converter_prefs";
    public static final String theme_key = "app_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//         Initializing toolbar
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        // Initializing views
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        lightThemeRadio = findViewById(R.id.lightThemeRadio);
        darkThemeRadio = findViewById(R.id.darkThemeRadio);

        // Initializing SharedPreferences
        sharedPreferences = getSharedPreferences(pref_name, MODE_PRIVATE);

        // Setting radio button based on saved themes
        int savedTheme = sharedPreferences.getInt(theme_key, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (savedTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            darkThemeRadio.setChecked(true);
        } else {
            lightThemeRadio.setChecked(true);
        }

        // Set up for radio button listener
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.lightThemeRadio) {
                saveThemePreference(AppCompatDelegate.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkedId == R.id.darkThemeRadio) {
                saveThemePreference(AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }


    //saving the selected theme to sharedPreferences
    private void saveThemePreference(int themeMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(theme_key, themeMode);
        editor.apply();
    }


    //when back arrow is clicked, this will just close the screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



