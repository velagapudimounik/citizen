package com.drughub.citizen.myprofile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.model.Address;
import com.drughub.citizen.model.AllCity;
import com.drughub.citizen.model.AllState;
import com.drughub.citizen.model.City;
import com.drughub.citizen.model.Country;
import com.drughub.citizen.model.DoctorClinic;
import com.drughub.citizen.model.Qualification;
import com.drughub.citizen.model.ServiceProvider;
import com.drughub.citizen.model.Specialization;
import com.drughub.citizen.model.State;
import com.drughub.citizen.network.Globals;
import com.drughub.citizen.network.Urls;
import com.drughub.citizen.utils.CustomDialog;
import com.drughub.citizen.utils.DrughubIcon;
import com.drughub.citizen.utils.HintAdapter;
import com.drughub.citizen.utils.HintSpinner;
import com.drughub.citizen.utils.PrefUtils;
import com.drughub.citizen.utils.SimpleDividerItemDecoration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int PICK_IMAGE = 110;
    public static final int CROP_IMAGE = 200;
    private static final int SELECT_PICTURE = 120;
    private final String HINT_COUNTRY = "Country";
    private final String HINT_STATE = "State";
    private final String HINT_CITY = "City";
    int day = -1, month = -1, year = -1;
    // ChangePassword
    EditText currentPassword, newPassword, confirmPassword;
    Realm realm;
    LinearLayout detailsLayout;
    LinearLayout changePasswordLayout;
    RelativeLayout myClinicsLayout;
    LinearLayout editLayout;
    Location mCurrentLocation;
    // My Profile Objects
    private ImageView profileImage;
    private TextView profileIcon;
    private View imageView;
    private RadioButton myProfile;
    private RadioGroup mProfileRadiogroup;
    private DrughubIcon editIcon, rightIcon;
    private TextView doctorName, doctorDHCode, doctorEmail;
    private boolean editMode = false;
    private String fileName;
    private Uri outputUri;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private LocationRequest mLocationRequest;
    private GoogleApiClient googleApiClient;
    // ProfileDetails
    private TextView doctorNameDetails;
    private TextView qualification;
    private TextView adressline1;
    private TextView adressline2;
    private TextView email;
    private TextView mobile;
    private TextView practiceStartDate;

    // ProfileEditDetails
    private Spinner spinnerCountry;
    private Spinner spinnerState;
    private Spinner spinnerDistrict;
    private Spinner spinnerCity;
    private Spinner spinnerSpecialization;
    private Spinner spinnerQualification;
    private RealmResults<Country> countries;
    private MultiSpinnerAdapter qualificatioAdapter, specializationAdapter;
    private View addQualification;

    // MyClinics
    private RealmResults<AllState> states;
    private RealmResults<AllCity> cities;
    private RealmResults<Specialization> specializations;
    private RealmResults<Qualification> qualifications;
    private EditText editFirstName, editMiddleName, editLastName, editBuildNumber, editDoorNumber, editStreetName, editAreaName, editPinCode, editLandMark, editEmailID, editMobile, editPractiseStartDate;
    private ArrayList<String> stateValues, cityValues;
    private View itemView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private RealmResults<DoctorClinic> doctorClinics;
    private ServiceProvider serviceProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile_activity);
        detailsLayout = (LinearLayout) findViewById(R.id.detailsLayout);
        changePasswordLayout = (LinearLayout) findViewById(R.id.changePasswordLayout);
        myClinicsLayout = (RelativeLayout) findViewById(R.id.myClinicsLayout);
        editLayout = (LinearLayout) findViewById(R.id.myProfileEditLayout);

        realm = Realm.getDefaultInstance();
        setBackButton(true);
        if (!Globals.className.equalsIgnoreCase(MyProfileActivity.this.getClass().getName()))
            getData();

        initMyProfile();
//        initProfileDetails();
//        initProfileEditDetails();
        initChangePassword();
        initMyClinics();
//        createLocationRequest();
        googleApiClient = new GoogleApiClient.Builder(MyProfileActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }

    private void initMyClinics() {
        mRecyclerView = (RecyclerView) findViewById(R.id.myclinic_recyclerview);
        itemView = (LinearLayout) findViewById(R.id.no_items);
        findViewById(R.id.addAccountButton).setOnClickListener(this);
    }

    private void initChangePassword() {
        currentPassword = (EditText) findViewById(R.id.currentPassword);
        currentPassword.setTypeface(Typeface.DEFAULT);
        newPassword = (EditText) findViewById(R.id.newPassword);
        newPassword.setTypeface(Typeface.DEFAULT);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        confirmPassword.setTypeface(Typeface.DEFAULT);
        findViewById(R.id.buttonSubmit).setOnClickListener(this);

    }

