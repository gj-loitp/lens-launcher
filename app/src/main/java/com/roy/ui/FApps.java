package com.roy.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roy.R;
import com.roy.a.AppAdapter;
import com.roy.app.AppsSingleton;
import com.roy.model.App;
import com.roy.sv.BroadcastReceivers;
import com.roy.util.AppSorter;
import com.roy.util.Settings;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FApps extends Fragment implements SettingsA.AppsInterface {

    @BindView(R.id.recycler_apps)
    RecyclerView mRecycler;

    @BindView(R.id.progress_apps)
    MaterialProgressBar mProgress;

    private Settings mSettings;
    private int mScrolledItemIndex;

    public FApps() {
    }

    public static FApps newInstance() {
        return new FApps();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_apps, container, false);
        ButterKnife.bind(this, view);
        mSettings = new Settings(getActivity());
        setupRecycler(Objects.requireNonNull(AppsSingleton.getInstance()).getApps());
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof SettingsA) {
            ((SettingsA) getActivity()).setAppsInterface(this);
        }
    }

    private void sendEditAppsBroadcast() {
        if (getActivity() == null) {
            return;
        }
        Intent editAppsIntent = new Intent(getActivity(), BroadcastReceivers.AppsEditedReceiver.class);
        getActivity().sendBroadcast(editAppsIntent);
    }

    private void setupRecycler(ArrayList<App> apps) {
        if (getActivity() == null || apps.size() == 0) {
            return;
        }
        if (mRecycler.getLayoutManager() != null) {
            mScrolledItemIndex = ((LinearLayoutManager) mRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        mProgress.setVisibility(View.INVISIBLE);
        mRecycler.setVisibility(View.VISIBLE);
        AppAdapter mAppAdapter = new AppAdapter(getActivity(), apps);
        mRecycler.setAdapter(mAppAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.columns_apps)));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.scrollToPosition(mScrolledItemIndex);
        mScrolledItemIndex = 0;
    }

    @Override
    public void onDefaultsReset() {
        if (mSettings.getSortType() != AppSorter.SortType.values()[Settings.DEFAULT_SORT_TYPE]) {
            mSettings.save(Settings.KEY_SORT_TYPE, Settings.DEFAULT_SORT_TYPE);
            sendEditAppsBroadcast();
        }
    }

    @Override
    public void onAppsUpdated(ArrayList<App> apps) {
        setupRecycler(apps);
    }
}
