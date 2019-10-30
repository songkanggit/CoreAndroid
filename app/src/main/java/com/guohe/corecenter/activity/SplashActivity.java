package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.content.Intent;
import android.os.Bundle;

import com.guohe.corecenter.R;
import com.gyf.immersionbar.ImmersionBar;

public class SplashActivity extends BaseActivity {

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {}
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_splash;}
    protected void viewAffairs(){
        ImmersionBar.with(this).fullScreen(true).init();
    }
    protected void assembleViewClickAffairs(){}
    protected void initDataAfterUiAffairs(){
        mCoreContext.postTaskAsDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
