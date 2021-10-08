package com.master.plannerway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.master.plannerway.databinding.FragmentCreateNewGroupBinding;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

public class CreateNewGroup extends Fragment {

    FragmentCreateNewGroupBinding binding;
    NavController mcontroller;
    EditText mgroupNameEt;
    ImageView mgroupImage;
    final int Request_Image = 100;
    Toolbar toolbar;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateNewGroupBinding.inflate(inflater,container,false);

        View root = binding.getRoot();
        mcontroller = NavHostFragment.findNavController(this);
        mgroupNameEt = binding.groupNameET;
        mgroupImage = binding.groupImage;
        toolbar = binding.createGroupToolbar;


        toolbar.setTitle("Create New Group");
        mgroupImage.setOnClickListener(v->{
            if(ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                chooseImageFromGallery();
            }
            else{
                ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontroller.navigateUp();
            }
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImageFromGallery();
            } else {
                Toast.makeText(getContext(), "You need to grant storage permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("QueryPermissionsNeeded")
    void chooseImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        if(intent.resolveActivity(requireActivity().getPackageManager())!=null){
            startActivityForResult(intent,Request_Image);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==Request_Image && resultCode==RESULT_OK){
            if(data!=null){
                Uri photouri = data.getData();
                mgroupImage.setImageURI(photouri);
            }
            else {
                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

}