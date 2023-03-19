package com.roy.ui;

import android.animation.Animator;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.roy.R;
import com.roy.sv.NightModeObservable;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

//2023.03.19 tried to convert kotlin but failed
public class AAbout extends ABase implements Observer {
    TextView tvAbout;
    ImageView backdrop;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_about);

        findViews();

        setupViews();
        backdrop.postDelayed(this::circularRevealAboutImage, 150);
        NightModeObservable.getInstance().addObserver(this);
    }

    private void findViews() {
        tvAbout = findViewById(R.id.tvAbout);
        backdrop = findViewById(R.id.backdrop);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        toolbar = findViewById(R.id.toolbar);
    }

    private void circularRevealAboutImage() {
        if (backdrop != null) {
            int cx = backdrop.getWidth() / 2;
            int cy = backdrop.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(backdrop, cx, cy, 0, finalRadius);
            backdrop.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    private void setupViews() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorTransparent));
        tvAbout.setText(Html.fromHtml(getString(R.string.about)));
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.a_slide_in_right, R.anim.a_slide_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof NightModeObservable) {
            updateNightMode();
        }
    }
}
