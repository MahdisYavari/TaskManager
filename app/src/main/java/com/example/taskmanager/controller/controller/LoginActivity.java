package com.example.taskmanager.controller.controller;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LoginActivity extends SingleFragmentActivity {

    public static final String ARG_USER = "ARG USER";
    public static final String ARG_PASS = "ARG_PASS";

    public static Intent newIntent(Context context){
        Intent intent =new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {

        SignUpFragment signupFragment =new SignUpFragment();
        String username =getIntent().getStringExtra(SignupActivity.ARG_USER);
        String password =getIntent().getStringExtra(SignupActivity.ARG_PASS);


        Bundle bundle =new Bundle();
        bundle.putString(ARG_USER,username);
        bundle.putString(ARG_PASS,password);
        signupFragment.setArguments(bundle);


        return new LoginFragment() ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
