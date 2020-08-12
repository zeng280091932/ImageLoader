package com.beauney.imageloader.loader;

import com.beauney.imageloader.request.BitmapRequest;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public interface Loader {
    /**
     * 加载图片
     * @param request
     */
    void loadImage(BitmapRequest request);
}
