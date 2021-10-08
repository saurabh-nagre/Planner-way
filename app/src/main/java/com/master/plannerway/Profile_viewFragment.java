package com.master.plannerway;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.master.plannerway.databinding.FragmentProfileViewBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Locale;

public class Profile_viewFragment extends Fragment {

    FragmentProfileViewBinding binding;
    TextInputEditText musernameET, mNameET,mdobET;
    TextView memailTV;
    Button mpasswordReset, msaveprofile;//mverifyemail;
    FirebaseAuth mauth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mdatabase;
    UserInfo userInfo;
    ImageView mdatepicker;
    NavController navController;
    Toolbar toolbar;
    DatePickerDialog pickerDialog = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        toolbar = binding.profileToolbar;
        //setHasOptionsMenu(true);
        musernameET = binding.profileUsernameET;
        mNameET = binding.profileNameET;
        mdobET = binding.profileDobET;
        msaveprofile = binding.profileSaveButton;
        memailTV = binding.profileemailTV;
        mdatepicker = binding.datePicker;
//        mverifyemail = binding.sendverificationlinkButton;
        mpasswordReset = binding.profilePasswordResetButton;
        mauth = FirebaseAuth.getInstance();
        navController = NavHostFragment.findNavController(this);
        firebaseDatabase = FirebaseDatabase.getInstance("https://planner-way-8b4fd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mdatabase = firebaseDatabase.getReference("users").child(mauth.getCurrentUser().getUid());

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
        toolbar.setTitle("Profile");


        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userInfo = snapshot.getValue(UserInfo.class);
                musernameET.setText(userInfo.getUsername());
                mNameET.setText(userInfo.getName());
                memailTV.setText(userInfo.getEmail());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault());
                String date = sdf.format(userInfo.getDob());
                mdobET.setText(date);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("Realtime msg","Got error in profile",error.toException());
            }
        });

//        if(mauth.getCurrentUser().isEmailVerified()){
//            mverifyemail.setText(R.string.verified);
//            mverifyemail.setBackgroundColor(Color.GREEN);
//            mverifyemail.setTextColor(Color.BLACK);
//        }

        mdatepicker.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pickerDialog = new DatePickerDialog(getContext());
                pickerDialog.updateDate(2001,0,1);
                pickerDialog.show();

                pickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    String date = year +"/"+ (month + 1) +"/"+ dayOfMonth;
                    mdobET.setText(date);
                });
            }
        });


        mpasswordReset.setOnClickListener(v->{
            if(mpasswordReset.getText().toString().equals("Send Link")){
                mauth.sendPasswordResetEmail(userInfo.getEmail()).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        mpasswordReset.setText(R.string.linksent);
                        mpasswordReset.setBackgroundColor(Color.GREEN);
                        mpasswordReset.setTextColor(Color.BLACK);
                    }
                });
            }
        });

//        mverifyemail.setOnClickListener(v->{
//            if(!mauth.getCurrentUser().isEmailVerified() || mverifyemail.getText().toString().equals("Send Verification link")){
//                mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
//                    if(task.isSuccessful()) {
//                        mverifyemail.setText(R.string.emailsent);
//                        mverifyemail.setBackgroundColor(Color.GREEN);
//                        mverifyemail.setTextColor(Color.BLACK);
//                    }
//                });
//            }
//        });
        //checkEmailVerification();

        msaveprofile.setOnClickListener(v->{
            boolean error = false;
            userInfo.username = musernameET.getText().toString();
            if(userInfo.getUsername().length()<6){
                musernameET.setError("Username should be more than 5 letters");
                error = true;
            }
            userInfo.name = mNameET.getText().toString();
            String[] date = mdobET.getText().toString().split("/");

            if(date.length!=3){
                mdobET.setError("Format must be YYYY/MM/DD");
                error = true;
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                YearMonth yearMonth = YearMonth.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]));
                if(date[0].length()!=4 || Integer.parseInt(date[1])>12
                      || Integer.parseInt(date[2])<1 ||yearMonth.lengthOfMonth()<Integer.parseInt(date[2])){
                    mdobET.setError("Date does not exist");
                    error = true;
                }
                else{
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.set(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]));
                    userInfo.dob = calendar.getTime();
                }
            }


            if(!error){
                mdatabase.setValue(userInfo).addOnFailureListener(e -> {
                    Log.d("Exception","Error in profile saving",e.getCause());
                    e.printStackTrace();
                });
                Toast.makeText(getContext(),"Profile Saved",Toast.LENGTH_SHORT).show();
            }
        });


        return  root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void checkEmailVerification(){
        if(!mauth.getCurrentUser().isEmailVerified()){

            long creationtime = mauth.getCurrentUser().getMetadata().getCreationTimestamp();
            Calendar currentDate = Calendar.getInstance(Locale.getDefault());

            long remainingtimetoverify = (3000000000L-(currentDate.getTimeInMillis()-creationtime));

            if(remainingtimetoverify< 0){
                mauth.getCurrentUser().delete();
            }
            else{
                int daysremaining =(int) (remainingtimetoverify/86400000L);
                Toast.makeText(getContext(),"Reset your account password in "+daysremaining+" days," +
                        " otherwise you no longer able to log in with your current email.",Toast.LENGTH_SHORT).show();
            }
        }
    }

}