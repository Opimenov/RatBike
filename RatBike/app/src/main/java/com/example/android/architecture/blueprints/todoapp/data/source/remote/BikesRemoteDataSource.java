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

package com.example.android.architecture.blueprints.todoapp.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.data.source.BikesDataSource;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class BikesRemoteDataSource implements BikesDataSource {

    private static BikesRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Bike> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static BikesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BikesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private BikesRemoteDataSource() {}

    private static void addTask(String title, String description) {
        //TODO: implement this
//        Bike newBike = new Bike(title, description);
//        TASKS_SERVICE_DATA.put(newBike.getId(), newBike);
    }

    @Override
    public void addBike(Bike bike) {
        Log.i(TAG, "addBike: in BikesRemoteDataSource");
    }

    /**
     * Note: {@link LoadBikesCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getTasks(final @NonNull LoadBikesCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onBikesLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    /**
     * Note: {@link GetBikeCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getTask(@NonNull String taskId, final @NonNull GetBikeCallback callback) {
        final Bike bike = TASKS_SERVICE_DATA.get(taskId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(bike);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveTask(@NonNull Bike bike) {
        TASKS_SERVICE_DATA.put(bike.getId(), bike);
    }

    @Override
    public void completeTask(@NonNull Bike bike) {
        //TODO: implement this
//        Bike completedBike = new Bike(bike.getTitle(), bike.getDescription(), bike.getId(), true);
//        TASKS_SERVICE_DATA.put(bike.getId(), completedBike);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void activateTask(@NonNull Bike bike) {
        //TODO: implement this
//        Bike activeBike = new Bike(bike.getTitle(), bike.getDescription(), bike.getId());
//        TASKS_SERVICE_DATA.put(bike.getId(), activeBike);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void clearCompletedTasks() {
        Iterator<Map.Entry<String, Bike>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Bike> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshBikes() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }
}
