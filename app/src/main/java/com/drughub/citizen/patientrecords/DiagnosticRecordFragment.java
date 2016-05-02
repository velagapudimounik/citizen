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
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class DiagnosticRecordFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    RadioGroup radioGroup;
    ArrayList<OutPatientPrescription> outPatientPrescriptions;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getResources().getString(R.string.diagnostic_report));
        ((BaseActivity) getActivity()).setBackButton(true);

        final View view = inflater.inflate(R.layout.patient_record_fragment, container, false);

        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.testTabs);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(this);
        frameLayout.setVisibility(View.VISIBLE);


        RadioButton urineTests = (RadioButton) view.findViewById(R.id.radioButton1);
        RadioButton bloodTests = (RadioButton) view.findViewById(R.id.radioButton2);
        RadioButton radiologicalTests = (RadioButton) view.findViewById(R.id.radioButton3);
        urineTests.setChecked(true);

//        urineTests.setOnCheckedChangeListener(this);
//        bloodTests.setOnCheckedChangeListener(this);
//        radiologicalTests.setOnCheckedChangeListener(this);
//        Typeface ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/drughub-mobile-doctor.ttf");
//        urineTests.setTypeface(ttf);
//        bloodTests.setTypeface(ttf);
//        radiologicalTests.setTypeface(ttf);


        EditText editText = (EditText) view.findViewById(R.id.patientRecordSearch);
        editText.setHint(getResources().getString(R.string.hintDiagnosticSearch));
        recyclerView = (RecyclerView) view.findViewById(R.id.patient_records_list);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));


        outPatientPrescriptions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            outPatientPrescriptions.add(new OutPatientPrescription("Amar " + i, "Fever", "Suriya Nursing Home", "Date : 24th Oct 2015"));
        }
        DiagnosticAdapter adapter = new DiagnosticAdapter(getActivity(), getString(R.string.urine_test), outPatientPrescriptions);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:

                DiagnosticAdapter adapter = new DiagnosticAdapter(getActivity(), getString(R.string.urine_test), outPatientPrescriptions);
                recyclerView.setAdapter(adapter);
                break;
            case R.id.radioButton2:
                DiagnosticAdapter adapter1 = new DiagnosticAdapter(getActivity(), getString(R.string.blood_test), outPatientPrescriptions);
                recyclerView.setAdapter(adapter1);
                break;
            case R.id.radioButton3:
                DiagnosticAdapter adapter2 = new DiagnosticAdapter(getActivity(), getString(R.string.radiological_test), outPatientPrescriptions);
                recyclerView.setAdapter(adapter2);
                break;
        }
    }
}

class DiagnosticAdapter extends RecyclerView.Adapter<DiagnosticAdapter.DataObjectHolder> {
    Context context;
    ArrayList<OutPatientPrescription> outPatientPrescriptions;
    String reportName;

    public DiagnosticAdapter(Context context, String reportName, ArrayList<OutPatientPrescription> outPatientPrescriptions) {
        this.context = context;
        this.reportName = reportName;
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
        holder.hospital_name.setText(outPatientPrescription.getHospital());
        holder.date.setText(outPatientPrescription.getDate());
    }

    @Override
    public int getItemCount() {
        return outPatientPrescriptions.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, date , hospital_name;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.patient_name);
            date = (TextView) itemView.findViewById(R.id.patient_dob);
            hospital_name = (TextView) itemView.findViewById(R.id.hospital_name);
            hospital_name.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PatientRecordActivity activity = (PatientRecordActivity) context;
            DetailDiagnosticRecordFragment fragment = new DetailDiagnosticRecordFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", reportName);
            fragment.setArguments(bundle);
            activity.changeFragment(fragment);

        }
    }
}