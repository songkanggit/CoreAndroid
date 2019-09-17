package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohe.corecenter.R;
import com.guohe.corecenter.constant.UrlConst;
import com.guohe.corecenter.view.CachedImageView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.intellij.lang.annotations.MagicConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mChoosePictureLL;
    private LinearLayout mControlCameraLL;
    private LinearLayout mLocalGalleryLL;
    private RecyclerView mRecyclerView;
    private TwinklingRefreshLayout mTwinklingRefreshLayout;
    private LinearLayout mBackLL;
    private TextView mTitleTV;
    private ImageView mMenuIV;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private ImageAdapter mImageAdapter;
    private SpaceItemDecoration mSpaceItemDecoration;
    private HashMap<String, List<String>> mDataHashMap;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {
        mDataHashMap = new HashMap<>();
        List<String> imageList = new ArrayList<>();
        for(int i=0; i<4; i++) {
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/7388c0adac804730af1b29edd47ece5d.jpg");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/847e5463ea964628a20115c67720c0a4.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/36ea89121dd54ff3a3a3a2b234b18cda.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/83012006574c4c46b522c775a12d7536.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/1a8ea3d5a00245d5ba1dbd642f46ccd3.png");
        }
        mDataHashMap.put("2019年8月7日", imageList);
        imageList = new ArrayList<>();
        for(int i=0; i<2; i++) {
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/1a8ea3d5a00245d5ba1dbd642f46ccd3.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/759d94c9242440f68885ec274d2d5887.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/f3c4bcdd0e074e31b8c0e8ff3c31f9bf.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/7388c0adac804730af1b29edd47ece5d.jpg");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/847e5463ea964628a20115c67720c0a4.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/36ea89121dd54ff3a3a3a2b234b18cda.png");
            imageList.add(UrlConst.PICTURE_DOMAIN + "/ImageCache/83012006574c4c46b522c775a12d7536.png");
        }
        mDataHashMap.put("2019年8月9日", imageList);
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_device;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mMenuIV = fvb(R.id.toolbar_menu);

        mChoosePictureLL = fvb(R.id.ll_choose_picture);
        mControlCameraLL = fvb(R.id.ll_control_camera);
        mLocalGalleryLL = fvb(R.id.ll_local_gallery);

        mRecyclerView = fvb(R.id.recycler_view);
        mTwinklingRefreshLayout = fvb(R.id.refresh_layout);
    }
    protected void assembleViewClickAffairs(){
        mChoosePictureLL.setOnClickListener(this::onClick);
        mControlCameraLL.setOnClickListener(this::onClick);
        mLocalGalleryLL.setOnClickListener(this::onClick);

        mBackLL.setOnClickListener(this::onClick);
        mMenuIV.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("自定义设备名");
        mMenuIV.setImageResource(R.mipmap.menu_icn);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mImageAdapter = new ImageAdapter(getApplicationContext()));
        mRecyclerView.addItemDecoration(mSpaceItemDecoration = new SpaceItemDecoration(14));
        mImageAdapter.setData(mDataHashMap);

        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingDown(refreshLayout, fraction);
            }

            @Override
            public void onPullingUp(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingUp(refreshLayout, fraction);
            }

            @Override
            public void onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullDownReleasing(refreshLayout, fraction);
            }

            @Override
            public void onPullUpReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullUpReleasing(refreshLayout, fraction);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> refreshLayout.finishRefreshing(),1000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> refreshLayout.finishLoadmore(),1000);
            }

            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
            }

            @Override
            public void onFinishLoadMore() {
                super.onFinishLoadMore();
            }

            @Override
            public void onRefreshCanceled() {
                super.onRefreshCanceled();
            }

            @Override
            public void onLoadmoreCanceled() {
                super.onLoadmoreCanceled();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_local_gallery: {
                break;
            }
            case R.id.ll_control_camera: {
                Intent intent = new Intent(DeviceActivity.this, DeviceControlActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_choose_picture: {
                break;
            }
            case R.id.ll_back: {
                finish();
                break;
            }
            case R.id.toolbar_menu: {
                break;
            }
        }
    }

    private static class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private List<String> mImageList;

        @MagicConstant(intValues = {ItemType.DATE, ItemType.IMAGE})
        public @interface ItemType {
            int DATE = 0;
            int IMAGE = 1;
        }

        public ImageAdapter(Context context) {
            mContext = context;
            mImageList = new ArrayList<>();
        }

        public void setData(HashMap<String, List<String>> dataMap) {
            mImageList.clear();
            for(String key:dataMap.keySet()) {
                mImageList.add(key);
                mImageList.addAll(dataMap.get(key));
            }
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if(viewType == ItemType.IMAGE) {
                view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_device_image, parent, false);
                return new ImageHolder(view);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_device_image_date, parent, false);
                return new TextHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof TextHolder) {
                TextHolder textHolder = (TextHolder)holder;
                textHolder.textView.setText(mImageList.get(position));
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)textHolder.itemView.getLayoutParams();
                lp.setFullSpan(true);
            } else {
                ImageHolder imageHolder = (ImageHolder)holder;
                imageHolder.imageView.setImageUrl(mImageList.get(position));
            }
        }

        @Override
        public int getItemViewType(int position) {
            final String value = mImageList.get(position);
            if(value.startsWith("http")) {
                return ItemType.IMAGE;
            } else {
                return ItemType.DATE;
            }
        }

        @Override
        public int getItemCount() {
            return mImageList.size();
        }
    }

    private static class TextHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public TextHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_text);
        }
    }

    private static class ImageHolder extends RecyclerView.ViewHolder {
        CachedImageView imageView;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int s) {
            space = s;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams();
            switch (lp.getSpanIndex()) {
                case 0: {
                    outRect.left = space;
                    outRect.right = space/2;
                    break;
                }
                case 1: {
                    outRect.left = space/2;
                    outRect.right = space/2;
                    break;
                }
                case 2: {
                    outRect.left = space/2;
                    outRect.right = space;
                    break;
                }
            }
            outRect.bottom = space;
        }
    }
}
