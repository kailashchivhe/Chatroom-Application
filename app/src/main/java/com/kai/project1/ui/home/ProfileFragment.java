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
    Bitmap bitmapProfile = null;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

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
        String ans = FirebaseHelper.userDetails(FirebaseHelper.getUser());

//        binding.editTextProfileEmail.setText((CharSequence) map.get("email"));
//        binding.editTextProfileCity.setText((CharSequence) map.get("city"));
//        binding.editTextProfileFirstName.setText((CharSequence) map.get("firstName"));
//        binding.editTextProfileLastName.setText((CharSequence) map.get("lastName"));
//        binding.editTextProfilePassword.setText((CharSequence) map.get("password"));
//        if(map.get("gender").equals("male")){
//            //check male
//        }
//        else{
//            //check female
//        }
        binding.editTextProfileFirstName.setText((CharSequence) ans);

        binding.buttonProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClicked();
            }
        });
        binding.buttonProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateClicked();
            }
        });
        binding.buttonProfileProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProfileClicked();
            }
        });

    }
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK)
            {
                bitmapProfile = (Bitmap) result.getData().getExtras().get("data");
                binding.imageViewProfile.setImageBitmap(bitmapProfile);
            }
        }
    });

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startForResult.launch(cameraIntent);
            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    void onCancelClicked(){
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }
    void onProfileClicked(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startForResult.launch(cameraIntent);
        }
    }
    void onUpdateClicked(){
        String userid = FirebaseHelper.getUser().getUid();
        String firstName = binding.editTextProfileFirstName.getText().toString();
        String lastName = binding.editTextProfileLastName.getText().toString();
        String email = binding.editTextProfileEmail.getText().toString();
        String password = binding.editTextProfilePassword.getText().toString();
        String city = binding.editTextProfileCity.getText().toString();
        int genderID = binding.RadioGroupProfileGender.getCheckedRadioButtonId();
        String gender = null;
        if(genderID == binding.radioButtonProfileMale.getId()){
            gender = "male";
        }
        else{
            gender = "female";
        }
        if(!password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !gender.isEmpty() && !city.isEmpty() && !bitmapProfile.equals(null)){
            User user = new User(firstName,lastName,userid,gender,city,email,password);
            FirebaseHelper.profileUpdate(password,firstName,lastName,city,gender,bitmapProfile,this);
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