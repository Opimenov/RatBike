/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.addedittask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.architecture.blueprints.todoapp.R;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the add bike screen.
 * Users can:
 * - take picture,
 * - select what kind of bike is it,
 * - pick what parts are available,
 * - location of the bike is set by using fine geolocation of the user
 */
public class AddEditBikeFragment extends Fragment implements AddEditBikeContract.View {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ARGUMENT_EDIT_BIKE_ID = "EDIT_BIKE_ID";

    private AddEditBikeContract.Presenter mPresenter;


    private RadioGroup typeRadioGroup;
    private RadioGroup completeRadioGroup;
    private TextView locationTextView;
    private Button   locateMeButton;
    private LinearLayout partsList;
    private ImageView mImageView;

    private CheckBox[]  checkBoxes= new CheckBox[17];

    private CheckBox frameCheckBox;
    private CheckBox seatCheckBox;
    private CheckBox frontWheelCheckBox;
    private CheckBox decentTireCheckBox;
    private CheckBox forkCheckBox;
    private CheckBox stemCheckBox;
    private CheckBox handlebarCheckBox;
    private CheckBox brakeLeversCheckBox;
    private CheckBox gearShiftersCheckBox;
    private CheckBox frontBrakeCheckBox;
    private CheckBox pedalsCheckBox;
    private CheckBox crankArmsCheckBox;
    private CheckBox frontDerailleurCheckBox;
    private CheckBox chainCheckBox;
    private CheckBox rearBreakCheckBox;
    private CheckBox rearWheelCheckBox;
    private CheckBox rearDerailleurCheckBox;
    private CheckBox derailleurOrBrakeCableCheckBox;


    public static AddEditBikeFragment newInstance() {
        return new AddEditBikeFragment();
    }

    public AddEditBikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditBikeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        typeRadioGroup = (RadioGroup) getActivity().findViewById(R.id.type_radiogroup);
        completeRadioGroup= (RadioGroup) getActivity().findViewById(R.id.yes_no_radiogroup);
        locationTextView = (TextView) getActivity().findViewById(R.id.location_text_view);
        locateMeButton = (Button) getActivity().findViewById(R.id.share_location_btn);
        partsList = (LinearLayout) getActivity().findViewById(R.id.parts_list_linear_layout);
        mImageView = (ImageView) getActivity().findViewById(R.id.bike_picture);
        mImageView.setVisibility(View.GONE);
        frameCheckBox  = (CheckBox) getActivity().findViewById(R.id.frame_radio);
        seatCheckBox = (CheckBox) getActivity().findViewById(R.id.seat_radio);
        frontWheelCheckBox = (CheckBox) getActivity().findViewById(R.id.front_wheel_radio);
        decentTireCheckBox = (CheckBox) getActivity().findViewById(R.id.decent_tire_radio);
        forkCheckBox = (CheckBox) getActivity().findViewById(R.id.fork_radio);
        stemCheckBox  = (CheckBox) getActivity().findViewById(R.id.stem_radio);
        handlebarCheckBox = (CheckBox) getActivity().findViewById(R.id.habdlebars_radio);
        brakeLeversCheckBox = (CheckBox) getActivity().findViewById(R.id.brake_levers_radio);
        gearShiftersCheckBox = (CheckBox) getActivity().findViewById(R.id.gear_shifters_radio);
        frontBrakeCheckBox = (CheckBox) getActivity().findViewById(R.id.front_brake_radio);
        pedalsCheckBox = (CheckBox) getActivity().findViewById(R.id.pedals_radio);
        crankArmsCheckBox = (CheckBox) getActivity().findViewById(R.id.crank_arms_radio);
        frontDerailleurCheckBox = (CheckBox) getActivity().findViewById(R.id.front_derailleur_radio);
        chainCheckBox = (CheckBox) getActivity().findViewById(R.id.chain_radio);
        rearBreakCheckBox = (CheckBox) getActivity().findViewById(R.id.rear_break_radio);
        rearWheelCheckBox = (CheckBox) getActivity().findViewById(R.id.rear_wheel_radio);
        rearDerailleurCheckBox = (CheckBox) getActivity().findViewById(R.id.rear_deraolleur_radio);
        derailleurOrBrakeCableCheckBox = (CheckBox) getActivity().findViewById(R.id.cable_radio);

        //save bike fab
        FloatingActionButton fab_save =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
        fab_save.setImageResource(R.drawable.ic_done);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),
                        "Saving (but not really) for now", Toast.LENGTH_LONG).show();


                mPresenter.saveBike(
                        //TODO: implement getting image
                        BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.logo),
                        //get bike type from the radio group of bike types
                        getBikeType(typeRadioGroup.getCheckedRadioButtonId()),
                        //TODO: implement getting location
                        "50 Milk street, Boston, MA, USA",
                        getPartsAvailable(),
                        isBikeComplete());
            }
        });

        //take a picture fab
        FloatingActionButton fabTakePicture = (FloatingActionButton)
                getActivity().findViewById(R.id.fab_take_picture);
        fabTakePicture.setImageResource(R.drawable.ic_camera);
        fabTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        //if bike is complete bike hide parts selection UI
        completeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case (R.id.complete_bike_yes_radio):
                        partsList.setVisibility(View.GONE);
                        return;
                    case (R.id.complete_bike_no_radio):
                        partsList.setVisibility(View.VISIBLE);
                        return;
                    default:
                        Log.i(TAG, "onCheckedChanged: something is going really bad");
                }
            }
        });
    }
    //TODO: implement getting bike type
    private String getBikeType(int id) {
        switch (id) {
            case(R.id.road_bike_radio):
            case(R.id.mountain_bike_radio):
            case(R.id.hybrid_bike_radio):
            case(R.id.cruiser_bike_radio):
            case(R.id.bmx_bike_radio):
        }
        return "Road";
    }

    private boolean isBikeComplete() {
        switch (completeRadioGroup.getCheckedRadioButtonId()) {
            case(R.id.complete_bike_yes_radio):
                return true;
            case(R.id.complete_bike_no_radio):
                return false;
            default:
                return false;
        }
    }

    //TODO: get values from the bunch of checkboxes
    private boolean[] getPartsAvailable() {
        boolean[] res = new boolean[17];
        return res;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_bike_frag, container, false);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void showNoBikeFoundError() {
        Toast.makeText(getContext(),R.string.empty_bike_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showBikesList() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setLocation(String location) {

    }


    @Override
    public void setPicture(Bitmap image) {

    }

    @Override
    public boolean isComplete() {
        return false;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
    }
}
