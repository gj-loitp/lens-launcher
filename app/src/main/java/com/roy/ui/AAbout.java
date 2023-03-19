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

import butterknife.BindView;
import butterknife.ButterKnife;

public class AAbout extends BaseActivity implements Observer {

    @BindView(R.id.text_view_about)
    TextView mTextViewAbout;

    @BindView(R.id.backdrop)
    ImageView mImageAbout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.pro_about)
    View mProAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorTransparent));
        setupViews();
        mImageAbout.postDelayed(this::circularRevealAboutImage, 150);
        NightModeObservable.getInstance().addObserver(this);
    }

    private void circularRevealAboutImage() {
        if (mImageAbout != null) {
            int cx = mImageAbout.getWidth() / 2;
            int cy = mImageAbout.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(mImageAbout, cx, cy, 0, finalRadius);
            mImageAbout.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    private void setupViews() {
        mProAbout.setVisibility(View.GONE);
        mTextViewAbout.setText(Html.fromHtml(getString(R.string.about)));
        mTextViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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