package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.view.KeyEvent;
import androidx.appcompat.app.AppCompatActivity;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;

public class MainActivity extends AppCompatActivity {
    DatabaseLayer layer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layer = DatabaseLayer.createDatabase();
        layer.checkCurrentUser(this);
    }




    @Override
    protected void onStart() {
        super.onStart();
        layer.checkCurrentUser(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}