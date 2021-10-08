package com.master.plannerway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.master.plannerway.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;
    NavController navController;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        if(intent==null){
            Toast.makeText(this,"Something went wrong,you may feel some problem." +
                    "Try log in again.",Toast.LENGTH_LONG).show();
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}