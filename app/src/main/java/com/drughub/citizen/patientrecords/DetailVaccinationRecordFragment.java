package com.drughub.citizen.patientrecords;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.utils.CustomDialog;

import java.util.ArrayList;

/**
 * Created by Achyuth V on 4/7/2016.
 */
public class DetailVaccinationRecordFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.vaccination_reports));
        ((BaseActivity) getActivity()).setBackButton(true);

        final View view = inflater.inflate(R.layout.patient_record_out_patient_details_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.out_patient_grid);
        recyclerView.hasFixedSize();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        ArrayList<String> image_urls = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            image_urls.add("" + i);
        }
        image_urls.add("dummy");

        VaccinationDetailAdapter adapter = new VaccinationDetailAdapter(getActivity(), image_urls, getString(R.string.vaccination_reports));
        recyclerView.setAdapter(adapter);


        return view;

    }
}

class VaccinationDetailAdapter extends RecyclerView.Adapter<VaccinationDetailAdapter.DataObjectHolder> {
    Context context;
    ArrayList<String> imageUrls;
    String title;

    public VaccinationDetailAdapter(Context context, ArrayList<String> imageUrls, String title) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.title = title;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_records_image_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        if (imageUrl.equalsIgnoreCase("dummy")) {
            holder.plusIocn.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
        } else {
            holder.plusIocn.setVisibility(View.GONE);
            holder.image.setImageResource(R.drawable.mahesh);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        FrameLayout plusIocn;

        public DataObjectHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            plusIocn = (FrameLayout) itemView.findViewById(R.id.icon_plus);
            itemView.setOnClickListener(this);
            plusIocn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.icon_plus) {
                ((BaseActivity) context).setTitle("Upload Report");
                ((PatientRecordActivity) context).hideButton(true);
                final Dialog dialog = CustomDialog.showCustomDialog((BaseActivity) context, R.layout.patient_record_upload_dialog,
                        Gravity.CENTER, true, false, true);

                Spinner types = (Spinner) dialog.findViewById(R.id.type_reports);
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, context.getResources().getStringArray(R.array.typesOfRecords)) {
                    @Override
                    public int getCount() {
                        return 3;
                    }
                };
                types.setAdapter(adapter);
                types.setSelection(4);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ((BaseActivity) context).setTitle(title);
                        ((PatientRecordActivity) context).hideButton(false);
                    }
                });


            } else {
                PatientRecordActivity activity = (PatientRecordActivity) context;
                Bundle mBundle = new Bundle();
                mBundle.putString("image_url", imageUrls.get(getPosition()));
                mBundle.putString("title", context.getString(R.string.hospitalization_reports));
                ImageFragment fragment = new ImageFragment();
                fragment.setArguments(mBundle);
                activity.changeFragment(fragment);
            }

        }
    }
}