/*    private void initProfileEditDetails() {
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerDistrict = (Spinner) findViewById(R.id.spinnerDistrict);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
//        spinnerQualification = (Spinner) findViewById(R.id.spinnerQualification);
//        spinnerSpecialization = (Spinner) findViewById(R.id.spinnerSpecialization);
//        addQualification = findViewById(R.id.addQualification);

        editFirstName = (EditText) findViewById(R.id.editFirstName);
                editMiddleName = (EditText) findViewById(R.id.editMiddleName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editBuildNumber = (EditText) findViewById(R.id.editBuildingName);
        // editYearsOfExp = (EditText) findViewById(R.id.editYearsOfExperience);
        editDoorNumber = (EditText) findViewById(R.id.editDoorNo);
        editStreetName = (EditText) findViewById(R.id.editStreetName);
        editAreaName = (EditText) findViewById(R.id.editAreaName);
        editPinCode = (EditText) findViewById(R.id.editPinCode);
        editLandMark = (EditText) findViewById(R.id.editLandMark);
        editEmailID = (EditText) findViewById(R.id.editEmailAddress);
        editMobile = (EditText) findViewById(R.id.editMobile);
        editPractiseStartDate = (EditText) findViewById(R.id.editPracticeStartDate);
        editPractiseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int local_year, int monthOfYear, int dayOfMonth) {
                        editPractiseStartDate.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + local_year);
                        editPractiseStartDate.setTextColor(Color.GRAY);
                        day = dayOfMonth;
                        month = monthOfYear;
                        year = local_year;
                    }
                };
                CustomDialog.showDatePicker(MyProfileActivity.this, onDateSetListener, day, month, year);

            }
        });

        addQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQualificationDialog();
            }
        });

        findViewById(R.id.buttonUpdate).setOnClickListener(this);

    }*/

 /*   private void addQualificationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MyProfileActivity.this);
        final EditText editText = new EditText(MyProfileActivity.this);
        alert.setTitle("Qualification");
        alert.setView(editText);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String val = editText.getText().toString();
                if (!val.isEmpty()) {
                    addQualification(val);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void addQualification(String value) {
        final JSONObject object = new JSONObject();
        try {
            object.put("qualificationName", value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Globals.POST(Urls.ADD_QUALIFICATION, object.toString(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object1 = new JSONObject(result);
                    realm.beginTransaction();
                    realm.createOrUpdateObjectFromJson(Qualification.class, object1.getJSONObject("response").toString());
                    realm.commitTransaction();
                    arrangeQualificationSpinner();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
                Log.v("result", result);

            }
        }, "Please Wait...");
    }*/
