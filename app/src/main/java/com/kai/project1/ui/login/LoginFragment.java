package com.kai.project1.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.kai.project1.R;
import com.kai.project1.databinding.FragmentLoginBinding;
import com.kai.project1.listener.LoginListener;

import com.kai.project1.utils.FirebaseHelper;

public class LoginFragment extends Fragment implements LoginListener {

    private FragmentLoginBinding binding;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Login");

        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Goto Registeration page
                onRegisterButtonClicked();
            }
        });
    }

    void onLoginButtonClicked(){
        String email = binding.editTextLoginEmail.getText().toString();
        String password = binding.editTextLoginPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            //Call login function
            FirebaseHelper.login(email,password,this);
        }
        else{
            builder.setMessage("Please enter Data");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void onRegisterButtonClicked(){
        NavHostFragment.findNavController(this).navigate(R.id.action_LoginFragment_to_RegistrationFragment);
    }

    @Override
    public void onSuccess() {
        //Go to home page
        NavHostFragment.findNavController(this).navigate(R.id.action_LoginFragment_to_HomeFragment);
    }

    @Override
    public void onFailure(String message) {
        // Show failure msg
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}