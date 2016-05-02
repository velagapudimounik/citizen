package com.drughub.citizen.patientrecords;

import android.content.Context;
import android.os.Bundle;
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


public class BookmarkedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_record_fragment);
        setBackButton(true);
        setTitle(getResources().getString(R.string.bookmark));
        EditText editText = (EditText) findViewById(R.id.patientRecordSearch);
        editText.setHint(getResources().getString(R.string.bookmarkSearch));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.patient_records_list);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        ArrayList<PatientRecord> patientRecords = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            patientRecords.add(new PatientRecord("Amar " + i, "D.O.B : 24th Nov 1998", "16"));
        }

        BookmarksAdapter adapter = new BookmarksAdapter(this, patientRecords);
        recyclerView.setAdapter(adapter);
    }
}

class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.DataObjectHolder> {
    Context context;
    ArrayList<PatientRecord> patientRecords;

    public BookmarksAdapter(Context context, ArrayList<PatientRecord> patientRecords) {
        this.context = context;
        this.patientRecords = patientRecords;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_record_item, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        PatientRecord patientRecord = patientRecords.get(position);
        holder.name.setText(patientRecord.getName());
        holder.dob.setText(patientRecord.getDob());
        holder.records.setText(patientRecord.getRecords());
    }

    @Override
    public int getItemCount() {
        return patientRecords.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, dob, records;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.patient_name);
            dob = (TextView) itemView.findViewById(R.id.patient_dob);
            records = (TextView) itemView.findViewById(R.id.records_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}



