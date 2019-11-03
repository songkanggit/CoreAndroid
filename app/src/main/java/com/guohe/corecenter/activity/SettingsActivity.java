package com.guohe.corecenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.guohe.corecenter.R;
import com.guohe.corecenter.utils.CacheCleanUtil;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mBackLL;
    private TextView mTitleTV, mCacheTV;
    private RelativeLayout mNotifyRL, mPrefRL, mClearRL, mAboutRL, mUserRL;
    private boolean isCleaned;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {
        isCleaned = false;
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.settings_activity;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mNotifyRL = fvb(R.id.rl_notify);
        mPrefRL = fvb(R.id.rl_pref);
        mClearRL = fvb(R.id.rl_clear);
        mAboutRL = fvb(R.id.rl_about);
        mUserRL = fvb(R.id.rl_user);
        mCacheTV = fvb(R.id.tv_cache);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
        mNotifyRL.setOnClickListener(this::onClick);
        mPrefRL.setOnClickListener(this::onClick);
        mClearRL.setOnClickListener(this::onClick);
        mAboutRL.setOnClickListener(this::onClick);
        mUserRL.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("设置");
        try {
            mCacheTV.setText("("+ CacheCleanUtil.getTotalCacheSize(getApplicationContext()) + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back: {
                finish();
                break;
            }
            case R.id.rl_clear: {
                if(!isCleaned) {
                    isCleaned = true;
                    CacheCleanUtil.clearAllCache(getApplicationContext());
                    mCacheTV.setText("0.00MB");
                }
                showToastMessage("缓存清除成功");
                break;
            }
        }
    }
}