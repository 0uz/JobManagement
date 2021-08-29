package com.zirhgrup.jobmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.zirhgrup.jobmanagement.R;
import com.zirhgrup.jobmanagement.model.Maintenance;
import com.zirhgrup.jobmanagement.model.Part;
import com.zirhgrup.jobmanagement.tools.StaticFun;

import java.util.List;

public class MaintenanceRecyclerAdapter extends RecyclerView.Adapter<MaintenanceRecyclerAdapter.MaintenanceViewHolder> {
    List<Maintenance> maintenances;
    Context context;

    public MaintenanceRecyclerAdapter(Context context, List<Maintenance> maintenances) {
        this.maintenances = maintenances;
        this.context = context;
    }

    @NonNull
    @Override
    public MaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.maintenance_row,parent,false);
        return new MaintenanceRecyclerAdapter.MaintenanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceViewHolder holder, int position) {
        holder.type.setText(context.getString(R.string.type)+": "+maintenances.get(position).formattedType(context));
        holder.description.setText(maintenances.get(position).getJobExplanation());
        holder.owner.setText(context.getString(R.string.owner)+": "+maintenances.get(position).getOwnerServiceEmail());
        holder.date.setText(context.getString(R.string.date)+": "+StaticFun.getTimeData(maintenances.get(position).getCreateTime()));

        holder.owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:"+maintenances.get(position).getOwnerServiceEmail()));
                context.startActivity(i);
            }
        });

        for (Part part : maintenances.get(position).getChangedParts()){
            TextView tv = new TextView(context);
            tv.setText(context.getString(R.string.part_name)+": "+part.getPartName()+"\n"+context.getString(R.string.job_description)+": "+part.getJobExplanation());
            tv.setPadding(0,0,0,5);
            holder.changedParts.addView(tv);
        }

        if (position == maintenances.size()-1){
            holder.bottom.setVisibility(View.INVISIBLE);
        }

        if (maintenances.get(position).getChangedParts().size()==0){
            holder.changedPartsTV.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return maintenances.size();
    }

    public class MaintenanceViewHolder extends RecyclerView.ViewHolder{
        TextView type,description,owner,date,changedPartsTV;
        LinearLayout changedParts;
        View bottom;

        public MaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.maintenType);
            description = itemView.findViewById(R.id.jobDescrption);
            owner = itemView.findViewById(R.id.maintenanceOwner);
            date = itemView.findViewById(R.id.maintenanceDate);
            changedParts = itemView.findViewById(R.id.changedParts);
            bottom = itemView.findViewById(R.id.bottomView);
            changedPartsTV = itemView.findViewById(R.id.changedPartsTextView);

        }
    }
}
