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

import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesDataSource;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link BikesPresenter}
 */
public class BikesPresenterTest {

    private static List<Bike> BIKES;

    @Mock
    private BikesRepository mBikesRepository;

    @Mock
    private BikesContract.View mTasksView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<BikesDataSource.LoadBikesCallback> mLoadTasksCallbackCaptor;

    private BikesPresenter mBikesPresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mBikesPresenter = new BikesPresenter(mBikesRepository, mTasksView);

        // The presenter won't update the view unless it's active.
        when(mTasksView.isActive()).thenReturn(true);

        // We start the tasks to 3, with one active and two completed
//        BIKES = Lists.newArrayList(new Bike("Title1", "Description1"),
//                new Bike("Title2", "Description2", true), new Bike("Title3", "Description3", true));
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        mBikesPresenter = new BikesPresenter(mBikesRepository, mTasksView);

        // Then the presenter is set to the view
        verify(mTasksView).setPresenter(mBikesPresenter);
    }

    @Test
    public void loadAllTasksFromRepositoryAndLoadIntoView() {
        // Given an initialized BikesPresenter with initialized tasks
        // When loading of Tasks is requested
        mBikesPresenter.setFiltering(BikesFilterType.ALL_BIKES);
        mBikesPresenter.loadTasks(true);

        // Callback is captured and invoked with stubbed tasks
        verify(mBikesRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onBikesLoaded(BIKES);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(mTasksView);
        inOrder.verify(mTasksView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all tasks are shown in UI
        inOrder.verify(mTasksView).setLoadingIndicator(false);
        ArgumentCaptor<List> showTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mTasksView).showBikes(showTasksArgumentCaptor.capture());
        assertTrue(showTasksArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void loadActiveTasksFromRepositoryAndLoadIntoView() {
        // Given an initialized BikesPresenter with initialized tasks
        // When loading of Tasks is requested
        mBikesPresenter.setFiltering(BikesFilterType.ACTIVE_TASKS);
        mBikesPresenter.loadTasks(true);

        // Callback is captured and invoked with stubbed tasks
        verify(mBikesRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onBikesLoaded(BIKES);

        // Then progress indicator is hidden and active tasks are shown in UI
        verify(mTasksView).setLoadingIndicator(false);
        ArgumentCaptor<List> showTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mTasksView).showBikes(showTasksArgumentCaptor.capture());
        assertTrue(showTasksArgumentCaptor.getValue().size() == 1);
    }

    @Test
    public void loadCompletedTasksFromRepositoryAndLoadIntoView() {
        // Given an initialized BikesPresenter with initialized tasks
        // When loading of Tasks is requested
        mBikesPresenter.setFiltering(BikesFilterType.COMPLETED_TASKS);
        mBikesPresenter.loadTasks(true);

        // Callback is captured and invoked with stubbed tasks
        verify(mBikesRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onBikesLoaded(BIKES);

        // Then progress indicator is hidden and completed tasks are shown in UI
        verify(mTasksView).setLoadingIndicator(false);
        ArgumentCaptor<List> showTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mTasksView).showBikes(showTasksArgumentCaptor.capture());
        assertTrue(showTasksArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void clickOnFab_ShowsAddTaskUi() {
        // When adding a new task
        mBikesPresenter.addNewTask();

        // Then add task UI is shown
        verify(mTasksView).showAddTask();
    }

    @Test
    public void clickOnTask_ShowsDetailUi() {
        // Given a stubbed active task
//        Bike requestedBike = new Bike("Details Requested", "For this task");

        // When open task details is requested
//        mBikesPresenter.openBikeDetails(requestedBike);

        // Then task detail UI is shown
//        verify(mTasksView).showBikeDetailsUi(any(String.class));
    }

    @Test
    public void completeTask_ShowsTaskMarkedComplete() {
        // Given a stubbed bike
//        Bike bike = new Bike("Details Requested", "For this bike");

        // When bike is marked as complete
//        mBikesPresenter.completeTask(bike);

        // Then repository is called and bike marked complete UI is shown
//        verify(mBikesRepository).completeTask(bike);
//        verify(mTasksView).showTaskMarkedComplete();
    }

    @Test
    public void activateTask_ShowsTaskMarkedActive() {
        // Given a stubbed completed bike
//        Bike bike = new Bike("Details Requested", "For this bike", true);
//        mBikesPresenter.loadTasks(true);

        // When bike is marked as activated
//        mBikesPresenter.activateTask(bike);

        // Then repository is called and bike marked active UI is shown
//        verify(mBikesRepository).activateTask(bike);
//        verify(mTasksView).showTaskMarkedActive();
    }

    @Test
    public void unavailableTasks_ShowsError() {
        // When tasks are loaded
        mBikesPresenter.setFiltering(BikesFilterType.ALL_BIKES);
        mBikesPresenter.loadTasks(true);

        // And the tasks aren't available in the repository
        verify(mBikesRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onDataNotAvailable();

        // Then an error message is shown
        verify(mTasksView).showLoadingTasksError();
    }
}
