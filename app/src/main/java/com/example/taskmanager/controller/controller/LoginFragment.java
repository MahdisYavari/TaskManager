package com.example.taskmanager.controller.controller;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.model.Person;
import com.example.taskmanager.controller.model.Task;
import com.example.taskmanager.controller.repository.RepositoryPerson;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String EXTRA_USER = "EXTRA_USER.com.example.taskmanager.controller";
    public static final String EXTRA_PASS = "EXTRA_PASS.com.example.taskmanager.controller";
    public static final int REQUEST_CODE_SIGNUP = 0;
    private EditText mUser, mPass;
    private Button mLogin;
    private TextView mTextView, mTextViewAdmin;
    String user;
    String pass;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mUser = view.findViewById(R.id.username);
        mPass = view.findViewById(R.id.password);
        mTextView = view.findViewById(R.id.text_view);
        mLogin = view.findViewById(R.id.login_button);
        mTextViewAdmin = view.findViewById(R.id.text_view_admin);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!mUser.getText().toString().equals("") && !mPass.getText().toString().equals("")
                        &&RepositoryPerson.getInstance(getContext()).
                        existPerson(mUser.getText().toString()) &&
                        RepositoryPerson.getInstance(getContext()).validateUserAndPass(mUser.getText().toString(), mPass.getText().toString()) )) {


                        Person person = RepositoryPerson.getInstance(getContext()).getPerson(mUser.getText().toString());
                        Intent intent = TaskManagerActivity.newIntent(getContext(), person.getID());
                        startActivity(intent);

                } else if ((mUser.getText().toString().equals("") && !mPass.getText().toString().equals("")
                        || !(RepositoryPerson.getInstance(getContext()).existPerson(mUser.getText().toString())&&
                        RepositoryPerson.getInstance(getContext()).validateUserAndPass(mUser.getText().toString(), mPass.getText().toString())
                        ))) {
                    Toast.makeText(getActivity(), " first ,you should register! " +
                            " press REGISTER to register ", Toast.LENGTH_LONG).show();
                }

            }
        });
        mTextViewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser.getText().toString().equalsIgnoreCase("admin")
                        && mPass.getText().toString().equalsIgnoreCase("admin")) {

                    Intent intent = AdminActivity.newIntent(getContext());
                    startActivity(intent);

                }else{
                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignupActivity.class);
                intent.putExtra(EXTRA_PASS, String.valueOf(mPass.getText()));
                intent.putExtra(EXTRA_USER, String.valueOf(mUser.getText()));
                startActivityForResult(intent, REQUEST_CODE_SIGNUP);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == SignupActivity.RESULT_CANCELED || data == null)
            return;
        if (requestCode == REQUEST_CODE_SIGNUP) {
            pass = data.getStringExtra(SignUpFragment.PASS);
            user = data.getStringExtra(SignUpFragment.USER);
        }
        mPass.setText(pass);
        mUser.setText(user);


    }
}
