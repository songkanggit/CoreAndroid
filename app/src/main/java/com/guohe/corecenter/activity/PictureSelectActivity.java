package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guohe.corecenter.R;
import com.guohe.corecenter.utils.BitmapUtil;
import com.guohe.corecenter.utils.DensityUtil;
import com.guohe.corecenter.view.bigimageview.ImageViewer;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class PictureSelectActivity extends BaseActivity implements View.OnClickListener {
    private static final int MAX_IMAGE_SELECT = 9;

    private LinearLayout mBackLL;
    private TextView mTitleTV, mPublishTV;
    private RecyclerView mRecyclerView;
    private List<LocalMedia> mSelectedImageList;
    private ImageRecyclerViewAdapter mRecyclerAdapter;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {
        mSelectedImageList = new ArrayList<>();
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_picture_select;}
    protected void viewAffairs(){
        mBackLL = fvb(R.id.ll_back);
        mTitleTV = fvb(R.id.toolbar_title);
        mPublishTV = fvb(R.id.toolbar_menu);
        mRecyclerView = fvb(R.id.recycler_view);
    }
    protected void assembleViewClickAffairs(){
        mBackLL.setOnClickListener(this::onClick);
        mPublishTV.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mTitleTV.setText("发布图集");

        int displayWidth = DensityUtil.deviceDisplayWidth(getApplicationContext());
        int itemGap = DensityUtil.dip2px(getApplicationContext(), 4);
        int itemWidth = (displayWidth - 4*itemGap)/3;
        int itemHeight = (displayWidth - 6*itemGap)/3;

        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        mRecyclerView.setAdapter(mRecyclerAdapter = new ImageRecyclerViewAdapter(itemWidth, itemHeight));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back: {
                finish();
                break;
            }
            case R.id.toolbar_menu: {
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
                    mSelectedImageList.addAll(selectList);
                    mRecyclerAdapter.refreshData();
                    break;
                }
            }
        }
    }

    public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageHolder> {
        private List<String> imageList;
        private int itemWidth, itemHeight;

        public ImageRecyclerViewAdapter(int width, int height) {
            itemWidth = width;
            itemHeight = height;
            imageList = new ArrayList<>();
        }

        public void refreshData() {
            imageList.clear();
            for(LocalMedia localMedia:mSelectedImageList) {
                final String localUri = "file://" + localMedia.getCompressPath();
                imageList.add(localUri);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.recycler_view_select_picture, parent, false);
            return new ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
            if(position < mSelectedImageList.size() || (position == mSelectedImageList.size() && mSelectedImageList.size() == MAX_IMAGE_SELECT)) {
                holder.imageViewSelect.setImageBitmap(BitmapFactory.decodeFile(mSelectedImageList.get(position).getCompressPath()));
                holder.imageViewDelete.setVisibility(View.VISIBLE);
            } else {
                holder.imageViewSelect.setImageResource(R.mipmap.add_photo_icn);
                holder.imageViewDelete.setVisibility(View.INVISIBLE);
            }
            holder.holderCL.getLayoutParams().width = itemWidth;
            holder.holderCL.getLayoutParams().height = itemHeight;
            holder.itemView.setOnClickListener(view -> {
                if(position == mSelectedImageList.size() && mSelectedImageList.size() < MAX_IMAGE_SELECT) {
                    final int canSelectImageCount = MAX_IMAGE_SELECT - mSelectedImageList.size();
                    PictureSelector.create(PictureSelectActivity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .compress(true)
                            .maxSelectNum(canSelectImageCount)
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                } else {
                    new ImageViewer.Builder<>(PictureSelectActivity.this, imageList).setFormatter(s -> s)
                            .allowSwipeToDismiss(true)
                            .setStartPosition(position)
                            .hideStatusBar(false)
                            .show();
                }
            });
//            holder.imageViewDelete.setOnClickListener(view -> {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MomentPublishActivity.this);
//                builder.setMessage(R.string.moment_publish_activity_alert_title);
//                builder.setTitle(R.string.moment_publish_activity_alert_message);
//                builder.setPositiveButton(R.string.app_confirm, (dialogInterface, i) -> {
//                    mSelectedImageList.remove(mSelectedImageList.get(position));
//                    refreshData();
//                });
//                builder.setNegativeButton(R.string.app_update_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
//                builder.create().show();
//            });
        }

        @Override
        public int getItemCount() {
            if(mSelectedImageList.size() < MAX_IMAGE_SELECT) {
                return mSelectedImageList.size() + 1;
            }
            return mSelectedImageList.size();
        }
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        ConstraintLayout holderCL;
        ImageView imageViewSelect, imageViewDelete;

        public ImageHolder(View view) {
            super(view);
            holderCL = view.findViewById(R.id.cl_container);
            imageViewSelect = view.findViewById(R.id.iv_image);
            imageViewDelete = view.findViewById(R.id.iv_delete);
        }
    }

    public class ImageItemDecoration extends RecyclerView.ItemDecoration {
        int verticalSpace, horizonSpace;

        public ImageItemDecoration(int h, int v) {
            horizonSpace = h;
            verticalSpace = v;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = verticalSpace;
            switch (parent.getChildLayoutPosition(view)%3) {
                case 0: {
                    outRect.left = 0;
                    outRect.right = horizonSpace*2;
                    break;
                }
                case 1: {
                    outRect.left = horizonSpace;
                    outRect.right = horizonSpace;
                    break;
                }
                case 2: {
                    outRect.left = horizonSpace*2;
                    outRect.right = 0;
                    break;
                }
            }
        }
    }
}
