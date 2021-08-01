package com.zirhgrup.jobmanagement;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.GeoPoint;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.model.Elevator;


public class AddElevatorFragment extends Fragment {

    ImageView imageView;
    TextView locationTV;
    TextView[] textViews = new TextView[2];
    Bitmap[] bitmaps =  new Bitmap[2];
    RadioGroup typeGroup, paintingGroup, engineGroup, capacityGroup;
    TextInputEditText serialEditT,platformHeightEditT,platformWidthEditT,workHeightEditT;
    Button save, photoUpload, location;
    RadioButton horizontalType, stairsType;
    Spinner spinner;
    FusedLocationProviderClient fusedLocationProviderClient;
    GeoPoint locationPoint;
    DatabaseLayer layer;
;

    public AddElevatorFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layer = DatabaseLayer.createDatabase();

    }
    int picCode = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            bitmaps[picCode] = (Bitmap) data.getExtras().get("data");
            textViews[picCode].setText("Photo" + (picCode+1));
            imageView.setImageBitmap(bitmaps[0]);
            picCode++;
        }
        if (picCode < 2) startCam();

        for (TextView t : textViews){
            t.setVisibility(View.VISIBLE);
        }
        imageView.setVisibility(View.VISIBLE);
    }

    void createToast(){
        Toast.makeText(getContext(), "Please fill all blanks", Toast.LENGTH_SHORT).show();
    }

    void createTimerRadioError(RadioButton view, String msg){
        Handler handler = new Handler();
        view.setError(msg);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setError(null);
            }},3000);

    }

    void createTimerButtonError(Button view, String msg){
        Handler handler = new Handler();
        view.setError(msg);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setError(null);
            }},3000);

    }



    void selectSpinnerType(Spinner spin, int selectID){
        ArrayAdapter<CharSequence> adapter;
        if (selectID == R.id.radioHorizontalType){
            adapter = ArrayAdapter.createFromResource(getContext(),R.array.HorizontalTypeArray,android.R.layout.simple_spinner_item);
        }else{
            adapter = ArrayAdapter.createFromResource(getContext(),R.array.StairsTypeArray,android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoUpload = view.findViewById(R.id.addE_photo_button);
        save = view.findViewById(R.id.addE_saveButton);
        location = view.findViewById(R.id.addE_locationButton);

        imageView = view.findViewById(R.id.addE_imageview);
        textViews[0] = view.findViewById(R.id.addE_TextView1);
        textViews[1] = view.findViewById(R.id.addE_TextView2);
        locationTV = view.findViewById(R.id.addE_locationTextView);


        typeGroup = view.findViewById(R.id.addE_typeRadioGroup);
        paintingGroup = view.findViewById(R.id.addE_paintingRadioGroup);
        engineGroup = view.findViewById(R.id.addE_EngineRadioGroup);
        capacityGroup = view.findViewById(R.id.addE_capacityRadioGroup);

        spinner = view.findViewById(R.id.addE_spinnerModels);



        serialEditT = view.findViewById(R.id.addE_serialEditText);
        platformHeightEditT = view.findViewById(R.id.addE_heightPlatformEditText);
        platformWidthEditT = view.findViewById(R.id.addE_widthPlatformEditText);
        workHeightEditT = view.findViewById(R.id.addE_heightWorkEditText);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Please check your location permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        locationPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                        locationTV.setText(location.getLatitude()+"N\n"+ location.getLongitude()+"E");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MSG",e.getMessage());
                    }
                });
            }
        });

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectSpinnerType(spinner,checkedId);
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emptyError = getResources().getString(R.string.empty);
                Log.d("ITEM",spinner.getSelectedItem().toString());
                if (bitmaps[0] == null){
                    photoUpload.setError("Please upload photo");
                    createTimerButtonError(photoUpload,"Please upload photo");
                    createToast();
                    return;
                }


                if(serialEditT.getText().toString().isEmpty()){
                  serialEditT.setError(emptyError);
                  createToast();
                  return;
                }
                if (typeGroup.getCheckedRadioButtonId() == -1){
                    RadioButton r =view.findViewById(R.id.radioStairsType);
                    createTimerRadioError(stairsType,emptyError);
                    createToast();
                    return;
                }

                if (paintingGroup.getCheckedRadioButtonId() == -1){
                    RadioButton r = view.findViewById(R.id.radioStainlessPainting);
                    createTimerRadioError(r,emptyError);
                    createToast();
                    return;
                }

                if (platformHeightEditT.getText().toString().isEmpty()){
                    platformHeightEditT.setError(emptyError);
                    createToast();
                    return;
                }

                if (platformWidthEditT.getText().toString().isEmpty()){
                    platformWidthEditT.setError(emptyError);
                    createToast();
                    return;
                }

                if (workHeightEditT.getText().toString().isEmpty()){
                    workHeightEditT.setError(emptyError);
                    createToast();
                    return;
                }

                if (engineGroup.getCheckedRadioButtonId() == -1){
                    RadioButton r =view.findViewById(R.id.radioFiveEngine);
                    createTimerRadioError(r,emptyError);
                    createToast();
                    return;
                }

                if (capacityGroup.getCheckedRadioButtonId() == -1){
                    RadioButton r =view.findViewById(R.id.radio350kg);
                    createTimerRadioError(r,emptyError);
                    createToast();
                    return;
                }

                if (locationPoint == null){
                    createTimerButtonError(location,"Please get your location!");
                    createToast();
                }
                Elevator.ElevatorType currentType;
                Elevator.PaintingType currentPainting;
                Elevator.WorkingEngine currentEngine;
                Elevator.WorkingCapacity currentCapacity;
                if (typeGroup.getCheckedRadioButtonId() == R.id.radioHorizontalType)
                    currentType = Elevator.ElevatorType.HORIZONTAL;
                else
                    currentType = Elevator.ElevatorType.STAIRS;

                if(paintingGroup.getCheckedRadioButtonId() == R.id.radioStaticPainting)
                    currentPainting = Elevator.PaintingType.STATIC;
                else
                    currentPainting = Elevator.PaintingType.STAINLESS;

                if(engineGroup.getCheckedRadioButtonId() == R.id.radioTwoEngine)
                    currentEngine = Elevator.WorkingEngine.TWO;
                else
                    currentEngine = Elevator.WorkingEngine.FIVE;

                if (capacityGroup.getCheckedRadioButtonId() == R.id.radio125kg)
                    currentCapacity = Elevator.WorkingCapacity.KG125;
                else if (capacityGroup.getCheckedRadioButtonId() == R.id.radio225kg)
                    currentCapacity = Elevator.WorkingCapacity.KG225;
                else
                    currentCapacity = Elevator.WorkingCapacity.KG350;



                Elevator elevator = new Elevator(serialEditT.getText().toString(),
                        spinner.getSelectedItem().toString(),
                        currentType,
                        currentPainting,
                        Double.valueOf(platformHeightEditT.getText().toString()),
                        Double.valueOf(platformWidthEditT.getText().toString()),
                        Double.valueOf(workHeightEditT.getText().toString()),currentEngine,currentCapacity,locationPoint);
                layer.uploadPhotos(bitmaps[0],bitmaps[1],elevator);

            }
        });



        

        photoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picCode = 0;
                startCam();
            }
        });

        textViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(bitmaps[0]);
            }
        });

        textViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(bitmaps[1]);
            }
        });



    }

    void uploadElevator(){

    }

    void startCam(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_elevator, container, false);
    }
}