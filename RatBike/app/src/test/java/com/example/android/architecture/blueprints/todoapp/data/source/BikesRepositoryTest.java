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

package com.example.android.architecture.blueprints.todoapp.data.source;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import android.content.Context;

import com.example.android.architecture.blueprints.todoapp.data.Bike;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class BikesRepositoryTest {

    private final static String TASK_TITLE = "title";

    private final static String TASK_TITLE2 = "title2";

    private final static String TASK_TITLE3 = "title3";

//    private static List<Bike> BIKES = Lists.newArrayList(new Bike("Title1", "Description1"),
//            new Bike("Title2", "Description2"));

    private BikesRepository mBikesRepository;

    @Mock
    private BikesDataSource mTasksRemoteDataSource;

    @Mock
    private BikesDataSource mTasksLocalDataSource;

    @Mock
    private Context mContext;

    @Mock
    private BikesDataSource.GetBikeCallback mGetBikeCallback;

    @Mock
    private BikesDataSource.LoadBikesCallback mLoadBikesCallback;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<BikesDataSource.LoadBikesCallback> mTasksCallbackCaptor;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<BikesDataSource.GetBikeCallback> mTaskCallbackCaptor;

    @Before
    public void setupTasksRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mBikesRepository = BikesRepository.getInstance(
                mTasksRemoteDataSource, mTasksLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        BikesRepository.destroyInstance();
    }

    @Test
    public void getTasks_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the tasks repository
        twoTasksLoadCallsToRepository(mLoadBikesCallback);

        // Then tasks were only requested once from Service API
        verify(mTasksRemoteDataSource).getTasks(any(BikesDataSource.LoadBikesCallback.class));
    }

    @Test
    public void getTasks_requestsAllTasksFromLocalDataSource() {
        // When tasks are requested from the tasks repository
        mBikesRepository.getTasks(mLoadBikesCallback);

        // Then tasks are loaded from the local data source
        verify(mTasksLocalDataSource).getTasks(any(BikesDataSource.LoadBikesCallback.class));
    }

    @Test
    public void saveTask_savesTaskToServiceAPI() {
        // Given a stub task with title and description
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description");

        // When a task is saved to the tasks repository
//        mBikesRepository.saveTask(newBike);

        // Then the service API and persistent repository are called and the cache is updated
//        verify(mTasksRemoteDataSource).saveTask(newBike);
//        verify(mTasksLocalDataSource).saveTask(newBike);
//        assertThat(mBikesRepository.mCachedTasks.size(), is(1));
    }

    @Test
    public void completeTask_completesTaskToServiceAPIUpdatesCache() {
        // Given a stub active task with title and description added in the repository
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description");
//        mBikesRepository.saveTask(newBike);
//
//        // When a task is completed to the tasks repository
//        mBikesRepository.completeTask(newBike);
//
//        // Then the service API and persistent repository are called and the cache is updated
//        verify(mTasksRemoteDataSource).completeTask(newBike);
//        verify(mTasksLocalDataSource).completeTask(newBike);
//        assertThat(mBikesRepository.mCachedTasks.size(), is(1));
//        assertThat(mBikesRepository.mCachedTasks.get(newBike.getId()).isActive(), is(false));
    }

    @Test
    public void completeTaskId_completesTaskToServiceAPIUpdatesCache() {
        // Given a stub active task with title and description added in the repository
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description");
//        mBikesRepository.saveTask(newBike);
//
//        // When a task is completed using its id to the tasks repository
//        mBikesRepository.completeTask(newBike.getId());
//
//        // Then the service API and persistent repository are called and the cache is updated
//        verify(mTasksRemoteDataSource).completeTask(newBike);
//        verify(mTasksLocalDataSource).completeTask(newBike);
//        assertThat(mBikesRepository.mCachedTasks.size(), is(1));
//        assertThat(mBikesRepository.mCachedTasks.get(newBike.getId()).isActive(), is(false));
    }

    @Test
    public void activateTask_activatesTaskToServiceAPIUpdatesCache() {
        // Given a stub completed task with title and description in the repository
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description", true);
//        mBikesRepository.saveTask(newBike);
//
//        // When a completed task is activated to the tasks repository
//        mBikesRepository.activateTask(newBike);
//
//        // Then the service API and persistent repository are called and the cache is updated
//        verify(mTasksRemoteDataSource).activateTask(newBike);
//        verify(mTasksLocalDataSource).activateTask(newBike);
//        assertThat(mBikesRepository.mCachedTasks.size(), is(1));
//        assertThat(mBikesRepository.mCachedTasks.get(newBike.getId()).isActive(), is(true));
    }

    @Test
    public void activateTaskId_activatesTaskToServiceAPIUpdatesCache() {
        // Given a stub completed task with title and description in the repository
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description", true);
//        mBikesRepository.saveTask(newBike);
//
//        // When a completed task is activated with its id to the tasks repository
//        mBikesRepository.activateTask(newBike.getId());
//
//        // Then the service API and persistent repository are called and the cache is updated
//        verify(mTasksRemoteDataSource).activateTask(newBike);
//        verify(mTasksLocalDataSource).activateTask(newBike);
//        assertThat(mBikesRepository.mCachedTasks.size(), is(1));
//        assertThat(mBikesRepository.mCachedTasks.get(newBike.getId()).isActive(), is(true));
    }

    @Test
    public void getTask_requestsSingleTaskFromLocalDataSource() {
        // When a task is requested from the tasks repository
        mBikesRepository.getTask(TASK_TITLE, mGetBikeCallback);

        // Then the task is loaded from the database
        verify(mTasksLocalDataSource).getTask(eq(TASK_TITLE), any(
                BikesDataSource.GetBikeCallback.class));
    }

    @Test
    public void deleteCompletedTasks_deleteCompletedTasksToServiceAPIUpdatesCache() {
        // Given 2 stub completed tasks and 1 stub active tasks in the repository
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description", true);
//        mBikesRepository.saveTask(newBike);
//        Bike newBike2 = new Bike(TASK_TITLE2, "Some Bike Description");
//        mBikesRepository.saveTask(newBike2);
//        Bike newBike3 = new Bike(TASK_TITLE3, "Some Bike Description", true);
//        mBikesRepository.saveTask(newBike3);
//
//        // When a completed tasks are cleared to the tasks repository
//        mBikesRepository.clearCompletedTasks();
//
//
//        // Then the service API and persistent repository are called and the cache is updated
//        verify(mTasksRemoteDataSource).clearCompletedTasks();
//        verify(mTasksLocalDataSource).clearCompletedTasks();
//
//        assertThat(mBikesRepository.mCachedTasks.size(), is(1));
//        assertTrue(mBikesRepository.mCachedTasks.get(newBike2.getId()).isActive());
//        assertThat(mBikesRepository.mCachedTasks.get(newBike2.getId()).getTitle(), is(TASK_TITLE2));
    }

    @Test
    public void deleteAllTasks_deleteTasksToServiceAPIUpdatesCache() {
        // Given 2 stub completed tasks and 1 stub active tasks in the repository
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description", true);
//        mBikesRepository.saveTask(newBike);
//        Bike newBike2 = new Bike(TASK_TITLE2, "Some Bike Description");
//        mBikesRepository.saveTask(newBike2);
//        Bike newBike3 = new Bike(TASK_TITLE3, "Some Bike Description", true);
//        mBikesRepository.saveTask(newBike3);
//
//        // When all tasks are deleted to the tasks repository
//        mBikesRepository.deleteAllTasks();
//
//        // Verify the data sources were called
//        verify(mTasksRemoteDataSource).deleteAllTasks();
//        verify(mTasksLocalDataSource).deleteAllTasks();
//
//        assertThat(mBikesRepository.mCachedTasks.size(), is(0));
    }

    @Test
    public void deleteTask_deleteTaskToServiceAPIRemovedFromCache() {
        // Given a task in the repository
//        Bike newBike = new Bike(TASK_TITLE, "Some Bike Description", true);
//        mBikesRepository.saveTask(newBike);
//        assertThat(mBikesRepository.mCachedTasks.containsKey(newBike.getId()), is(true));
//
//        // When deleted
//        mBikesRepository.deleteTask(newBike.getId());
//
//        // Verify the data sources were called
//        verify(mTasksRemoteDataSource).deleteTask(newBike.getId());
//        verify(mTasksLocalDataSource).deleteTask(newBike.getId());
//
//        // Verify it's removed from repository
//        assertThat(mBikesRepository.mCachedTasks.containsKey(newBike.getId()), is(false));
    }

    @Test
    public void getTasksWithDirtyCache_tasksAreRetrievedFromRemote() {
        // When calling getTasks in the repository with dirty cache
//        mBikesRepository.refreshBikes();
//        mBikesRepository.getTasks(mLoadBikesCallback);
//
//        // And the remote data source has data available
//        setTasksAvailable(mTasksRemoteDataSource, BIKES);
//
//        // Verify the tasks from the remote data source are returned, not the local
//        verify(mTasksLocalDataSource, never()).getTasks(mLoadBikesCallback);
//        verify(mLoadBikesCallback).onBikesLoaded(BIKES);
    }

    @Test
    public void getTasksWithLocalDataSourceUnavailable_tasksAreRetrievedFromRemote() {
        // When calling getTasks in the repository
//        mBikesRepository.getTasks(mLoadBikesCallback);
//
//        // And the local data source has no data available
//        setTasksNotAvailable(mTasksLocalDataSource);
//
//        // And the remote data source has data available
//        setTasksAvailable(mTasksRemoteDataSource, BIKES);
//
//        // Verify the tasks from the local data source are returned
//        verify(mLoadBikesCallback).onBikesLoaded(BIKES);
    }

    @Test
    public void getTasksWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // When calling getTasks in the repository
        mBikesRepository.getTasks(mLoadBikesCallback);

        // And the local data source has no data available
        setTasksNotAvailable(mTasksLocalDataSource);

        // And the remote data source has no data available
        setTasksNotAvailable(mTasksRemoteDataSource);

        // Verify no data is returned
        verify(mLoadBikesCallback).onDataNotAvailable();
    }

    @Test
    public void getTaskWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // Given a task id
        final String taskId = "123";

        // When calling getTask in the repository
        mBikesRepository.getTask(taskId, mGetBikeCallback);

        // And the local data source has no data available
        setTaskNotAvailable(mTasksLocalDataSource, taskId);

        // And the remote data source has no data available
        setTaskNotAvailable(mTasksRemoteDataSource, taskId);

        // Verify no data is returned
        verify(mGetBikeCallback).onDataNotAvailable();
    }

    @Test
    public void getTasks_refreshesLocalDataSource() {
        // Mark cache as dirty to force a reload of data from remote data source.
//        mBikesRepository.refreshBikes();
//
//        // When calling getTasks in the repository
//        mBikesRepository.getTasks(mLoadBikesCallback);
//
//        // Make the remote data source return data
//        setTasksAvailable(mTasksRemoteDataSource, BIKES);
//
//        // Verify that the data fetched from the remote data source was saved in local.
//        verify(mTasksLocalDataSource, times(BIKES.size())).saveTask(any(Bike.class));
    }

    /**
     * Convenience method that issues two calls to the tasks repository
     */
    private void twoTasksLoadCallsToRepository(BikesDataSource.LoadBikesCallback callback) {
        // When tasks are requested from repository
//        mBikesRepository.getTasks(callback); // First call to API
//
//        // Use the Mockito Captor to capture the callback
//        verify(mTasksLocalDataSource).getTasks(mTasksCallbackCaptor.capture());
//
//        // Local data source doesn't have data yet
//        mTasksCallbackCaptor.getValue().onDataNotAvailable();
//
//
//        // Verify the remote data source is queried
//        verify(mTasksRemoteDataSource).getTasks(mTasksCallbackCaptor.capture());
//
//        // Trigger callback so tasks are cached
//        mTasksCallbackCaptor.getValue().onBikesLoaded(BIKES);
//
//        mBikesRepository.getTasks(callback); // Second call to API
    }

    private void setTasksNotAvailable(BikesDataSource dataSource) {
        verify(dataSource).getTasks(mTasksCallbackCaptor.capture());
        mTasksCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setTasksAvailable(BikesDataSource dataSource, List<Bike> bikes) {
        verify(dataSource).getTasks(mTasksCallbackCaptor.capture());
        mTasksCallbackCaptor.getValue().onBikesLoaded(bikes);
    }

    private void setTaskNotAvailable(BikesDataSource dataSource, String taskId) {
        verify(dataSource).getTask(eq(taskId), mTaskCallbackCaptor.capture());
        mTaskCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setTaskAvailable(BikesDataSource dataSource, Bike bike) {
        verify(dataSource).getTask(eq(bike.getId()), mTaskCallbackCaptor.capture());
        mTaskCallbackCaptor.getValue().onTaskLoaded(bike);
    }
 }
