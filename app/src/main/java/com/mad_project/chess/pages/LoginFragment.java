package com.mad_project.chess.pages;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mad_project.chess.R;

public class LoginFragment extends Fragment {

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    LoginInterface mInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginInterface) {
            mInterface = (LoginInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        emailText = v.findViewById(R.id.login_email);
        passwordText = v.findViewById(R.id.login_password);
        loginButton = v.findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            mInterface.login(emailText.getText().toString(), passwordText.getText().toString());
        });

        return v;
    }

    public interface LoginInterface {
        void login(String email, String password);
    }
}