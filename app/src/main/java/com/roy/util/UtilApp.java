package com.roy.util;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.roy.R;
import com.roy.model.App;
import com.roy.model.AppPersistent;
import com.roy.sv.BroadcastReceivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UtilApp {

    // Get all available apps for launcher
    public static ArrayList<App> getApps(PackageManager packageManager, Context context, Application application, String iconPackLabelName, UtilAppSorter.SortType sortType) {
        ArrayList<App> apps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = null;
        try {
            availableActivities = packageManager.queryIntentActivities(intent, 0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.error_too_many_apps, Toast.LENGTH_SHORT).show();
        }
        if (availableActivities != null) {
            UtilIconPackManager.IconPack selectedIconPack = null;
            ArrayList<UtilIconPackManager.IconPack> iconPacks = new UtilIconPackManager().getAvailableIconPacksWithIcons(true, application);

            for (UtilIconPackManager.IconPack iconPack : iconPacks) {
                if (iconPack.mName.equals(iconPackLabelName)) selectedIconPack = iconPack;
            }

            for (int i = 0; i < availableActivities.size(); i++) {
                ResolveInfo resolveInfo = availableActivities.get(i);
                App app = new App();
                app.setId(i);
                try {
                    app.setInstallDate(packageManager.getPackageInfo(resolveInfo.activityInfo.packageName, 0).firstInstallTime);
                } catch (PackageManager.NameNotFoundException e) {
                    app.setInstallDate(0);
                }
                app.setLabel(resolveInfo.loadLabel(packageManager));
                app.setPackageName(resolveInfo.activityInfo.packageName);
                app.setName(resolveInfo.activityInfo.name);
                app.setIconResId(resolveInfo.activityInfo.getIconResource());
                Bitmap defaultBitmap = UtilBitmap.packageNameToBitmap(packageManager, resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.getIconResource());
                if (selectedIconPack != null)
                    app.setIcon(selectedIconPack.getIconForPackage(Objects.requireNonNull(app.getPackageName()).toString(), defaultBitmap));
                else app.setIcon(defaultBitmap);
                app.setPaletteColor(UtilColor.getPaletteColorFromApp(app));
                apps.add(app);
            }
        }
        UtilAppSorter.sort(apps, sortType);
        return apps;
    }

    // Launch apps, for launcher :-P
    public static void launchComponent(Context context, String packageName, String name, View view, Rect bounds) {
        if (packageName != null && name != null) {
            Intent componentIntent = new Intent();
            componentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            componentIntent.setComponent(new ComponentName(packageName, name));
            if (!packageName.equals("com.roy93group.lenslauncher")) {
                componentIntent.setAction(Intent.ACTION_MAIN);
            }
            componentIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            try {
                // Launch Component
                ContextCompat.startActivity(context, componentIntent, getLauncherOptionsBundle(context, view, bounds));
                // Increment app open count
                AppPersistent.incrementAppCount(packageName, name);
                // Resort apps (if open count selected)
                UtilSettings utilSettings = new UtilSettings(context);
                if (utilSettings.getSortType() == UtilAppSorter.SortType.OPEN_COUNT_ASCENDING || utilSettings.getSortType() == UtilAppSorter.SortType.OPEN_COUNT_DESCENDING) {
                    Intent editAppsIntent = new Intent(context, BroadcastReceivers.AppsEditedReceiver.class);
                    context.sendBroadcast(editAppsIntent);
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private static Bundle getLauncherOptionsBundle(Context context, View source, Rect bounds) {
        Bundle optionsBundle = null;
        if (source != null) {
            ActivityOptionsCompat options;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && bounds != null) {
                // Clip reveal animation for Marshmallow and above
                options = ActivityOptionsCompat.makeClipRevealAnimation(source, bounds.left, bounds.top, bounds.width(), bounds.height());
            } else {
                // Fade animation otherwise
                options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.a_fade_in, R.anim.a_fade_out);
            }
            optionsBundle = options.toBundle();
        }
        return optionsBundle;
    }
}