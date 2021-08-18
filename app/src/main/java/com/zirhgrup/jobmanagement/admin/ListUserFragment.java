package com.zirhgrup.jobmanagement.admin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zirhgrup.jobmanagement.adapter.UserRecyclerAdapter;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.FragmentListUserBinding;
import com.zirhgrup.jobmanagement.model.Service;
import com.zirhgrup.jobmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class ListUserFragment extends Fragment {

    FragmentListUserBinding binding;


    void setDataToRecycle(List<Service> services){
        UserRecyclerAdapter adapter = new UserRecyclerAdapter(getContext(),services);
        binding.userDataRecyclerView.setAdapter(adapter);
        binding.userDataRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public ListUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseLayer.getDb().collection("users").whereNotEqualTo("role", User.Role.ADMIN).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Service> services = new ArrayList<>();
                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    services.add(data.toObject(Service.class));
                }
                setDataToRecycle(services);
                binding.addUserProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}