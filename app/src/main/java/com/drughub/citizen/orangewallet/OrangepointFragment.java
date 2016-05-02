package com.drughub.citizen.orangewallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drughub.citizen.R;

public class OrangepointFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstancestate){
       super.onCreate(savedInstancestate);

    }
    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.orange_points,container,false);
    }
}




