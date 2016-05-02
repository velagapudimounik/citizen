package com.drughub.citizen.orangeconnect;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drughub.citizen.R;
import com.drughub.citizen.utils.StringUtils;


import java.util.ArrayList;

public class DetailOrangeConnectFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.orange_connect_detail_fragment, container, false);

        getActivity().setTitle("Suriya");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.orange_connect_list);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<OrangeConnect> orangeConnects = new ArrayList<>();

        String temp = "5";

        for (int i = 0; i < 5; i++) {
            orangeConnects.add(new OrangeConnect("Hepatitis B3", "24 Nov 1998", StringUtils.findAndReplace(getString(R.string.orange_connect_temperature), "{temp}", temp)));
        }

        DetailOrangeConnectAdapter adapter = new DetailOrangeConnectAdapter(getActivity(), orangeConnects);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setTitle(getString(R.string.orangeconnect));
    }
}

class DetailOrangeConnectAdapter extends RecyclerView.Adapter<DetailOrangeConnectAdapter.DataObjectHolder> {
    Context context;
    ArrayList<OrangeConnect> orderConnects;

    public DetailOrangeConnectAdapter(Context context, ArrayList<OrangeConnect> orderConnects) {
        this.context = context;
        this.orderConnects = orderConnects;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orange_connect_patient_vaccine_item, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        OrangeConnect orangeConnect = orderConnects.get(position);
        holder.diseaseName.setText(orangeConnect.getDisease());
        holder.dob.setText(orangeConnect.getDob() + " | ");
        holder.administered.setText(orangeConnect.getAdministered());
    }

    @Override
    public int getItemCount() {
        return orderConnects.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView diseaseName, dob, administered;

        public DataObjectHolder(View itemView) {
            super(itemView);
            diseaseName = (TextView) itemView.findViewById(R.id.disease_name);
            dob = (TextView) itemView.findViewById(R.id.dateOfBirth);
            administered = (TextView) itemView.findViewById(R.id.administered);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OrangeConnectActivity activity = (OrangeConnectActivity) context;
            activity.changeFragment(new Detail2OrangeConnectFragment());
        }
    }


}