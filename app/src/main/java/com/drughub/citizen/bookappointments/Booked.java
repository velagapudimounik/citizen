package com.drughub.citizen.bookappointments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.utils.CustomDialog;

import org.json.JSONArray;


public class Booked extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booked, container,false);


    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycleBooked);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        SensoroListAdapter adptr=new SensoroListAdapter();
        recyclerView.setAdapter(adptr);
    }



    public class SensoroListAdapter extends RecyclerSwipeAdapter<SensoroListAdapter.ViewHolderr> {

        private static final String TAG = "VaccineListAdapter";

        JSONArray connectedDevicesList;
        FragmentActivity context;

     /*   public SensoroListAdapter(JSONArray connectedDevicesList, FragmentActivity context) {
            this.connectedDevicesList = connectedDevicesList;
            this.context = context;
        }*/

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
            //   mItemManger.bindView(viewHolder.itemView, position);

        }

        @Override
        public ViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.booked_single, parent, false);

            return new ViewHolderr(v);

        }

        @Override
        public void onBindViewHolder(final ViewHolderr viewHolder, final int position) {


            viewHolder.name.setText(Html.fromHtml("<font color=\"#FF5722\">Suriya |</font> disease"));
            viewHolder.date.setText(Html.fromHtml("<font color=\"#A9A9A9\">12 Apr 2016 | 05:00PM-06:00PM</font>"));


            viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            viewHolder.LLreschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {





                   final Dialog dialog=CustomDialog.showQuestionDialog((BaseActivity) getActivity(),"Are you sure? You want to Reschedule.");
                    Button yes=(Button)dialog.findViewById(R.id.dialogYesBtn);
                    Button no=(Button)dialog.findViewById(R.id.dialogNoBtn);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getActivity(),"Yes",Toast.LENGTH_LONG).show();
                            //new DeleteSensoro(url, request, true).execute();
                            dialog.dismiss();

                        }

                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(),"No",Toast.LENGTH_LONG).show();

                        }
                    });


                }
            });

            viewHolder.LLcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {





                    final Dialog dialog=CustomDialog.showQuestionDialog((BaseActivity) getActivity(),"Are you sure? You want to Cancel the Appointment.");
                    Button yes=(Button)dialog.findViewById(R.id.dialogYesBtn);
                    Button no=(Button)dialog.findViewById(R.id.dialogNoBtn);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getActivity(),"Yes",Toast.LENGTH_LONG).show();
                            //new DeleteSensoro(url, request, true).execute();
                            dialog.dismiss();

                        }

                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            Toast.makeText(getActivity(),"No",Toast.LENGTH_LONG).show();

                        }
                    });




                }


            });

            mItemManger.bindView(viewHolder.itemView, position);


        }

        @Override
        public int getItemCount() {
            return 9;
        }


        public class ViewHolderr extends RecyclerView.ViewHolder {
             ImageView ImgDelete;
            TextView name, date, cancel, Reschedule;
            SwipeLayout swipeLayout;
            LinearLayout LLreschedule,LLcancel;


            public ViewHolderr(View v) {
                super(v);

                name = (TextView)v.findViewById(R.id.name);
                date = (TextView)v.findViewById(R.id.date1);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
                cancel = (TextView) v.findViewById(R.id.Cancel);
                Reschedule = (TextView) v.findViewById(R.id.Reschedule);
                LLreschedule = (LinearLayout) v.findViewById(R.id.LLreschedule);
                LLcancel = (LinearLayout) v.findViewById(R.id.LLcancel);




            }

        }
    }

/*
    public class adappter extends RecyclerSwipeAdapter<ViewHolderr> {


        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }



        @Override
        public ViewHolderr onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.booked_single, viewGroup, false);

            return new ViewHolderr(v);
        }

        @Override
        public void onBindViewHolder(ViewHolderr viewHolder, int position) {

            viewHolder.name.setText(Html.fromHtml("<font color=\"#FF5722\">Suriya |</font> disease"));
            viewHolder.date.setText(Html.fromHtml("<font color=\"#A9A9A9\">12 Apr 2016 | 05:00PM-06:00PM</font>"));

            viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }


        @Override
        public int getItemCount() {
            return 9;
        }



    }*/






}
