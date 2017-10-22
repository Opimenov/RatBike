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

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.addeditbike.AddEditBikeActivity;
import com.example.android.architecture.blueprints.todoapp.data.Bike;
import com.example.android.architecture.blueprints.todoapp.bikedetail.BikeDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Bike}s. User can choose to view all, active or completed tasks.
 */
public class BikesFragment extends Fragment implements BikesContract.View {

    private BikesContract.Presenter mPresenter;

    private BikesAdapter mListAdapter;

    private View mNoBikesView;

    private ImageView mNoBikeIcon;

    private TextView mNoBikeMainView;

    private TextView mNoBikeAddView;

    private LinearLayout mBikesView;

    private TextView mFilteringLabelView;

    public BikesFragment() {
        // Requires empty public constructor
    }

    public static BikesFragment newInstance() {
        return new BikesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: BikesFragment");
        super.onCreate(savedInstanceState);
        //TODO create bike without context
//        mListAdapter = new BikesAdapter(new ArrayList<Bike>(0), mItemListener);
        //TODO this is just to prove the idea
        mListAdapter = new BikesAdapter(new ArrayList<Bike>(0), mItemListener, getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: BikesFragment");
        mPresenter.start();
        //TODO: this is extra added
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(@NonNull BikesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tasks_frag, container, false);

        // Set up tasks view
        ListView listView = (ListView) root.findViewById(R.id.bikes_list);
        listView.setAdapter(mListAdapter);
        mFilteringLabelView = (TextView) root.findViewById(R.id.filteringLabel);
        mBikesView = (LinearLayout) root.findViewById(R.id.bikesLL);

        // Set up  no tasks view
        mNoBikesView = root.findViewById(R.id.noBikes);
        mNoBikeIcon = (ImageView) root.findViewById(R.id.noBikesIcon);
        mNoBikeMainView = (TextView) root.findViewById(R.id.noBikesMain);
        mNoBikeAddView = (TextView) root.findViewById(R.id.noTasksAdd);
        mNoBikeAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTask();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTasks(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.clearCompletedTasks();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        mPresenter.setFiltering(BikesFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        mPresenter.setFiltering(BikesFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        mPresenter.setFiltering(BikesFilterType.ALL_BIKES);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }

    /**
     * Listener for clicks on bikes in the ListView.
     */
    BikeItemListener mItemListener = new BikeItemListener() {
        @Override
        public void onBikeClick(Bike clickedBike) {
            mPresenter.openBikeDetails(clickedBike);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showBikes(List<Bike> bikes) {
        mListAdapter.replaceData(bikes);

        mBikesView.setVisibility(View.VISIBLE);
        mNoBikesView.setVisibility(View.GONE);
    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_bikes_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_bikes_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_bikes_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mBikesView.setVisibility(View.GONE);
        mNoBikesView.setVisibility(View.VISIBLE);

        mNoBikeMainView.setText(mainText);
        mNoBikeIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoBikeAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showActiveFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_active));
    }

    @Override
    public void showCompletedFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_completed));
    }

    @Override
    public void showAllFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_all));
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddEditBikeActivity.class);
        startActivityForResult(intent, AddEditBikeActivity.REQUEST_ADD_BIKE);
    }

    @Override
    public void showBikeDetailsUi(String bikeId) {
        // in it's own Activity, since it makes more
        // sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), BikeDetailActivity.class);
        intent.putExtra(BikeDetailActivity.EXTRA_BIKE_ID, bikeId);
        startActivity(intent);
    }

    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private static class BikesAdapter extends BaseAdapter {

        private List<Bike> mBikes;
        private BikeItemListener mItemListener;

        public BikesAdapter(List<Bike> bikes, BikeItemListener itemListener) {
            setList(bikes);
            mItemListener = itemListener;
        }

        public BikesAdapter(List<Bike> bikes, BikeItemListener itemListener, Context ctx) {
            setList(bikes);

            //TODO remove this test data
            bikes.add(new Bike(
                    BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo),
                    "Road",
                    new boolean[18],
                    "50 Harvard ave, Brighton, MA USA",
                    true));

            bikes.add(new Bike(
                    BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo),
                    "Mountain",
                    new boolean[18],
                    "88 Huntington road, Newton, MA",
                    false));

            mItemListener = itemListener;
        }


        public void replaceData(List<Bike> bikes) {
            setList(bikes);
            notifyDataSetChanged();
        }

        private void setList(List<Bike> bikes) {
            mBikes = checkNotNull(bikes);
        }

        @Override
        public int getCount() {
            return mBikes.size();
        }

        @Override
        public Bike getItem(int i) {
            return mBikes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.bike_list_item, viewGroup, false);
            }

            final Bike bike = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.bike_type_item);
//            titleTV.setText(bike.getTitleForList());
            titleTV.setText(mBikes.get(i).getDescription());
            ImageView bikesThumbnail = (ImageView) rowView.findViewById(R.id.bikes_thumbnail);
            bikesThumbnail.setImageBitmap(mBikes.get(i).getBikeImage());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onBikeClick(bike);
                }
            });

            return rowView;
        }
    }

    public interface BikeItemListener {

        void onBikeClick(Bike clickedBike);

//        void onCompleteTaskClick(Bike completedBike);
//
//        void onActivateTaskClick(Bike activatedBike);
    }

}
