package com.drughub.citizen.myprofile;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.drughub.citizen.R;

import java.util.ArrayList;

public class MultiSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    ArrayList<String> values;
    ArrayList<Boolean> checked;
    String hintText;

    public MultiSpinnerAdapter(Context context, ArrayList<String> values, ArrayList<Boolean> checked, String hintText) {
        super(context, R.layout.myporfile_spinner_multi, values);
        this.context = context;
        this.values = values;
        this.checked = checked;
        this.hintText = hintText;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return spinnerSet(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.myporfile_spinner_item, null);

        String selectedItems = "";
        for(int i = 0; i < checked.size(); i++) {
            if(checked.get(i)) {
                if(!selectedItems.isEmpty())
                    selectedItems += ", ";
                selectedItems += values.get(i);
            }
        }
        TextView textView = (TextView) view.findViewById(R.id.spinner_text_view);
        textView.setText(selectedItems.isEmpty() ? hintText : selectedItems);

        if(selectedItems.isEmpty())
            textView.setTextColor(Color.LTGRAY);
        else
            textView.setTextColor(Color.DKGRAY);

        return view;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    View spinnerSet(final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(R.layout.myporfile_spinner_multi, null);
        String val = values.get(position);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.spinner_textview);
        checkBox.setText(val);
        checkBox.setChecked(checked.get(position));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked.set(position, checkBox.isChecked());
                notifyDataSetChanged();
            }
        });

        checkBox.setTextColor(Color.DKGRAY);

        return convertView;
    }

    public ArrayList<Boolean> getCheckedArray() {
        return checked;
    }

    public ArrayList<String> getStringValues() {
        return values;
    }
}