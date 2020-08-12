package com.beauney.imageloader.cache;

import android.graphics.Bitmap;

import com.beauney.imageloader.request.BitmapRequest;

/**
 * @author zengjiantao
 * @since 2020-08-12
 */
public class DoubleChache implements BitmapCache {
    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {

    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return null;
    }

    @Override
    public void remove(BitmapRequest request) {

    }
}
