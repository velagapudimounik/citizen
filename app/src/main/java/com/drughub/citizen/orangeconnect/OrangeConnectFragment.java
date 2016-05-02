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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drughub.citizen.R;
import com.drughub.citizen.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class OrangeConnectFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.myorders_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.order_list);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        ArrayList<OrangeConnect> orangeConnects = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            orangeConnects.add(new OrangeConnect("Suriya " + i,"DH8958"+i, "24 Nov 1998", "05:12 AM","55"));
        }

        OrangeConnectAdapter adapter = new OrangeConnectAdapter(getActivity(), orangeConnects);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

class OrangeConnectAdapter extends RecyclerView.Adapter<OrangeConnectAdapter.DataObjectHolder> {
    Context context;
    ArrayList<OrangeConnect> orderConnects;

    public OrangeConnectAdapter(Context context, ArrayList<OrangeConnect> orderConnects) {
        this.context = context;
        this.orderConnects = orderConnects;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_record_out_patient_item, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        OrangeConnect orangeConnect = orderConnects.get(position);
        holder.name.setText(orangeConnect.getName() + " | " + orangeConnect.getId());
        holder.dob.setText("D.O.B : " + orangeConnect.getDob() + " | " + orangeConnect.getTime() + " | OPI:" + orangeConnect.getOpi());
    }

    @Override
    public int getItemCount() {
        return orderConnects.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, dob;
        FrameLayout userIcon;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.patient_name);
            dob = (TextView) itemView.findViewById(R.id.patient_dob);
            userIcon = (FrameLayout) itemView.findViewById(R.id.userIcon);
            userIcon.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OrangeConnectActivity activity = (OrangeConnectActivity) context;
            activity.changeFragment(new DetailOrangeConnectFragment());
        }
    }

}

