package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;  // Firebase auth object
    TextView welcome;
    Button logout;
    private  LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        auth = FirebaseAuth.getInstance(); // Getting firebase auth instance
        welcome = findViewById(R.id.welcome);
        logout = findViewById(R.id.logout);

        //Getting current logged-in user
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            welcome.setText("Welcome, " + name + "!");
        }

        //Lottie Animation
        lottie = findViewById(R.id.Lottie);
        lottie.playAnimation();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut(); // Sign the user out
                Toast.makeText(HomeActivity.this, "Shukar h chala gya!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Close HomeActivity
            }
        });
    }
}
