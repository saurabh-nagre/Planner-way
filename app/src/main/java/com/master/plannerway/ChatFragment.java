package com.master.plannerway;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.master.plannerway.databinding.FragmentChatBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    FloatingActionButton fab;
    NavController navController;
    Toolbar toolbar;
    FirebaseAuth mauth;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        //setHasOptionsMenu(true); //for To make special menu for this fragment if toolbar is set in main activity
        fab = binding.addNewChatfab;
        navController = NavHostFragment.findNavController(this);
        mauth = FirebaseAuth.getInstance();
        toolbar = binding.chatToolbar;
        toolbar.inflateMenu(R.menu.chat_toolbar_menu);

        toolbar.setTitle("Chat");
        toolbar.setTitleTextColor(Color.WHITE);
        fab.setOnClickListener(v->{
            replaceFragment();
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.addNewChat){
                replaceFragment();
            }
            else if(item.getItemId()==R.id.setting){
                Toast.makeText(getContext(),"Setting item pressed",Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        binding.button1.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Chat fragment button",Toast.LENGTH_SHORT).show();
        });

        //checkEmailVerification();
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    void checkEmailVerification(){
        if(!Objects.requireNonNull(mauth.getCurrentUser()).isEmailVerified()){

            long creationtime = mauth.getCurrentUser().getMetadata().getCreationTimestamp();
            Calendar currentDate = Calendar.getInstance(Locale.getDefault());

            long remainingtimetoverify = (3000000000L - (currentDate.getTimeInMillis()-creationtime));
            if(remainingtimetoverify < 0){
                mauth.getCurrentUser().delete();
            }
            else{
                int daysremaining =(int) (remainingtimetoverify/86400000L);
                Toast.makeText(getContext(),"Verify your account in "+daysremaining+" days," +
                        " otherwise you no longer able to log in with your current email.",Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(fab,"Verify Email",Snackbar.LENGTH_LONG);
                snackbar.setAction("Verify", v ->
                        mauth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful())
                                        Toast.makeText(getContext(),"Email Verification link sent, check your email and come back",
                                            Toast.LENGTH_SHORT).show();
                                }));

                snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                snackbar.show();
            }
        }
    }
    void replaceFragment(){
        navController.navigate(R.id.action_nav_chat_to_nav_createNewGroup);
    }
}