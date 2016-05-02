package com.drughub.citizen.utils;
/*
 * Copyright (c) 2015 Sergio Rodrigo
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.List;

/**
 * Provides methods to work with a hint element.
 */
public class HintSpinner<T> {
    private static final String TAG = HintSpinner.class.getSimpleName();
    private final Spinner spinner;
    private final HintAdapter<T> adapter;
    private final Callback<T> callback;
    public HintSpinner(Spinner spinner, HintAdapter<T> adapter, Callback<T> callback) {
        this.spinner = spinner;
        this.adapter = adapter;
        this.callback = callback;

        init();
    }

    /**
     * Initializes the hint spinner.
     * <p/>
     * By default, the hint is selected when calling this method.
     */
    public void init() {
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (HintSpinner.this.callback == null) {
                    return;
                }
                if (!isHintPosition(position)) {
                    Object item = HintSpinner.this.spinner.getItemAtPosition(position);
                    HintSpinner.this.callback.onItemSelected(position, (T) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing selected");
            }
        });
        adapter.setHintSpinner(this);
        spinner.setEnabled(!adapter.isEmpty());
        selectHint();
    }

    public void changeData(List<T> dataSet) {
        adapter.changeDate(dataSet);
    }

    private boolean isHintPosition(int position) {
        return position == adapter.getHintPosition();
    }

    /**
     * Selects the hint element.
     */
    public void selectHint() {
        spinner.setSelection(adapter.getHintPosition());
    }

    /**
     * Used to handle the spinner events.
     *
     * @param <T> Type of the data used by the spinner
     */
    public interface Callback<T> {
        /**
         * When a spinner item has been selected.
         *
         * @param position       Position selected
         * @param itemAtPosition Item selected
         */
        void onItemSelected(int position, T itemAtPosition);
    }

    public Spinner getSpinner()
    {
        return spinner;
    }
}

