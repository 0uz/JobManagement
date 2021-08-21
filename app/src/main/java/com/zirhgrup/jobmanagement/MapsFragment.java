package com.zirhgrup.jobmanagement;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapsFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {


    private Marker mMarker;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(39.96701788567862, 32.9042233979418);
    LatLng location;

    Button locationButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationButton = view.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = mMarker.getPosition();
                Log.d("MARKER", mMarker.getPosition().toString());
                NavController navController = NavHostFragment.findNavController(MapsFragment.this);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("location", mMarker.getPosition());
                navController.popBackStack();

            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "Location set", Toast.LENGTH_SHORT).show();
        mMarker.remove();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        mMarker = mMap.addMarker(new MarkerOptions().position(location).draggable(true));
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        this.location = new LatLng(location.getLatitude(),location.getLongitude());
    }

    private void getDeviceLocation() {
        try {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        location = new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());
                        mMarker = mMap.addMarker(new MarkerOptions().position(location).draggable(true));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                    } else {
                        Log.d("LOCATION", "Current location is null. Using defaults.");
                        mMarker = mMap.addMarker(new MarkerOptions().position(mDefaultLocation).draggable(true));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 15));
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        getDeviceLocation();
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                mMarker = marker;
            }
        });

    }
}