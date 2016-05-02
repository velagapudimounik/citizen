package com.drughub.citizen.patientrecords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.utils.DrughubIcon;

public class ImageFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).setBackButton(true);

        getActivity().setTitle(getArguments().getString("title"));
        final View view = inflater.inflate(R.layout.patient_record_imagescreen_fragment, container, false);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        DrughubIcon download = (DrughubIcon) view.findViewById(R.id.download);
        DrughubIcon favorite = (DrughubIcon) view.findViewById(R.id.favorite);

        download.setOnClickListener(this);
        favorite.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                Toast.makeText(getActivity(), "Download Image", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorite:
                Toast.makeText(getActivity(), "Favourite Image", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
