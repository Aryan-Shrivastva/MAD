package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;
import com.airbnb.lottie.LottieAnimationView;
public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth; // Firebase auth object
    GoogleSignInClient googleSignInClient; // Google Sign-In client
    LottieAnimationView lottie2;

    // Launcher for handling result from Google Sign-In intent
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Getting google sign-in account from result
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

                            // signing in to Firebase with Google credentials
                            auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Aree kaun aaa gya", Toast.LENGTH_SHORT).show();

                                        // Go to HomeActivity
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // this will show error if sign-in failed
                                        Toast.makeText(MainActivity.this,
                                                "Are nhi ho paya, hehe " + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);  // Initializing Firebase

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id)) // Firebase client ID from strings.xml
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, options); // Initializing sign-in client
        auth = FirebaseAuth.getInstance(); // Firebase auth instance

        // Force logout to always show account picker
        // applying the below codes will make you log out everytime
//        googleSignInClient.signOut();
//        auth.signOut();


        //Animation
        lottie2 = findViewById(R.id.Lottie2);
        lottie2.playAnimation();



        SignInButton signInButton = findViewById(R.id.signIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Google Sign-In intent
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });
    }
}
