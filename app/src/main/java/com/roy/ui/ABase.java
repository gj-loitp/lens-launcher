package com.roy.ui;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.roy.R;
import com.roy.util.Settings;

public class ABase extends AppCompatActivity {

    protected Settings mSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSettings = new Settings(this);
        if (savedInstanceState == null) {
            updateNightMode();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setTaskDescription();
    }

    private void setTaskDescription() {
        Bitmap appIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                appIconBitmap,
                ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
        setTaskDescription(taskDescription);
    }

    protected void updateNightMode() {
        if (mSettings == null) {
            mSettings = new Settings(this);
        }
        getDelegate().setLocalNightMode(mSettings.getNightMode());
    }
}