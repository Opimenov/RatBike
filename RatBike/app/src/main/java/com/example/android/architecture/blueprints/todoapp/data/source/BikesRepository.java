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

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.architecture.blueprints.todoapp.data.Bike;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class BikesRepository implements BikesDataSource {

    ArrayList<Bike> listOfAvailableBikes= new ArrayList<>();

    private static BikesRepository INSTANCE = null;

    private final BikesDataSource mTasksRemoteDataSource;

    private final BikesDataSource mTasksLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Bike> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private BikesRepository(@NonNull BikesDataSource tasksRemoteDataSource,
                            @NonNull BikesDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
        mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksRemoteDataSource the backend data source
     * @param tasksLocalDataSource  the device storage data source
     * @return the {@link BikesRepository} instance
     */
    public static BikesRepository getInstance(BikesDataSource tasksRemoteDataSource,
                                              BikesDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BikesRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(BikesDataSource, BikesDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void addBike(Bike bike) {
        listOfAvailableBikes.add(bike);
    }

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadBikesCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getTasks(@NonNull final LoadBikesCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onBikesLoaded(new ArrayList<>(mCachedTasks.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mTasksLocalDataSource.getTasks(new LoadBikesCallback() {
                @Override
                public void onBikesLoaded(List<Bike> bikes) {
                    refreshCache(bikes);
                    callback.onBikesLoaded(new ArrayList<>(mCachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveTask(@NonNull Bike bike) {
        checkNotNull(bike);
        mTasksRemoteDataSource.saveTask(bike);
        mTasksLocalDataSource.saveTask(bike);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(bike.getId(), bike);
    }

    @Override
    public void completeTask(@NonNull Bike bike) {
        checkNotNull(bike);
        mTasksRemoteDataSource.completeTask(bike);
        mTasksLocalDataSource.completeTask(bike);
        //TODO: fix this
//        Bike completedBike = new Bike(bike.getTitle(), bike.getDescription(), bike.getId(), true);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        //TODO:  fix this
//        mCachedTasks.put(bike.getId(), completedBike);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Bike bike) {
        checkNotNull(bike);
        mTasksRemoteDataSource.activateTask(bike);
        mTasksLocalDataSource.activateTask(bike);
        //TODO: fix
//        Bike activeBike = new Bike(bike.getTitle(), bike.getDescription(), bike.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        //TODO: fix
//        mCachedTasks.put(bike.getId(), activeBike);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks();
        mTasksLocalDataSource.clearCompletedTasks();

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Bike>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Bike> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetBikeCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetBikeCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Bike cachedBike = getTaskWithId(taskId);

        // Respond immediately with cache if available
        if (cachedBike != null) {
            callback.onTaskLoaded(cachedBike);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mTasksLocalDataSource.getTask(taskId, new GetBikeCallback() {
            @Override
            public void onTaskLoaded(Bike bike) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedTasks == null) {
                    mCachedTasks = new LinkedHashMap<>();
                }
                mCachedTasks.put(bike.getId(), bike);
                callback.onTaskLoaded(bike);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksRemoteDataSource.getTask(taskId, new GetBikeCallback() {
                    @Override
                    public void onTaskLoaded(Bike bike) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedTasks == null) {
                            mCachedTasks = new LinkedHashMap<>();
                        }
                        mCachedTasks.put(bike.getId(), bike);
                        callback.onTaskLoaded(bike);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshBikes() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        mTasksRemoteDataSource.deleteAllTasks();
        mTasksLocalDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksRemoteDataSource.deleteTask(checkNotNull(taskId));
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));

        mCachedTasks.remove(taskId);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadBikesCallback callback) {
        mTasksRemoteDataSource.getTasks(new LoadBikesCallback() {
            @Override
            public void onBikesLoaded(List<Bike> bikes) {
                refreshCache(bikes);
                refreshLocalDataSource(bikes);
                callback.onBikesLoaded(new ArrayList<>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Bike> bikes) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Bike bike : bikes) {
            mCachedTasks.put(bike.getId(), bike);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Bike> bikes) {
        mTasksLocalDataSource.deleteAllTasks();
        for (Bike bike : bikes) {
            mTasksLocalDataSource.saveTask(bike);
        }
    }

    @Nullable
    private Bike getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }
}
