package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohe.corecenter.R;

public class CommonEditActivity extends BaseActivity implements View.OnClickListener {
    public static final int RESULT_CODE = 100;

    private EditText mEditText;
    private LinearLayout mBackLL;
    private TextView mTitleTV, mMenuTV;
    private String mTitle, mHint;

    protected void parseNonNullBundle(Bundle bundle){
        mTitle = bundle.getString("title");
        mHint = bundle.getString("hint");
    }
    protected void initDataIgnoreUi() {
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_common_edit;}
    protected void viewAffairs(){
        mEditText = fvb(R.id.et_content);
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mMenuTV = fvb(R.id.toolbar_menu);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
        mMenuTV.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs() {
        mMenuTV.setText("确定");
        if(!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mHint)) {
            mTitleTV.setText(mTitle);
            mEditText.setHint(mHint);
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back: {
                finish();
                break;
            }
            case R.id.toolbar_menu: {
                Intent intent = new Intent();
                intent.putExtra("data", mEditText.getText().toString());
                setResult(RESULT_CODE, intent);
                finish();
            }
        }
    }
}
