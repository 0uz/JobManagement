package com.zirhgrup.jobmanagement;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.StorageReference;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.FragmentAddElevatorBinding;
import com.zirhgrup.jobmanagement.model.Customer;
import com.zirhgrup.jobmanagement.model.Elevator;
import com.zirhgrup.jobmanagement.tools.StaticFun;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class AddElevatorFragment extends Fragment {

    ImageView imageView;
    TextView locationTV;
    TextView[] textViews = new TextView[2];
    List<Bitmap> bitmaps = new ArrayList<>();
    RadioGroup typeGroup, paintingGroup, engineGroup, capacityGroup;
    TextInputEditText serialEditT, platformHeightEditT, platformWidthEditT, workHeightEditT;
    Button save, photoUpload, location;
    RadioButton horizontalType, stairsType;
    Spinner spinner;
    FusedLocationProviderClient fusedLocationProviderClient;
    GeoPoint locationPoint;
    DatabaseLayer layer;
    FragmentManager fragmentManager;
    private FragmentAddElevatorBinding binding;
    private int currentImage = 0;

    public AddElevatorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layer = DatabaseLayer.createDatabase();
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    ActivityResultLauncher<Intent> activityResultSelectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        try {
                            ClipData data = result.getData().getClipData();
                            if (data.getItemCount() > 2){
                                Toast.makeText(getContext(), getString(R.string.select_two_photo), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (int i = 0; i < 2; i++) {
                                Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                                Bitmap bitmap;
                                try {
                                    if (Build.VERSION.SDK_INT > 29) {
                                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getApplicationContext().getContentResolver(), uri));
                                        bitmaps.add(bitmap);
                                    }else {
                                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                        bitmaps.add(bitmap);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            imageView.setImageBitmap(bitmaps.get(0));

                            for (TextView t : textViews) {
                                t.setVisibility(View.VISIBLE);
                            }
                            imageView.setVisibility(View.VISIBLE);
                            binding.addERotateButton.setVisibility(View.VISIBLE);

                        }catch (NullPointerException e){
                            Toast.makeText(getContext(), getString(R.string.select_two_photo), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
    );


    void createToast() {
        Toast.makeText(getContext(), "Please fill all blanks", Toast.LENGTH_SHORT).show();
    }

    void createTimerRadioError(RadioButton view, String msg) {
        Handler handler = new Handler();
        view.setError(msg);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setError(null);
            }
        }, 3000);

    }

    void createTimerButtonError(Button view, String msg) {
        Handler handler = new Handler();
        view.setError(msg);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setError(null);
            }
        }, 3000);

    }


    void selectSpinnerType(Spinner spin, int selectID) {
        ArrayAdapter<CharSequence> adapter;
        if (selectID == R.id.radioHorizontalType) {
            adapter = ArrayAdapter.createFromResource(getContext(), R.array.HorizontalTypeArray, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(getContext(), R.array.StairsTypeArray, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

    ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
//                        navigate to map screen
                        NavHostFragment.findNavController(AddElevatorFragment.this).navigate(R.id.action_addElevatorFragment_to_mapsFragment);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.location_permission), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = NavHostFragment.findNavController(this);
        MutableLiveData<LatLng> liveData = navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData("location");
        liveData.observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng data) {
                locationTV.setText(data.latitude + "N\n" + data.longitude + "E");
                locationPoint = new GeoPoint(data.latitude, data.longitude);

            }
        });

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


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPermissionResult.launch(ACCESS_FINE_LOCATION);
            }
        });

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectSpinnerType(spinner, checkedId);
            }
        });

        binding.addERotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap currentBitmap = bitmaps.get(currentImage);
                Bitmap rotatedBitmap = Bitmap.createBitmap(currentBitmap,0,0,currentBitmap.getWidth(),currentBitmap.getHeight(),matrix,false);
                binding.addEImageview.setImageBitmap(rotatedBitmap);
                bitmaps.set(currentImage,rotatedBitmap);

            }
        });

        binding.addESerialEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 2){
                    if(s.charAt(2) != '-'){
                        binding.addESerialEditText.setText(s.subSequence(0,2)+"-"+s.subSequence(2,s.length()));
                        binding.addESerialEditText.setSelection(count+1);
                    }
                }
                if (count<7){
                    binding.addESerialEditText.setError(getString(R.string.serial_lenght));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addESerialEditText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.addESerialEditText.getText().toString().length() < 7){
                    Toast.makeText(getContext(), getString(R.string.serial_lenght), Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseLayer.getDb().collection("elevators").document(binding.addESerialEditText.getText().toString()).get().
                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult().exists()&&binding.addESerialEditText.getError().length()==0){
                                    Toast.makeText(getContext(), getString(R.string.elevator_exist), Toast.LENGTH_SHORT).show();
                                }else{
                                    checkFields();
                                }

                            }
                        });

            }
        });


        photoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotosFromGallery();
            }
        });

        textViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(bitmaps.get(0));
                currentImage = 0;
            }
        });

        textViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(bitmaps.get(1));
                currentImage = 1;
            }
        });
    }

    void checkFields(){
        String emptyError = getResources().getString(R.string.empty);
        if (bitmaps.size() == 0) {
            photoUpload.setError("Please upload photo");
            createTimerButtonError(photoUpload, "Please upload photo");
            createToast();
            return;
        }

        if (serialEditT.getText().toString().isEmpty() && serialEditT.getText().toString().length() < 7) {
            serialEditT.setError(emptyError);
            createToast();
            return;
        }



        if (typeGroup.getCheckedRadioButtonId() == -1) {
            createTimerRadioError(binding.radioStairsType, emptyError);
            createToast();
            return;
        }

        if (paintingGroup.getCheckedRadioButtonId() == -1) {
            createTimerRadioError(binding.radioStainlessPainting, emptyError);
            createToast();
            return;
        }

        if (platformHeightEditT.getText().toString().isEmpty()) {
            platformHeightEditT.setError(emptyError);
            createToast();
            return;
        }

        if (platformWidthEditT.getText().toString().isEmpty()) {
            platformWidthEditT.setError(emptyError);
            createToast();
            return;
        }

        if (workHeightEditT.getText().toString().isEmpty()) {
            workHeightEditT.setError(emptyError);
            createToast();
            return;
        }

        if (engineGroup.getCheckedRadioButtonId() == -1) {
            createTimerRadioError(binding.radioFiveEngine, emptyError);
            createToast();
            return;
        }

        if (capacityGroup.getCheckedRadioButtonId() == -1) {
            createTimerRadioError(binding.radio350kg, emptyError);
            createToast();
            return;
        }

        if (locationPoint == null) {
            createTimerButtonError(location, getString(R.string.location_error));
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

        if (paintingGroup.getCheckedRadioButtonId() == R.id.radioStaticPainting)
            currentPainting = Elevator.PaintingType.STATIC;
        else
            currentPainting = Elevator.PaintingType.STAINLESS;

        if (engineGroup.getCheckedRadioButtonId() == R.id.radioTwoEngine)
            currentEngine = Elevator.WorkingEngine.TWO;
        else
            currentEngine = Elevator.WorkingEngine.FIVE;

        if (capacityGroup.getCheckedRadioButtonId() == R.id.radio125kg)
            currentCapacity = Elevator.WorkingCapacity.KG125;
        else if (capacityGroup.getCheckedRadioButtonId() == R.id.radio225kg)
            currentCapacity = Elevator.WorkingCapacity.KG225;
        else
            currentCapacity = Elevator.WorkingCapacity.KG350;

        if (binding.addECustomerName.getText().toString().isEmpty()){
            binding.addECustomerName.setError(emptyError);
            createToast();
            return;
        }
        if (binding.addECustomerSurname.getText().toString().isEmpty()){
            binding.addECustomerSurname.setError(emptyError);
            createToast();
            return;
        }
        if (StaticFun.validateEmail(binding.addECustomerEmail.getText().toString()) && binding.addECustomerEmail.getText().toString().isEmpty()){
            binding.addECustomerEmail.setError(emptyError);
            createToast();
            return;
        }

        if (binding.addECustomerPhone.getText().toString().isEmpty()){
            binding.addECustomerPhone.setError(emptyError);
            createToast();
            return;
        }

        Customer customer =  new Customer(binding.addECustomerName.getText().toString(),
                binding.addECustomerSurname.getText().toString(),
                binding.addECustomerEmail.getText().toString(),
                binding.addECustomerPhone.getText().toString());
        Elevator elevator = new Elevator(serialEditT.getText().toString(),
                spinner.getSelectedItem().toString(),
                currentType,
                currentPainting,
                Double.valueOf(platformHeightEditT.getText().toString()),
                Double.valueOf(platformWidthEditT.getText().toString()),
                Double.valueOf(workHeightEditT.getText().toString()), currentEngine, currentCapacity, locationPoint);

        uploadElevatorData(elevator,customer);
    }


    public void uploadElevatorData(Elevator elevator,Customer customer) {
        if (bitmaps.size() > 0) {
            binding.addESaveButton.setEnabled(false);
            binding.addEProgressBar.setVisibility(View.VISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmaps.get(0).compress(Bitmap.CompressFormat.JPEG,80, baos);
            byte[] data = baos.toByteArray();
            bitmaps.remove(0);
            StorageReference reference = layer.getStorage().getReference("elevators").child(System.currentTimeMillis() + ".png");
            reference.putBytes(data).addOnCompleteListener(task -> {
                if (elevator.getPhotoURL1() == null) {
                    Toast.makeText(getContext(), getString(R.string.upload_starting), Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnCompleteListener(task1 -> {
                        elevator.setPhotoURL1(task1.getResult().toString());
                        binding.addEProgressBar.setProgress(0);
                    });
                } else {
                    reference.getDownloadUrl().addOnCompleteListener(task1 -> {
                        elevator.setPhotoURL2(task1.getResult().toString());
                        layer.uploadElevatorData(elevator,customer);
                        binding.addEProgressBar.setVisibility(View.INVISIBLE);
                        binding.addESaveButton.setEnabled(true);
                        clearInputs();
                        Toast.makeText(getContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                    });
                }
                uploadElevatorData(elevator,customer);
            }).addOnProgressListener(snapshot -> {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                binding.addEProgressBar.setProgress((int) progress);
            });
        }

    }

    private void clearInputs() {
        binding.addESerialEditText.setText("");
        binding.addETypeRadioGroup.clearCheck();
        binding.addECapacityRadioGroup.clearCheck();
        binding.addEEngineRadioGroup.clearCheck();
        binding.addEPaintingRadioGroup.clearCheck();
        binding.addEHeightPlatformEditText.setText("");
        binding.addEWidthPlatformEditText.setText("");
        binding.addEHeightWorkEditText.setText("");
        binding.addESpinnerModels.setAdapter(null);
        binding.addELocationTextView.setText(R.string.location_information);
        binding.addECustomerName.setText("");
        binding.addECustomerSurname.setText("");
        binding.addECustomerEmail.setText("");
        binding.addECustomerPhone.setText("");
        locationPoint = null;
    }


    void selectPhotosFromGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        activityResultSelectImage.launch(Intent.createChooser(i, "Select Elevators"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddElevatorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}