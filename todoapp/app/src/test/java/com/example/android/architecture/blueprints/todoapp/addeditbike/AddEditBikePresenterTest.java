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

import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesRepository;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link AddEditBikePresenter}.
 */
public class AddEditBikePresenterTest {

    @Mock
    private BikesRepository mBikesRepository;

    @Mock
    private AddEditBikeContract.View mAddEditTaskView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<BikesDataSource.GetBikeCallback> mGetTaskCallbackCaptor;

    private AddEditBikePresenter mAddEditBikePresenter;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(mAddEditTaskView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenter_setsThePresenterToView(){
        // Get a reference to the class under test
        mAddEditBikePresenter = new AddEditBikePresenter(
                null, mBikesRepository, mAddEditTaskView, true);

        // Then the presenter is set to the view
        verify(mAddEditTaskView).setPresenter(mAddEditBikePresenter);
    }

    @Test
    public void saveNewTaskToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test
        mAddEditBikePresenter = new AddEditBikePresenter(
                null, mBikesRepository, mAddEditTaskView, true);

        // When the presenter is asked to save a task
        mAddEditBikePresenter.saveTask("New Bike Title", "Some Bike Description");

        // Then a task is saved in the repository and the view updated
        verify(mBikesRepository).saveTask(any(Bike.class)); // saved to the model
        verify(mAddEditTaskView).showBikesList(); // shown in the UI
    }

    @Test
    public void saveTask_emptyTaskShowsErrorUi() {
        // Get a reference to the class under test
        mAddEditBikePresenter = new AddEditBikePresenter(
                null, mBikesRepository, mAddEditTaskView, true);

        // When the presenter is asked to save an empty task
        mAddEditBikePresenter.saveTask("", "");

        // Then an empty not error is shown in the UI
        verify(mAddEditTaskView).showNoBikeFoundError();
    }

    @Test
    public void saveExistingTaskToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test
        mAddEditBikePresenter = new AddEditBikePresenter(
                "1", mBikesRepository, mAddEditTaskView, true);

        // When the presenter is asked to save an existing task
        mAddEditBikePresenter.saveTask("Existing Bike Title", "Some Bike Description");

        // Then a task is saved in the repository and the view updated
        verify(mBikesRepository).saveTask(any(Bike.class)); // saved to the model
        verify(mAddEditTaskView).showBikesList(); // shown in the UI
    }

    @Test
    public void populateTask_callsRepoAndUpdatesView() {
        Bike testBike = new Bike("TITLE", "DESCRIPTION");
        // Get a reference to the class under test
        mAddEditBikePresenter = new AddEditBikePresenter(testBike.getId(),
                mBikesRepository, mAddEditTaskView, true);

        // When the presenter is asked to populate an existing task
        mAddEditBikePresenter.populateTask();

        // Then the task repository is queried and the view updated
        verify(mBikesRepository).getTask(eq(testBike.getId()), mGetTaskCallbackCaptor.capture());
        assertThat(mAddEditBikePresenter.isDataMissing(), is(true));

        // Simulate callback
        mGetTaskCallbackCaptor.getValue().onTaskLoaded(testBike);

        verify(mAddEditTaskView).setTitle(testBike.getTitle());
        verify(mAddEditTaskView).setDescription(testBike.getDescription());
        assertThat(mAddEditBikePresenter.isDataMissing(), is(false));
    }
}
