package com.guohe.corecenter.activity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;

import com.guohe.corecenter.R;
import com.guohe.corecenter.bean.Account;
import com.guohe.corecenter.bean.HttpResponse;
import com.guohe.corecenter.bean.RequestParam;
import com.guohe.corecenter.constant.PreferenceConst;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.utils.DateTimeUtil;
import com.guohe.corecenter.utils.JacksonUtil;
import com.guohe.corecenter.view.RoundImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AccountDetailActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mBackLL;
    private TextView mTitleTV, mNickNameTV, mTelephoneTV, mFamilyTV, mBabyTV, mLogoutTV;
    private RelativeLayout mNickNameRL, mHeadImageRL, mTelephoneRL, mFaimlyRL, mBabyRL;
    private RoundImageView mHeadImage;
    private Account mAccountInfo;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {
        try {
            mAccountInfo = JacksonUtil.convertValue(mPreferencesManager.readObject(PreferenceConst.USER_INFO), Account.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mAccountInfo == null) {
            finish();
        }
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_account_detail;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mHeadImage = fvb(R.id.iv_head_image);
        mNickNameTV = fvb(R.id.tv_nick_name);
        mTelephoneTV = fvb(R.id.tv_telephone);
        mFamilyTV = fvb(R.id.tv_family);
        mBabyTV = fvb(R.id.tv_baby);
        mLogoutTV = fvb(R.id.tv_logout);

        mHeadImageRL = fvb(R.id.rl_headimage);
        mNickNameRL = fvb(R.id.rl_nick_name);
        mTelephoneRL = fvb(R.id.rl_telephone);
        mFaimlyRL = fvb(R.id.rl_family);
        mBabyRL = fvb(R.id.rl_baby);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
        mLogoutTV.setOnClickListener(this::onClick);

        mHeadImageRL.setOnClickListener(this::onClick);
        mNickNameRL.setOnClickListener(this::onClick);
        mTelephoneRL.setOnClickListener(this::onClick);
        mFaimlyRL.setOnClickListener(this::onClick);
        mBabyRL.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("个人信息");
        if(!TextUtils.isEmpty(mAccountInfo.getHeadImage())) {
            mHeadImage.setImageUrl("http://img.guostory.com//ImageCache/16a341b3713c4c32ac2f39b523674099.png");
        } else {
            mHeadImage.setImageUrl("http://img.guostory.com//ImageCache/16a341b3713c4c32ac2f39b523674099.png");
        }
        if(!TextUtils.isEmpty(mAccountInfo.getNickName())) {
            mNickNameTV.setText(mAccountInfo.getNickName());
        }
        if(!TextUtils.isEmpty(mAccountInfo.getTelephone())) {
            mTelephoneTV.setText(mAccountInfo.getTelephone().substring(0, 3) + "****" + mAccountInfo.getTelephone().substring(7, 11));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back: {
                finish();
                break;
            }
            case R.id.tv_logout: {
                mPreferencesManager.clear();
                finish();
                break;
            }
            case R.id.rl_headimage: {
                PictureSelector.create(AccountDetailActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .compress(true)
                        .maxSelectNum(1)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST: {
                    //reference: https://github.com/LuckSiege/PictureSelector
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    mHeadImage.setImageBitmap(BitmapFactory.decodeFile(selectList.get(0).getCompressPath()));
                    break;
                }
            }
        }
    }

    private void updateInfo(final String nickName, final String headImage) {
        if(isNetworkAvailable()) {
            RequestParam requestParam = RequestParam.newInstance(mPreferencesManager);
            if(!TextUtils.isEmpty(nickName)) {
                requestParam.addAttribute("nickName", nickName);
            }
            if(!TextUtils.isEmpty(headImage)) {
                requestParam.addAttribute("headImage", headImage);
            }
            mCoreContext.executeAsyncTask(() -> {
                try {
                    HttpResponse response = JacksonUtil.readValue(mHttpService.post(UrlConst.USER_UPDATE_URL, requestParam.toString()), HttpResponse.class);
                    if(response.isSuccess()) {
                        mPreferencesManager.put(PreferenceConst.ACCESS_TOKEN, response.getAccessToken());
                        mPreferencesManager.writeObject(PreferenceConst.USER_INFO, response.getData());
                        Date validDate = DateTimeUtil.addDays(new Date(), 7);
                        mPreferencesManager.put(PreferenceConst.LOGIN_TIME, DateTimeUtil.YYYY_MM_DD_HH_MM_SS.format(validDate));
                        finish();
                    } else {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), response.getMsg(), Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_disable, Toast.LENGTH_SHORT).show();
        }
    }
}
