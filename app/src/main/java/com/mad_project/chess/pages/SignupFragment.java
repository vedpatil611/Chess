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

public class SignupFragment extends Fragment {

    EditText emailText;
    EditText passwordText;
    EditText cPasswordText;
    Button signupButton;

    private SignupInterface mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SignupInterface) {
            mListener = (SignupInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        emailText = v.findViewById(R.id.signup_email);
        passwordText = v.findViewById(R.id.signup_password);
        cPasswordText = v.findViewById(R.id.signup_c_password);
        signupButton = v.findViewById(R.id.signup_button);
        signupButton.setOnClickListener(view -> {
            mListener.signup(emailText.getText().toString(), passwordText.getText().toString(), cPasswordText.getText().toString());
        });

        return v;
    }

    public interface SignupInterface {
        void signup(String email, String password, String cpassword);
    }
}