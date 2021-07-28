package com.zirhgrup.jobmanagement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddElevatorFragment extends Fragment {


;

    public AddElevatorFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    int picCode = 0;
    ImageView imageView;
    TextView[] textViews = new TextView[2];
    Bitmap[] bitmaps =  new Bitmap[2];
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button photoUpload = view.findViewById(R.id.addE_photo_button);
        imageView = view.findViewById(R.id.addE_imageview);
        textViews[0] = view.findViewById(R.id.addE_TextView1);
        textViews[1] = view.findViewById(R.id.addE_TextView2);

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