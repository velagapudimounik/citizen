package com.drughub.citizen.orangewallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drughub.citizen.R;


public class SavedcardsAdpter extends RecyclerView.Adapter<SavedcardsAdpter.adptrinfo> {

    Context context;

    SavedcardsAdpter(Context l_context)
    {
        this.context = l_context;
    }
    @Override
    public adptrinfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orange_wallet_saved_card_item,parent,false);
        return new adptrinfo(v);
    }
    @Override
    public void onBindViewHolder(adptrinfo holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class adptrinfo extends RecyclerView.ViewHolder
    {

        public adptrinfo(View itemView) {
            super(itemView);
        }
    }

}