package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.ActivityLoginBinding;
import com.zirhgrup.jobmanagement.tools.StaticFun;

public class LoginActivity extends AppCompatActivity {

    DatabaseLayer db;
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar bar = getSupportActionBar();
        bar.hide();
        db = DatabaseLayer.createDatabase();


        binding.loginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextEmailAddress.getText().toString().isEmpty()){
                    binding.editTextEmailAddress.setError(getResources().getString(R.string.error_emailEmpty));
                }else if (binding.editTextPassword.getText().toString().isEmpty()){
                    binding.editTextPassword.setError(getResources().getString(R.string.error_passwordEmpty));
                }else {
                    db.getUserLoginData(LoginActivity.this,binding.editTextEmailAddress.getText().toString(),binding.editTextPassword.getText().toString());
                }
            }
        });

        binding.forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailTV = binding.editTextEmailAddress.getText().toString();
                if(!StaticFun.validateEmail(mailTV)){
                    binding.editTextEmailAddress.setError(getString(R.string.error_emailEmpty));
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