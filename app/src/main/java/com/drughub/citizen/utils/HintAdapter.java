package com.drughub.citizen.utils;

/*
 * Copyright (c) 2015 Sergio Rodrigo
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Allows adding a hint at the end of the list. It will show the hint when adding it and selecting
 * the last object. Otherwise, it will show the dropdown view implemented by the concrete class.
 */
public class HintAdapter<T> extends ArrayAdapter<T> {
    private static final String TAG = HintAdapter.class.getSimpleName();

    private static final int DEFAULT_LAYOUT_RESOURCE = android.R.layout.simple_spinner_dropdown_item;
    private final LayoutInflater layoutInflater;
    private int layoutResource;
    private String hintResource;
    private String noItemsMessage;
    private final Callback<T> callback;
    private HintSpinner<T> hintSpinner;
    private List<T> dataSet;

    public HintAdapter(Context context, int hintResource, int noItemsMessage, List<T> data) {
        this(context, DEFAULT_LAYOUT_RESOURCE, context.getString(hintResource), context.getString(noItemsMessage), data, null);
    }

    public HintAdapter(Context context, String hint, String noItemsMessage, List<T> data) {
        this(context, DEFAULT_LAYOUT_RESOURCE, hint, noItemsMessage, data, null);
    }

    public HintAdapter(Context context, int layoutResource, int hintResource, int noItemsMessage, List<T> data, Callback<T> callback) {
        this(context, layoutResource, context.getString(hintResource), context.getString(noItemsMessage), data, callback);
    }

    public HintAdapter(Context context, int layoutResource, String hintResource, String noItemsMessage, List<T> data, Callback<T> callback) {
        // Create a copy, as we need to be able to add the hint without modifying the array passed in
        // or crashing when the user sets an unmodifiable.
        super(context, layoutResource, data);
        this.layoutResource = layoutResource;
        this.hintResource = hintResource;
        this.noItemsMessage = noItemsMessage;
        this.layoutInflater = LayoutInflater.from(context);
        this.callback = callback;
        this.dataSet = data;
    }

    public void changeDate(List<T> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position == getHintPosition()) {
            view = getDefaultView();
        } else {
            view = getCustomView(position);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position);
    }

    @Override
    public T getItem(int position) {
        return dataSet.get(position);
    }

    private View getDefaultView() {
        View view = inflateDefaultLayout();
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText("");
        textView.setHint(isEmpty() ? noItemsMessage : hintResource);
        textView.setHintTextColor(Color.LTGRAY);
        return view;
    }

    protected View getCustomView(int position) {
        View view = inflateLayout(layoutResource);
        if(callback == null) {
            Object item = getItem(position);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(item.toString());
            textView.setHint("");
            textView.setTextColor(Color.DKGRAY);
        }
        else
            view = callback.setItemDetails(view, position, (T) getItem(position));
        return view;
    }

    private View inflateDefaultLayout() {
        return inflateLayout(DEFAULT_LAYOUT_RESOURCE);
    }

    private View inflateLayout(int resource) {
        return layoutInflater.inflate(resource, null);
    }

    @Override
    public int getCount() {
        int count = dataSet.size();
        return count > 0 ? count : 1;
    }

    /**
     * Gets the position of the hint.
     *
     * @return Position of the hint
     */
    public int getHintPosition() {
        int count = dataSet.size();
        return count > 0 ? count + 1 : count;
    }

    @Override
    public boolean isEmpty() {
        return dataSet.size() == 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        hintSpinner.getSpinner().setEnabled(!isEmpty());
    }

    public void setHintSpinner(HintSpinner<T> hintSpinner) {
        this.hintSpinner = hintSpinner;
    }

    public HintSpinner<T> getHintSpinner() {
        return hintSpinner;
    }

    public interface Callback<T> {
        /**
         * When a spinner item view created.
         *
         * @param position       Position
         * @param itemAtPosition Item
         */
        View setItemDetails(View view, int position, T itemAtPosition);
    }
}



