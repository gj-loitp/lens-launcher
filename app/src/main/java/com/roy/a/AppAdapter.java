package com.roy.a;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.roy.R;
import com.roy.sv.BroadcastReceivers;
import com.roy.model.App;
import com.roy.model.AppPersistent;
import com.roy.ui.ASettings;
import com.roy.util.UtilApp;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

//2023.03.19 tried to convert to kotlin but failed
public class AppAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final List<App> mApps;

    public AppAdapter(Context mContext,
                      List<App> mApps) {
        this.mContext = mContext;
        this.mApps = mApps;
    }

    public App getItemForPosition(int position) {
        return mApps.get(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    @Override
    public long getItemId(int position) {
        return mApps.get(position).getId();
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_item_app, parent, false);
        final AppViewHolder holder = new AppViewHolder(view, mContext);
        holder.setOnClickListeners();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        App app = getItemForPosition(position);
        if (app == null) {
            return;
        }
        AppViewHolder appViewHolder = (AppViewHolder) holder;
        appViewHolder.setAppElement(app);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.element_app_container)
        CardView mContainer;

        @BindView(R.id.element_app_label)
        TextView mLabel;

        @BindView(R.id.element_app_icon)
        ImageView mIcon;

        @BindView(R.id.element_app_hide)
        ImageView mToggleAppVisibility;

        @BindView(R.id.element_app_menu)
        ImageView mMenu;

        private App mApp;
        private final Context mContext;

        public AppViewHolder(View itemView,
                             Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mContext = context;
        }

        public void setAppElement(App app) {
            this.mApp = app;
            mLabel.setText(mApp.getLabel());
            mIcon.setImageBitmap(mApp.getIcon());
            boolean isAppVisible =
                    AppPersistent.getAppVisibility(Objects.requireNonNull(mApp.getPackageName()).toString(), Objects.requireNonNull(mApp.getName()).toString());
            if (isAppVisible) {
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_grey_24dp);
            } else {
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }
            if (mApp.getPackageName().toString().equals("com.roy93group.lenslauncher")) {
                mToggleAppVisibility.setVisibility(View.INVISIBLE);
            } else {
                mToggleAppVisibility.setVisibility(View.VISIBLE);
            }
        }

        public void toggleAppVisibility(App app) {
            this.mApp = app;
            boolean isAppVisible =
                    AppPersistent.getAppVisibility(Objects.requireNonNull(mApp.getPackageName()).toString(), Objects.requireNonNull(mApp.getName()).toString());
            AppPersistent.setAppVisibility(
                    mApp.getPackageName().toString(),
                    mApp.getName().toString(),
                    !isAppVisible);
            if (isAppVisible) {
                Snackbar.make(mContainer, mApp.getLabel() + " is now hidden", Snackbar.LENGTH_LONG).show();
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            } else {
                Snackbar.make(mContainer, mApp.getLabel() + " is now visible", Snackbar.LENGTH_LONG).show();
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_grey_24dp);
            }
        }

        private void sendChangeAppsVisibilityBroadcast() {
            if (mContext == null) {
                return;
            }
            if (!(mContext instanceof ASettings)) {
                return;
            }
            ASettings activitySettings = (ASettings) mContext;
            Intent changeAppsVisibilityIntent = new Intent(activitySettings, BroadcastReceivers.AppsVisibilityChangedReceiver.class);
            activitySettings.sendBroadcast(changeAppsVisibilityIntent);
        }

        public void setOnClickListeners() {
            itemView.setOnClickListener(view -> UtilApp.launchComponent(
                    mContext,
                    Objects.requireNonNull(mApp.getPackageName()).toString(), Objects.requireNonNull(mApp.getName()).toString(),
                    itemView, new Rect(0, 0, itemView.getMeasuredWidth(), itemView.getMeasuredHeight())));
            mToggleAppVisibility.setOnClickListener(v -> {
                if (mApp != null) {
                    sendChangeAppsVisibilityBroadcast();
                    toggleAppVisibility(mApp);
                } else {
                    Snackbar.make(mContainer, mContext.getString(R.string.error_app_not_found), Snackbar.LENGTH_LONG).show();
                }
            });

            mMenu.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(AppViewHolder.this);
                popupMenu.inflate(R.menu.menu_app);
                popupMenu.show();
            });
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_item_element_app_info:
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + mApp.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.menu_item_element_uninstall:
                    try {
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:" + mApp.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        }

    }
}
