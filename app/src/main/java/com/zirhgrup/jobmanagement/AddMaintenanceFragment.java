package com.zirhgrup.jobmanagement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.FragmentAddMaintenanceBinding;
import com.zirhgrup.jobmanagement.databinding.FragmentListElevatorBinding;
import com.zirhgrup.jobmanagement.model.Maintenance;
import com.zirhgrup.jobmanagement.model.Part;

import java.util.ArrayList;
import java.util.List;


public class AddMaintenanceFragment extends Fragment {

    FragmentAddMaintenanceBinding binding;
    List<Part> parts;
    Maintenance.MaintenanceType type;
    public AddMaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.saveButton.setEnabled(true);
        binding.progressBar.setVisibility(View.INVISIBLE);
        parts = new ArrayList<>();

        if (getArguments().getString("type").equals("periodic")){
            binding.titleTextView.setText(getString(R.string.periodicMaintenance));
            type = Maintenance.MaintenanceType.PERIODIC;
        }else{
            type = Maintenance.MaintenanceType.CUSTOM;
            binding.titleTextView.setText(getString(R.string.customMaintenance));
        }

        binding.addPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Part part =  new Part(binding.partNameET.getText().toString(),binding.addPartJobDescET.getText().toString());

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.part_row,null);
                Button button = view.findViewById(R.id.partRow_deleteButton);
                TextView name = view.findViewById(R.id.partRow_partName);
                TextView desc = view.findViewById(R.id.partRow_partDesc);
                name.setText("Part name: "+binding.partNameET.getText());
                desc.setText("Description: "+binding.addPartJobDescET.getText());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.addedPartsLinearLayout.removeView(view);
                        parts.remove(part);
                    }
                });
                binding.addedPartsLinearLayout.addView(view);
                parts.add(part);
                binding.partNameET.setText("");
                binding.addPartJobDescET.setText("");
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.jobDescriptionET.getText().toString().length() < 6 || binding.jobDescriptionET.getText().toString().isEmpty()){
                    binding.jobDescriptionET.setError(R.string.error_charLength+" 6");
                    return;
                }
                binding.saveButton.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
                Maintenance maintenance =  new Maintenance(type,getArguments().getString("user"),binding.jobDescriptionET.getText().toString(),parts);
                if (type == Maintenance.MaintenanceType.PERIODIC){
                    DatabaseLayer.getDb().collection("elevators").document(getArguments().getString("serial")).
                            update("maintenances", FieldValue.arrayUnion(maintenance),"nextMaintenanceTime",getArguments().getLong("maintenance")+7889229).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    NavHostFragment.findNavController(AddMaintenanceFragment.this).popBackStack();
                                    Toast.makeText(getContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    DatabaseLayer.getDb().collection("elevators").document(getArguments().getString("serial")).update("maintenances", FieldValue.arrayUnion(maintenance)).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    NavHostFragment.findNavController(AddMaintenanceFragment.this).popBackStack();
                                    Toast.makeText(getContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMaintenanceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}