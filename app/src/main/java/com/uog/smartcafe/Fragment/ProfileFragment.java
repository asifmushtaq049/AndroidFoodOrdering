package com.uog.smartcafe.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uog.smartcafe.EditProfileActivity;
import com.uog.smartcafe.R;
import com.uog.smartcafe.entities.LoginObject;
import com.uog.smartcafe.util.CustomApplication;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.my_profile));
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profileImage = (ImageView)view.findViewById(R.id.profile_image);
        TextView profileName = (TextView)view.findViewById(R.id.profile_name);
        TextView profileAddress = (TextView)view.findViewById(R.id.profile_address);
        TextView profilePhone = (TextView)view.findViewById(R.id.profile_phone_number);
        TextView profileEmail = (TextView)view.findViewById(R.id.profile_email_adress);

        LoginObject loginUser = ((CustomApplication)getActivity().getApplication()).getLoginUser();
        profileName.setText(loginUser.getUsername());
        profileAddress.setText(loginUser.getAddress());
        profilePhone.setText(loginUser.getPhone());
        profileEmail.setText(loginUser.getEmail());


        return view;
    }

}
