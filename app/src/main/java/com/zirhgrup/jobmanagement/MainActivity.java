package com.zirhgrup.jobmanagement;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        Log.d("MSG","OnCreate");
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d("MSG","OnStart");
        if (user == null){
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MSG","ONDestroy");
        FirebaseAuth.getInstance().signOut();
    }
}