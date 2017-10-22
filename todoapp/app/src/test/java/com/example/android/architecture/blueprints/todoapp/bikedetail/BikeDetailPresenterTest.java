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

package com.example.android.architecture.blueprints.todoapp.bikedetail;

import com.example.android.architecture.blueprints.todoapp.data.source.BikesDataSource;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link BikeDetailPresenter}
 */
public class BikeDetailPresenterTest {

    public static final String TITLE_TEST = "title";

    public static final String DESCRIPTION_TEST = "description";

    public static final String INVALID_TASK_ID = "";

//    public static final Bike ACTIVE_BIKE = new Bike(TITLE_TEST, DESCRIPTION_TEST);

//    public static final Bike COMPLETED_BIKE = new Bike(TITLE_TEST, DESCRIPTION_TEST, true);

    @Mock
    private BikesRepository mBikesRepository;

    @Mock
    private BikeDetailContract.View mTaskDetailView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<BikesDataSource.GetBikeCallback> mGetTaskCallbackCaptor;

    private BikeDetailPresenter mBikeDetailPresenter;

    @Before
    public void setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter won't update the view unless it's active.
        when(mTaskDetailView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
//        mBikeDetailPresenter = new BikeDetailPresenter(
//                ACTIVE_BIKE.getId(), mBikesRepository, mTaskDetailView);
//
//        // Then the presenter is set to the view
//        verify(mTaskDetailView).setPresenter(mBikeDetailPresenter);
    }

    @Test
    public void getActiveTaskFromRepositoryAndLoadIntoView() {
        // When tasks presenter is asked to open a task
//        mBikeDetailPresenter = new BikeDetailPresenter(
//                ACTIVE_BIKE.getId(), mBikesRepository, mTaskDetailView);
//        mBikeDetailPresenter.start();
//
//        // Then task is loaded from model, callback is captured and progress indicator is shown
//        verify(mBikesRepository).getTask(eq(ACTIVE_BIKE.getId()), mGetTaskCallbackCaptor.capture());
//        InOrder inOrder = inOrder(mTaskDetailView);
//        inOrder.verify(mTaskDetailView).setLoadingIndicator(true);
//
//        // When task is finally loaded
//        mGetTaskCallbackCaptor.getValue().onTaskLoaded(ACTIVE_BIKE); // Trigger callback
//
//        // Then progress indicator is hidden and title, description and completion status are shown
//        // in UI
//        inOrder.verify(mTaskDetailView).setLoadingIndicator(false);
//        verify(mTaskDetailView).showTitle(TITLE_TEST);
//        verify(mTaskDetailView).showDescription(DESCRIPTION_TEST);
//        verify(mTaskDetailView).showCompletionStatus(false);
    }

    @Test
    public void getCompletedTaskFromRepositoryAndLoadIntoView() {
//        mBikeDetailPresenter = new BikeDetailPresenter(
//                COMPLETED_BIKE.getId(), mBikesRepository, mTaskDetailView);
//        mBikeDetailPresenter.start();
//
//        // Then task is loaded from model, callback is captured and progress indicator is shown
//        verify(mBikesRepository).getTask(
//                eq(COMPLETED_BIKE.getId()), mGetTaskCallbackCaptor.capture());
//        InOrder inOrder = inOrder(mTaskDetailView);
//        inOrder.verify(mTaskDetailView).setLoadingIndicator(true);
//
//        // When task is finally loaded
//        mGetTaskCallbackCaptor.getValue().onTaskLoaded(COMPLETED_BIKE); // Trigger callback
//
//        // Then progress indicator is hidden and title, description and completion status are shown
//        // in UI
//        inOrder.verify(mTaskDetailView).setLoadingIndicator(false);
//        verify(mTaskDetailView).showTitle(TITLE_TEST);
//        verify(mTaskDetailView).showDescription(DESCRIPTION_TEST);
//        verify(mTaskDetailView).showCompletionStatus(true);
    }

    @Test
    public void getUnknownTaskFromRepositoryAndLoadIntoView() {
        // When loading of a task is requested with an invalid task ID.
        mBikeDetailPresenter = new BikeDetailPresenter(
                INVALID_TASK_ID, mBikesRepository, mTaskDetailView);
        mBikeDetailPresenter.start();
        verify(mTaskDetailView).showMissingTask();
    }

    @Test
    public void deleteTask() {
        // Given an initialized BikeDetailPresenter with stubbed bike
//        Bike bike = new Bike(TITLE_TEST, DESCRIPTION_TEST);
//
//        // When the deletion of a bike is requested
//        mBikeDetailPresenter = new BikeDetailPresenter(
//                bike.getId(), mBikesRepository, mTaskDetailView);
//        mBikeDetailPresenter.deleteTask();
//
//        // Then the repository and the view are notified
//        verify(mBikesRepository).deleteTask(bike.getId());
//        verify(mTaskDetailView).showTaskDeleted();
    }

    @Test
    public void completeTask() {
        // Given an initialized presenter with an active bike
//        Bike bike = new Bike(TITLE_TEST, DESCRIPTION_TEST);
//        mBikeDetailPresenter = new BikeDetailPresenter(
//                bike.getId(), mBikesRepository, mTaskDetailView);
//        mBikeDetailPresenter.start();
//
//        // When the presenter is asked to complete the bike
//        mBikeDetailPresenter.completeTask();
//
//        // Then a request is sent to the bike repository and the UI is updated
//        verify(mBikesRepository).completeTask(bike.getId());
//        verify(mTaskDetailView).showTaskMarkedComplete();
    }

    @Test
    public void activateTask() {
        // Given an initialized presenter with a completed bike
//        Bike bike = new Bike(TITLE_TEST, DESCRIPTION_TEST, true);
//        mBikeDetailPresenter = new BikeDetailPresenter(
//                bike.getId(), mBikesRepository, mTaskDetailView);
//        mBikeDetailPresenter.start();
//
//        // When the presenter is asked to activate the bike
//        mBikeDetailPresenter.activateTask();
//
//        // Then a request is sent to the bike repository and the UI is updated
//        verify(mBikesRepository).activateTask(bike.getId());
//        verify(mTaskDetailView).showTaskMarkedActive();
    }

    @Test
    public void activeTaskIsShownWhenEditing() {
        // When the edit of an ACTIVE_BIKE is requested
//        mBikeDetailPresenter = new BikeDetailPresenter(
//                ACTIVE_BIKE.getId(), mBikesRepository, mTaskDetailView);
//        mBikeDetailPresenter.editTask();
//
//        // Then the view is notified
//        verify(mTaskDetailView).showEditTask(ACTIVE_BIKE.getId());
    }

    @Test
    public void invalidTaskIsNotShownWhenEditing() {
        // When the edit of an invalid task id is requested
        mBikeDetailPresenter = new BikeDetailPresenter(
                INVALID_TASK_ID, mBikesRepository, mTaskDetailView);
        mBikeDetailPresenter.editTask();

        // Then the edit mode is never started
        verify(mTaskDetailView, never()).showEditTask(INVALID_TASK_ID);
        // instead, the error is shown.
        verify(mTaskDetailView).showMissingTask();
    }

}
