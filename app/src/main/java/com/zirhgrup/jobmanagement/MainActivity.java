package com.zirhgrup.jobmanagement;

import android.os.Bundle;
import android.view.KeyEvent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;
import com.zirhgrup.jobmanagement.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    DatabaseLayer layer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainPageFragment);
//        NavController navController = navHostFragment.getNavController();
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