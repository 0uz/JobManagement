package com.zirhgrup.jobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.FragmentListUserBinding;
import com.zirhgrup.jobmanagement.databinding.FragmentMainPageBinding;
import com.zirhgrup.jobmanagement.model.Admin;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPageFragment extends Fragment {

    static DatabaseLayer layer;
    private FragmentMainPageBinding binding;

    public MainPageFragment() {
        // Required empty public constructor
    }

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
        binding = FragmentMainPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layer = DatabaseLayer.createDatabase();
        layer.checkCurrentUser(getContext());

        Button addElevator = view.findViewById(R.id.addElevatorButton);
        Button addUser = view.findViewById(R.id.addUserButton);
        Button elevatorList = view.findViewById(R.id.elevatorListButton);
        Button logout = view.findViewById(R.id.logoutButton);


        binding.listUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainPageFragment.this).navigate(R.id.action_mainPageFragment_to_listUserFragment);
            }
        });

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
                NavHostFragment.findNavController(MainPageFragment.this).navigate(R.id.action_mainPageFragment_to_listElevatorFragment);
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