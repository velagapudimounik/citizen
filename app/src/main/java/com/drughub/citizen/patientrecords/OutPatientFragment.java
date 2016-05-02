package com.drughub.citizen.patientrecords;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class OutPatientFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getResources().getString(R.string.outpatientPrescription));
        ((BaseActivity) getActivity()).setBackButton(true);

        final View view = inflater.inflate(R.layout.patient_record_fragment, container, false);

        EditText editText = (EditText) view.findViewById(R.id.patientRecordSearch);
        editText.setHint(getResources().getString(R.string.doctorSearch));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.patient_records_list);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));


        ArrayList<OutPatientPrescription> outPatientPrescriptions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            outPatientPrescriptions.add(new OutPatientPrescription("Dr.Amar " + i, "Fever", "Suriya Nursing Home", "Date : 24th Oct 2015"));
        }

        OutPatientAdapter adapter = new OutPatientAdapter(getActivity(), outPatientPrescriptions);
        recyclerView.setAdapter(adapter);

        return view;
    }

}

class OutPatientAdapter extends RecyclerView.Adapter<OutPatientAdapter.DataObjectHolder> {
    Context context;
    ArrayList<OutPatientPrescription> outPatientPrescriptions;

    public OutPatientAdapter(Context context, ArrayList<OutPatientPrescription> outPatientPrescriptions) {
        this.context = context;
        this.outPatientPrescriptions = outPatientPrescriptions;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_record_out_patient_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        OutPatientPrescription outPatientPrescription = outPatientPrescriptions.get(position);
        holder.name.setText(outPatientPrescription.getName() + " | " + outPatientPrescription.getDisease());
        holder.date.setText(outPatientPrescription.getHospital() + " | " + outPatientPrescription.getDate());
    }

    @Override
    public int getItemCount() {
        return outPatientPrescriptions.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, date;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.patient_name);
            date = (TextView) itemView.findViewById(R.id.patient_dob);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PatientRecordActivity activity = (PatientRecordActivity) context;
            activity.changeFragment(new DetailOutPatientRecordFragment());

        }
    }
}