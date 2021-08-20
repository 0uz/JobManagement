package com.zirhgrup.jobmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.zirhgrup.jobmanagement.R;
import com.zirhgrup.jobmanagement.model.Customer;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.Maintenance;
import com.zirhgrup.jobmanagement.tools.StaticFun;

import java.util.List;

public class ElevatorRecyclerAdapter extends RecyclerView.Adapter<ElevatorRecyclerAdapter.ElevatorViewHolder> {
    Context context;
    List<Elevator> elevator;

    public ElevatorRecyclerAdapter(Context context, List<Elevator> elevator) {
        this.context = context;
        this.elevator = elevator;
    }

    @NonNull
    @Override
    public ElevatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.elevator_row,parent,false);
        return new ElevatorRecyclerAdapter.ElevatorViewHolder(view);
    }
    int currentPhoto = 0;
    @Override
    public void onBindViewHolder(@NonNull ElevatorViewHolder holder, int position) {
        holder.serialNo.setText(elevator.get(position).getSerialNo());
        holder.typeAndInfo.setText(elevator.get(position).getType().toString()+ elevator.get(position).getElevatorInfo());
        holder.createDate.setText(StaticFun.getTimeData(elevator.get(position).getCreateTime()));
        holder.lastPeriodicMain.setText(StaticFun.getTimeData(elevator.get(position).getNextMaintenanceTime()));
        holder.locationText.setText("Location"); //TODO location data
        holder.paintType.setText(elevator.get(position).getPaintingType().toString());
        holder.engineNumber.setText(elevator.get(position).getEngine().toString());
        holder.platformSize.setText(elevator.get(position).getHeight()+"x"+ elevator.get(position).getWidth());
        holder.workingHeight.setText(elevator.get(position).getWorkHeight()+"cm");
        holder.workingCap.setText(elevator.get(position).getCapacity().toString());
        holder.customerName.setText(elevator.get(position).getCustomer().getName() + "  " + elevator.get(position).getCustomer().getSurname());
        holder.customerMail.setText(elevator.get(position).getCustomer().getEmail());
        holder.customerNumber.setText(elevator.get(position).getCustomer().getPhoneNo());
        Picasso.get().load(elevator.get(position).getPhotoURL1()).resize(300,400).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPhoto == 0){
                    Picasso.get().load(elevator.get(position).getPhotoURL2()).resize(300,400).into(holder.imageView);
                    currentPhoto = 1;
                }else{
                    Picasso.get().load(elevator.get(position).getPhotoURL1()).resize(300,400).into(holder.imageView);
                    currentPhoto = 0;
                }
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.detailsLayout.getVisibility() == View.VISIBLE){
                    holder.detailsLayout.setVisibility(View.GONE);
                }else{
                    holder.detailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return elevator.size();
    }

    public class ElevatorViewHolder extends RecyclerView.ViewHolder{
        TextView serialNo,typeAndInfo,createDate,lastPeriodicMain,locationText,paintType,engineNumber,platformSize,workingHeight,workingCap;
        TextView customerName,customerMail,customerNumber;
        TextView details;
        RecyclerView maintenance;
        ImageView imageView;
        ConstraintLayout detailsLayout;

        public ElevatorViewHolder(@NonNull View itemView) {
            super(itemView);
            serialNo = itemView.findViewById(R.id.serialNo);
            typeAndInfo = itemView.findViewById(R.id.typeAndInfo);
            createDate = itemView.findViewById(R.id.createDate);
            lastPeriodicMain = itemView.findViewById(R.id.lastPeriodicMain);
            locationText = itemView.findViewById(R.id.locationText);
            paintType = itemView.findViewById(R.id.paintType);
            engineNumber = itemView.findViewById(R.id.engineNumber);
            platformSize = itemView.findViewById(R.id.platformSize);
            workingHeight = itemView.findViewById(R.id.workingHeight);
            workingCap = itemView.findViewById(R.id.workingCap);
            customerName = itemView.findViewById(R.id.customerName);
            customerMail = itemView.findViewById(R.id.customerMail);
            customerNumber = itemView.findViewById(R.id.customerNumber);
            imageView = itemView.findViewById(R.id.elevatorImageView);
            details = itemView.findViewById(R.id.detailsTV);
            detailsLayout = itemView.findViewById(R.id.detailsConstLayout);
            maintenance = itemView.findViewById(R.id.maintenanceRecView);
        }
    }
}
