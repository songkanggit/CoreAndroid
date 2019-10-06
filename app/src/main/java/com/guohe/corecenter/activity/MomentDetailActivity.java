package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohe.corecenter.R;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.view.CachedImageView;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class MomentDetailActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private ViewPager mViewPager;
    private List<String> mImageList;
    private ViewPagerAdapter mViewAdapter;
    private TextView mFollowTV, mCommentTV, mLikeTV, mFavoriteTV;


    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {
        ImmersionBar.with(this).statusBarColor(R.color.smssdk_black).init();
        mImageList = new ArrayList<>();
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_1.jpg");
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_2.jpg");
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_3.jpg");
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_4.jpg");
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_5.jpg");
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_6.jpg");
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_7.jpg");
        mImageList.add(UrlConst.PICTURE_DOMAIN + "demo_8.jpg");
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_moment_detail;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mViewPager = fvb(R.id.view_pager);

        mFollowTV = fvb(R.id.tv_follow);
        mCommentTV = fvb(R.id.tv_comment);
        mLikeTV = fvb(R.id.tv_like);
        mFavoriteTV = fvb(R.id.tv_favorite);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
        mFollowTV.setOnClickListener(this::onClick);
        mCommentTV.setOnClickListener(this::onClick);
        mLikeTV.setOnClickListener(this::onClick);
        mFavoriteTV.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        String initTitle = "1/" + mImageList.size();
        mTitleTV.setText(initTitle);
        mViewPager.setAdapter(mViewAdapter = new ViewPagerAdapter(getApplicationContext()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                StringBuilder sb = new StringBuilder();
                sb.append(position+1);
                sb.append("/" + mImageList.size());
                mTitleTV.setText(sb.toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewAdapter.addAllView(mImageList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back: {
                finish();
                break;
            }
            case R.id.tv_follow: {
                break;
            }
            case R.id.tv_comment: {
                break;
            }
            case R.id.tv_like: {
                break;
            }
            case R.id.tv_favorite: {
                break;
            }
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {
        List<View> mViewList;
        Context mContext;

        public ViewPagerAdapter(Context context) {
            mViewList = new ArrayList<>();
            mContext = context;
        }

        public void addAllView(List<String> imageList) {
            mViewList.clear();
            for(String url:imageList) {
                CachedImageView imageView = new CachedImageView(mContext);
                imageView.setImageUrl(url);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mViewList.add(imageView);
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = mViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
