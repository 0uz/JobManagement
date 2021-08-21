package com.zirhgrup.jobmanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.zirhgrup.jobmanagement.R;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.model.User;
import com.zirhgrup.jobmanagement.tools.StaticFun;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
        View view = inflater.inflate(R.layout.elevator_row, parent, false);
        return new ElevatorRecyclerAdapter.ElevatorViewHolder(view);
    }

    int currentPhoto = 0;

    @Override
    public void onBindViewHolder(@NonNull ElevatorViewHolder holder, int position) {
        holder.serialNo.setText(context.getString(R.string.serial_no) + ": " + elevator.get(position).getSerialNo());
        holder.typeAndInfo.setText(elevator.get(position).formatType() + "\n" + elevator.get(position).getElevatorInfo());
        holder.createDate.setText(context.getString(R.string.createTime) + ":\n" + StaticFun.getTimeData(elevator.get(position).getCreateTime()));
        holder.lastPeriodicMain.setText(context.getString(R.string.lastPeriodic) + ":\n" + StaticFun.getTimeData(elevator.get(position).getNextMaintenanceTime()));
        getLocationData(elevator.get(position).getPoint().getLatitude(), elevator.get(position).getPoint().getLongitude(), holder.locationText);
        holder.paintType.setText(context.getString(R.string.paintType) + ": " + elevator.get(position).formatPainting());
        holder.engineNumber.setText(context.getString(R.string.workingEngine) + ": " + elevator.get(position).formatEngine());
        holder.platformSize.setText(context.getString(R.string.platformSize2) + ": " + StaticFun.doubleToInt(elevator.get(position).getHeight()) + "x" +
                StaticFun.doubleToInt(elevator.get(position).getWidth()) + " cm");
        holder.workingHeight.setText(context.getString(R.string.workingHeight2) + ": " + StaticFun.doubleToInt(elevator.get(position).getWorkHeight()) + " cm");
        holder.workingCap.setText(context.getString(R.string.workingCapacity) + ": " + elevator.get(position).formatCapacity());

        holder.customerName.setText(elevator.get(position).getCustomer().getName() + "  " + elevator.get(position).getCustomer().getSurname());
        holder.customerMail.setText(elevator.get(position).getCustomer().getEmail());
        holder.customerNumber.setText(elevator.get(position).getCustomer().getPhoneNo());

        holder.serviceName.setText(elevator.get(position).getOwnerData().getName() + " " + elevator.get(position).getOwnerData().getSurname());
        holder.serviceNumber.setText(elevator.get(position).getOwnerData().getPhoneNo());
        holder.serviceMail.setText(elevator.get(position).getOwnerData().getEmail());

        Picasso.get().load(elevator.get(position).getPhotoURL1()).resize(300, 400).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPhoto == 0) {
                    Picasso.get().load(elevator.get(position).getPhotoURL2()).resize(300, 400).into(holder.imageView);
                    currentPhoto = 1;
                } else {
                    Picasso.get().load(elevator.get(position).getPhotoURL1()).resize(300, 400).into(holder.imageView);
                    currentPhoto = 0;
                }
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.detailsLayout.getVisibility() == View.VISIBLE) {
                    holder.detailsLayout.setVisibility(View.GONE);
                } else {
                    holder.detailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void getLocationData(double latitude, double longitude, TextView textView) {
        String URL = "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=" + latitude + "&longitude=" + longitude + "&localityLanguage=tr";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            textView.setText(response.getString("principalSubdivision")+", "+response.getString("locality"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);

    }

    @Override
    public int getItemCount() {
        return elevator.size();
    }

    public class ElevatorViewHolder extends RecyclerView.ViewHolder {
        TextView serialNo, typeAndInfo, createDate, lastPeriodicMain, locationText, paintType, engineNumber, platformSize, workingHeight, workingCap;
        TextView customerName, customerMail, customerNumber;
        TextView serviceName, serviceNumber, serviceMail;
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

            serviceName = itemView.findViewById(R.id.serviceName);
            serviceNumber = itemView.findViewById(R.id.serviceNumber);
            serviceMail = itemView.findViewById(R.id.serviceMail);

            imageView = itemView.findViewById(R.id.elevatorImageView);
            details = itemView.findViewById(R.id.detailsTV);
            detailsLayout = itemView.findViewById(R.id.detailsConstLayout);
            maintenance = itemView.findViewById(R.id.maintenanceRecView);
        }
    }
}
