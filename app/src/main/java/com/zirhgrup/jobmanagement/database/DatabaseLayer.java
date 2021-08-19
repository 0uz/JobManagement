package com.zirhgrup.jobmanagement.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.zirhgrup.jobmanagement.LoginActivity;
import com.zirhgrup.jobmanagement.MainActivity;
import com.zirhgrup.jobmanagement.R;
import com.zirhgrup.jobmanagement.model.Customer;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


public class DatabaseLayer implements OnCompleteListener<QuerySnapshot> {

    static private FirebaseAuth mAuth;
    static private FirebaseFirestore db;
    FirebaseStorage storage;
    private static Set<DatabaseLayer> databaseLayers = new HashSet<DatabaseLayer>();

    public DatabaseLayer() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static DatabaseLayer createDatabase(){
        if (mAuth == null || db == null){
            return new DatabaseLayer();
        }else if (databaseLayers.size() > 0){
            DatabaseLayer layer = databaseLayers.iterator().next();
            databaseLayers.remove(layer);
            return layer;
        }else{
            return new DatabaseLayer();
        }
    }

    public FirebaseStorage getStorage() {
        return storage;
    }





    public void getUserLoginData(Context context,String mail,String password){
        db.collection("users").document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                login(context,documentSnapshot.getString("role"),documentSnapshot.getBoolean("banned"),mail,password);
            }
        });
    }


    void login(Context activity , String role, boolean isBanned, String email , String psw){
        if (isBanned){
            Toast.makeText(activity, "You are banned", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener((Activity) activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if (role.equals("ADMIN")){
                        Intent i = new Intent(activity, MainActivity.class);
                        activity.startActivity(i);
                        ((AppCompatActivity) activity).finish();
                    }else{
                        //TODO service
                    }
                }else{
                    Toast.makeText(activity, activity.getResources().getText(R.string.login_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }







    public void signOut(Context context){
        mAuth.signOut();
        Intent i = new Intent(context,LoginActivity.class);
        context.startActivity(i);
        ((AppCompatActivity) context).finish();

    }

    public void checkCurrentUser(Context context){
       FirebaseUser user =  mAuth.getCurrentUser();
       if (user == null){
           signOut(context);
       }
    }



   private void ban_UnBan(Context cont,String id, boolean ban){
       DocumentReference ref = db.collection("users").document(id);
       if(ban) {
           ref.update("isBanned",true).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   Toast.makeText(cont, "Successfully Banned", Toast.LENGTH_SHORT).show();
               }
           });
       }else{
           ref.update("isBanned",false).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   Toast.makeText(cont, "Successfully UnBanned", Toast.LENGTH_SHORT).show();
               }
           });
       }
    }


    public void uploadElevatorData(Elevator elevator, Customer customer){
        if (elevator.getPhotoURL1() != null &&  elevator.getPhotoURL2() != null) {
            db.collection("elevators").document(elevator.getSerialNo()).set(elevator);
            db.collection("elevators").document(elevator.getSerialNo()).collection("customers").document(customer.getEmail()).set(customer);
        }
    }









    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static FirebaseFirestore getDb() {
        return db;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {

    }
}
