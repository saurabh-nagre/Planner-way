package com.master.plannerway;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputBinding;

import com.master.plannerway.databinding.FragmentScheduleBinding;

public class ScheduleFragment extends Fragment {

    FragmentScheduleBinding binding;
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScheduleBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        toolbar = binding.scheduleToolbar;

        toolbar.setTitle(R.string.schedule);
        toolbar.setTitleTextColor(Color.WHITE);
        return  root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}