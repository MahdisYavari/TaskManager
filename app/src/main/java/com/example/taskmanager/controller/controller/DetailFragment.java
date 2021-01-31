package com.example.taskmanager.controller.controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Person;
import com.example.taskmanager.controller.model.Task;
import com.example.taskmanager.controller.repository.RepositoryPerson;
import com.example.taskmanager.controller.repository.RepositoryTask;
import com.example.taskmanager.controller.utils.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends DialogFragment {

    public static final String TAG_DATE_PICKER = "tag_date_picker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final String ARG_TASK_ID = "arg_task_id";
    public static final String
            TAG_TIME_PICKER = "Tag_Time_picker";
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    public static final String ARG_CURRENT_PAGE_ = "ARG_CURRENT_PAGE_";
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 2;
    public static final String AUTHORITY_FILE_PROVIDER = "com.example.taskmanager.fileProvider";
    public static final String ARG_USER_UUID = "Arg_user_uuid";
    private EditText mTaskTitle, mTaskDescription;
    private Date mDate, mTime;
    private ImageView mImageView;
    private Button mButtonDatePicker, mButtonTimePicker;
    private int currentPage;
    private File mPhotoFile;
    private RadioButton mRadioButtonTodo, mRadioButtonDoing, mRadioButtonDone;
    Task mTask = new Task();
    Person mPerson = new Person();
    private UUID mUUID;
    private Uri mPhotoUri;


    public static DetailFragment newInstance(int currentPage , UUID uuid) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENT_PAGE_, currentPage);
        args.putSerializable(ARG_USER_UUID,uuid);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = (int) getArguments().getSerializable(ARG_CURRENT_PAGE_);
        mUUID = (UUID) getArguments().getSerializable(ARG_USER_UUID);
        mPhotoFile = RepositoryTask.getInstance(getContext()).getPhotoFile(mTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_detail, null, false);

        mTask.setDate(new Date());
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

        mButtonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePickerFragment = DatePicker.newInstance(mTask.getDate());
                datePickerFragment.setTargetFragment(DetailFragment.this, REQUEST_CODE_DATE_PICKER);
                datePickerFragment.show(getFragmentManager(), TAG_DATE_PICKER);
            }
        });

        mButtonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker timePickerFragment = TimePicker.newInstance(mTask.getDate());
                timePickerFragment.setTargetFragment(DetailFragment.this, REQUEST_CODE_TIME_PICKER);
                timePickerFragment.show(getFragmentManager(), TAG_TIME_PICKER);
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(view).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (mRadioButtonTodo.isChecked())
                    mTask.setStateViewPager("ToDo");
                else if (mRadioButtonDoing.isChecked())
                    mTask.setStateViewPager("Doing");
                else
                    mTask.setStateViewPager("Done");


                mTask.setTitle(mTaskTitle.getText().toString());
                mTask.setDescription(mTaskDescription.getText().toString());
                mTask.setDate(mDate);
                mTask.setDate(mTime);

                if (mTask.getTitle().equals("") || mTask.getDescription().equals("") || mTask.getDate() == null) {
                    onDismiss(dialogInterface);
                    Toast.makeText(getActivity(), " Fill the items ! ", Toast.LENGTH_LONG).show();

                } else {
                    Fragment fragment = getTargetFragment();
                    if (fragment instanceof ToDo) {

//                        Person person = RepositoryPerson.getInstance(getActivity()).getPerson(mUUID);
//                        person.setMTaskCount(RepositoryPerson.getInstance(getActivity()).getTaskCount(person.getMID()) + 1);
                       // RepositoryPerson.getInstance(getActivity()).update(user);

                        RepositoryTask.getInstance(getActivity()).addTask(mTask);
                        ToDo todoFragment = (ToDo) getTargetFragment();
                        todoFragment.notifyAdapter();

                    } else if (fragment instanceof Doing) {
                        RepositoryTask.getInstance(getActivity()).addTask(mTask);
                        Doing doing = (Doing) getTargetFragment();
                        doing.notifyAdapter();
                    } else if (fragment instanceof Done) {
                        RepositoryTask.getInstance(getActivity()).addTask(mTask);
                        Done done = (Done) getTargetFragment();
                        done.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent());
                        done.notifyAdapter();
                    }
                }

            }
        }).setNegativeButton("CANCEL", null).create();

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


        if (resultCode != Activity.RESULT_OK ) {
            return;
        }
        if (requestCode == REQUEST_CODE_DATE_PICKER && data != null) {
            Date date = (Date) data.getSerializableExtra(DatePicker.EXTRA_TASK_DATE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            mTask.setDate(date);
            mButtonDatePicker.setText(dateFormat.format(date));
            mDate = date;

        }
        if (requestCode == REQUEST_CODE_TIME_PICKER && data != null) {
            Date date = (Date) data.getSerializableExtra(TimePicker.EXTRA_TASK_TIME);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh: mm ");
            mTask.setDate(date);
            mButtonTimePicker.setText(timeFormat.format(date));
            mTime = date;
        }
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
            updatePhotoView();
          //  getActivity().revokeUriPermission(mPhotoUri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }


    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        RepositoryTask.getInstance(getContext()).update(mTask);
//    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageView.setImageResource(R.drawable.camera_ic);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }
    }


}



