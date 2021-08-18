package com.zirhgrup.jobmanagement.admin;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.zirhgrup.jobmanagement.R;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.FragmentAddUserBinding;
import com.zirhgrup.jobmanagement.model.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AddUserFragment extends Fragment {
    DatabaseLayer layer;
    private FragmentAddUserBinding binding;

    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layer = DatabaseLayer.createDatabase();


        binding.addUserSingupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nameVAL = validateString(binding.addUserEditTextName,1, R.string.error_nameEmpty,R.string.error_nameShort);
                boolean surnameVAL = validateString(binding.addUserEditTextSurname, 1,R.string.error_surnameEmpty,R.string.error_surnameShort);
                boolean emailVAL = validateEmail(binding.addUserEditTextEmail);
//                boolean phoneVAL = vali
                if (nameVAL && surnameVAL && emailVAL){
                   createUser(binding.addUserEditTextName.getText().toString(),
                        binding.addUserEditTextSurname.getText().toString(),
                        binding.addUserEditTextEmail.getText().toString(),
                        binding.addUserEditTextPhone.getText().toString());
                }
            }
        });
    }

    public void createUser(String name, String surname,String email,String phoneNo){
        Random rand = new Random();
        int password = rand.nextInt(999999)*100000;
        DatabaseLayer.getmAuth().createUserWithEmailAndPassword(email,Integer.toString(password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Service service = new Service(name,surname,email,phoneNo,false);
                    DatabaseLayer.getDb().collection("users").document(service.getEmail()).set(service);
                    Toast.makeText(getContext(),getString(R.string.userRegister_success),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),getString(R.string.user_exist),Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    boolean validateString(EditText view, int min, int errorEmpty, int errorMin){
        String data = view.getText().toString();
        if (data.isEmpty()){
            view.setError(getResources().getString(errorEmpty));
            return false;
        }else if (data.length()<=min){
            view.setError(getResources().getString(errorMin));
            return false;
        }else{
            return true;
        }
    }

    boolean validateEmail(EditText view){
        String data = view.getText().toString();
        if (data.isEmpty()){
            view.setError(getResources().getString(R.string.error_emailEmpty));
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(data).matches()){
            view.setError(getResources().getString(R.string.error_emailMatch));
            return false;
        }else{
            return true;
        }
    }

}