package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.guohe.corecenter.R;
import com.guohe.corecenter.bean.HttpResponse;
import com.guohe.corecenter.bean.RequestParam;
import com.guohe.corecenter.constant.PermissionConst;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.core.logger.Logger;
import com.guohe.corecenter.fragment.BaseFragment;
import com.guohe.corecenter.fragment.FirstFragment;
import com.guohe.corecenter.fragment.ForthFragment;
import com.guohe.corecenter.fragment.FragmentActivityInterface;
import com.guohe.corecenter.fragment.SecondFragment;
import com.guohe.corecenter.fragment.ThirdFragment;
import com.guohe.corecenter.utils.JacksonUtil;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener, BaseFragment.OnFragmentInteraction, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {
    private static final boolean isNeedLogin = true;

    private Button mGLButton, mGHButton, mShopButton, mMineButton;
    private FrameLayout mPublishFL;
    private List<Fragment> mFragmentList;

    protected void parseNonNullBundle(Bundle bundle){}

    protected void initDataIgnoreUi() {
        askPermissions();
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
        mGLButton = fvb(R.id.bt_gl);
        mGHButton = fvb(R.id.bt_gh);
        mShopButton = fvb(R.id.bt_shop);
        mMineButton = fvb(R.id.bt_mine);
        mPublishFL = fvb(R.id.fl_publish);
    }

    protected void assembleViewClickAffairs(){
        mGLButton.setOnClickListener(this::onClick);
        mGHButton.setOnClickListener(this::onClick);
        mShopButton.setOnClickListener(this::onClick);
        mMineButton.setOnClickListener(this::onClick);
        mPublishFL.setOnClickListener(this::onClick);
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
                if(!loginInterception()) {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.iv_group: {
                if(!loginInterception()) {
                    Intent intent = new Intent(MainActivity.this, FamilyActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.iv_setting: {
                if(!loginInterception()) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.iv_head_image: {
                if(!loginInterception()) {
                    Intent intent = new Intent(MainActivity.this, AccountDetailActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.tv_bind: {
                Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.fl_publish: {
                Intent intent = new Intent(MainActivity.this, PictureSelectActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_shop: {
                break;
            }
            case R.id.ll_sunshine: {
                Intent intent = new Intent(MainActivity.this, AboutSunshineActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_follower: {
                Intent intent = new Intent(MainActivity.this, FollowerListActivity.class);
                intent.putExtra("type", FollowerListActivity.FOLLOWER_TYPE);
                startActivity(intent);
                break;
            }
            case R.id.ll_favorite: {
                Intent intent = new Intent(MainActivity.this, FollowerListActivity.class);
                intent.putExtra("type", FollowerListActivity.FAVORITE_TYPE);
                startActivity(intent);
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

    private boolean loginInterception() {
        final String accessToken = mPreferencesManager.get("AccessToken", "");
        if(!isNeedLogin || !TextUtils.isEmpty(accessToken)) {
            return false;
        }
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        return true;
    }

    private void requestMomentList(@NonNull FragmentActivityInterface callBack) {
        mCoreContext.executeAsyncTask(() -> {
            RequestParam requestParam = RequestParam.newInstance(mPreferencesManager);
            List<Object> dataList = new ArrayList<>();
            try {
                HttpResponse response = JacksonUtil.readValue(mHttpService.post(UrlConst.MOMENT_LIST_URL, requestParam.toString()), HttpResponse.class);
                if(response.isSuccess()) {
                    dataList.addAll((List<Object>) response.getData());
                } else {
                    showToastMessage(response.getMsg());
                }
            } catch (IOException e) {
                showToastMessage("网络请求错误");
                e.printStackTrace();
            }
            callBack.onDataUpdate(dataList);
        });
    }

    //============================================Pull Listener Start==============================================
    @Override
    public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {

    }

    @Override
    public void onPullingUp(TwinklingRefreshLayout refreshLayout, float fraction) {

    }

    @Override
    public void onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {

    }

    @Override
    public void onPullUpReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {

    }

    @Override
    public void onRefresh(TwinklingRefreshLayout refreshLayout) {
        mCoreContext.executeAsyncTask(() -> {
            try {
                RequestParam requestParam = RequestParam.newInstance(mPreferencesManager);
                HttpResponse response = JacksonUtil.readValue(mHttpService.post(UrlConst.CHECK_TOKEN_URL, requestParam.toString()), HttpResponse.class);
                if(!response.isSuccess()) {
                    showToastMessage("登陆已过期");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        requestMomentList((FirstFragment)mFragmentList.get(0));
    }

    @Override
    public void onLoadMore(TwinklingRefreshLayout refreshLayout) {

    }

    @Override
    public void onFinishRefresh() {

    }

    @Override
    public void onFinishLoadMore() {

    }

    @Override
    public void onRefreshCanceled() {

    }

    @Override
    public void onLoadmoreCanceled() {

    }
    //============================================Pull Listener End==============================================
    //============================================Permission Start==============================================
    private static final String[] REQUIRED_PERMISSIONS =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

    @AfterPermissionGranted(PermissionConst.STORAGE_PERMISSION)
    private void askPermissions() {
        if(!hasRequiredPermissions()) {
            EasyPermissions.requestPermissions(this, getString(R.string.main_rational_message), PermissionConst.STORAGE_PERMISSION, REQUIRED_PERMISSIONS);
        }
    }

    private boolean hasRequiredPermissions() {
        return EasyPermissions.hasPermissions(this, REQUIRED_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Logger.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Logger.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Logger.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Logger.d(TAG, "onRationaleDenied:" + requestCode);
    }
    //============================================Permission End==============================================
}
