package com.guohe.corecenter.utils;

import android.content.Context;

/**
 * Created by kousou on 2019/2/22.
 */

public class QiniuImageUtil {
    public static final String LEFT_TOP_FIX_SIZE_200PX = "?imageMogr2/auto-orient/gravity/NorthWest/crop/200x200/blur/1x0/quality/75|imageslim";

    public static final String getFixSizeSquareImageAppender(final Context context, int dip) {
        final int width = DensityUtil.dip2px(context, dip);
        final int height = width;
        return "?imageView2/1/w/"+ width +"/h/"+ height +"/q/75|imageslim";
    }

    public static final String getFixSizeRectImageAppender(final Context context, int wdip, int hdip) {
        final int width = DensityUtil.dip2px(context, wdip);
        final int height = DensityUtil.dip2px(context, hdip);
        return "?imageView2/1/w/"+ width +"/h/"+ height +"/q/75|imageslim";
    }

    public static final String getFixHeightAppender(final Context context, int dip) {
        final int height = DensityUtil.dip2px(context, dip);
        return "?imageView2/1/w/"+ DensityUtil.deviceDisplayWidth(context) +"/h/"+ height +"/q/75|imageslim";
    }

    public static final String getFixSizeSquareThumbnailAppender(final Context context, int dip) {
        final int height = DensityUtil.dip2px(context, dip);
        return "?imageView2/2/w/"+height+"/h/"+height+"/q/100|imageslim";
    }
}
