package com.zirhgrup.jobmanagement.database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.zirhgrup.jobmanagement.*;

import java.util.*;


public class DatabaseLayer implements OnCompleteListener<QuerySnapshot> {
    static private FirebaseAuth mAuth;
    static private FirebaseFirestore db;
    private static Set<DatabaseLayer> databaseLayers = new HashSet<DatabaseLayer>();

    public DatabaseLayer() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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

    boolean success;
    public boolean createUser(String email,String name, String surname){
        Random rand = new Random();
        int password = rand.nextInt(999999)*100000;
        mAuth.createUserWithEmailAndPassword(email,Integer.toString(password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                success = task.isSuccessful();
                if (task.isSuccessful()){
                    createServiceUserInformation(email,name,surname);
                }
            }
        });
        return success;
    }

    private void createServiceUserInformation(String email, String name, String surname){
        Map<String,Object> user = new HashMap<>();
        user.put("email",email);
        user.put("name",name);
        user.put("surname",surname);
        user.put("permission","service");
        user.put("createTime",new Date().getTime());
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("SERVICE","SUCCESS");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SERVICE","FAIL");
            }
        });

    }

    public void getUserPermission(String mail){
        Query query = db.collection("users").whereEqualTo("email",mail);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot ds : task.getResult()){
                        Log.d("Permission",ds.get("permission").toString());
                    }
                }
            }
        });

    }

    public void signOut(Context context){
        mAuth.signOut();
        Intent i = new Intent(context,LoginActivity.class);
        context.startActivity(i);

    }

    public void checkCurrentUser(Context context){
       FirebaseUser user =  mAuth.getCurrentUser();
       if (user == null){
           signOut(context);
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
