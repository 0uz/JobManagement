package com.zirhgrup.jobmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;

public class MainActivity extends AppCompatActivity {
    DatabaseLayer layer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layer = DatabaseLayer.createDatabase();
        layer.checkCurrentUser(this);
    }




    @Override
    protected void onStart() {
        super.onStart();
        layer.checkCurrentUser(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //layer.signOut(this);
    }
}