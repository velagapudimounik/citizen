package com.drughub.citizen.utils;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CustomDialog
{

    final static int COLOR_TRANSPARENT = 0xDD000000;

    public static Dialog showCustomDialog(BaseActivity activity, int layoutId, int gravity,
                                          boolean isTranslucent, boolean wrapContent, boolean showActionbar)
    {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutId);

        dialog.getWindow().setGravity(gravity);

        if(isTranslucent)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(COLOR_TRANSPARENT));

        if(showActionbar || wrapContent) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int activity_height = rect.height();
            int actionbar_height = activity.getActionBarHeight();
            if(wrapContent)
                dialog.getWindow().setLayout(rect.width(), WindowManager.LayoutParams.WRAP_CONTENT);
            else
                dialog.getWindow().setLayout(rect.width(), activity_height-actionbar_height);

            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }

        //dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;
    }

    public static Dialog showMessageDialog(BaseActivity activity, String messageTxt)
    {
        return showMessageDialog(activity, messageTxt, activity.getResources().getString(R.string.custom_dialog_default_message));
    }

    public static Dialog showMessageDialog(BaseActivity activity, String messageTxt, String okBtnTxt)
    {
        Dialog dialog = showCustomDialog(activity, R.layout.dialog_message, Gravity.CENTER, true, false, true);

        TextView textView = (TextView)dialog.findViewById(R.id.dialogMessage);
        textView.setText(Html.fromHtml(messageTxt));
        Button okBtn = (Button)dialog.findViewById(R.id.dialogOkBtn);
        okBtn.setText(okBtnTxt);

        return dialog;
    }

    public static Dialog showQuestionDialog(BaseActivity activity, String questionTxt)
    {
        return showQuestionDialog(activity, questionTxt, activity.getResources().getString(R.string.custom_dialog_no_message), activity.getResources().getString(R.string.custom_dialog_yes_message));
    }

    public static Dialog showQuestionDialog(BaseActivity activity, String questionTxt, String noBtnTxt, String yesBtnTxt)
    {
        Dialog dialog = showCustomDialog(activity, R.layout.dialog_question, Gravity.CENTER, true, false, true);

        TextView textView = (TextView)dialog.findViewById(R.id.dialogQuestion);
        textView.setText(questionTxt);
        Button noBtn = (Button)dialog.findViewById(R.id.dialogNoBtn);
        noBtn.setText(noBtnTxt);
        Button yesBtn = (Button)dialog.findViewById(R.id.dialogYesBtn);
        yesBtn.setText(yesBtnTxt);

        return dialog;
    }
    public static Dialog showRatingDialog(final BaseActivity activity)
    {
        final Dialog dialog = showCustomDialog(activity, R.layout.dialog_rateapp, Gravity.CENTER, true, false, true);

        RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.ratingbar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        Button submit = (Button)dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

   public static void showDatePicker(BaseActivity context,DatePickerDialog.OnDateSetListener onDateSetListener , int local_day ,int local_month, int local_year)
   {
       if((local_day == -1) || (local_month == -1) || (local_year == -1))
       {
             Calendar cal = GregorianCalendar.getInstance();
             local_day = cal.get(Calendar.DAY_OF_MONTH);
             local_month = cal.get(Calendar.MONTH);
             local_year = cal.get(Calendar.YEAR);
       }

       DatePickerDialog mDatePicker = new DatePickerDialog(context, onDateSetListener, local_year, local_month, local_day);
       mDatePicker.getDatePicker().setCalendarViewShown(false);
       mDatePicker.setTitle("Select date");
       mDatePicker.show();
   }
    public static void showtimePicker(BaseActivity context,TimePickerDialog.OnTimeSetListener OnTimeSetListener , int local_hour ,int local_time)
    {
        if((local_time == -1) || (local_hour == -1) )
        {
            Calendar cal = GregorianCalendar.getInstance();
            local_hour = cal.get(Calendar.HOUR_OF_DAY);
            local_time = cal.get(Calendar.MINUTE);
            Log.i(" CAlender", Calendar.AM + "") ;
            Log.i(" CAlender", Calendar.PM + "") ;
        }

        TimePickerDialog mTimePicker = new TimePickerDialog(context, OnTimeSetListener, local_hour, local_time, false);
        mTimePicker.setTitle("Select Time");

        mTimePicker.show();
    }

}
