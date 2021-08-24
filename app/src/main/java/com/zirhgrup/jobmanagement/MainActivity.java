package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import com.zirhgrup.jobmanagement.adapter.ElevatorRecyclerAdapter;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.ActivityMainBinding;
import com.zirhgrup.jobmanagement.model.Customer;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.Maintenance;
import com.zirhgrup.jobmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseLayer layer;
    private List<Elevator> elevators = new ArrayList<>();
    CollectionReference elevatorsRef;
    ShareViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layer = DatabaseLayer.createDatabase();
        layer.checkCurrentUser(this);
        elevatorsRef =  DatabaseLayer.getDb().collection("elevators");

        elevatorsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.w("ChangeListener", "listen:error", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    switch (dc.getType()) {
                        case ADDED:
                            elevators.add(dc.getDocument().toObject(Elevator.class));

                            break;
                        case MODIFIED:
                            Elevator changed = dc.getDocument().toObject(Elevator.class);
                            for (Elevator elevator : elevators ){
                                if (elevator.getSerialNo() == changed.getSerialNo()){
                                    elevators.set(elevators.indexOf(elevator),changed);
                                }
                            }
                            break;
                        case REMOVED:
                            Elevator removed = dc.getDocument().toObject(Elevator.class);
                            for (Elevator elevator : elevators ){
                                if (elevator.getSerialNo() == removed.getSerialNo()){
                                    elevators.remove(elevator);
                                }
                            }
                            break;
                    }
                }
                getCustomers();
                getMaintenance();
                getOwnerData();

            }
        });

        viewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        viewModel.saveData(elevators);

    }

    public void getCustomers(){
        for (Elevator elevator : elevators){
            DatabaseLayer.getDb().collection("elevators").document(elevator.getSerialNo()).collection("customers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    elevator.setCustomer(queryDocumentSnapshots.getDocuments().get(0).toObject(Customer.class));
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
                        elevator.setMaintenances(maintenances);
                    }
                }
            });
        }
    }


    private void getOwnerData(){
        for (Elevator elevator :elevators){
            DatabaseLayer.getDb().collection("users").document(elevator.getOwner()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    elevator.setOwnerData(documentSnapshot.toObject(User.class));
                }
            });

        }
    }






    @Override
    protected void onStart() {
        super.onStart();
        layer.checkCurrentUser(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}