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

package com.example.android.architecture.blueprints.todoapp.addeditbike;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesDataSource;

import static android.content.ContentValues.TAG;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link AddEditBikeFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditBikePresenter implements AddEditBikeContract.Presenter,
        BikesDataSource.GetBikeCallback {


    @NonNull
    private final BikesDataSource mBikesRepository;

    @NonNull
    private final AddEditBikeContract.View mAddBikeView;

    @Nullable
    private String mBikeId;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param taskId                 ID of the task to edit or null for a new task
     * @param tasksRepository        a repository of data for tasks
     * @param addTaskView            the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditBikePresenter(@Nullable String taskId, @NonNull BikesDataSource tasksRepository,
                                @NonNull AddEditBikeContract.View addTaskView, boolean shouldLoadDataFromRepo) {
        mBikeId = taskId;
        mBikesRepository = checkNotNull(tasksRepository);
        mAddBikeView = checkNotNull(addTaskView);
        mIsDataMissing = shouldLoadDataFromRepo;
        mAddBikeView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewTask() && mIsDataMissing) {
            populateBike();
        }
    }

    @Override
    public void saveBike(
            Bitmap image,
            String type,
            String location,
            boolean[] parts,
            boolean complete) {
        //TODO: fix not use internal
        mBikesRepository.addBike(new Bike(image, type, parts, location, complete));
        mAddBikeView.showBikesList();


//        if (isNewTask()) {
//            createTask(title, description);
//        } else {
//            updateTask(title, description);
//        }
    }

    @Override
    public void populateBike() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        mBikesRepository.getTask(mBikeId, this);
    }

    @Override
    public void onTaskLoaded(Bike bike) {
        //TODO: fix
        // The view may not be able to handle UI updates anymore
//        if (mAddBikeView.isActive()) {
//            mAddBikeView.setTitle(bike.getTitle());
//            mAddBikeView.setDescription(bike.getDescription());
//        }
//        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddBikeView.isComplete()) {
            mAddBikeView.showNoBikeFoundError();
        }
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private boolean isNewTask() {
        return mBikeId == null;
    }

    //TODO: fix
    private void createBike(String title, String description) {
//        Bike newBike = new Bike(title, description);
//        if (newBike.isEmpty()) {
//            mAddBikeView.showNoBikeFoundError();
//        } else {
//            mBikesRepository.saveTask(newBike);
//            mAddBikeView.showBikesList();
//        }
    }

    //TODO: fix
    private void updateBike(String title, String description) {
//        if (isNewTask()) {
//            throw new RuntimeException("updateTask() was called but task is new.");
//        }
//        mBikesRepository.saveTask(new Bike(title, description, mBikeId));
//        mAddBikeView.showBikesList(); // After an edit, go back to the list.
    }
}
