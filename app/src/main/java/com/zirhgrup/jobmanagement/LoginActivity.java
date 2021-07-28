package com.zirhgrup.jobmanagement;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.tools.StaticFun;

public class LoginActivity extends AppCompatActivity {

    DatabaseLayer db;
    EditText mail;
    EditText password;
    TextView forgotPW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar bar = getSupportActionBar();
        bar.hide();
        db = DatabaseLayer.createDatabase();

        mail = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        forgotPW = findViewById(R.id.forgotPasswordTextView);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mail.getText().toString().isEmpty()){
                    mail.setError(getResources().getString(R.string.error_emailEmpty));
                }else if (password.getText().toString().isEmpty()){
                    password.setError(getResources().getString(R.string.error_passwordEmpty));
                }else {
                    db.getUserLoginData(LoginActivity.this,mail.getText().toString(),password.getText().toString());
                }
            }
        });

        forgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailTV = mail.getText().toString();
                if(!StaticFun.validateEmail(mailTV)){
                    mail.setError("Please Enter mail");
                    return;
                }
                DatabaseLayer.getmAuth().sendPasswordResetEmail(mailTV).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, getResources().getText(R.string.resetMail_Succes), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, getResources().getText(R.string.resetMail_Fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}