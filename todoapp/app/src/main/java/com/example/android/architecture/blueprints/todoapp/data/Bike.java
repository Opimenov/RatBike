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

package com.example.android.architecture.blueprints.todoapp.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Immutable model class for a Bike.
 */
public final class Bike {

    @NonNull
    private final Bitmap mbikeImage;

    //automativally generated
    @NonNull
    private final String mId;

    //getting from the radio buttons
    @Nullable
    private final String mBikeType;

    //getting all true if bike is complete
    //otherwise the value is true is the part is available
    /*
    0  Frame
    1  Seat
    2  Front Wheel
    3  Decent Tire
    4  Fork
    5  Stem
    6  Handlebar
    7  Brake Levers
    8  Gear Shifters
    9  Front Brake
    10 Pedals
    11 Crank Arms
    12 Front Derailleur
    13 Chain
    14 Rear Break
    15 Rear Wheel
    16 Rear Derailleur
    17 Derailleur or Brake Cable
     */
    @Nullable
    private boolean[] mParts;

    @Nullable
    private final String mAddress;

    //if this is true then the bike is a complete bike with all parts available
    private final boolean isComplete;

    /**
     * Use this constructor to create a new Bike that is not complete
     *
     * @param bikeImage image of the bike
     * @param type       type of the bike
     * @param parts      boolean array for available parts
     * @param location description of the task
     */
    public Bike(
            @Nullable Bitmap bikeImage,
            @Nullable String type,
            @Nullable boolean[] parts,
            @Nullable String location) {
        this(bikeImage, type, parts, location, UUID.randomUUID().toString(), false);
    }

    /**
     * Use this constructor to create a complete Bike if the Bike already has an id (copy of another
     * Bike).
     * @param bikeImage image of the bike
     * @param type       type of the bike
     * @param parts      boolean array for available parts
     * @param location  location of the task
     * @param id          id of the task
     */
    public Bike(
            @Nullable Bitmap bikeImage,
            @Nullable String type,
            @Nullable boolean[] parts,
            @Nullable String location,
            @NonNull String id) {
        this(bikeImage, type, parts, location, id, false);
    }

    /**
     * Use this constructor to create a new complete Bike.
     * @param bikeImage image of the bike
     * @param type       title of the task
     * @param parts      available parts
     * @param location location of the task
     * @param completed   true if the task is completed, false if it's active
     */
    public Bike(
            @Nullable Bitmap bikeImage,
            @Nullable String type,
            @Nullable boolean[] parts,
            @Nullable String location,
            boolean completed) {
        this(bikeImage, type, parts, location, UUID.randomUUID().toString(), completed);
    }

    /**
     * Use this constructor to specify a complete Bike if the Bike already has an id (copy of
     * another Bike).
     *
     * @param bikeImage   image of the bike
     * @param type       type of the bike
     * @param parts      available parts
     * @param location location of the task
     * @param id          id of the task
     * @param completed   true if the task is completed, false if it's active
     */
    public Bike(
            @Nullable Bitmap bikeImage,
            @Nullable String type,
            @Nullable boolean[] parts,
            @Nullable String location,
            @NonNull String id,
            boolean completed) {
        mbikeImage = bikeImage;
        mId = id;
        mBikeType = type;
        mParts = parts;
        mAddress = location;
        isComplete = completed;
    }

    @NonNull
    public boolean[] getParts() {return mParts;}

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mBikeType;
    }

    @Nullable
    public Bitmap getBikeImage() {
        return mbikeImage;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mBikeType)) {
            return mBikeType;
        } else {
            return mAddress;
        }
    }

    @Nullable
    public String getDescription() {
        return mAddress;
    }

    public boolean isCompleted() {
        return isComplete;
    }

    public boolean isActive() {
        return !isComplete;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mBikeType) &&
                Strings.isNullOrEmpty(mAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bike bike = (Bike) o;
        return Objects.equal(mId, bike.mId) &&
                Objects.equal(mBikeType, bike.mBikeType) &&
                Objects.equal(mAddress, bike.mAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mBikeType, mAddress);
    }

    @Override
    public String toString() {
        return "Bike type " + mBikeType;
    }
}
