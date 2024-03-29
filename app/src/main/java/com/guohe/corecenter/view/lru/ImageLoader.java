package com.guohe.corecenter.view.lru;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.guohe.corecenter.R;


/**
 * 三级缓存图片加载器
 * Created by songkang on 2018/4/19.
 */

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();
    private static ImageLoader mInstance = null;

    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public static ImageLoader getInstance(){
        if(mInstance==null){
            synchronized (ImageLoader.class){
                if(mInstance==null){
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    private ImageLoader(){
        mMemoryCacheUtils=new MemoryCacheUtils();
        mLocalCacheUtils=new LocalCacheUtils();
        mNetCacheUtils=new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
    }

    public Bitmap getCachedBitmap(Resources res, final String url) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_launcher_background);
        if(!TextUtils.isEmpty(url)){
            //内存缓存
            Log.i(TAG, "Load from memory.");
            bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
            if (bitmap == null){
                bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
                if(bitmap != null){
                    mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
                }
            }
        }
        return bitmap;
    }

    /**
     * 加载网络图片
     * @param imageView
     * @param url
     */
    public void loadImageView(ImageView imageView, String url) {
//        imageView.setImageResource(R.drawable.wx_share);
        if(TextUtils.isEmpty(url)){
            return;
        }
        Bitmap bitmap;
        //内存缓存
        Log.i(TAG, "Load from memory.");
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        //本地缓存
        Log.i(TAG,"Load from local.");
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap !=null){
            imageView.setImageBitmap(bitmap);
            //从本地获取图片后,保存至内存中
            mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
            return;
        }
        //网络缓存
        Log.i(TAG,"Load form internet.");
        mNetCacheUtils.getBitmapFromNet(imageView,url);
    }

    /**
     * 加载网络图片
     * @param imageView
     * @param url
     * @param callback
     */
    public void loadImageView(ImageView imageView, final String url, ImageDownLoadCallback callback) {
        if(TextUtils.isEmpty(url)){
            return;
        }
        Bitmap bitmap;
        //内存缓存
        Log.i(TAG, "Load from memory.");
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap!=null){
            if(imageView != null)
            imageView.setImageBitmap(bitmap);
            if(callback != null) {
                callback.onImageLoadComplete(bitmap);
            }
            return;
        }

        //本地缓存
        Log.i(TAG,"Load from local.");
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap !=null){
            if(imageView != null)
            imageView.setImageBitmap(bitmap);
            //从本地获取图片后,保存至内存中
            mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
            if(callback != null) {
                callback.onImageLoadComplete(bitmap);
            }
            return;
        }
        //网络缓存
        Log.i(TAG,"Load form internet.");
        mNetCacheUtils.getBitmapFromNet(imageView, url, callback);
    }

    public interface ImageDownLoadCallback {
        void onImageLoadComplete(final Bitmap bitmap);
    }
}
