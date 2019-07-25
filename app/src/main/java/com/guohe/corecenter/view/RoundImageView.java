package com.guohe.corecenter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.guohe.corecenter.R;
import com.guohe.corecenter.utils.BitmapUtil;
import com.guohe.corecenter.view.lru.ImageLoader;

/**
 * Created by kousou on 2019/2/28.
 */

public class RoundImageView extends AppCompatImageView implements ImageLoader.ImageDownLoadCallback {
    protected static final int DEFAULT_CORNER_RADIUS = 5;

    protected int mRoundRadius;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImage, defStyleAttr, 0);
        mRoundRadius = a.getDimensionPixelSize(R.styleable.RoundCornerImage_corner_radius, DEFAULT_CORNER_RADIUS);
    }

    public void setImageUrl(String imageUrl){
        if(!TextUtils.isEmpty(imageUrl)) {
            ImageLoader.getInstance().loadImageView(null, imageUrl, this);
        }
    }

    @Override
    public void onImageLoadComplete(Bitmap bitmap) {
//        setImageBitmap(BitmapUtil.getRoundRectBitmap(bitmap, DensityUtil.dip2px(getContext(), mRoundRadius)));
        Bitmap newBitmap = BitmapUtil.roundBitmapByShader(bitmap, mRoundRadius);
        setImageBitmap(newBitmap);
    }
}
