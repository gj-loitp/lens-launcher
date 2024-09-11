package com.mckimquyen.adt;

import static com.mckimquyen.util.CKt.PKG_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mckimquyen.R;
import com.mckimquyen.ext.Biometric;
import com.mckimquyen.model.App;
import com.mckimquyen.model.AppPersistent;
import com.mckimquyen.services.BroadcastReceivers;
import com.mckimquyen.ui.ActSettings;
import com.mckimquyen.util.UtilApp;

import java.util.List;
import java.util.Objects;

//2023.03.19 tried to convert to kotlin but failed
public class AppAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final List<App> mApps;

    public AppAdapter(Context mContext, List<App> mApps) {
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

    static class AppViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        CardView cvAppContainer;
        TextView tvAppLabel;
        ImageView ivAppIcon;
        ImageView ivAppHide;
        Button btAppLock;
        ImageView ivAppMenu;

        private App mApp;
        private final Context mContext;
        private final boolean mIsHaveBiometric;

        public AppViewHolder(View itemView, Context context) {
            super(itemView);
            this.mContext = context;
            this.mIsHaveBiometric = Biometric.INSTANCE.isHaveBiometric(mContext);
            this.cvAppContainer = itemView.findViewById(R.id.cvAppContainer);
            this.tvAppLabel = itemView.findViewById(R.id.tvAppLabel);
            this.ivAppIcon = itemView.findViewById(R.id.ivAppIcon);
            this.ivAppHide = itemView.findViewById(R.id.ivAppHide);
            this.btAppLock = itemView.findViewById(R.id.btAppLock);
            this.ivAppMenu = itemView.findViewById(R.id.ivAppMenu);
        }

        public void setAppElement(App app) {
            this.mApp = app;
            tvAppLabel.setText(mApp.getLabel());
            ivAppIcon.setImageBitmap(mApp.getIcon());

            String pkgName = Objects.requireNonNull(mApp.getPackageName()).toString();
            String name = Objects.requireNonNull(mApp.getName()).toString();

            if (this.mIsHaveBiometric) {
                boolean isAppOpened = AppPersistent.getAppOpened(pkgName, name);
                btAppLock.setVisibility(View.VISIBLE);
                if (isAppOpened) {
                    btAppLock.setText(R.string.lock);
                    ViewCompat.setBackgroundTintList(
                            btAppLock,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryTrans))
                    );
                } else {
                    btAppLock.setText(R.string.unlock);
                    ViewCompat.setBackgroundTintList(
                            btAppLock,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimary))
                    );
                }
            } else {
                btAppLock.setVisibility(View.GONE);
            }

            boolean isAppVisible = AppPersistent.getAppVisibility(pkgName, name);
            if (isAppVisible) {
                ivAppHide.setImageResource(R.drawable.ic_visibility_grey_24dp);
                ivAppHide.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimaryTrans));
            } else {
                ivAppHide.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
                ivAppHide.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }

            if (mApp.getPackageName().toString().equals(PKG_NAME)) {
                ivAppHide.setVisibility(View.GONE);
                btAppLock.setVisibility(View.GONE);
            } else {
                ivAppHide.setVisibility(View.VISIBLE);
                if (mIsHaveBiometric) {
                    btAppLock.setVisibility(View.VISIBLE);
                } else {
                    btAppLock.setVisibility(View.GONE);
                }
            }
        }

        public void toggleAppVisibility(App app) {
            this.mApp = app;
            boolean isAppVisible = AppPersistent.getAppVisibility(Objects.requireNonNull(mApp.getPackageName()).toString(), Objects.requireNonNull(mApp.getName()).toString());
            AppPersistent.setAppVisibility(mApp.getPackageName().toString(), mApp.getName().toString(), !isAppVisible);
            if (isAppVisible) {
                Snackbar.make(cvAppContainer, mApp.getLabel() + " is now hidden", Snackbar.LENGTH_LONG).show();
                ivAppHide.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
                ivAppHide.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                Snackbar.make(cvAppContainer, mApp.getLabel() + " is now visible", Snackbar.LENGTH_LONG).show();
                ivAppHide.setImageResource(R.drawable.ic_visibility_grey_24dp);
                ivAppHide.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimaryTrans));
            }
        }

        public void toggleAppLock(App app) {
            this.mApp = app;
            String pkgName = Objects.requireNonNull(mApp.getPackageName()).toString();
            String name = Objects.requireNonNull(mApp.getName()).toString();
            String label = Objects.requireNonNull(mApp.getLabel()).toString();
            boolean isAppOpened = AppPersistent.getAppOpened(pkgName, name);

            if (mContext instanceof AppCompatActivity) {
                Biometric.INSTANCE.toggleLockApp((AppCompatActivity) mContext, label, pkgName, isAppOpened, (s, aBoolean) -> {
                    AppPersistent.setAppOpened(pkgName, name, !isAppOpened);
                    if (isAppOpened) {
                        Snackbar.make(cvAppContainer, label + " is now locked", Snackbar.LENGTH_LONG).show();
                        btAppLock.setText(R.string.unlock);
                        ViewCompat.setBackgroundTintList(
                                btAppLock,
                                ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimary))
                        );
                    } else {
                        Snackbar.make(cvAppContainer, label + " is now unlocked", Snackbar.LENGTH_LONG).show();
                        btAppLock.setText(R.string.lock);
                        ViewCompat.setBackgroundTintList(
                                btAppLock,
                                ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryTrans))
                        );
                    }
                    return null;
                });
            }
        }

        private void sendChangeAppsVisibilityBroadcast() {
            if (mContext == null) {
                return;
            }
            if (!(mContext instanceof ActSettings)) {
                return;
            }
            ActSettings activitySettings = (ActSettings) mContext;
            Intent changeAppsVisibilityIntent = new Intent(activitySettings, BroadcastReceivers.AppsVisibilityChangedReceiver.class);
            activitySettings.sendBroadcast(changeAppsVisibilityIntent);
        }

        private void sendChangeAppsLockBroadcast() {
            if (mContext == null) {
                return;
            }
            if (!(mContext instanceof ActSettings)) {
                return;
            }
            ActSettings activitySettings = (ActSettings) mContext;
            Intent intent = new Intent(activitySettings, BroadcastReceivers.AppsLockChangedReceiver.class);
            activitySettings.sendBroadcast(intent);
        }

        public void setOnClickListeners() {
            itemView.setOnClickListener(view ->
                    UtilApp.launchComponent(
                            mContext,
                            Objects.requireNonNull(mApp.getPackageName()).toString(),
                            Objects.requireNonNull(mApp.getLabel()).toString(),
                            Objects.requireNonNull(mApp.getName()).toString(),
                            itemView,
                            new Rect(
                                    0,
                                    0,
                                    itemView.getMeasuredWidth(),
                                    itemView.getMeasuredHeight())
                    )
            );
            ivAppHide.setOnClickListener(v -> {
                if (mApp != null) {
                    sendChangeAppsVisibilityBroadcast();
                    toggleAppVisibility(mApp);
                } else {
                    Snackbar.make(cvAppContainer, mContext.getString(R.string.error_app_not_found), Snackbar.LENGTH_LONG).show();
                }
            });
            btAppLock.setOnClickListener(v -> {
                if (mApp != null) {
                    sendChangeAppsLockBroadcast();
                    toggleAppLock(mApp);
                } else {
                    Snackbar.make(cvAppContainer, mContext.getString(R.string.error_app_not_found), Snackbar.LENGTH_LONG).show();
                }
            });

            ivAppMenu.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(AppViewHolder.this);
                popupMenu.inflate(R.menu.menu_app);
                popupMenu.show();
            });
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.menuItemElementAppInfo) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + mApp.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(mContext, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (id == R.id.menuItemElementUninstall) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + mApp.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(mContext, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        }

    }
}
