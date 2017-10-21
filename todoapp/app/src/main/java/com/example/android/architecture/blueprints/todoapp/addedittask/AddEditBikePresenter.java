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

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link AddEditBikeFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditBikePresenter implements AddEditBikeContract.Presenter,
        TasksDataSource.GetTaskCallback {
    //TODO: remove this internal saving and save it in database
    ArrayList<Bike> listOfAvailableBikes= new ArrayList<>();

    @NonNull
    private final TasksDataSource mTasksRepository;

    @NonNull
    private final AddEditBikeContract.View mAddTaskView;

    @Nullable
    private String mTaskId;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param taskId ID of the task to edit or null for a new task
     * @param tasksRepository a repository of data for tasks
     * @param addTaskView the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditBikePresenter(@Nullable String taskId, @NonNull TasksDataSource tasksRepository,
                                @NonNull AddEditBikeContract.View addTaskView, boolean shouldLoadDataFromRepo) {
        mTaskId = taskId;
        mTasksRepository = checkNotNull(tasksRepository);
        mAddTaskView = checkNotNull(addTaskView);
        mIsDataMissing = shouldLoadDataFromRepo;

        mAddTaskView.setPresenter(this);
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
        listOfAvailableBikes.add(new Bike(image,type,parts,location,complete));
        Log.i(TAG, "saveBike: available bikes number"+ listOfAvailableBikes.size());
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
        mTasksRepository.getTask(mTaskId, this);
    }

    @Override
    public void onTaskLoaded(Bike bike) {
        //TODO: fix
        // The view may not be able to handle UI updates anymore
//        if (mAddTaskView.isActive()) {
//            mAddTaskView.setTitle(bike.getTitle());
//            mAddTaskView.setDescription(bike.getDescription());
//        }
//        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddTaskView.isComplete()) {
            mAddTaskView.showNoBikeFoundError();
        }
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private boolean isNewTask() {
        return mTaskId == null;
    }

    //TODO: fix
    private void createBike(String title, String description) {
//        Bike newBike = new Bike(title, description);
//        if (newBike.isEmpty()) {
//            mAddTaskView.showNoBikeFoundError();
//        } else {
//            mTasksRepository.saveTask(newBike);
//            mAddTaskView.showBikesList();
//        }
    }

    //TODO: fix
    private void updateBike(String title, String description) {
//        if (isNewTask()) {
//            throw new RuntimeException("updateTask() was called but task is new.");
//        }
//        mTasksRepository.saveTask(new Bike(title, description, mTaskId));
//        mAddTaskView.showBikesList(); // After an edit, go back to the list.
    }
}
