package com.example.taskmanager.controller.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Task;
import com.example.taskmanager.controller.repository.RepositoryTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.util.UUID;

public class TaskManagerActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String EXTRA_USER_UUID = "extra_user_uuid";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int currentPage;
    private UUID mUUID;

    public static Intent newIntent(Context context, UUID UserId){
        Intent intent =new Intent(context, TaskManagerActivity.class);
        intent.putExtra(EXTRA_USER_ID,UserId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
         mUUID = (UUID) getIntent().getSerializableExtra(EXTRA_USER_UUID);

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {


            @Override
            public Fragment getItem(int position) {
                currentPage = position;
                switch (position) {
                    case 0:
                       return (ToDo.newInstance(currentPage,mUUID));

                    case 1:
                      return (Done.newInstance(currentPage,mUUID));

                    case 2:
                        return (Doing.newInstance(currentPage,mUUID));

                }

            return null;


            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "TODO";
                    case 1:
                        return "DONE";
                    case 2:
                        return "DOInG";
                    default:
                        return "";
                }

            }
        });



    }




}
