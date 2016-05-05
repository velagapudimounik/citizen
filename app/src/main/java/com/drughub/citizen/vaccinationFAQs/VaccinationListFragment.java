package com.drughub.citizen.vaccinationFAQs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drughub.citizen.R;
import com.drughub.citizen.utils.DrughubIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Achyuth V on 5/4/2016.
 */
public class VaccinationListFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vaccination_faq_main,container,false);
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.itemList);
        listView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);


        VaccineAdapter vaccineAdapter = new VaccineAdapter(getContext());
        listView.setAdapter(vaccineAdapter);



        return view;
    }

    public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.viewHolder> {
        Context mContext;
        private List<Boolean> expanded;

        VaccineAdapter(Context context) {
            mContext = context;
            expanded = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                expanded.add(false);

        }

        @Override
        public VaccineAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_fad_child, parent, false);
            viewHolder info = new viewHolder(view);

            return info;
        }

        @Override
        public void onBindViewHolder(final VaccineAdapter.viewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.linearLayout.getVisibility() == View.VISIBLE) {
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.arrow.setRotation(270);
                        expanded.set(position,false);
                    } else {
                        holder.linearLayout.setVisibility(View.VISIBLE);
                        holder.arrow.setRotation(360);
                        expanded.set(position,true);
                    }
                }
            });
            if (!expanded.get(position)) {
                holder.linearLayout.setVisibility(View.GONE);
                holder.arrow.setRotation(270);
            } else {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.arrow.setRotation(360);
            }
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.layoutShow);
            DrughubIcon arrow = (DrughubIcon) itemView.findViewById(R.id.arrow);

            public viewHolder(final View itemView) {
                super(itemView);
                TextView temperature_txt = (TextView) itemView.findViewById(R.id.temperature_text);
                temperature_txt.setText("IPV can be given two doses at 2 month interval.\nOPV need not be given with these IPV doses.\nPolio vaccine storage and dosing OPV is a very \nheat sensitive vaccine having a shelf life of 2\n years at a temperature of 20°C, 6 months at 2 \nto 8°C and 1-3 days at roo temperature");
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }
}
