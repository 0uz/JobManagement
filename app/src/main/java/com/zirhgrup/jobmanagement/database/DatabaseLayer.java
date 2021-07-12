package com.zirhgrup.jobmanagement.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zirhgrup.jobmanagement.AddUserFragment;
import com.zirhgrup.jobmanagement.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class DatabaseLayer {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    public DatabaseLayer() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    public void createUser(String email,String name, String surname, Activity act, Context context){
        //TODO kullanicinin olup olmadigini checkle

        Random rand = new Random();
        int password = rand.nextInt(999999)*100000;
        mAuth.createUserWithEmailAndPassword(email,Integer.toString(password)).addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    createServiceUserInformation(email,name,surname,context);
                }
            }
        });
    }

    private void createServiceUserInformation(String email, String name, String surname, Context context){
        Map<String,Object> user = new HashMap<>();
        user.put("email",email);
        user.put("name",name);
        user.put("surname",surname);
        user.put("permission","service");
        user.put("createTime",new Date().getTime());
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Succesfully",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail",Toast.LENGTH_LONG).show();
            }
        });

    }



}
