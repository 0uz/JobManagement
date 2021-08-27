package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.model.Customer;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseLayer layer;
    private List<Elevator> elevators = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    CollectionReference elevatorsRef;
    ShareViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layer = DatabaseLayer.createDatabase();
        layer.checkCurrentUser(this);
        elevatorsRef = DatabaseLayer.getDb().collection("elevators");
        getAllElevators();
        addListener();
        viewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        viewModel.saveData(elevators);

    }

    private void handleData(){
        for (Elevator elevator : elevators){
            for (Customer customer : customers){
                if (elevator.getSerialNo().equals(customer.getOwnerSerial())){
                    elevator.setCustomer(customer);
                }
            }
            for (User user : users){
                if (elevator.getOwner().equals(user.getEmail())){
                    elevator.setOwnerData(user);
                }
            }
        }
    }


    private void addListener(){
        elevatorsRef.orderBy("createTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("ChangeListener", "listen:error", error);
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            for (Elevator elevator : elevators){
                                if (elevator.getSerialNo().equals(dc.getDocument().toObject(Elevator.class).getSerialNo())) return;
                            }
                            getOwnerData(dc.getDocument().toObject(Elevator.class));
                            break;
                        case MODIFIED:
                            Elevator changed = dc.getDocument().toObject(Elevator.class);
                            for (Elevator elevator : elevators) {
                                if (elevator.getSerialNo() == changed.getSerialNo()) {
                                    elevators.set(elevators.indexOf(elevator), changed);
                                }
                            }
                            break;
                        case REMOVED:
                            Elevator removed = dc.getDocument().toObject(Elevator.class);
                            for (Elevator elevator : elevators) {
                                if (elevator.getSerialNo() == removed.getSerialNo()) {
                                    elevators.remove(elevator);
                                }
                            }
                            break;
                    }
                }

            }
        });
    }



    private void getAllElevators() {
        DatabaseLayer.getDb().collection("elevators").get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    elevators.add(data.toObject(Elevator.class));
                }
                getAllCustomers();
            }
        });
    }


    private void getAllCustomers() {
        DatabaseLayer.getDb().collectionGroup("customers").get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    customers.add(data.toObject(Customer.class));
                }
                getAllOwnerData();
            }
        });
    }


    private void getAllOwnerData() {
        DatabaseLayer.getDb().collection("users").get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    users.add(data.toObject(User.class));
                }
                handleData();
            }
        });
    }


    private void getOwnerData(Elevator data){
        DatabaseLayer.getDb().collection("users").document(data.getOwner()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                users.add(documentSnapshot.toObject(User.class));
                data.setOwnerData(documentSnapshot.toObject(User.class));
                getCustomerData(data);
            }
        });
    }

    private void getCustomerData(Elevator elevator){
        DatabaseLayer.getDb().collection("elevators").document(elevator.getSerialNo()).collection("customers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()){
                    customers.add(data.toObject(Customer.class));
                    elevator.setCustomer(data.toObject(Customer.class));
                    elevators.add(elevator);
                }
            }
        });
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