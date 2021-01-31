package com.example.taskmanager.controller.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignupActivity extends SingleFragmentActivity {

    public static final String ARG_USER = "arg_user";
    public static final String ARG_PASS = "arg_pass";

    public static Intent newIntent(Context context){
        Intent intent =new Intent(context, SignupActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {

       SignUpFragment signupFragment =new SignUpFragment();
        String username =getIntent().getStringExtra(LoginFragment.EXTRA_USER);
        String password =getIntent().getStringExtra(LoginFragment.EXTRA_PASS);


        Bundle bundle =new Bundle();
        bundle.putString(ARG_USER,username);
        bundle.putString(ARG_PASS,password);

        signupFragment.setArguments(bundle);


        return  signupFragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
