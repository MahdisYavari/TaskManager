package com.example.taskmanager.controller.controller;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Person;
import com.example.taskmanager.controller.repository.RepositoryPerson;
import com.example.taskmanager.controller.repository.RepositoryTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;
    private List<Person> mPersonList = new ArrayList<>();

    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance() {

        Bundle args = new Bundle();
        AdminFragment fragment = new AdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        initUi(view);
        mPersonList = RepositoryPerson.getInstance(getContext()).getAllUser();
        setUpAdapter();

        return view;
    }

    private class UserHolder extends RecyclerView.ViewHolder {

        private Person mPerson ;
        private CardView mCardView;
        private ImageView mdelete;
        private TextView mTxtPass,
                mTxtUser, mPass, mUser, mCount,
                mTxtNumberOfTask;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            mdelete = itemView.findViewById(R.id.img_delete_user);
            mTxtUser = itemView.findViewById(R.id.txt_username);
            mTxtPass = itemView.findViewById(R.id.txt_password);
            mPass = itemView.findViewById(R.id.txt_pass);
            mCardView = itemView.findViewById(R.id.card_view_user);
            mUser = itemView.findViewById(R.id.txt_user);
            mCount = itemView.findViewById(R.id.txt_count);
            mTxtNumberOfTask = itemView.findViewById(R.id.txt_count_of_task);
            mCount = itemView.findViewById(R.id.txt_count);

            mdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RepositoryPerson.getInstance(getContext()).deleteUser(mPerson.getMID());
                    updateUI();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = TaskManagerActivity.newIntent(getContext(),mPerson.getMID());
                    startActivity(intent);
                }
            });
        }

        public void bind(Person person){
            mPerson = person;
            mTxtUser.setText(mPerson.getMUser());
            mTxtPass.setText(mPerson.getMPass());
            mCount.setText(RepositoryPerson.getInstance(getContext())
            .getAllTaskPerUser(mPerson.getMID()).size()+"");
        }

    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {

        List<Person> personList;

        public void setPersonList(List<Person> personList) {
            this.personList = personList;
        }

        public UserAdapter(List<Person> personList) {
            this.personList = personList;
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_user,parent,false);
            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
         holder.bind(mPersonList.get(position));
        }

        @Override
        public int getItemCount() {
            return personList.size();
        }
    }

    public void updateUI() {
        mPersonList = RepositoryPerson.getInstance(getContext()).getAllUser();
        if (mUserAdapter == null) {
            mUserAdapter = new UserAdapter(mPersonList);
            mRecyclerView.setAdapter(mUserAdapter);
        } else {
            mUserAdapter.setPersonList(mPersonList);
            mUserAdapter.notifyDataSetChanged();
        }
    }

    private void initUi(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview_admin);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpAdapter() {
        mUserAdapter = new UserAdapter(mPersonList);
        mRecyclerView.setAdapter(mUserAdapter);
        if (mUserAdapter != null)
            mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_alltask:
               // RepositoryTask.getInstance(getContext()).deleteAll();
                updateUI();
                return true;
            case R.id.menu_delete_alluser:
                RepositoryPerson.getInstance(getContext()).deleteAllUser();
                updateUI();
                return true;

            case R.id.menu_logout:
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
