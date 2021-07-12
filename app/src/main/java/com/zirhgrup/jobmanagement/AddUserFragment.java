package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;


public class AddUserFragment extends Fragment {
    EditText ET_name,ET_surname,ET_email;
    DatabaseLayer layer;

    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layer = new DatabaseLayer();
        ET_name = view.findViewById(R.id.addUser_editTextName);
        ET_surname = view.findViewById(R.id.addUser_editTextSurname);
        ET_email =  view.findViewById(R.id.addUser_editTextEmail);

        view.findViewById(R.id.addUser_singupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nameVAL = validateString(ET_name,1,R.string.error_nameEmpty,R.string.error_nameShort);
                boolean surnameVAL = validateString(ET_surname,1,R.string.error_surnameEmpty,R.string.error_surnameShort);
                boolean emailVAL = validateEmail(ET_email);
                if (nameVAL && surnameVAL && emailVAL){
                    layer.createUser(ET_email.getText().toString(),ET_name.getText().toString(),ET_surname.getText().toString(),getActivity(),getContext());
                    NavHostFragment.findNavController(AddUserFragment.this).navigate(R.id.action_addUser_to_mainPageFragment);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false);
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