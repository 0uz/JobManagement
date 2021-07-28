package com.zirhgrup.jobmanagement;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {
    String[] nameData,surnameData,mailData;
    String[] timeData;
    boolean[] isBannedData;
    Context context;

    public UserRecyclerAdapter(Context ct, String[] name, String[] surname, String[] email, String[] time , boolean[] isBanned){
        context=ct;
        nameData=name;
        surnameData=surname;
        mailData=email;
        timeData=time;
        isBannedData = isBanned;
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
        Resources res  = context.getResources();
        holder.tv1.setText(nameData[position]+" "+ surnameData[position]);
        holder.tv2.setText(mailData[position]);
        holder.tv3.setText(timeData[position]);
        if (isBannedData[position]){
            holder.switchBan.setChecked(true);
        }
        holder.iv.setImageResource(R.drawable.mechanic);
    }

    @Override
    public int getItemCount() {
        return nameData.length;
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
