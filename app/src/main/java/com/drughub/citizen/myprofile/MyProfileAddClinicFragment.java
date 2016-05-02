package com.drughub.citizen.myprofile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.drughub.citizen.R;
import com.drughub.citizen.model.Address;
import com.drughub.citizen.model.AllCity;
import com.drughub.citizen.model.AllState;
import com.drughub.citizen.model.City;
import com.drughub.citizen.model.Country;
import com.drughub.citizen.model.DoctorClinic;
import com.drughub.citizen.model.State;
import com.drughub.citizen.network.Globals;
import com.drughub.citizen.utils.HintAdapter;
import com.drughub.citizen.utils.HintSpinner;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class MyProfileAddClinicFragment extends DialogFragment {
    private static final int SELECT_PICTURE = 12;
    ArrayList<Uri> image_urls;
    LinearLayout imageLayout;
    Spinner spinnerCountry, spinnerState, spinnerDistrict, spinnerCity;
    private final String HINT_COUNTRY = "Country";
    private final String HINT_STATE = "State";
    private final String HINT_CITY = "City";
    Button addClinic;
    RealmResults<Country> countries;
    RealmResults<AllState> states;
    RealmResults<AllCity> cities;
    Realm realm;
    EditText clinic_Name, building_name, doorNo, streetName, areaName, landmark, Mobile, consultationFee, pincode;
    CheckBox consultationHome;
    String clinicString;
    DoctorClinic selectedClinic = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.addClinic));
        View view = inflater.inflate(R.layout.myprofile_addclinic_dailogbox, container, false);
        spinnerCountry = (Spinner) view.findViewById(R.id.country_spinner);
        spinnerState = (Spinner) view.findViewById(R.id.state_spinner);
        spinnerDistrict = (Spinner) view.findViewById(R.id.district_spinner);
        spinnerCity = (Spinner) view.findViewById(R.id.townorcity_spinner);
        clinic_Name = (EditText) view.findViewById(R.id.clinic_name);
        building_name = (EditText) view.findViewById(R.id.building_name);
        doorNo = (EditText) view.findViewById(R.id.door_no);
        streetName = (EditText) view.findViewById(R.id.street_name);
        areaName = (EditText) view.findViewById(R.id.area_name);
        Mobile = (EditText) view.findViewById(R.id.mobile);
        consultationFee = (EditText) view.findViewById(R.id.consultation_fee);
        pincode = (EditText) view.findViewById(R.id.pincode);
        landmark = (EditText) view.findViewById(R.id.landmark);
        addClinic = (Button) view.findViewById(R.id.addButton);
        consultationHome = (CheckBox) view.findViewById(R.id.consultHome);
        // FrameLayout plusicon=(FrameLayout)view.findViewById(R.id.plusicon);
        image_urls = new ArrayList<>();
        image_urls.add(0, Uri.EMPTY);

        selectedClinic = null;

        Bundle bundle = this.getArguments();
        clinicString = bundle.getString("doctorClinic");
        Log.i("Bundle_vales", bundle.toString());
        if (clinicString.equals("fromEdit")) {
            getActivity().setTitle(getString(R.string.editClinic));
            selectedClinic = Globals.selectedDoctorClinic;
            clinic_Name.setText(Globals.selectedDoctorClinic.getClinicName());
            building_name.setText(Globals.selectedDoctorClinic.getAddress().getBuildingName());
            doorNo.setText(Globals.selectedDoctorClinic.getAddress().getDoorNumber());
            streetName.setText(Globals.selectedDoctorClinic.getAddress().getStreetName());
            areaName.setText(Globals.selectedDoctorClinic.getAddress().getAreaName());
            pincode.setText(Globals.selectedDoctorClinic.getAddress().getPostalCode());
            landmark.setText(Globals.selectedDoctorClinic.getAddress().getLandMark());
            Mobile.setText(Globals.selectedDoctorClinic.getPhoneNo());
            consultationFee.setText("" + Globals.selectedDoctorClinic.getConsultationFee());
            addClinic.setText("Save");
            if (Globals.selectedDoctorClinic.getIsConsultationAtHome())
                consultationHome.setChecked(true);
            else
                consultationHome.setChecked(false);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setTitle(getString(R.string.myProfile));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageLayout = (LinearLayout) view.findViewById(R.id.mainImageLayout);
        FrameLayout iconPlus = (FrameLayout) view.findViewById(R.id.icon_plus);
        iconPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
        addClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clinicName = clinic_Name.getText().toString();
                String con_fee = consultationFee.getText().toString();
                String mobile = Mobile.getText().toString();
                String buildingName = building_name.getText().toString();
                String doorno = doorNo.getText().toString();
                String streetname = streetName.getText().toString();
                String areaName = MyProfileAddClinicFragment.this.areaName.getText().toString();
                String postalcode = pincode.getText().toString();
                String landMark = landmark.getText().toString();

                if (clinicName.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.clinicNameValidation), Toast.LENGTH_SHORT).show();
                else if (buildingName.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.buildingNumberValidation), Toast.LENGTH_SHORT).show();
                else if (doorno.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.doorNumberValidation), Toast.LENGTH_SHORT).show();
                else if (streetname.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.streetNameValidation), Toast.LENGTH_SHORT).show();
                else if (areaName.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.areaNameValidation), Toast.LENGTH_SHORT).show();
                else if (spinnerState.getSelectedItem() == null)
                    Toast.makeText(getContext(), getString(R.string.stateValidation), Toast.LENGTH_SHORT).show();
                else if (spinnerCity.getSelectedItem() == null)
                    Toast.makeText(getContext(), getString(R.string.cityValidation), Toast.LENGTH_SHORT).show();
                else if (postalcode.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.postalCodeValidation), Toast.LENGTH_SHORT).show();
                else if (postalcode.length() < 6 || postalcode.equalsIgnoreCase("000000"))
                    Toast.makeText(getContext(), getString(R.string.validpostalCode), Toast.LENGTH_SHORT).show();
                else if (landMark.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.landmarkValidation), Toast.LENGTH_SHORT).show();
                else if (con_fee.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.consultaionFeeValidation), Toast.LENGTH_SHORT).show();
                else {
                    Country country = countries.get(spinnerCountry.getSelectedItemPosition());
                    AllState state = states.get(spinnerState.getSelectedItemPosition());
                    AllCity city = cities.get(spinnerCity.getSelectedItemPosition());

                    DoctorClinic clinic = new DoctorClinic();
                    clinic.setClinicName(clinicName);
                    clinic.setPhoneNo(mobile);
                    clinic.setConsultationFee(Integer.parseInt(con_fee));
                    clinic.setYearOfEstablishment(2010);
                    clinic.setWebsite("www.google.com");
                    clinic.setIsConsultationAtHome(consultationHome.isChecked());

                    if (clinic.getAddress() == null)
                        clinic.setAddress(new Address());

                    clinic.getAddress().setBuildingName(buildingName);
                    clinic.getAddress().setDoorNumber(doorno);
                    clinic.getAddress().setStreetName(streetname);
                    clinic.getAddress().setAreaName(areaName);
                    try {
                        realm.beginTransaction();
                        clinic.getAddress().setCountry(realm.createOrUpdateObjectFromJson(Country.class, country.getValueIdsCode()));
                        clinic.getAddress().setState(realm.createOrUpdateObjectFromJson(State.class, state.getValueIds()));
                        clinic.getAddress().setCity(realm.createOrUpdateObjectFromJson(City.class, city.getValueIds()));
                        realm.commitTransaction();
                    } catch (Exception e) {
                        realm.cancelTransaction();
                    }
                    clinic.getAddress().setPostalCode(postalcode);
                    clinic.getAddress().setLandMark(landMark);
                    clinic.getAddress().setAreaCode("null");
                    if (clinicString.equals("addClinic")) {
                        clinic.AddClinic(new Globals.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                    try {
                                        realm.beginTransaction();
                                        realm.createOrUpdateObjectFromJson(DoctorClinic.class, object.getJSONObject("response").toString());
                                        realm.commitTransaction();
                                    } catch (Exception e) {
                                        realm.cancelTransaction();
                                    }
                                    Toast.makeText(getContext(), getString(R.string.clinicAdded), Toast.LENGTH_SHORT).show();
                                    ((MyProfileActivity) getActivity()).getClinicsFromRealm();
                                    getFragmentManager().popBackStack();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), getString(R.string.clinicAddedError), Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFail(String result) {
                            }
                        });

                    } else {
                        clinic.setClinicId(selectedClinic.getClinicId());
                        clinic.getAddress().setAddressId(selectedClinic.getAddress().getAddressId());
                        clinic.UpdateClinic(new Globals.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                    try {
                                        realm.beginTransaction();
                                        realm.createOrUpdateObjectFromJson(DoctorClinic.class, object.getJSONObject("response").toString());
                                        realm.commitTransaction();
                                    } catch (Exception e) {
                                        realm.cancelTransaction();
                                    }
                                    Toast.makeText(getContext(), getString(R.string.clinicUpdated), Toast.LENGTH_SHORT).show();
                                    ((MyProfileActivity) getActivity()).getClinicsFromRealm();
                                    getFragmentManager().popBackStack();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), getString(R.string.clinicAddedError), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFail(String result) {
                            }
                        });
                    }
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        countries = realm.where(Country.class).findAllSorted("value");
        if (countries.size() > 0) {
            final ArrayList<String> values = new ArrayList<>();
            for (Country country : countries) {
                values.add(country.getValue());
            }
            convertToHintSpinner(spinnerCountry, values, HINT_COUNTRY);

            if (selectedClinic != null && selectedClinic.getAddress().getCountry() != null) {
                int pos = values.indexOf(selectedClinic.getAddress().getCountry().getValue());
                if (pos > 0)
                    spinnerCountry.setSelection(pos);
            }
        }

        ArrayList<String> stateValues = new ArrayList<>();
        convertToHintSpinner(spinnerState, stateValues, HINT_STATE);

        ArrayList<String> cityValues = new ArrayList<>();
        convertToHintSpinner(spinnerCity, cityValues, HINT_CITY);

    }

    private void convertToHintSpinner(final Spinner spinner, List<String> dataSet, String hint)
    {
        HintSpinner<String> hintSpinner = new HintSpinner<>(spinner,
                new HintAdapter<>(getActivity(), hint, hint, dataSet), new HintSpinner.Callback<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                if(spinner == spinnerCountry)
                    getStates(position);
                else if(spinner == spinnerState)
                    getCities(position);
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
                        realm.allObjects(AllState.class).clear();
                        realm.createAllFromJson(AllState.class, (new JSONObject(result)).getJSONArray("response").toString());
                        realm.commitTransaction();
                    } catch (Exception e) {
                        realm.cancelTransaction();
                    }

                    states = realm.where(AllState.class).findAllSorted("value");
                    final ArrayList<String> values = new ArrayList<>();
                    for (AllState state : states)
                        values.add(state.getValue());

                    if (values.size() == 0) {
                        ArrayList<String> cityValues = new ArrayList<>();
                        convertToHintSpinner(spinnerCity, cityValues, HINT_CITY);
                    }
                    convertToHintSpinner(spinnerState, values, HINT_STATE);
                    if (selectedClinic != null) {
                        int pos = values.indexOf(selectedClinic.getAddress().getState().getValue());
                        if (pos > 0)
                            spinnerState.setSelection(pos);
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
                        realm.allObjects(AllCity.class).clear();
                        realm.createAllFromJson(AllCity.class, (new JSONObject(result)).getJSONArray("response").toString());
                        realm.commitTransaction();
                    }
                    catch (Exception e){
                        realm.cancelTransaction();
                    }

                    cities = realm.where(AllCity.class).findAllSorted("value");
                    ArrayList<String> values = new ArrayList<>();
                    for (AllCity city : cities)
                        values.add(city.getValue());

                    convertToHintSpinner(spinnerCity, values, HINT_CITY);

                    if (selectedClinic != null) {
                        int pos = values.indexOf(selectedClinic.getAddress().getCity().getValue());
                        if (pos >= 0)
                            spinnerCity.setSelection(pos);
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

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == FragmentActivity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                image_urls = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                // image_urls.add(Uri.EMPTY);
                System.out.println(image_urls + "=========1");
                for (int i = 0; i < image_urls.size(); i++) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageURI(image_urls.get(i));
                    layoutParams.setMargins(10, 0, 0, 10);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    //System.out.println(i+"========i");
                    imageLayout.addView(imageView);

                }
            }
        }
    }


}
