package com.zirhgrup.jobmanagement.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zirhgrup.jobmanagement.LoginActivity;
import com.zirhgrup.jobmanagement.MainActivity;
import com.zirhgrup.jobmanagement.R;
import com.zirhgrup.jobmanagement.UserRecyclerAdapter;
import com.zirhgrup.jobmanagement.model.Elevator;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


public class DatabaseLayer implements OnCompleteListener<QuerySnapshot> {

    static private FirebaseAuth mAuth;
    static private FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference reference;
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




    public void createUser(String email,String name, String surname, Context cont){
        Random rand = new Random();
        int password = rand.nextInt(999999)*100000;
        mAuth.createUserWithEmailAndPassword(email,Integer.toString(password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    createServiceUserInformation(email,name,surname);
                    Toast.makeText(cont,cont.getResources().getText(R.string.userRegister_success),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(cont,cont.getResources().getText(R.string.user_exist),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createServiceUserInformation(String email, String name, String surname){
        Map<String,Object> user = new HashMap<>();
        user.put("email",email);
        user.put("name",name);
        user.put("surname",surname);
        user.put("permission","service");
        user.put("createTime",new Date().getTime());
        user.put("isBanned",false);
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

    public void getUserLoginData(Context context,String mail,String password){
        Query query = db.collection("users").whereEqualTo("email",mail);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot ds : task.getResult()){
                        login(context,ds.getString("permission"),ds.getBoolean("isBanned"),mail,password);
                    }
                }
            }
        });

    }


    void login(Context activity ,String perm, boolean isBanned, String email , String psw){
        mAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener((Activity) activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if (isBanned){
                        Toast.makeText(activity, "You are banned", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (perm.equals("admin")){
                        Intent i = new Intent(activity, MainActivity.class);
                        activity.startActivity(i);
                        ((AppCompatActivity) activity).finish();
                    }

                }else{
                    Toast.makeText(activity, activity.getResources().getText(R.string.login_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Map<String,Object> name = new HashMap<>();
    Map<String,Object> surname = new HashMap<>();
    Map<String,Object> mail = new HashMap<>();
    Map<String,Object> time = new HashMap<>();
    Map<String,Object> isBanned = new HashMap<>();

    public void downloadUserData(RecyclerView rec, Context cont, ProgressBar bar){
        db.collection("users").whereNotEqualTo("permission","admin").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        name.put(doc.getId(), doc.getData().get("name"));
                        surname.put(doc.getId(), doc.getData().get("surname"));
                        mail.put(doc.getId(), doc.getData().get("email"));
                        time.put(doc.getId(), doc.getData().get("createTime"));
                        isBanned.put(doc.getId(), doc.getData().get("isBanned"));
                    }
                    setDataToRecycle(rec, cont);
                    bar.setVisibility(View.GONE);
                }else{
                    Log.d("MSG","ERROR");
                }
            }
        });

    }

    void setDataToRecycle(RecyclerView userRecyclerView, Context context){
        UserRecyclerAdapter adapter = new UserRecyclerAdapter(context,getNameData(),getSurnameData(),getMailData(),getTimeData(),getBanData());
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public String[] getNameData() {
        String[] data = new String[name.size()];
        int counter = 0;
       for(Map.Entry<String, Object> map : name.entrySet()){
           data[counter] = map.getValue().toString();
           counter++;
       }
       return data;
    }

    public String[] getSurnameData() {
        String[] data = new String[surname.size()];
        int counter = 0;
        for(Map.Entry<String, Object> map : surname.entrySet()){
            data[counter] = map.getValue().toString();
            counter++;
        }
        return data;
    }

    public String[] getMailData() {
        String[] data = new String[mail.size()];
        int counter = 0;
        for(Map.Entry<String, Object> map : mail.entrySet()){
            data[counter] = map.getValue().toString();
            counter++;
        }
        return data;
    }

    public String[] getTimeData() {
        String[] data = new String[time.size()];
        int counter = 0;
        Timestamp ts;
        Date date;
        for(Map.Entry<String, Object> map : time.entrySet()){
            ts = new Timestamp(Long.parseLong(map.getValue().toString()));
            date = ts;
            data[counter] = new SimpleDateFormat("dd-MM-yyyy").format(date);
            counter++;
        }
        return data;
    }

    public boolean[] getBanData() {
        boolean[] data = new boolean[isBanned.size()];
        int counter = 0;
        for(Map.Entry<String, Object> map : isBanned.entrySet()){
            data[counter] = Boolean.parseBoolean(map.getValue().toString());
            counter++;
        }
        return data;
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

    public void ban_unBanUser(Context cont ,String email, boolean ban){
        db.collection("users").whereEqualTo("email",email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            ban_UnBan(cont , doc.getId(), ban);
                        }
                    }
                });
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


    public void uploadElevatorData(Elevator elevator){
        if (elevator.getPhotoURL1() != null &&  elevator.getPhotoURL2() != null) {
            db.collection("elevators").document().set(elevator);
        }
    }

    public void uploadPhotos(Bitmap pho1 , Bitmap pho2, Elevator elevator){
        reference = storage.getReference("elevators").child(System.currentTimeMillis()+".png");
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        pho1.compress(Bitmap.CompressFormat.PNG,100,baos1);
        byte[] data = baos1.toByteArray();
        reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadURL(reference , elevator,1);
            }
        });
        pho2.compress(Bitmap.CompressFormat.PNG,100,baos2);
        data = baos2.toByteArray();
        reference = storage.getReference("elevators").child(System.currentTimeMillis()+".png");

        reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadURL(reference ,elevator,2);
            }
        });

    }

    void getDownloadURL(StorageReference reference, Elevator elevator , int photoID){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (photoID == 1) {
                    elevator.setPhotoURL1(uri.toString());
                    uploadElevatorData(elevator);
                }else{
                    elevator.setPhotoURL2(uri.toString());
                    uploadElevatorData(elevator);
                }
            }
        });
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
