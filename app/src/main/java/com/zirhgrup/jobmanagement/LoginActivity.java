package com.zirhgrup.jobmanagement;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText mail;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar bar = getSupportActionBar();
        bar.hide();
        mail = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    void login(){
        String email = mail.getText().toString();
        String psw = password.getText().toString();
        mAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.putExtra("name",email);
                    startActivity(i);

                }
            }
        });
    }
}