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

import android.support.annotation.NonNull;

import com.example.android.architecture.blueprints.todoapp.BaseView;
import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.BasePresenter;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface BikesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showBikes(List<Bike> bikes);

        void showAddTask();

        void showBikeDetailsUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openBikeDetails(@NonNull Bike requestedBike);

        void completeTask(@NonNull Bike completedBike);

        void activateTask(@NonNull Bike activeBike);

        void clearCompletedTasks();

        void setFiltering(BikesFilterType requestType);

        BikesFilterType getFiltering();
    }
}
