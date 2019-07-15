package com.guohe.corecenter.activity;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guohe.corecenter.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mFollowTV, mRecommendTV, mHotTV;
    private ImageView mFollowIV, mRecommendIV, mHotIV, mScanIV, mGroupIV;
    private Button mGLButton, mGHButton, mShopButton, mMineButton;
    private ImageView mPlusIV;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {}
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_main;}
    protected void viewAffairs(){
        mFollowTV = fvb(R.id.tv_follow);
        mRecommendTV = fvb(R.id.tv_recommend);
        mHotTV = fvb(R.id.tv_hot);

        mFollowIV = fvb(R.id.iv_follow);
        mRecommendIV = fvb(R.id.iv_recommend);
        mHotIV = fvb(R.id.iv_hot);
        mScanIV = fvb(R.id.iv_scan);
        mGroupIV = fvb(R.id.iv_group);

        mGLButton = fvb(R.id.bt_gl);
        mGHButton = fvb(R.id.bt_gh);
        mShopButton = fvb(R.id.bt_shop);
        mMineButton = fvb(R.id.bt_mine);

        mPlusIV = fvb(R.id.iv_plus);
    }
    protected void assembleViewClickAffairs(){
        mScanIV.setOnClickListener(this);
        mGroupIV.setOnClickListener(this);

        mFollowTV.setOnClickListener(this);
        mRecommendTV.setOnClickListener(this);
        mHotTV.setOnClickListener(this);

        mGLButton.setOnClickListener(this);
        mGHButton.setOnClickListener(this);
        mShopButton.setOnClickListener(this);
        mMineButton.setOnClickListener(this);

        mPlusIV.setOnClickListener(this);
    }
    protected void initDataAfterUiAffairs(){}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_gl:
            case R.id.bt_gh:
            case R.id.bt_shop:
            case R.id.bt_mine: {
                toggleBottomBar(view.getId());
                break;
            }
            case R.id.tv_hot:
            case R.id.tv_recommend:
            case R.id.tv_follow: {
                toggleTopBar(view.getId());
                break;
            }
        }
    }

    private void toggleBottomBar(final int viewId) {
        mGLButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guoli), null, null);
        mGHButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guohe), null, null);
        mShopButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.shangcheng), null, null);
        mMineButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.wode), null, null);
        switch (viewId) {
            case R.id.bt_gl: {
                mGLButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guoli_pre), null, null);
                break;
            }
            case R.id.bt_gh: {
                mGHButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guohe_pre), null, null);
                break;
            }
            case R.id.bt_shop: {
                mShopButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.shangcheng_pre), null, null);
                break;
            }
            case R.id.bt_mine: {
                mMineButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.wode_pre), null, null);
                break;
            }
        }
    }

    private void toggleTopBar(final int viewId) {
        mFollowIV.setVisibility(View.GONE);
        mFollowTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mFollowTV.setTextColor(Color.parseColor("#B0B0DA"));
        mRecommendIV.setVisibility(View.GONE);
        mRecommendTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mRecommendTV.setTextColor(Color.parseColor("#B0B0DA"));
        mHotIV.setVisibility(View.GONE);
        mHotTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mHotTV.setTextColor(Color.parseColor("#B0B0DA"));
        switch (viewId) {
            case R.id.tv_follow: {
                mFollowIV.setVisibility(View.VISIBLE);
                mFollowTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                mFollowTV.setTextColor(Color.parseColor("#818ACC"));
                break;
            }
            case R.id.tv_recommend: {
                mRecommendIV.setVisibility(View.VISIBLE);
                mRecommendTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                mRecommendTV.setTextColor(Color.parseColor("#818ACC"));
                break;
            }
            case R.id.tv_hot: {
                mHotIV.setVisibility(View.VISIBLE);
                mHotTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                mHotTV.setTextColor(Color.parseColor("#818ACC"));
                break;
            }
        }
    }
}
