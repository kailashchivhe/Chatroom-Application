package com.kai.project1.ui.home;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.kai.project1.R;
import com.kai.project1.databinding.FragmentProfileBinding;
import com.kai.project1.listener.ProfileListener;
import com.kai.project1.utils.FirebaseHelper;
import com.kai.project1.utils.User;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment  implements ProfileListener {
    
    FragmentProfileBinding binding;
    AlertDialog.Builder builder;
    final String TAG="Vidit";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!!!!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        binding.buttonProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });

    }

    void onCancelClicked(){
        Log.d(TAG, "onClick: 1");
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }

    void onUpdateClicked(){
        String userid = FirebaseHelper.getUser().getUid();
        String firstName = binding.editTextProfileFirstName.getText().toString();
        String lastName = binding.editTextProfileLastName.getText().toString();
        String city = binding.editTextProfileCity.getText().toString();
        int genderID = binding.RadioGroupProfileGender.getCheckedRadioButtonId();
        String gender = null;
        if(genderID == binding.radioButtonProfileMale.getId()){
            gender = "male";
        }
        else{
            gender = "female";
        }
        if(!firstName.isEmpty() && !lastName.isEmpty() && !city.isEmpty()){
            User user = new User(firstName,lastName,gender,city,userid);
            FirebaseHelper.profileUpdate(user,this);
        }
        else{
            builder.setMessage("Please enter Data");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onSuccess() {
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }

    @Override
    public void onFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}