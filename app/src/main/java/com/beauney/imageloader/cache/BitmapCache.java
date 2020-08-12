package com.beauney.imageloader.cache;

import android.graphics.Bitmap;

import com.beauney.imageloader.request.BitmapRequest;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public interface BitmapCache {
    /**
     * 添加缓存
     *
     * @param request
     * @param bitmap
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 获取缓存
     *
     * @param request
     * @return
     */
    Bitmap get(BitmapRequest request);

    /**
     * 删除缓存
     *
     * @param request
     */
    void remove(BitmapRequest request);
}
