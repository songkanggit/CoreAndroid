package com.guohe.corecenter.view.lru;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.guohe.corecenter.utils.MD5Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * 本地缓存
 * Created by songkang on 2018/4/19.
 */

public class LocalCacheUtils {
    private static final String CACHE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/ImageLoader";

    /**
     * 从本地读取图片
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url){
        String fileName = null;//把图片的url当做文件名,并进行MD5加密
        try {
            fileName = MD5Util.md5String(url);
            File file=new File(CACHE_PATH,fileName);

            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从网络获取图片后,保存至本地缓存
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap){
        try {
            String fileName = MD5Util.md5String(url);//把图片的url当做文件名,并进行MD5加密
            File file=new File(CACHE_PATH,fileName);

            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }

            //把图片保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
