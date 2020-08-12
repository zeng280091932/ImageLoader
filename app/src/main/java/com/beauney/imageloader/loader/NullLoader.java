package com.beauney.imageloader.loader;

import android.graphics.Bitmap;
import android.util.Log;

import com.beauney.imageloader.request.BitmapRequest;

/**
 * @author zengjiantao
 * @since 2020-08-12
 */
public class NullLoader extends AbsLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        Log.d("Debug", "无法加载图片");
        return null;
    }
}
