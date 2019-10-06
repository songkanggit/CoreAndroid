package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohe.corecenter.R;

public class AboutSunshineActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mBackLL;
    private TextView mTitleTV;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {}
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_about_sunshine;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("关于阳光");
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
