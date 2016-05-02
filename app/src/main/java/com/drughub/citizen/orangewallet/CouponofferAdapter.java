package com.drughub.citizen.orangewallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drughub.citizen.R;

public class CouponofferAdapter extends RecyclerView.Adapter<CouponofferAdapter.AdapterInfo> {

    Context context;

    CouponofferAdapter(Context l_context)
    {
        this.context = l_context;
    }

    @Override
    public AdapterInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orange_coupon_offer,parent,false);
        return new AdapterInfo(v);
    }
    @Override
    public void onBindViewHolder(AdapterInfo holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class AdapterInfo extends RecyclerView.ViewHolder
    {

        public AdapterInfo(View itemView) {
            super(itemView);
        }
    }

}

