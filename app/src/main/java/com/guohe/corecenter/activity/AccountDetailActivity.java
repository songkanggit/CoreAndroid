package com.guohe.corecenter.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.guohe.corecenter.R;

public class AccountDetailActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mBackLL;
    private TextView mTitleTV;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {}
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_account_detail;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("个人信息");
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
