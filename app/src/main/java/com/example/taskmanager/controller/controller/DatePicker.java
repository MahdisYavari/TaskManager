package com.example.taskmanager.controller.controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePicker extends DialogFragment {
    public static final String EXTRA_TASK_DATE = "com.example.taskmanager.controller.controller.extra_task_date";
    private android.widget.DatePicker mDatePicker;
    private Date mDate;


    public static final String ARG_TASK_DATE = "arg_Task_date";

    public DatePicker() {
        // Required empty public constructor
    }

    public static DatePicker newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_DATE, date);
        DatePicker fragment = new DatePicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_TASK_DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null, false);
        mDatePicker = view.findViewById(R.id.date_picker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, monthOfYear, dayOfMonth, null);

        return new AlertDialog.Builder(getActivity()).setView(view).setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = mDatePicker.getYear();
                int monthOfYear = mDatePicker.getMonth();
                int dayOfMonth = mDatePicker.getDayOfMonth();

                GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Date date = calendar.getTime();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_TASK_DATE,date);

                Fragment fragment = getTargetFragment();
                fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
//
//                DetailFragment detailFragment = (DetailFragment) getTargetFragment();
//                detailFragment.upDateTaskDate(date);



            }
        })
                .setNegativeButton("CANCEL", null).create();
    }
}
