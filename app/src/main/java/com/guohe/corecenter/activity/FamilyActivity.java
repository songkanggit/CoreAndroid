package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohe.corecenter.R;

public class FamilyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mBackLL;
    private TextView mCreateFamilyTV, mTitleTV;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {}
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_family;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mCreateFamilyTV = fvb(R.id.tv_family_create);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
        mCreateFamilyTV.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("我的家庭");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back: {
                finish();
                break;
            }
            case R.id.tv_family_create: {
                break;
            }
        }
    }
}
