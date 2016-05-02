package com.drughub.citizen.myfamily;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.model.DoctorClinic;
import com.drughub.citizen.model.Patient;
import com.drughub.citizen.model.VaccinationSchedule;
import com.drughub.citizen.network.Globals;
import com.drughub.citizen.network.Urls;
import com.drughub.citizen.utils.CustomDialog;
import com.drughub.citizen.utils.HintAdapter;
import com.drughub.citizen.utils.HintSpinner;
import com.drughub.citizen.utils.StringUtils;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PatientVaccineScheduleActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    int clicked_type = 0;
    boolean clickedType = true;

    private Realm realm;
    private RealmResults<VaccinationSchedule> vaccinationSchedules;
    private Patient patient;
    private String patientDHCode;
    private RealmResults<DoctorClinic> clinicList;
    private Spinner clinicSpinner;
    private TextView patientInfo;
    private View rootView;
    private ProgressBar vaccinationProgress;
    private View progressMarker;
    private TextView progressText;
    private TextView progressMaxText;
    private int day = -1, month = -1, year = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfamily_patient_vaccine_schedule);
        setTitle("");
        setBackButton(true);
        patientDHCode = getIntent().getStringExtra("patientDHCode");
        realm = Realm.getDefaultInstance();

        rootView = findViewById(R.id.rootView);
        rootView.setVisibility(View.INVISIBLE);

        Globals.GET(StringUtils.findAndReplace(Urls.PATIENT_VACCINATION_SCHEDULE, Urls.DH_CODE, patientDHCode), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    realm.beginTransaction();

                    JSONObject object = new JSONObject(result);
                    JSONObject responseObject = new JSONObject(object.getString("response"));
                    patient = realm.createOrUpdateObjectFromJson(Patient.class, responseObject.getJSONObject("patientDetails"));
                    realm.clear(VaccinationSchedule.class);
                    realm.createOrUpdateAllFromJson(VaccinationSchedule.class, responseObject.getJSONArray("vaccinationScheduleList"));

                    RealmResults<VaccinationSchedule> vaccinationSchedules = realm.where(VaccinationSchedule.class).findAll();
                    int total = vaccinationSchedules.size();
                    int completed = (int) vaccinationSchedules.where().equalTo("status.value", "Vaccination Administered").count();
                    patient.setIsVaccineDetailsAvailable(true);
                    patient.setVaccinesTotal(total);
                    patient.setVaccinesDone(completed);

                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    setPatientDetails(patient);
                    rootView.setVisibility(View.VISIBLE);

                    realm.commitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                    realm.cancelTransaction();
                    Toast.makeText(PatientVaccineScheduleActivity.this, "Exception In Code", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFail(String result) {
                finish();
            }
        }, "");

        Globals.GET(Urls.CLINIC, new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    realm.beginTransaction();
                    realm.createOrUpdateAllFromJson(DoctorClinic.class, object.getJSONArray("response"));
                    realm.commitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                    realm.cancelTransaction();
                    Toast.makeText(PatientVaccineScheduleActivity.this, "Exception In Code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String result) {
            }
        }, "");

        patient = realm.where(Patient.class).equalTo("patientDHCode", patientDHCode).findFirst();
        if (patient != null)
            setTitle("Patient Name");
        final Date today = Calendar.getInstance().getTime();
        vaccinationSchedules = realm.where(VaccinationSchedule.class)
                .lessThanOrEqualTo("fromDate", today)
                .greaterThanOrEqualTo("toDate", today)
                .notEqualTo("status.value", "Vaccination Administered")
                .findAll();
        clinicList = realm.where(DoctorClinic.class).findAll();

        patientInfo = (TextView) findViewById(R.id.patientInfo);
        vaccinationProgress = (ProgressBar) findViewById(R.id.vaccinationProgress);
        progressMarker = findViewById(R.id.progressMarker);
        progressText = (TextView) findViewById(R.id.progressText);
        progressMaxText = (TextView) findViewById(R.id.progressMaxText);

        mRecyclerView = (RecyclerView) findViewById(R.id.patient_vaccine_schedule_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        VaccineScheduleListAdapter mAdapter = new VaccineScheduleListAdapter(vaccinationSchedules, PatientVaccineScheduleActivity.this, clicked_type);
        mRecyclerView.setAdapter(mAdapter);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.toggle);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.todaysSchedule:
                        clicked_type = 0;
                        clickedType = true;
                        vaccinationSchedules = realm.where(VaccinationSchedule.class)
                                .lessThanOrEqualTo("fromDate", today)
                                .greaterThanOrEqualTo("toDate", today)
                                .notEqualTo("status.value", "Vaccination Administered")
                                .findAll();
                        break;
                    case R.id.upcomingSchedule:
                        clicked_type = 1;
                        clickedType = false;
                        vaccinationSchedules = realm.where(VaccinationSchedule.class)
                                .greaterThan("fromDate", today)
                                .notEqualTo("status.value", "Vaccination Administered").findAll();
                        break;
                    case R.id.missedSchedule:
                        clicked_type = 2;
                        clickedType = true;
                        vaccinationSchedules = realm.where(VaccinationSchedule.class)
                                .lessThan("toDate", today)
                                .notEqualTo("status.value", "Vaccination Administered").findAll();
                        break;
                    case R.id.administeredSchedules:
                        clicked_type = 3;
                        clickedType = false;
                        vaccinationSchedules = realm.where(VaccinationSchedule.class).equalTo("status.value", "Vaccination Administered").findAll();
                        break;
                }
                VaccineScheduleListAdapter mAdapter = new VaccineScheduleListAdapter(vaccinationSchedules, PatientVaccineScheduleActivity.this, clicked_type);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    private void setPatientDetails(Patient patient) {
        if (patient == null)
            return;

        int total = patient.getVaccinesTotal();
        int completed = patient.getVaccinesDone();

        setTitle(patient.getPatientName());
        String DOB = Globals.convertDateFormat(patient.getDateOfBirth(), "yyyy-MM-dd", "dd MMM yyyy");
        String TOB = Globals.convertDateFormat(patient.getTimeOfBirth(), "kk:mm:ss", "hh:mm a");
        patientInfo.setText(getResources().getString(R.string.patientInfo, DOB, TOB, ((completed * 100) / total) + "%"));

        vaccinationProgress.setProgress((completed * vaccinationProgress.getMax()) / total);
        int progressBarWidth = vaccinationProgress.getWidth();
        progressMarker.setX((completed * progressBarWidth) / total);
        progressText.setText(String.format("%02d", completed));
        progressText.setX((completed * progressBarWidth) / total);
        progressMaxText.setText(String.format("%02d", total));
    }

    public void showAdministerDialog(final Context sContext, final int vaccineScheduleId) {
        final Dialog dialog = CustomDialog.showCustomDialog((BaseActivity) sContext, R.layout.myfamily_administer,
                Gravity.BOTTOM, true, true, false);
        final CheckBox alreadyAdministered = (CheckBox) dialog.findViewById(R.id.alreadyAdminister);
        Button doneButton = (Button) dialog.findViewById(R.id.doneButton);
        clinicSpinner = (Spinner) dialog.findViewById(R.id.changeClinic);
        HintSpinner<DoctorClinic> hintSpinner = new HintSpinner<>(clinicSpinner,
                new HintAdapter<>(sContext, R.layout.clinic_spinner_item, "My Clinics", "No Clinics Available", clinicList, new HintAdapter.Callback<DoctorClinic>() {
                    @Override
                    public View setItemDetails(View view, int position, DoctorClinic itemAtPosition) {
                        TextView clinicName = (TextView) view.findViewById(R.id.string1);
                        TextView Address = (TextView) view.findViewById(R.id.string2);
                        clinicName.setText(itemAtPosition.getClinicName() + " | ");
                        Address.setText(itemAtPosition.getAddress().getAreaName());
                        return view;
                    }
                }), null);

        if (clinicList.size() > 0)
            clinicSpinner.setSelection(0);

        final EditText doctorName = (EditText) dialog.findViewById(R.id.doctorName);
        final EditText clinicName = (EditText) dialog.findViewById(R.id.clinicName);
        final EditText dateOfAdminister = (EditText) dialog.findViewById(R.id.dateOfAdminister);
        if (clicked_type == 2) {
            final FrameLayout changeClinic = (FrameLayout) dialog.findViewById(R.id.clinicLayout);
            final LinearLayout edtLayout = (LinearLayout) dialog.findViewById(R.id.edtTextLayout);
            alreadyAdministered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        changeClinic.setVisibility(View.GONE);
                        edtLayout.setVisibility(View.VISIBLE);
                    } else {
                        changeClinic.setVisibility(View.VISIBLE);
                        edtLayout.setVisibility(View.GONE);
                    }
                }
            });
        }

        if (clicked_type == 0) {
            alreadyAdministered.setVisibility(View.GONE);
        }

        dateOfAdminister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.showDatePicker(PatientVaccineScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int local_year, int monthOfYear, int dayOfMonth) {
                        dateOfAdminister.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + local_year);
                        day = dayOfMonth;
                        month = monthOfYear;
                        year = local_year;
                    }
                }, day, month, year);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alreadyAdministered.isChecked()) {
                    boolean goOn = false;
                    if (doctorName.getText().toString().isEmpty())
                        Toast.makeText(PatientVaccineScheduleActivity.this, "Enter Doctor Name", Toast.LENGTH_SHORT).show();
                    else if (clinicName.getText().toString().isEmpty())
                        Toast.makeText(PatientVaccineScheduleActivity.this, "Enter Clinic Name", Toast.LENGTH_SHORT).show();
                    else if (dateOfAdminister.getText().toString().isEmpty())
                        Toast.makeText(PatientVaccineScheduleActivity.this, "Select Administered Date", Toast.LENGTH_SHORT).show();
                    else
                        goOn = true;

                    if (!goOn) return;
                } else {
                    if (clinicSpinner.getAdapter().isEmpty()) {
                        Toast.makeText(PatientVaccineScheduleActivity.this, "Select clinic to proceed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                try {
                    JSONObject requestObject = new JSONObject();
                    requestObject.put("vaccinationScheduleId", vaccineScheduleId);
                    requestObject.put("brandItemId", 7);
                    requestObject.put("status", new JSONObject().put("value", "Administered"));
                    requestObject.put("isAdministered", alreadyAdministered.isChecked());
                    if (alreadyAdministered.isChecked()) {
                        requestObject.put("doctorName", doctorName.getText().toString());
                        //requestObject.put("addressDetails", "address");
                        requestObject.put("administeredDate", Globals.convertDateFormat(dateOfAdminister.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
                        requestObject.put("remarks", clinicName.getText().toString());
                    } else
                        requestObject.put("clinicId", clinicList.get(clinicSpinner.getSelectedItemPosition()).getClinicId());

                    Globals.PUT(StringUtils.findAndReplace(Urls.PATIENT_VACCINATION_SCHEDULE, Urls.DH_CODE, patientDHCode), requestObject.toString()
                            , new Globals.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                realm.beginTransaction();

                                JSONObject resultObject = new JSONObject(result);
                                realm.createOrUpdateObjectFromJson(VaccinationSchedule.class, resultObject.getString("response"));
                                RealmResults<VaccinationSchedule> vaccinationSchedules = realm.where(VaccinationSchedule.class).findAll();
                                int total = vaccinationSchedules.size();
                                int completed = (int) vaccinationSchedules.where().equalTo("status.value", "Vaccination Administered").count();
                                patient.setIsVaccineDetailsAvailable(true);
                                patient.setVaccinesTotal(total);
                                patient.setVaccinesDone(completed);

                                if (alreadyAdministered.isChecked()) {
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                    setPatientDetails(patient);
                                }
                                else {
                                    setResult(RESULT_OK);
                                    finish();
                                }

                                realm.commitTransaction();
                            } catch (Exception e) {
                                e.printStackTrace();
                                realm.cancelTransaction();
                                Toast.makeText(PatientVaccineScheduleActivity.this, "Exception In Code", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(String result) {}
                    }, "");

                    //sContext.startActivity(new Intent(sContext, AvailableInventoryActivity.class));
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class VaccineScheduleListAdapter extends RecyclerView.Adapter<VaccineScheduleListAdapter.ViewHolder> {
        FragmentActivity sContext;
        int clicked;
        private List<VaccinationSchedule> mDataSet;

        public VaccineScheduleListAdapter(List<VaccinationSchedule> dataSet, FragmentActivity context, int clicked_type) {
            mDataSet = dataSet;
            sContext = context;
            clicked = clicked_type;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.myfamily_patient_vaccine_schedule_item, viewGroup, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.setItemDetails(mDataSet.get(position), clicked);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  sContext.startActivity(new Intent(sContext, AvailableInventoryActivity.class));
                    if (clickedType)
                        PatientVaccineScheduleActivity.this.showAdministerDialog(sContext, mDataSet.get(position).getVaccinationScheduleId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView diseaseDetails;
            private final TextView scheduleDetails;
            private final com.drughub.citizen.utils.DrughubIcon upcoming_icon;
            private final TextView upcoming_text;
            private final LinearLayout layoutIcon;

            public ViewHolder(View v) {
                super(v);

                diseaseDetails = (TextView) v.findViewById(R.id.diseaseDetails);
                scheduleDetails = (TextView) v.findViewById(R.id.scheduleDetails);
                upcoming_icon = (com.drughub.citizen.utils.DrughubIcon) v.findViewById(R.id.inventory_icon);
                upcoming_text = (TextView) v.findViewById(R.id.inventory);
                layoutIcon = (LinearLayout) v.findViewById(R.id.iconLayout);
            }

            public void setItemSelected(boolean selected) {
                itemView.setSelected(selected);
            }

            public void setItemDetails(VaccinationSchedule item, int clicked) {

                String text = item.getVaccineName() + " | <font color=\"#ff5722\">" + item.getCycleName() + "</font>";
                diseaseDetails.setText(Html.fromHtml(text));
                String fromDate = Globals.getStringFromDate(item.getFromDate(), "dd MMM yyyy");
                String toDate = Globals.getStringFromDate(item.getToDate(), "dd MMM yyyy");
                scheduleDetails.setText(fromDate + " to " + toDate);
                if (clicked == 1) {
                    upcoming_text.setText("Booked");
                    upcoming_icon.setText(sContext.getString(R.string.icon_reschedule));
                }
                if (clicked == 2) {
                    layoutIcon.setVisibility(View.INVISIBLE);
                }
                if (clicked == 3) {
                    layoutIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
