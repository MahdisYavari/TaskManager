package com.example.taskmanager.controller.controller;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Person;
import com.example.taskmanager.controller.repository.RepositoryPerson;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    public static final String PASS = "pass";
    public static final String USER = "user";
    private Button mSignUp;
    EditText mPass,mUser;
    String username,password;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mSignUp = view.findViewById(R.id.sign_up_button2);
        mUser = view.findViewById(R.id.username_sign);
        mPass = view.findViewById(R.id.password_sign);
        username = getArguments().getString(SignupActivity.ARG_USER);
        password = getArguments().getString(SignupActivity.ARG_PASS);

        mPass.setText(password);
        mUser.setText(username);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPass.getText().toString().equals("") || mUser.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "enter your username and password!!", Toast.LENGTH_LONG).show();
                }
                else if(RepositoryPerson.getInstance(getContext()).existPerson(username)){
                    Toast.makeText(getActivity(), "This Account is already exist ! ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Person person = new Person(username,password);
                    RepositoryPerson.getInstance(getContext()).add(person);
                    Intent intent = new Intent();
                    intent.putExtra(USER, mUser.getText().toString());
                    intent.putExtra(PASS, mPass.getText().toString());
                    getActivity().setResult(SignupActivity.RESULT_OK,intent);
                    getActivity().finish();
                }
            }

        });
        return view;
    }
}
