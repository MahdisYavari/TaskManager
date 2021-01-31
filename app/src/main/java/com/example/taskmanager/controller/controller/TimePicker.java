package com.example.taskmanager.controller.controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Task;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePicker extends DialogFragment {

    public static final String EXTRA_TASK_TIME = "com.example.taskmanager.controller.controller.extra_task_time";
    private Date mDate;
    private android.widget.TimePicker mTimePicker;
    public static final String ARG_TIME_PICKER = "Arg_Time_picker";

    public TimePicker() {
        // Required empty public constructor
    }

    public static TimePicker newInstance(Date date) {
        
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME_PICKER,date);
        TimePicker fragment = new TimePicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         mDate = (Date) getArguments().getSerializable(ARG_TIME_PICKER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_time_picker,null , false);
        mTimePicker = view.findViewById(R.id.time_picker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);


        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);




        return new AlertDialog.Builder(getActivity()).setView(view).setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                mDate.setHours(mTimePicker.getCurrentHour());
                mDate.setMinutes(mTimePicker.getCurrentMinute());


                Intent intent = new Intent();
                intent.putExtra(EXTRA_TASK_TIME,mDate);

                Fragment fragment = getTargetFragment();
                fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
            }
        })
                .setNegativeButton("CANCEL",null).create();
    }
}
