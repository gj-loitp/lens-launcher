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
import com.roy.itf.AppsInterface;
import com.roy.model.App;
import com.roy.sv.BroadcastReceivers;
import com.roy.util.UtilAppSorter;
import com.roy.util.UtilSettings;

import java.util.ArrayList;
import java.util.Objects;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FApps extends Fragment implements AppsInterface {
    RecyclerView rvApps;
    MaterialProgressBar progressBarApps;

    private UtilSettings utilSettings;
    private int indexScrolledItem;

    public FApps() {
    }

    public static FApps newInstance() {
        return new FApps();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_apps, container, false);
        utilSettings = new UtilSettings(getActivity());
        setupRecycler(Objects.requireNonNull(AppsSingleton.getInstance()).getApps());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);
    }

    private void setupViews(View view) {
        rvApps = view.findViewById(R.id.rvApps);
        progressBarApps = view.findViewById(R.id.progressBarApps);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).setAppsInterface(this);
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
        if (rvApps.getLayoutManager() != null) {
            indexScrolledItem = ((LinearLayoutManager) rvApps.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        progressBarApps.setVisibility(View.INVISIBLE);
        rvApps.setVisibility(View.VISIBLE);
        AppAdapter mAppAdapter = new AppAdapter(getActivity(), apps);
        rvApps.setAdapter(mAppAdapter);
        rvApps.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.columns_apps)));
        rvApps.setItemAnimator(new DefaultItemAnimator());
        rvApps.scrollToPosition(indexScrolledItem);
        indexScrolledItem = 0;
    }

    @Override
    public void onDefaultsReset() {
        if (utilSettings.getSortType() != UtilAppSorter.SortType.values()[UtilSettings.DEFAULT_SORT_TYPE]) {
            utilSettings.save(UtilSettings.KEY_SORT_TYPE, UtilSettings.DEFAULT_SORT_TYPE);
            sendEditAppsBroadcast();
        }
    }

    @Override
    public void onAppsUpdated(ArrayList<App> apps) {
        setupRecycler(apps);
    }
}
