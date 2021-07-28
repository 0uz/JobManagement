package com.zirhgrup.jobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.jar.Attributes;

public class MainPageFragment extends Fragment {

    static DatabaseLayer layer;

    public MainPageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_page, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layer =  DatabaseLayer.createDatabase();
        layer.checkCurrentUser(getContext());

        Button addElevator = view.findViewById(R.id.addElevatorButton);
        Button addUser = view.findViewById(R.id.addUserButton);
        Button elevatorList =  view.findViewById(R.id.elevatorListButton);
        Button logout = view.findViewById(R.id.logoutButton);

        addElevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainPageFragment.this).navigate(R.id.action_mainPageFragment_to_addElevatorFragment);
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainPageFragment.this).navigate(R.id.action_mainPageFragment_to_addUser);

            }
        });

        elevatorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layer.signOut(getContext());
                Intent i =  new Intent(getContext(),LoginActivity.class);
                startActivity(i);
            }
        });


    }

}