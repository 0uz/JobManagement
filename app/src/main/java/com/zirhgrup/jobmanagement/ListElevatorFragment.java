package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zirhgrup.jobmanagement.adapter.ElevatorRecyclerAdapter;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.FragmentListElevatorBinding;
import com.zirhgrup.jobmanagement.model.Customer;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.Maintenance;

import java.util.ArrayList;
import java.util.List;

public class ListElevatorFragment extends Fragment {

    FragmentListElevatorBinding binding;

    public ListElevatorFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private List<Elevator> elevators;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        elevators = new ArrayList<>();
        getElevators();

    }

    public void getElevators(){
        DatabaseLayer.getDb().collection("elevators").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    elevators.add(data.toObject(Elevator.class));
                }
                getCustomers();
            }
        });
    }

    public void getCustomers(){
        for (Elevator elevator : elevators){
            DatabaseLayer.getDb().collection("elevators").document(elevator.getSerialNo()).collection("customers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    elevator.setCustomer(queryDocumentSnapshots.getDocuments().get(0).toObject(Customer.class));
                    getMaintenance();
                }
            });

        }

    }

    private void getMaintenance(){
        for (Elevator elevator :elevators){
            DatabaseLayer.getDb().collection("elevators").document(elevator.getSerialNo()).collection("maintenances").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Maintenance> maintenances = new ArrayList<>();
                    for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                        maintenances.add(data.toObject(Maintenance.class));
                    }
                    elevator.setMaintenances(maintenances);
                    ElevatorRecyclerAdapter adapter = new ElevatorRecyclerAdapter(getContext(),elevators);
                    binding.elevatorListRecyclerView.setAdapter(adapter);
                    binding.elevatorListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            });
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListElevatorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}