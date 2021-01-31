package com.example.taskmanager.controller.controller;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Task;
import com.example.taskmanager.controller.repository.RepositoryTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class Done extends Fragment {


    public static final int REQUEST_CODE_DONE_DETAIL_FRAGMENT = 0;
    public static final String TAG_DETAIL_FRAGMENT_DONE = "TAG_DETAIL_FRAGMENT";
    public static final int REQUEST_CODE_EDIT_FRAGMENT_DONE = 1;
    public static final String TAG_EDIT_FRAGMENT_DONE = "TAG_EDIT_FRAGMENT";
    public static final String ARG_CURRENT_PAGE_TASK_LIST_DONE = "ARG_CURRENT_PAGE_TASK_LIST";
    public static final String ARG_USER_UUID = "Arg_user_uuid";
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private ImageView mImageView;
    private FloatingActionButton mFloatingActionButton;
    private int currentPage;
    private String state;
    List<Task> mTasks = new ArrayList<>();
    private UUID mUUID;


    public static Done newInstance(int currentPage,UUID uuid) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENT_PAGE_TASK_LIST_DONE, currentPage);
        args.putSerializable(ARG_USER_UUID,uuid);

        Done fragment = new Done();
        fragment.setArguments(args);
        return fragment;
    }

    public Done() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = (int) getArguments().getSerializable(ARG_CURRENT_PAGE_TASK_LIST_DONE);
        mUUID = (UUID) getArguments().getSerializable(ARG_USER_UUID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done, container, false);
        initUi(view);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragment detailFragment = DetailFragment.newInstance(currentPage,mUUID);
                detailFragment.setTargetFragment(Done.this, REQUEST_CODE_DONE_DETAIL_FRAGMENT);
                detailFragment.show(getFragmentManager(), TAG_DETAIL_FRAGMENT_DONE);
            }
        });


        return view;
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDate;
        private TextView mTextView;
        private  TextView mDescription;
        private ConstraintLayout mCardView;
        private Task mTasks;


        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.card_view);
            mTextView = itemView.findViewById(R.id.txt_view_task);
            mTitle = itemView.findViewById(R.id.title_task);
            mDescription = itemView.findViewById(R.id.description_task);
            mDate = itemView.findViewById(R.id.date_task);
            mImageView= itemView.findViewById(R.id.share_task);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"TASK REPORT");
                    intent.putExtra(Intent.EXTRA_TEXT,getStringToShare());
                    intent = Intent.createChooser(intent,"TASK REPORT");
                    startActivity(intent);
                }
            });

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditFragment editFragment = EditFragment.newInstance(currentPage, mTasks.getUUID());
                    editFragment.setTargetFragment(Done.this, REQUEST_CODE_EDIT_FRAGMENT_DONE);
                    editFragment.show(getFragmentManager(), TAG_EDIT_FRAGMENT_DONE);
                }
            });

        }
        public String getStringToShare(){
            return mTasks.getTitle() + " ,"+ mTasks.getDescription() + " ," + mTasks.getDate()+" ,"+ mTasks.getStateViewPager();
        }

        public void bind(Task task) {
            mTasks = task;

            mTitle.setText(mTasks.getTitle());
            mDescription.setText(mTasks.getDescription());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = simpleDateFormat.format(mTasks.getDate());
//            mDateTimeTextView.setText(dateString);

            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm");
            String timeString = simpleTimeFormat.format(mTasks.getDate());
////            mDateTimeTextView.setText(dateString);

            mDate.setText(dateString + "  " + timeString);

            if (!mTasks.getTitle().equals("")) {
                mTextView.setText(String.valueOf(mTasks.getTitle().charAt(0)));
            }


        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> tasks;

        public TaskAdapter(List<Task> task) {
            // mTasks = task;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_task, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            holder.bind(mTasks.get(position));
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }

    public void notifyAdapter() {


        //List<TaskDataBaseSchema> tasks = RepositoryTask.getInstance(getActivity()).getTasks();
        mTasks= new ArrayList<>();
        for(Task task:RepositoryTask.getInstance(getActivity()).getTasks()){
            if(task.getStateViewPager().equalsIgnoreCase("done"))
                mTasks.add(task);
        }
        if (mAdapter == null) {

            mAdapter = new Done.TaskAdapter(mTasks);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTasks(mTasks);
            mAdapter.notifyDataSetChanged();
        }
        if (mAdapter.getItemCount() > 0) {
            mImageView.setVisibility(View.GONE);

        }
        if (mAdapter.getItemCount() == 0) {
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    private void initUi(View view) {
        mImageView = view.findViewById(R.id.image_task);
        mRecyclerView = view.findViewById(R.id.recycler_view_done);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        for(Task task:RepositoryTask.getInstance(getActivity()).getTasks()){
            if(task.getStateViewPager().equalsIgnoreCase("done"))
                mTasks.add(task);
        }
        mAdapter = new TaskAdapter(mTasks);
        mRecyclerView.setAdapter(mAdapter);
        mFloatingActionButton = view.findViewById(R.id.Button_add);
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyAdapter();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.delete_item:



                AlertDialog delete = new AlertDialog.Builder(getActivity())
                        .setTitle("Delete all items")
                        .setMessage("All items will be delete.\nAre you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (mTasks.size()>0){
                                    Task task = new Task();
                                    RepositoryTask.getInstance(getActivity()).deleteAllTask(task);
                                    mAdapter.notifyDataSetChanged();
                                    notifyAdapter();
                                } else {
                                    Toast.makeText(getActivity(), "You didn't have any items", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null).create();
                delete.show();



                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
