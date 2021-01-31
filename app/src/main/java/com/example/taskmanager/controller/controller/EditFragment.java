package com.example.taskmanager.controller.controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Task;
import com.example.taskmanager.controller.repository.RepositoryTask;
import com.example.taskmanager.controller.utils.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends DialogFragment {


    public static final String ARG_ID_EDIT_TASK = "ARG_ID_EDIT_TASK";
    public static final int REQUEST_CODE_DATE_PICKER =0;
    public static final String TAG_DATE_PICKER_FRAGMENT = "TAG_DATE_PICKER_FRAGMENT";
    public static final int REQUEST_CODE_TIME_PICKER =1;
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 2;
    public static final String AUTHORITY_FILE_PROVIDER = "com.example.taskmanager.fileProvider";
    public static final String TAG_TIME_PICKER_FRAGMENT = "TAG_TIME_PICKER_FRAGMENT ";
    public static final String ARG_CURRENT_PAGE_EDIT_TASK = "ARG_CURRENT_PAGE_EDIT_TASk";
    private EditText mTaskTitle, mTaskDescription;
    private Date mDate, mTime;
    private Button mButtonDatePicker, mButtonTimePicker;
    RadioGroup mRadioGroup;
    private RadioButton getmRadioButtonTask;
    private RadioButton mRadioButtonTodo, mRadioButtonDoing, mRadioButtonDone;
    Task mTask = new Task();
    private int currentPage;
    private UUID uuid;
    private File mPhotoFile;
    private String timeString;
    Date temp = mTask.getDate();
    private String dateString;
    private Uri mPhotoUri;
    private ImageView mImageView;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(int currentPage,UUID uuid) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ID_EDIT_TASK,uuid);
        args.putSerializable(ARG_CURRENT_PAGE_EDIT_TASK,currentPage);

        EditFragment fragment = new EditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPage = (int) getArguments().getSerializable(ARG_CURRENT_PAGE_EDIT_TASK);
        uuid = (UUID) getArguments().getSerializable(ARG_ID_EDIT_TASK);
        mTask = RepositoryTask.getInstance(getActivity()).getTask(uuid);
        mPhotoFile = RepositoryTask.getInstance(getContext()).getPhotoFile(mTask);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updatePhotoView();
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_detail, null, false);
       initUi(view);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mPhotoFile == null)
                    return;

                mPhotoUri = FileProvider.getUriForFile(getContext(),
                        AUTHORITY_FILE_PROVIDER, mPhotoFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager()
                        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : cameraActivities) {
                    getActivity().grantUriPermission(resolveInfo.activityInfo.packageName,
                            mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                }
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);

            }
        });


        mTaskTitle.setText(mTask.getTitle());
        mTaskDescription.setText(mTask.getDescription());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
         dateString = simpleDateFormat.format(mTask.getDate());
        mButtonDatePicker.setText(dateString);

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm");
        timeString = simpleTimeFormat.format(mTask.getDate());
        mButtonTimePicker.setText(timeString);

       mButtonDatePicker.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DatePicker datePickerFragment = DatePicker.newInstance(mTask.getDate());
               datePickerFragment.setTargetFragment(EditFragment.this, REQUEST_CODE_DATE_PICKER);
               datePickerFragment.show(getFragmentManager(), TAG_DATE_PICKER_FRAGMENT);

           }
       });
       mButtonTimePicker.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               TimePicker timePickerFragment = TimePicker.newInstance(mTask.getDate());
               timePickerFragment.setTargetFragment(EditFragment.this, REQUEST_CODE_TIME_PICKER);
               timePickerFragment.show(getFragmentManager(), TAG_TIME_PICKER_FRAGMENT);

           }
       });


        return new AlertDialog.Builder(getActivity()).setView(view).setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mTask.setTitle(mTaskTitle.getText().toString());
                mTask.setDescription(mTaskDescription.getText().toString());
                mTask.setStateViewPager(setState(currentPage));


                if(mRadioButtonTodo.isChecked())
                    mTask.setStateViewPager("ToDo");
                else if(mRadioButtonDoing.isChecked())
                    mTask.setStateViewPager("Doing");
                else
                    mTask.setStateViewPager("Done");



                    mDate.setHours(temp.getHours());
                    mDate.setMinutes(temp.getMinutes());
                    mTask.setDate(mTask.getDate());

                    Fragment fragment = getTargetFragment();
                    if (fragment instanceof ToDo) {
                        RepositoryTask.getInstance(getActivity()).update(mTask);
                        ToDo todoFragment = (ToDo) getTargetFragment();
                        todoFragment.notifyAdapter();

                    } else if (fragment instanceof Doing) {
                        RepositoryTask.getInstance(getActivity()).update(mTask);
                        Doing doing = (Doing) getTargetFragment();
                        doing.notifyAdapter();
                    } else if (fragment instanceof Done) {
                        RepositoryTask.getInstance(getActivity()).update(mTask);
                        Done done = (Done) getTargetFragment();
                        done.notifyAdapter();
                    }
                }


   //         }
        }).setNeutralButton("CANCEL",null).setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Fragment fragment = getTargetFragment();
                if (fragment instanceof ToDo) {
                    RepositoryTask.getInstance(getActivity()).deleteTask(mTask);
                    ToDo todoFragment = (ToDo) getTargetFragment();
                    todoFragment.notifyAdapter();

                } else if (fragment instanceof Doing) {
                    RepositoryTask.getInstance(getActivity()).deleteTask(mTask);
                    Doing doing = (Doing) getTargetFragment();
                    doing.notifyAdapter();
                } else if (fragment instanceof Done) {
                    RepositoryTask.getInstance(getActivity()).deleteTask(mTask);
                    Done done = (Done) getTargetFragment();
                    done.notifyAdapter();
                }
            }
        }).create();

    }


    private void initUi(View view) {
        mTaskTitle = view.findViewById(R.id.edit_text_title);
        mTaskDescription = view.findViewById(R.id.edit_text_description);
        mButtonDatePicker = view.findViewById(R.id.date_picker_button);
        mButtonTimePicker = view.findViewById(R.id.time_picker_button);
        mRadioButtonDoing = view.findViewById(R.id.check_box2);
        mRadioButtonDone = view.findViewById(R.id.check_box3);
        mRadioButtonTodo = view.findViewById(R.id.check_box1);
        mImageView = view.findViewById(R.id.image_view_camera);
        mImageView.setImageResource(R.drawable.camera_ic);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null){
            return;
        }
        if (requestCode == REQUEST_CODE_DATE_PICKER){
            Date date = (Date) data.getSerializableExtra(DatePicker.EXTRA_TASK_DATE);

            mDate = date;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = simpleDateFormat.format(date);
            mButtonDatePicker.setText(dateString);
        }
        if (requestCode == REQUEST_CODE_TIME_PICKER){
            Date date = (Date) data.getSerializableExtra(TimePicker.EXTRA_TASK_TIME);

            if (data.equals(null)){
                mTime = new Date();
            }
            else {
                mTime = date;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                String dateString = simpleDateFormat.format(mTime);
                mButtonTimePicker.setText(dateString);

//                mTimeButton.setText(mTime.toString());
            }
        }
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
            updatePhotoView();
            getActivity().revokeUriPermission(mPhotoUri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

    }
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageView.setImageResource(R.drawable.camera_ic);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }
    }
    private String setState(int currentPage){
        String state = "";
        switch (currentPage){
            case 0: {
                state = "ToDo";
                break;
            }
            case 1: {
                state = "Doing";
                break;
            }
            case 2: {
                state = "Done";
                break;
            }
        }
        return state;
    }
}
