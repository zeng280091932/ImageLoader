package com.beauney.imageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.beauney.imageloader.cache.BitmapCache;
import com.beauney.imageloader.config.DisplayConfig;
import com.beauney.imageloader.core.SimpleImageLoader;
import com.beauney.imageloader.request.BitmapRequest;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public abstract class AbsLoader implements Loader {
    private final BitmapCache mCache = SimpleImageLoader.getInstance().getConfig().getBitmapCache();
    private DisplayConfig mDisplayConfig = SimpleImageLoader.getInstance().getConfig().getDisplayConfig();

    @Override
    public void loadImage(BitmapRequest request) {
        Bitmap bitmap = mCache.get(request);
        if (bitmap == null) {
            //加载前显示加载中的占位图
            showLoadingImage(request);

            //加载完成，在缓存，具体加载方式由子类确定
            bitmap = onLoad(request);
            cacheBitmap(request, bitmap);
        }
        //显示
        deliveryToUIThread(request, bitmap);
    }

    private void cacheBitmap(BitmapRequest request, Bitmap bitmap) {
        if (request != null && bitmap != null) {
            synchronized (mCache) {
                mCache.put(request, bitmap);
            }
        }
    }

    protected abstract Bitmap onLoad(BitmapRequest request);

    protected void showLoadingImage(BitmapRequest request) {
        if (hasLoadingPlaceHolder()) {
            final ImageView imageView = request.getImageView();
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(mDisplayConfig.mLoadingImage);
                }
            });
        }
    }

    protected boolean hasLoadingPlaceHolder() {
        return (mDisplayConfig != null && mDisplayConfig.mLoadingImage > 0);
    }

    protected boolean hasFailedPlaceHolder() {
        return (mDisplayConfig != null && mDisplayConfig.mFailedImage > 0);
    }

    /**
     * 交给主线程显示
     *
     * @param request
     * @param bitmap
     */
    protected void deliveryToUIThread(final BitmapRequest request, final Bitmap bitmap) {
        ImageView imageView = request.getImageView();
        imageView.post(new Runnable() {
            @Override
            public void run() {
                updateImageView(request, bitmap);
            }
        });
    }

    private void updateImageView(BitmapRequest request, Bitmap bitmap) {
        ImageView imageView = request.getImageView();

        //加载正常
        if (bitmap != null && imageView.getTag().equals(request.getImageUri())) {
            imageView.setImageBitmap(bitmap);
        }

        //加载失败
        if (bitmap == null && hasFailedPlaceHolder()) {
            imageView.setImageResource(mDisplayConfig.mFailedImage);
        }

        //监听回调
        if (request.getImageListener() != null) {
            request.getImageListener().onComplete(imageView, bitmap, request.getImageUri());
        }
    }
}
