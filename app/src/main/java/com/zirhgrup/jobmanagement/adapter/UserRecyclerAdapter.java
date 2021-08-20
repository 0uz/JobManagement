package com.zirhgrup.jobmanagement.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zirhgrup.jobmanagement.R;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.model.Service;
import com.zirhgrup.jobmanagement.tools.StaticFun;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {
    List<Service> services;
    Context context;

    public UserRecyclerAdapter(Context ct, List<Service> services){
        context=ct;
        this.services = services;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.tv1.setText(services.get(position).getName()+" "+ services.get(position).getSurname());
        holder.tv2.setText(services.get(position).getEmail());
        holder.tv3.setText(StaticFun.getTimeData(services.get(position).getCreateTime()));
        if (services.get(position).isBanned()){
            holder.switchBan.setChecked(true);
        }
        holder.iv.setImageResource(R.drawable.mechanic);
        holder.switchBan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    DatabaseLayer.getDb().collection("users").document(services.get(position).getEmail()).update("banned",true);
                    Toast.makeText(context, context.getString(R.string.ban_success), Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseLayer.getDb().collection("users").document(services.get(position).getEmail()).update("banned",false);
                    Toast.makeText(context, context.getString(R.string.unban_success), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return services.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2, tv3;
        Switch switchBan;
        ImageView iv;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.userRow_name);
            tv2 = itemView.findViewById(R.id.userRow_email);
            tv3 = itemView.findViewById(R.id.userRow_date);
            switchBan = itemView.findViewById(R.id.userRow_switch);
            iv = itemView.findViewById(R.id.userRow_imageView);
        }
    }




}
