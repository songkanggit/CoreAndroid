package com.guohe.corecenter.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.guohe.corecenter.view.lru.ImageLoader;


/**
 * Created by songkang on 2018/4/19.
 */

public class CachedImageView extends AppCompatImageView {
    public CachedImageView(Context context) {
        super(context);
    }

    public CachedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CachedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String imageUrl){
        if(!TextUtils.isEmpty(imageUrl)) {
            ImageLoader.getInstance().loadImageView(this, imageUrl);
        }
    }

    public void setImageUrl(String imageUrl, ImageLoader.ImageDownLoadCallback callback){
        if(!TextUtils.isEmpty(imageUrl)) {
            ImageLoader.getInstance().loadImageView(this, imageUrl, callback);
        }
    }
}
