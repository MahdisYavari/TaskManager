package com.example.taskmanager.controller.controller;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;


public class AdminActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, AdminActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return AdminFragment.newInstance();
    }

}
