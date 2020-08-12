package com.beauney.imageloader.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.beauney.imageloader.config.DisplayConfig;
import com.beauney.imageloader.config.ImageLoaderConfig;
import com.beauney.imageloader.request.BitmapRequest;
import com.beauney.imageloader.request.RequestQueue;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public class SimpleImageLoader {
    private ImageLoaderConfig mConfig;

    private RequestQueue mRequestQueue;

    private static volatile SimpleImageLoader mInstance;

    private SimpleImageLoader() {
    }

    private SimpleImageLoader(ImageLoaderConfig config) {
        this.mConfig = config;
        mRequestQueue = new RequestQueue(mConfig.getThreadCount());
        //开启请求队列
        mRequestQueue.start();
    }

    /**
     * 获取单例
     *
     * @param config
     * @return
     */
    public static SimpleImageLoader getInstance(ImageLoaderConfig config) {
        if (mInstance == null) {
            synchronized (SimpleImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new SimpleImageLoader(config);
                }
            }
        }

        return mInstance;
    }

    /**
     * 获取SimpleImageLoader的实例
     *
     * @return 只有在SimpleImageLoader getInstance(ImageLoaderConfig config)调用过之后，才能返回一个实例化了的SimpleImageLoader对象
     */
    public static SimpleImageLoader getInstance() {
        if (mInstance == null) {
            throw new UnsupportedOperationException("getInstance(ImageLoaderConfig config) 没有执行过！");
        }
        return mInstance;
    }

    public void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, null, null);
    }

    /**
     * 显示图片
     *
     * @param imageView     要显示图片的控件
     * @param uri           图片路径
     * @param config        显示配置
     * @param imageListener 图片加载的监听
     */
    public void displayImage(ImageView imageView, String uri, DisplayConfig config, ImageListener imageListener) {
        BitmapRequest bitmapRequest = new BitmapRequest(imageView, uri, config, imageListener);
        mRequestQueue.addRequest(bitmapRequest);
    }

    /**
     * 图片加载的监听
     */
    public static interface ImageListener {
        /**
         * 加载完成
         *
         * @param imageView
         * @param bitmap
         * @param uri
         */
        void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }

    public ImageLoaderConfig getConfig() {
        return mConfig;
    }
}
