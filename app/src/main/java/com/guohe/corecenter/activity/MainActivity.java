package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.guohe.corecenter.R;
import com.guohe.corecenter.fragment.BaseFragment;
import com.guohe.corecenter.fragment.FirstFragment;
import com.guohe.corecenter.fragment.ForthFragment;
import com.guohe.corecenter.fragment.SecondFragment;
import com.guohe.corecenter.fragment.ThirdFragment;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements View.OnClickListener, BaseFragment.OnFragmentViewClickListener {

    private Button mGLButton, mGHButton, mShopButton, mMineButton;
    private ImageView mPlusIV;
    private List<Fragment> mFragmentList;

    protected void parseNonNullBundle(Bundle bundle){}

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    protected void initDataIgnoreUi() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(FirstFragment.newInstance());
        mFragmentList.add(SecondFragment.newInstance());
        mFragmentList.add(ThirdFragment.newInstance());
        mFragmentList.add(ForthFragment.newInstance());
        for(Fragment fragment:mFragmentList) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragment).hide(fragment).commit();
        }
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_main;}
    protected void viewAffairs() {
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).statusBarDarkFont(true).fitsSystemWindows(true).init();

        mGLButton = fvb(R.id.bt_gl);
        mGHButton = fvb(R.id.bt_gh);
        mShopButton = fvb(R.id.bt_shop);
        mMineButton = fvb(R.id.bt_mine);

        mPlusIV = fvb(R.id.iv_plus);
    }

    protected void assembleViewClickAffairs(){
        mGLButton.setOnClickListener(this);
        mGHButton.setOnClickListener(this);
        mShopButton.setOnClickListener(this);
        mMineButton.setOnClickListener(this);

        mPlusIV.setOnClickListener(this);
    }

    protected void initDataAfterUiAffairs(){
        setSwipeBackEnable(false);
        toggleFragment(0);
    }

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
            case R.id.iv_scan: {

                break;
            }
            case R.id.iv_group: {

                break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(View view) {
        onClick(view);
    }

    private void toggleBottomBar(final int viewId) {
        mGLButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guoli), null, null);
        mGHButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guohe), null, null);
        mShopButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.shangcheng), null, null);
        mMineButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.wode), null, null);
        switch (viewId) {
            case R.id.bt_gl: {
                mGLButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guoli_pre), null, null);
                toggleFragment(0);
                break;
            }
            case R.id.bt_gh: {
                mGHButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.guohe_pre), null, null);
                toggleFragment(1);
                break;
            }
            case R.id.bt_shop: {
                mShopButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.shangcheng_pre), null, null);
                toggleFragment(2);
                break;
            }
            case R.id.bt_mine: {
                mMineButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.wode_pre), null, null);
                toggleFragment(3);
                break;
            }
        }
    }

    private void toggleFragment(final int index) {
        for(int i=0; i<mFragmentList.size(); i++) {
            if(i == index) {
                getSupportFragmentManager().beginTransaction().show(mFragmentList.get(i)).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragmentList.get(i)).commit();
            }
        }
    }

    //Permission
    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.main_permission_message)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
//        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
//        Toast.makeText(this, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }
}
