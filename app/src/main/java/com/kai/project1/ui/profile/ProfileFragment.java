package com.kai.project1.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kai.project1.R;
import com.kai.project1.databinding.FragmentProfileBinding;
import com.kai.project1.listener.ProfileListener;
import com.kai.project1.listener.ProfileRetrieveListener;
import com.kai.project1.utils.FirebaseHelper;
import com.kai.project1.model.User;

public class ProfileFragment extends Fragment implements ProfileListener, ProfileRetrieveListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentProfileBinding binding;
    AlertDialog.Builder builder;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        FirebaseHelper.userDetails(this);

        binding.buttonProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
        binding.buttonProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClicked();
            }
        });
    }
    void onCancelClicked(){
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }
    void onUpdateClicked(){
        String firstname = binding.editTextProfileFirstName.getText().toString();
        String lastname = binding.editTextProfileLastName.getText().toString();
        String city = binding.editTextProfileCity.getText().toString();
        String gender;
        int genderID = binding.radioGroup.getCheckedRadioButtonId();
        if(genderID == binding.radioButtonProfileMale.getId()){
            gender = "male";
        }
        else{
            gender = "female";
        }
        User user = new User(firstname,lastname,gender,city, FirebaseHelper.getUser().getUid());
        FirebaseHelper.profileUpdate(user,this);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }

    @Override
    public void onSuccess(User user) {
        binding.editTextProfileFirstName.setText(user.getFirstname());
        binding.editTextProfileLastName.setText(user.getLastname());
        binding.editTextProfileCity.setText(user.getCity());
        if(user.getGender().equals("male")){
            binding.radioButtonProfileMale.setChecked(true);
        }
        else{
            binding.radioButtonProfileFemale.setChecked(true);
        }
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }

    @Override
    public void onFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}