/*
    private void initProfileDetails() {
        doctorNameDetails = (TextView) findViewById(R.id.doctor_name);
        qualification = (TextView) findViewById(R.id.qualification);
        adressline1 = (TextView) findViewById(R.id.textAddressLine1);
        adressline2 = (TextView) findViewById(R.id.textAddressLine2);
        email = (TextView) findViewById(R.id.email);
        practiceStartDate = (TextView) findViewById(R.id.practiceStartDate);
        mobile = (TextView) findViewById(R.id.mobile);
        setDetails();
    }

    private void setDetails() {
        if (serviceProvider != null) {
            email.setText(serviceProvider.getEmailId());
            mobile.setText(serviceProvider.getMobile());
            doctorNameDetails.setText("");
            if (serviceProvider.getFirstName() != null)
                doctorNameDetails.setText(serviceProvider.getFirstName());
            if (serviceProvider.getMiddleName() != null)
                doctorNameDetails.append(" " + serviceProvider.getMiddleName());
            if (serviceProvider.getLastName() != null)
                doctorNameDetails.append(" " + serviceProvider.getLastName());

            String qualificationStr = "";
            if (serviceProvider.getQualificationList() != null && serviceProvider.getQualificationList().size() > 0) {
                for (Qualification qualification : serviceProvider.getQualificationList()) {
                    qualificationStr += qualification.getValue() + ", ";
                }
            }
            if (serviceProvider.getSpecializationList() != null && serviceProvider.getSpecializationList().size() > 0) {
                for (Specialization specialization : serviceProvider.getSpecializationList()) {
                    qualificationStr += specialization.getValue() + ", ";
                }
//                qualificationStr += (", " + serviceProvider.getSpecializationList().get(0).getValue());
            }


            if (!qualificationStr.isEmpty()) {
                qualificationStr = qualificationStr.substring(0, qualificationStr.length() - 2);
                qualification.setText(qualificationStr);
            }

            if (serviceProvider.getAddress() != null) {
                adressline1.setText(serviceProvider.getAddress().getAddressLine1());
                adressline2.setText(serviceProvider.getAddress().getAddressLine2());
            } else {
                realm.beginTransaction();
                realm.allObjects(State.class).clear();
                realm.allObjects(City.class).clear();
                serviceProvider.setAddress(realm.createObject(Address.class));
                serviceProvider.getAddress().setState(realm.createObject(State.class));
                serviceProvider.getAddress().setCity(realm.createObject(City.class));
                realm.commitTransaction();
            }

            if (serviceProvider.getPractiseStartDate() != null && !serviceProvider.getPractiseStartDate().isEmpty()) {
                String dateStr = Globals.convertDateFormat(serviceProvider.getPractiseStartDate(), "yyyy-MM-dd", "dd-MM-yyyy");
                practiceStartDate.setText(dateStr);
            }
        }

    }
    */

    private void initMyProfile() {
        mProfileRadiogroup = (RadioGroup) findViewById(R.id.myProfileRadiogroup);
        doctorName = (TextView) findViewById(R.id.doctorName);
        doctorDHCode = (TextView) findViewById(R.id.doctorID);
        doctorEmail = (TextView) findViewById(R.id.doctorEmail);
        editIcon = (DrughubIcon) findViewById(R.id.Editicon);
        rightIcon = (DrughubIcon) findViewById(R.id.rightmark);
        imageView = findViewById(R.id.image_view);
        profileIcon = (DrughubIcon) findViewById(R.id.profile_icon);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        imageView.setEnabled(false);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                profileIcon.setVisibility(View.INVISIBLE);
                profileImage.setVisibility(View.VISIBLE);
            }
        });

        myProfile = (RadioButton) findViewById(R.id.myProfileButton);
        final RadioButton changepassword = (RadioButton) findViewById(R.id.changepasswordbutton);
        final RadioButton myclinic = (RadioButton) findViewById(R.id.Myclinic_button);
