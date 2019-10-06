package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.guohe.corecenter.R;
import com.guohe.corecenter.utils.DensityUtil;

public class CreateFamilyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private Spinner mSpinner;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {}
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_family_create;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mSpinner = fvb(R.id.spinner);
    }
    protected void assembleViewClickAffairs() {
        mBackLL.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("请完善信息");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(CreateFamilyActivity.this, R.array.spinner_family, R.layout.spinner_layout);
        mSpinner.setAdapter(adapter);
        mSpinner.setDropDownVerticalOffset(DensityUtil.dip2px(getApplicationContext(), 45));
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
