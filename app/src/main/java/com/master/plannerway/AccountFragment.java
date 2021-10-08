package com.master.plannerway;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.master.plannerway.databinding.FragmentAccountBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    FirebaseAuth mAuth;
    Button mlogoutButton;
    MainActivity mainActivity;
    Button mviewProfileButton;
    ImageView mprofileImage;
    TextView mprofileUsername;
    NavController navController;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    UserInfo userInfo;
    Toolbar toolbar;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = new MainActivity();
        mAuth = FirebaseAuth.getInstance();
        mlogoutButton = binding.logoutButton;
        mviewProfileButton = binding.accountViewProfileButton;
        mprofileImage = binding.accountProfileImage;
        mprofileUsername = binding.accountUsernameTV;
        navController = NavHostFragment.findNavController(this);
        toolbar = binding.accountToolbar;

        toolbar.setTitle(R.string.account);
        toolbar.setTitleTextColor(Color.WHITE);

        firebaseDatabase = FirebaseDatabase.getInstance("https://planner-way-8b4fd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabase = firebaseDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo = snapshot.getValue(UserInfo.class);
                if(userInfo==null){
                    userInfo = setDefaultInfo();
                    mDatabase.setValue(userInfo);
                }
                else updateProfile(userInfo.getUsername());
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Realtime msg","Got error in account",error.toException());
            }
        });






        mviewProfileButton.setOnClickListener(v -> {
                    navController.navigate(R.id.action_nav_account_to_profile_viewFragment);
                }
        );
        mlogoutButton.setOnClickListener(v -> {
            switchActivity();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void updateProfile(String username){
        mprofileUsername.setText(username);
    }
    UserInfo setDefaultInfo(){
        UserInfo defaultInfo = new UserInfo();
        defaultInfo.email = mAuth.getCurrentUser().getEmail();
        int aindex = defaultInfo.getEmail().indexOf("@");
        defaultInfo.username = defaultInfo.getEmail().substring(0,aindex);
        updateProfile(defaultInfo.username);
        defaultInfo.name = " ";
        defaultInfo.uid = mAuth.getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(1981,1,1);
        defaultInfo.dob = calendar.getTime();
        return defaultInfo;
    }
    void switchActivity(){
        mAuth.signOut();
        Intent intent = new Intent(binding.getRoot().getContext(),SigninActivity.class);
        startActivity(intent);
        requireActivity().finishAffinity();
    }
}