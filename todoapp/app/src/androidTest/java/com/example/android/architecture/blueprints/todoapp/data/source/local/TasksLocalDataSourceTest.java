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

package com.example.android.architecture.blueprints.todoapp.data.source.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Integration test for the {@link TasksDataSource}, which uses the {@link TasksDbHelper}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TasksLocalDataSourceTest {

    private final static String TITLE = "title";

    private final static String TITLE2 = "title2";

    private final static String TITLE3 = "title3";

    private TasksLocalDataSource mLocalDataSource;

    @Before
    public void setup() {
        mLocalDataSource = TasksLocalDataSource.getInstance(
                InstrumentationRegistry.getTargetContext());
    }

    @After
    public void cleanUp() {
        mLocalDataSource.deleteAllTasks();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void saveTask_retrievesTask() {
        // Given a new task
        final Bike newBike = new Bike(TITLE, "");

        // When saved into the persistent repository
        mLocalDataSource.saveTask(newBike);

        // Then the task can be retrieved from the persistent repository
        mLocalDataSource.getTask(newBike.getId(), new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Bike bike) {
                assertThat(bike, is(newBike));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void completeTask_retrievedTaskIsComplete() {
        // Initialize mock for the callback.
        TasksDataSource.GetTaskCallback callback = mock(TasksDataSource.GetTaskCallback.class);
        // Given a new task in the persistent repository
        final Bike newBike = new Bike(TITLE, "");
        mLocalDataSource.saveTask(newBike);

        // When completed in the persistent repository
        mLocalDataSource.completeTask(newBike);

        // Then the task can be retrieved from the persistent repository and is complete
        mLocalDataSource.getTask(newBike.getId(), new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Bike bike) {
                assertThat(bike, is(newBike));
                assertThat(bike.isCompleted(), is(true));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void activateTask_retrievedTaskIsActive() {
        // Initialize mock for the callback.
        TasksDataSource.GetTaskCallback callback = mock(TasksDataSource.GetTaskCallback.class);

        // Given a new completed task in the persistent repository
        final Bike newBike = new Bike(TITLE, "");
        mLocalDataSource.saveTask(newBike);
        mLocalDataSource.completeTask(newBike);

        // When activated in the persistent repository
        mLocalDataSource.activateTask(newBike);

        // Then the task can be retrieved from the persistent repository and is active
        mLocalDataSource.getTask(newBike.getId(), callback);

        verify(callback, never()).onDataNotAvailable();
        verify(callback).onTaskLoaded(newBike);

        assertThat(newBike.isCompleted(), is(false));
    }

    @Test
    public void clearCompletedTask_taskNotRetrievable() {
        // Initialize mocks for the callbacks.
        TasksDataSource.GetTaskCallback callback1 = mock(TasksDataSource.GetTaskCallback.class);
        TasksDataSource.GetTaskCallback callback2 = mock(TasksDataSource.GetTaskCallback.class);
        TasksDataSource.GetTaskCallback callback3 = mock(TasksDataSource.GetTaskCallback.class);

        // Given 2 new completed tasks and 1 active task in the persistent repository
        final Bike newBike1 = new Bike(TITLE, "");
        mLocalDataSource.saveTask(newBike1);
        mLocalDataSource.completeTask(newBike1);
        final Bike newBike2 = new Bike(TITLE2, "");
        mLocalDataSource.saveTask(newBike2);
        mLocalDataSource.completeTask(newBike2);
        final Bike newBike3 = new Bike(TITLE3, "");
        mLocalDataSource.saveTask(newBike3);

        // When completed tasks are cleared in the repository
        mLocalDataSource.clearCompletedTasks();

        // Then the completed tasks cannot be retrieved and the active one can
        mLocalDataSource.getTask(newBike1.getId(), callback1);

        verify(callback1).onDataNotAvailable();
        verify(callback1, never()).onTaskLoaded(newBike1);

        mLocalDataSource.getTask(newBike2.getId(), callback2);

        verify(callback2).onDataNotAvailable();
        verify(callback2, never()).onTaskLoaded(newBike1);

        mLocalDataSource.getTask(newBike3.getId(), callback3);

        verify(callback3, never()).onDataNotAvailable();
        verify(callback3).onTaskLoaded(newBike3);
    }

    @Test
    public void deleteAllTasks_emptyListOfRetrievedTask() {
        // Given a new task in the persistent repository and a mocked callback
        Bike newBike = new Bike(TITLE, "");
        mLocalDataSource.saveTask(newBike);
        TasksDataSource.LoadTasksCallback callback = mock(TasksDataSource.LoadTasksCallback.class);

        // When all tasks are deleted
        mLocalDataSource.deleteAllTasks();

        // Then the retrieved tasks is an empty list
        mLocalDataSource.getTasks(callback);

        verify(callback).onDataNotAvailable();
        verify(callback, never()).onTasksLoaded(anyList());
    }

    @Test
    public void getTasks_retrieveSavedTasks() {
        // Given 2 new tasks in the persistent repository
        final Bike newBike1 = new Bike(TITLE, "");
        mLocalDataSource.saveTask(newBike1);
        final Bike newBike2 = new Bike(TITLE, "");
        mLocalDataSource.saveTask(newBike2);

        // Then the tasks can be retrieved from the persistent repository
        mLocalDataSource.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Bike> bikes) {
                assertNotNull(bikes);
                assertTrue(bikes.size() >= 2);

                boolean newTask1IdFound = false;
                boolean newTask2IdFound = false;
                for (Bike bike : bikes) {
                    if (bike.getId().equals(newBike1.getId())) {
                        newTask1IdFound = true;
                    }
                    if (bike.getId().equals(newBike2.getId())) {
                        newTask2IdFound = true;
                    }
                }
                assertTrue(newTask1IdFound);
                assertTrue(newTask2IdFound);
            }

            @Override
            public void onDataNotAvailable() {
                fail();
            }
        });
    }
}
