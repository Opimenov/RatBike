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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.architecture.blueprints.todoapp.R;

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

    public static final String ARGUMENT_EDIT_BIKE_ID = "EDIT_BIKE_ID";

    private AddEditBikeContract.Presenter mPresenter;


    private RadioGroup typeRadioGroup;
    private RadioGroup completeRadioGroup;
    private TextView locationTextView;
    private Button   locateMeButton;

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

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
    //TODO: implements this
    private String getBikeType(int id) {
        switch (id) {

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
        RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //find which radio button is selected
                switch (checkedId) {
                    //TODO: save this in the model
                    case(R.id.road_bike_radio):
                    case(R.id.mountain_bike_radio):
                    case(R.id.hybrid_bike_radio):
                    case(R.id.cruiser_bike_radio):
                    case(R.id.bmx_bike_radio):
                }

            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void showNoBikeFoundError() {
        Toast.makeText(getContext(),R.string.empty_bike_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showBikesList() {
        getActivity().setResult(Activity.RESULT_OK);
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
}
