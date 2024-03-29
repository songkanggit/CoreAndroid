package com.guohe.corecenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.guohe.corecenter.R;
import com.guohe.corecenter.core.CoreApplication;
import com.guohe.corecenter.core.CoreContext;
import com.guohe.corecenter.core.connection.NetworkManager;
import com.guohe.corecenter.core.http.HttpManager;
import com.guohe.corecenter.core.http.HttpService;
import com.guohe.corecenter.core.logger.LoggerManager;
import com.guohe.corecenter.core.logger.LoggerService;
import com.guohe.corecenter.core.pereference.PreferencesManager;
import com.guohe.corecenter.utils.NetworkUtil;
import com.shanxiang.swipeback.SwipeBackActivityBase;
import com.shanxiang.swipeback.SwipeBackActivityHelper;
import com.shanxiang.swipeback.SwipeBackLayout;
import com.shanxiang.swipeback.Utils;


/**
 * Created by SouKou on 2017/8/1.
 */

public abstract class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase {
    protected final String TAG;
    protected CoreContext mCoreContext;
    protected Context mContext;
    protected PreferencesManager mPreferencesManager;
    protected NetworkManager mNetworkManager;
    protected LoggerService mLoggerService;
    protected HttpService mHttpService;
    protected ViewGroup mRootVG;
    protected Toolbar mToolBar;
    private SwipeBackActivityHelper mHelper;

    public BaseActivity(){
        TAG = getClass().getSimpleName();
    }

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mCoreContext = getCoreContext();
        mContext = getApplicationContext();

        mLoggerService = mCoreContext.getApplicationService(LoggerManager.class);
        mNetworkManager = mCoreContext.getApplicationService(NetworkManager.class);
        mPreferencesManager = mCoreContext.getApplicationService(PreferencesManager.class);
        mHttpService = mCoreContext.getApplicationService(HttpManager.class);
        parseIntent(getIntent());

        initDataIgnoreUi();
        int layoutResId = getLayoutResourceId();
        if(layoutResId != 0) {
            setContentView(layoutResId);
        }

        mRootVG = fvb(R.id.contentPanel);
        mToolBar = fvb(R.id.toolbar);
        if(mToolBar != null) {
            setSupportActionBar(mToolBar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
        }

        viewAffairs();
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        assembleViewClickAffairs();
        initDataAfterUiAffairs();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    protected void showToastMessage(@NonNull final String msg) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show());
    }

    protected void parseIntent(Intent intent){
        if(intent == null) return;

        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            parseNonNullBundle(bundle);
        }
    }

    protected CoreContext getCoreContext(){
        return ((CoreApplication) getApplication()).getCoreContext();
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public NetworkManager getNetworkManager() {
        return mNetworkManager;
    }

    public LoggerService getLoggerService() {
        return mLoggerService;
    }

    public HttpService getHttpService() {
        return mHttpService;
    }

    protected boolean isNetworkAvailable() {
        return NetworkUtil.isNetworkConnected(getApplicationContext());
    }

    protected <V extends View> V fvb(@IdRes int id){
        return (V) findViewById(id);
    }

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {}
    @LayoutRes
    protected int getLayoutResourceId() { return 0;}
    protected void viewAffairs(){}
    protected void assembleViewClickAffairs(){}
    protected void initDataAfterUiAffairs(){}
}
