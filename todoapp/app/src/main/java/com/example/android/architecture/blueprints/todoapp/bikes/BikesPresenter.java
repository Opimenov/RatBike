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

package com.example.android.architecture.blueprints.todoapp.bikes;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.android.architecture.blueprints.todoapp.addeditbike.AddEditBikeActivity;
import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesDataSource;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesRepository;
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link BikesFragment}), retrieves the data and updates the
 * UI as required.
 */
public class BikesPresenter implements BikesContract.Presenter {

    private final BikesRepository mBikesRepository;

    private final BikesContract.View mBikesView;

    private BikesFilterType mCurrentFiltering = BikesFilterType.ALL_BIKES;

    private boolean mFirstLoad = true;

    public BikesPresenter(
            @NonNull BikesRepository bikesRepository,
            @NonNull BikesContract.View bikesView) {
        mBikesRepository = checkNotNull(bikesRepository, "bikesRepository cannot be null");
        mBikesView = checkNotNull(bikesView, "tasksView cannot be null!");
        mBikesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (AddEditBikeActivity.REQUEST_ADD_BIKE == requestCode && Activity.RESULT_OK == resultCode) {
            mBikesView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link BikesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mBikesView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mBikesRepository.refreshBikes();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mBikesRepository.getTasks(new BikesDataSource.LoadBikesCallback() {
            @Override
            public void onBikesLoaded(List<Bike> bikes) {
                List<Bike> tasksToShow = new ArrayList<Bike>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the bikes based on the requestType
                for (Bike bike : bikes) {
                    switch (mCurrentFiltering) {
                        case ALL_BIKES:
                            tasksToShow.add(bike);
                            break;
                        case ACTIVE_TASKS:
                            if (bike.isActive()) {
                                tasksToShow.add(bike);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (bike.isCompleted()) {
                                tasksToShow.add(bike);
                            }
                            break;
                        default:
                            tasksToShow.add(bike);
                            break;
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (!mBikesView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mBikesView.setLoadingIndicator(false);
                }

                processBikes(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mBikesView.isActive()) {
                    return;
                }
                mBikesView.showLoadingTasksError();
            }
        });
    }

    private void processBikes(List<Bike> bikes) {
        if (bikes.isEmpty()) {
            // Show a message indicating there are no bikes for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of bikes
            mBikesView.showBikes(bikes);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mBikesView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mBikesView.showCompletedFilterLabel();
                break;
            default:
                mBikesView.showAllFilterLabel();
                break;
        }
    }

    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mBikesView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mBikesView.showNoCompletedTasks();
                break;
            default:
                mBikesView.showNoTasks();
                break;
        }
    }

    @Override
    public void addNewTask() {
        mBikesView.showAddTask();
    }

    @Override
    public void openBikeDetails(@NonNull Bike requestedBike) {
        checkNotNull(requestedBike, "requestedBike cannot be null!");
        mBikesView.showBikeDetailsUi(requestedBike.getId());
    }

    @Override
    public void completeTask(@NonNull Bike completedBike) {
        checkNotNull(completedBike, "completedBike cannot be null!");
        mBikesRepository.completeTask(completedBike);
        mBikesView.showTaskMarkedComplete();
        loadTasks(false, false);
    }

    @Override
    public void activateTask(@NonNull Bike activeBike) {
        checkNotNull(activeBike, "activeBike cannot be null!");
        mBikesRepository.activateTask(activeBike);
        mBikesView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTasks() {
        mBikesRepository.clearCompletedTasks();
        mBikesView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be {@link BikesFilterType#ALL_BIKES},
     *                    {@link BikesFilterType#COMPLETED_TASKS}, or
     *                    {@link BikesFilterType#ACTIVE_TASKS}
     */
    @Override
    public void setFiltering(BikesFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public BikesFilterType getFiltering() {
        return mCurrentFiltering;
    }

}
