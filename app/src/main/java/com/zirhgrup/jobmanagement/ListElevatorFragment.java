package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import com.zirhgrup.jobmanagement.adapter.ElevatorRecyclerAdapter;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.FragmentListElevatorBinding;
import com.zirhgrup.jobmanagement.model.Customer;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.Maintenance;
import com.zirhgrup.jobmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class ListElevatorFragment extends Fragment {

    FragmentListElevatorBinding binding;
    DatabaseLayer layer;



    public ListElevatorFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layer = DatabaseLayer.createDatabase();

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShareViewModel model = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        model.getElevator().observe(getViewLifecycleOwner(), elevators->{
            ElevatorRecyclerAdapter adapter = new ElevatorRecyclerAdapter(getContext(),elevators,ListElevatorFragment.this);
            binding.elevatorListRecyclerView.setAdapter(adapter);
            binding.elevatorListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        });


    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListElevatorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}