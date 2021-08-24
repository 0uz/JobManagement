package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zirhgrup.jobmanagement.databinding.FragmentAddMaintenanceBinding;
import com.zirhgrup.jobmanagement.databinding.FragmentListElevatorBinding;


public class AddMaintenanceFragment extends Fragment {

    FragmentAddMaintenanceBinding binding;
    public AddMaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMaintenanceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}