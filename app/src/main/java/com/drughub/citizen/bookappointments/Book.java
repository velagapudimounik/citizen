package com.drughub.citizen.bookappointments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drughub.citizen.R;


public class Book extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycle);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        adappter adptr=new adappter();
        recyclerView.setAdapter(adptr);
    }


    public class adappter extends RecyclerView.Adapter<adappter.ViewHolderr>{


        public class ViewHolderr extends RecyclerView.ViewHolder {
           // final TextView degree, designation;
           // private final ImageView imageV;

            public ViewHolderr(View v) {
                super(v);
               // imageV = (ImageView) v.findViewById(R.id.IVimage);
               // degree = (TextView) v.findViewById(R.id.degree);
                //designation = (TextView) v.findViewById(R.id.designation);

            }

        }

        @Override
        public ViewHolderr onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.book_single, viewGroup, false);

            return new ViewHolderr(v);
        }

        @Override
        public void onBindViewHolder(ViewHolderr holder, int position) {
            //holder.degree.setText("ZZZZZZZZZ");

        }



        @Override
        public int getItemCount() {
            return 9;
        }
    }



}