//        getSupportFragmentManager().beginTransaction().replace(R.id.container2, new MyProfileDetailsFragment()).commit();
        myProfile.setChecked(true);
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateProfile();
                onUpdateProfile();

            }
        });
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllViews();

                if (!myProfile.isChecked()) {
                    editMode = true;
                    myProfile.setChecked(true);
                    changepassword.setChecked(false);
                    myclinic.setChecked(false);
                    imageView.setEnabled(true);
                }
                setEditDetails();
                editLayout.setVisibility(View.VISIBLE);
                profileIcon.setText(getResources().getText(R.string.icon_foradd_image));
                rightIcon.setVisibility(View.VISIBLE);
                editIcon.setVisibility(View.INVISIBLE);
            }
        });
        mProfileRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideAllViews();
                switch (checkedId) {
                    case R.id.myProfileButton:
                        if (!editMode)
                            detailsLayout.setVisibility(View.VISIBLE);
//                            getSupportFragmentManager().beginTransaction().replace(R.id.container2, new MyProfileDetailsFragment()).commit();
                        editMode = false;

                        break;
                    case R.id.changepasswordbutton:
                        myProfile.setChecked(false);
                        rightIcon.setVisibility(View.INVISIBLE);
                        editIcon.setVisibility(View.VISIBLE);
                        profileIcon.setText(getResources().getText(R.string.icon_noimage));
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container2, new MyProfileChangePasswordFragment()).commit();
                        changePasswordLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.Myclinic_button:
                        myProfile.setChecked(false);
                        rightIcon.setVisibility(View.INVISIBLE);
                        editIcon.setVisibility(View.VISIBLE);
                        profileIcon.setText(getResources().getText(R.string.icon_noimage));
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container2, new MyClinicsFragment()).commit();
                        myClinicsLayout.setVisibility(View.VISIBLE);
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

                        loadClinics();
                        break;
                }
            }
        });
    }

    private void setEditDetails() {
        if (serviceProvider != null) {
            editFirstName.setText(serviceProvider.getFirstName());
            editMiddleName.setText(serviceProvider.getMiddleName());
            editLastName.setText(serviceProvider.getLastName());
            editEmailID.setText(serviceProvider.getEmailId());
            editMobile.setText(serviceProvider.getMobile());
            if (serviceProvider.getPractiseStartDate() != null && !serviceProvider.getPractiseStartDate().isEmpty()) {
                String dateStr = Globals.convertDateFormat(serviceProvider.getPractiseStartDate(), "yyyy-MM-dd", "dd-MM-yyyy");
                editPractiseStartDate.setText(dateStr);
            }

            countries = realm.where(Country.class).findAllSorted("value");
            if (countries.size() > 0) {
                final ArrayList<String> values = new ArrayList<>();
                for (Country country : countries) {
                    values.add(country.getValue());
                }
                convertToHintSpinner(spinnerCountry, values, HINT_COUNTRY);

                if (serviceProvider.getAddress() != null) {
                    editBuildNumber.setText(serviceProvider.getAddress().getBuildingName());
                    editDoorNumber.setText(serviceProvider.getAddress().getDoorNumber());
                    editStreetName.setText(serviceProvider.getAddress().getStreetName());
                    editAreaName.setText(serviceProvider.getAddress().getAreaName());
                    editPinCode.setText(serviceProvider.getAddress().getPostalCode());
                    editLandMark.setText(serviceProvider.getAddress().getLandMark());
                    if (serviceProvider.getAddress().getCountry() != null) {
                        int pos = values.indexOf(serviceProvider.getAddress().getCountry().getValue());
                        if (pos > 0)
                            spinnerCountry.setSelection(pos);
                    }
                }
            }

            arrangeSpecializationSpinner();
            arrangeQualificationSpinner();

            stateValues = new ArrayList<>();
            convertToHintSpinner(spinnerState, stateValues, HINT_STATE);

            cityValues = new ArrayList<>();
            convertToHintSpinner(spinnerCity, cityValues, HINT_CITY);

        }
    }

    private HintSpinner<String> convertToHintSpinner(final Spinner spinner, List<String> dataSet, String hint) {
        return new HintSpinner<>(spinner,
                new HintAdapter<>(MyProfileActivity.this, hint, hint, dataSet), new HintSpinner.Callback<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                if (spinner == spinnerCountry)
                    getStates(position);
                else if (spinner == spinnerState)
                    getCities(position);
            }
        });
    }

    private void hideAllViews() {
        detailsLayout.setVisibility(View.GONE);
        changePasswordLayout.setVisibility(View.GONE);
        myClinicsLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.GONE);
    }

    private void getData() {


        Globals.GET(Urls.SERVICE_PROVIDER + PrefUtils.getUserName(getApplicationContext()), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.v("SP GET result", result);
                    JSONObject object = new JSONObject(result);
                    try {
                        realm.beginTransaction();
                        realm.allObjects(ServiceProvider.class).clear();
                        realm.createOrUpdateObjectFromJson(ServiceProvider.class, object.getJSONObject("response"));
                        realm.commitTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                        realm.cancelTransaction();
                    }
                    serviceProvider = realm.where(ServiceProvider.class).findFirst();
                    onUpdateProfile();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.containeractivity, new MyProfileFragment()).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
            }
        }, "");
        Globals.getCountries(new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    realm.beginTransaction();
//                    realm.allObjects(Country.class).clear();
                    realm.createOrUpdateAllFromJson(Country.class, (new JSONObject(result)).getJSONArray("response").toString());
                    realm.commitTransaction();
                } catch (JSONException e) {
                    e.printStackTrace();
                    realm.cancelTransaction();
                }
            }

            @Override
            public void onFail(String result) {
            }
        });

        getQualifications();
        getSpecializations();
        getClinics();
        Globals.className = MyProfileActivity.this.getClass().getName();
    }

    private void selectImage() {
        fileName = "temp_" + System.currentTimeMillis();
        //SyncStateContract.Constants.iscamera=true;
        final CharSequence[] items = {"Take a photo", "Select from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        builder.setTitle("Add a Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take a photo")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(android.os.Environment.getExternalStorageDirectory(), fileName);
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(camera, PICK_IMAGE);
                } else if (items[item].equals("Select from Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PICTURE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode + "====requestcode");
        System.out.println(resultCode + "====resultcode");
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                outputUri = selectedImageUri;
                Crop.of(selectedImageUri, outputUri).asSquare().start(MyProfileActivity.this);
            } else if (requestCode == PICK_IMAGE) {
                File file = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : file.listFiles()) {
                    if (temp.getName().equals(fileName)) {
                        file = temp;
                        break;
                    }
                }
                try {
                    Uri uri = Uri.fromFile(file);
                    outputUri = uri;
                    Crop.of(uri, outputUri).asSquare().start(MyProfileActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Crop.REQUEST_CROP) {
                System.out.println(requestCode + "=====requestcode2");
//                Toast.makeText(getActivity(), "Crop", Toast.LENGTH_SHORT).show();
                profileImage.setImageURI(outputUri);
                System.out.println(profileImage + "====p=====");
            }
        }
    }

    void onUpdateProfile() {
        hideAllViews();
        editMode = false;
        imageView.setEnabled(false);
        myProfile.setChecked(true);
        if (serviceProvider != null) {
            doctorName.setText("Dr." + serviceProvider.getFirstName());
            doctorDHCode.setText("" + serviceProvider.getSpProfileId());
            doctorEmail.setText(serviceProvider.getEmailId());
            //setDetails();
        }
        detailsLayout.setVisibility(View.VISIBLE);
        rightIcon.setVisibility(View.INVISIBLE);
        editIcon.setVisibility(View.VISIBLE);
        profileIcon.setText(getResources().getText(R.string.icon_noimage));
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
//        realm.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonUpdate:
                onUpdateProfile();
                //updateProfile();

                break;
            case R.id.buttonSubmit:
                changePassword();
                break;
            case R.id.addAccountButton:
                addClinic();
                break;
        }
    }

    private void addClinic() {
        MyProfileAddClinicFragment fragment = new MyProfileAddClinicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("doctorClinic", "addClinic");
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.containeractivity, fragment).addToBackStack(null).commit();
    }

    private void changePassword() {
        String currentPwd = currentPassword.getText().toString();
        String newPwd = newPassword.getText().toString();
        String confirmPwd = confirmPassword.getText().toString();
        if (currentPwd.isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.passwordValidation), Toast.LENGTH_SHORT).show();
        else if (newPwd.isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.newPasswordValidation), Toast.LENGTH_SHORT).show();
        else if (confirmPwd.isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.ValidNewPassword), Toast.LENGTH_SHORT).show();
        else {
            if (newPwd.equals(confirmPwd)) {
                JSONObject object = new JSONObject();
                try {
                    String newPass = Globals.encryptString(newPwd);
                    object.put("oldpassword", Globals.encryptString(currentPassword.getText().toString().trim()));
                    object.put("newpassword", newPass);
                    object.put("confirmpassword", newPass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Globals.POST(Urls.CHANGE_PASSWORD, object.toString(), new Globals.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            currentPassword.setText("");
                            newPassword.setText("");
                            confirmPassword.setText("");
                            Toast.makeText(getApplicationContext(), getString(R.string.passwordAdded), Toast.LENGTH_SHORT).show();
                            Log.v("result==change", result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(String result) {
                    }
                }, "");
            }
        }
    }

    public void updateProfile() {

        if (editFirstName.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.firstNameValidation), Toast.LENGTH_SHORT).show();
        else if (editMiddleName.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.middleNameValidation), Toast.LENGTH_SHORT).show();
        else if (editLastName.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.lastNameValidation), Toast.LENGTH_SHORT).show();
        else if (editBuildNumber.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.buildingNumberValidation), Toast.LENGTH_SHORT).show();
        else if (editPractiseStartDate.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.practiceValidation), Toast.LENGTH_SHORT).show();
        else if (editDoorNumber.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.doorNumberValidation), Toast.LENGTH_SHORT).show();
        else if (editStreetName.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.streetNameValidation), Toast.LENGTH_SHORT).show();
        else if (editAreaName.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.areaNameValidation), Toast.LENGTH_SHORT).show();
        else if (spinnerCountry.getSelectedItem().toString().equalsIgnoreCase(HINT_COUNTRY))
            Toast.makeText(getApplicationContext(), getString(R.string.countryValidation), Toast.LENGTH_SHORT).show();
        else if (spinnerState.getSelectedItem().toString().equalsIgnoreCase(HINT_STATE))
            Toast.makeText(getApplicationContext(), getString(R.string.stateValidation), Toast.LENGTH_SHORT).show();
        else if (spinnerCity.getSelectedItem().toString().equalsIgnoreCase(HINT_CITY))
            Toast.makeText(getApplicationContext(), getString(R.string.cityValidation), Toast.LENGTH_SHORT).show();
        else if (editPinCode.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.postalCodeValidation), Toast.LENGTH_SHORT).show();
        else if (editLandMark.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.landmarkValidation), Toast.LENGTH_SHORT).show();
        else if (editEmailID.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.emialIdValidation), Toast.LENGTH_SHORT).show();
        else if (mobile.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), getString(R.string.mobileValidation), Toast.LENGTH_SHORT).show();
        else {
            try {
                realm.beginTransaction();
                serviceProvider.setMobile(editMobile.getText().toString().trim());
                serviceProvider.setFirstName(editFirstName.getText().toString().trim());
                serviceProvider.setLastName(editLastName.getText().toString().trim());
                serviceProvider.setEmailId(editEmailID.getText().toString().trim());
                serviceProvider.setMiddleName(editMiddleName.getText().toString().trim());
                {
                    RealmList<Qualification> qualificationsList = new RealmList<>();
                    ArrayList<Boolean> checked = qualificatioAdapter.getCheckedArray();
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.get(i)) {
                            qualificationsList.add(qualifications.get(i));
                        }
                    }
                    serviceProvider.setQualificationList(qualificationsList);
                }
                {
                    RealmList<Specialization> specializationList = new RealmList<>();
                    ArrayList<Boolean> checked = specializationAdapter.getCheckedArray();
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.get(i)) {
                            specializationList.add(specializations.get(i));
                        }
                    }
                    serviceProvider.setSpecializationList(specializationList);

                }
                if (!editPractiseStartDate.getText().toString().isEmpty()) {
                    String dateStr = Globals.convertDateFormat(editPractiseStartDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd");
                    serviceProvider.setPractiseStartDate(dateStr);
                }

                if (serviceProvider.getAddress() == null) {
                    serviceProvider.setAddress(realm.createObject(Address.class));
                    serviceProvider.getAddress().setAddressId(-1);
                }
                if (mCurrentLocation != null) {
                    serviceProvider.getAddress().setLat(mCurrentLocation.getLatitude());
                    serviceProvider.getAddress().setLng(mCurrentLocation.getLongitude());
                }
                serviceProvider.getAddress().setBuildingName(editBuildNumber.getText().toString());
                serviceProvider.getAddress().setDoorNumber(editDoorNumber.getText().toString());
                serviceProvider.getAddress().setStreetName(editStreetName.getText().toString());
                serviceProvider.getAddress().setAreaName(editAreaName.getText().toString());
                serviceProvider.getAddress().setPostalCode(editPinCode.getText().toString());
                serviceProvider.getAddress().setLandMark(editLandMark.getText().toString());
                if (serviceProvider.getAddress().getCountry() == null) {
                    Country country = realm.createOrUpdateObjectFromJson(Country.class, countries.get(spinnerCountry.getSelectedItemPosition()).getValueIdsCode());
                    serviceProvider.getAddress().setCountry(country);
                } else {
                    serviceProvider.getAddress().setCountry(countries.get(spinnerCountry.getSelectedItemPosition()));
                }
                if (serviceProvider.getAddress().getCity() == null) {
                    City city = realm.createOrUpdateObjectFromJson(City.class, cities.get(spinnerCity.getSelectedItemPosition()).getValueIds());
                    serviceProvider.getAddress().setCity(city);
                } else {
                    serviceProvider.getAddress().setCity(realm.createOrUpdateObjectFromJson(City.class, cities.get(spinnerCity.getSelectedItemPosition()).getValueIds()));
                }
                if (serviceProvider.getAddress().getState() == null) {
                    State state = realm.createOrUpdateObjectFromJson(State.class, states.get(spinnerState.getSelectedItemPosition()).getValueIds());
                    serviceProvider.getAddress().setState(state);
                } else {
                    serviceProvider.getAddress().setState((realm.createOrUpdateObjectFromJson(State.class, states.get(spinnerState.getSelectedItemPosition()).getValueIds())));
                }

                realm.commitTransaction();
            } catch (Exception e) {
                realm.cancelTransaction();
            }

            serviceProvider.UpdateServiceProvider(getApplicationContext(), new Globals.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    onUpdateProfile();
                    Toast.makeText(getApplicationContext(), getString(R.string.profileUpdated), Toast.LENGTH_SHORT).show();
                    Log.v("update SP response", result);
                }

                @Override
                public void onFail(String result) {
                    Log.v("update SP fail response", result);
                }
            });
        }
    }

    private void getSpecializations() {
        Globals.getSpecialization(new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    realm.beginTransaction();
//                    realm.allObjects(Specialization.class).clear();
                    realm.createOrUpdateAllFromJson(Specialization.class, (new JSONObject(result)).getJSONArray("response").toString());
                    realm.commitTransaction();

                } catch (Exception e) {
                    e.printStackTrace();
                    realm.cancelTransaction();
                }
            }

            @Override
            public void onFail(String result) {
            }
        });
    }

    private void arrangeSpecializationSpinner() {
        try {

            specializations = realm.where(Specialization.class).findAllSorted("value");
            ArrayList<String> values = new ArrayList<>();
            ArrayList<Boolean> checked = new ArrayList<>();
            for (Specialization specialization : specializations) {
                values.add(specialization.getValue());
                if (serviceProvider.getSpecializationList().contains(specialization))
                    checked.add(true);
                else
                    checked.add(false);
            }
//                    spinnerSpecialization.setAdapter(new SpinnerAdapter(getApplicationContext(), values));
            specializationAdapter = new MultiSpinnerAdapter(getApplicationContext(), values, checked, "Specializations");
            spinnerSpecialization.setAdapter(specializationAdapter);
//            int pos = values.indexOf(serviceProvider.getSpecializationList().get(0).getValue());
//            if (pos >= 0)
//                spinnerSpecialization.setSelection(pos);
//            else
//                spinnerSpecialization.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void arrangeQualificationSpinner() {
        try {
            qualifications = realm.where(Qualification.class).findAllSorted("value");
            ArrayList<String> values = new ArrayList<>();
            ArrayList<Boolean> checked = new ArrayList<>();
            for (Qualification qualification : qualifications) {
                values.add(qualification.getValue());

                if (serviceProvider.getQualificationList().contains(qualification)) {
                    checked.add(true);
                } else {
                    checked.add(false);
                }
            }
            qualificatioAdapter = new MultiSpinnerAdapter(getApplicationContext(), values, checked, "Qualifications");
            spinnerQualification.setAdapter(qualificatioAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getQualifications() {

        Globals.getQualification(new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    realm.beginTransaction();
//                    realm.allObjects(Qualification.class).clear();
                    realm.createOrUpdateAllFromJson(Qualification.class, (new JSONObject(result)).getJSONArray("response").toString());
                    realm.commitTransaction();
                } catch (Exception e) {
                    realm.cancelTransaction();
                }
            }

            @Override
            public void onFail(String result) {
            }
        });
    }


    private void getStates(int position) {
        Globals.getState(countries.get(position).getId(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    try {
                        realm.beginTransaction();
                        if (states != null)
                            states.clear();
                        realm.allObjects(AllState.class).clear();
                        realm.createOrUpdateAllFromJson(AllState.class, (new JSONObject(result)).getJSONArray("response").toString());
                        realm.commitTransaction();
                    } catch (Exception e) {
                        realm.cancelTransaction();
                    }
                    states = realm.where(AllState.class).findAllSorted("value");
                    if (states.size() > 0) {
                        stateValues = new ArrayList<>();
                        for (AllState state : states)
                            stateValues.add(state.getValue());

                        convertToHintSpinner(spinnerState, stateValues, HINT_STATE);
                        int pos = stateValues.indexOf(serviceProvider.getAddress().getState().getValue());
                        if (pos > 0)
                            spinnerState.setSelection(pos);
                    } else {
                        stateValues = new ArrayList<>();
                        convertToHintSpinner(spinnerState, stateValues, HINT_STATE);

                        cityValues = new ArrayList<>();
                        convertToHintSpinner(spinnerCity, cityValues, HINT_CITY);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
            }
        });
    }

    private void getCities(int position) {
        Globals.getCity(states.get(position).getId(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    try {
                        realm.beginTransaction();
                        if (cities != null)
                            cities.clear();
                        realm.allObjects(AllCity.class).clear();
                        realm.createOrUpdateAllFromJson(AllCity.class, (new JSONObject(result)).getJSONArray("response").toString());
                        realm.commitTransaction();
                    } catch (Exception e) {
                        realm.cancelTransaction();
                    }
                    cities = realm.where(AllCity.class).findAllSorted("value");
                    cityValues = new ArrayList<>();
                    for (AllCity city : cities)
                        cityValues.add(city.getValue());

                    convertToHintSpinner(spinnerCity, cityValues, HINT_CITY);

                    int pos = cityValues.indexOf(serviceProvider.getAddress().getCity().getValue());
                    if (pos >= 0)
                        spinnerCity.setSelection(pos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
            }
        });
    }

    public void loadClinics() {
        if (doctorClinics != null && doctorClinics.size() > 0) {
            getClinicsFromRealm();
        }

    }

    private void getClinics() {
        Globals.GET(Urls.CLINIC, new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result")) {
                        realm.beginTransaction();
                        realm.allObjects(DoctorClinic.class).clear();
                        realm.createOrUpdateAllFromJson(DoctorClinic.class, object.getJSONArray("response"));
                        Log.i("Clinic_Response", object.getJSONArray("response").toString());
                        realm.commitTransaction();
                        getClinicsFromRealm();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
            }
        }, "");
    }


    public void getClinicsFromRealm() {
        doctorClinics = realm.allObjects(DoctorClinic.class);
        for (DoctorClinic doctorClinic : doctorClinics) {
            if (doctorClinic.getAddress() == null) {
                doctorClinic.setAddress(realm.createObject(Address.class));
                doctorClinic.getAddress().setState(realm.createObject(State.class));
                doctorClinic.getAddress().setCity(realm.createObject(City.class));
                doctorClinic.getAddress().setCountry(realm.createObject(Country.class));
            }
        }
        addValuesToRecyclerView();

    }

    private void addValuesToRecyclerView() {
        MyClinicsListAdapter adapter = new MyClinicsListAdapter(getApplicationContext(), doctorClinics);
        if (doctorClinics.size() > 0)
            itemView.setVisibility(View.GONE);
        else
            itemView.setVisibility(View.VISIBLE);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
            }
        });


        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public class MyClinicsListAdapter extends RecyclerSwipeAdapter<MyClinicsListAdapter.RecyclerViewHolder> {

        Context context = null;
        RealmResults<DoctorClinic> doctorClinics;

        public MyClinicsListAdapter(Context context, RealmResults<DoctorClinic> doctorClinics) {
            this.context = context;
            this.doctorClinics = doctorClinics;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.myprofile_clinic_item, parent, false);

            return new RecyclerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position) {
            final DoctorClinic doctorClinic = doctorClinics.get(position);

            viewHolder.hospitalName.setText(doctorClinic.getClinicName());
            viewHolder.hospitalAddress.setText(doctorClinic.getAddress().getStreetName() + ", " + doctorClinic.getAddress().getBuildingName());

            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = CustomDialog.showQuestionDialog(MyProfileActivity.this, getResources().getString(R.string.deleteClinicMessage));

                    View noBtn = dialog.findViewById(R.id.dialogNoBtn);
                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    View yesBtn = dialog.findViewById(R.id.dialogYesBtn);
                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("clinicID", doctorClinics.get(position).getAddress().getCountry().getId() + "");
                            doctorClinic.DeleteClinic(doctorClinics.get(position).getClinicId(), new Globals.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    realm.beginTransaction();
                                    doctorClinic.removeFromRealm();
                                    realm.commitTransaction();
                                    //notifyDataSetChanged();

                                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, doctorClinics.size());
                                    mItemManger.closeAllItems();

                                    if (doctorClinics.size() > 0)
                                        itemView.setVisibility(View.GONE);
                                    else
                                        itemView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFail(String result) {
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                }
            });

            viewHolder.updatebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globals.selectedDoctorClinic = doctorClinic;
                    MyProfileAddClinicFragment fragment = new MyProfileAddClinicFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("doctorClinic", "fromEdit");
                    notifyDataSetChanged();
                    Log.i("clinicID", Globals.selectedDoctorClinic.getAddress().getState().getValue() + "");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.containeractivity, fragment).addToBackStack(null).commit();
                }
            });
            viewHolder.myClinicItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globals.selectedDoctorClinic = doctorClinic;
                    getSupportFragmentManager().beginTransaction().add(R.id.containeractivity, new MyProfileClinicDetailsFragment()).addToBackStack(null).commit();
                }
            });

            mItemManger.bindView(viewHolder.itemView, position);
        }

        @Override
        public int getItemCount() {
            return doctorClinics.size();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {

            SwipeLayout swipeLayout;
            View deleteBtn, updatebtn;
            TextView hospitalName, hospitalAddress;
            View myClinicItem;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                myClinicItem = itemView.findViewById(R.id.myClinicItem);
                deleteBtn = itemView.findViewById(R.id.deleteClinic);
                updatebtn = itemView.findViewById(R.id.editClinic);
                hospitalName = (TextView) itemView.findViewById(R.id.hospitalName);
                hospitalAddress = (TextView) itemView.findViewById(R.id.hospitalAddress);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            }
        }
    }

}

