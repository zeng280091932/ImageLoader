package com.beauney.imageloader.request;

import android.widget.ImageView;

import com.beauney.imageloader.config.DisplayConfig;
import com.beauney.imageloader.core.SimpleImageLoader;
import com.beauney.imageloader.policy.LoadPolicy;
import com.beauney.imageloader.utils.MD5Utils;

import java.lang.ref.SoftReference;
import java.util.Comparator;
import java.util.Objects;

import androidx.annotation.Nullable;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public class BitmapRequest implements Comparable<BitmapRequest> {
    //加载策略
    private LoadPolicy mLoadPolicy = SimpleImageLoader.getInstance().getConfig().getLoadPolicy();
    //序列号
    private int mSerialNo;
    //图片控件
    //当系统内存不足时，把引用的对象进行回收
    private SoftReference<ImageView> mImageViewRef;
    //图片路径
    private String mImageUri;
    //MD5的图片路径
    private String mImageUriMD5;

    private DisplayConfig mDisplayConfig = SimpleImageLoader.getInstance().getConfig().getDisplayConfig();

    private SimpleImageLoader.ImageListener mImageListener;

    public BitmapRequest(ImageView imageView, String imageUri, DisplayConfig displayConfig, SimpleImageLoader.ImageListener imageListener) {
        mImageViewRef = new SoftReference<>(imageView);
        //设置可见的ImageView的tag为，要下载的图片路径
        imageView.setTag(imageUri);

        mImageUri = imageUri;
        mImageUriMD5 = MD5Utils.toMD5(imageUri);
        if (displayConfig != null) {
            mDisplayConfig = displayConfig;
        }
        mImageListener = imageListener;
    }

    @Override
    public int compareTo(BitmapRequest another) {
        return mLoadPolicy.compareTo(this, another);
    }

    public int getSerialNo() {
        return mSerialNo;
    }

    public void setSerialNo(int serialNo) {
        this.mSerialNo = serialNo;
    }

    public ImageView getImageView() {
        return mImageViewRef.get();
    }

    public String getImageUri() {
        return mImageUri;
    }

    public String getImageUriMD5() {
        return mImageUriMD5;
    }

    public SimpleImageLoader.ImageListener getImageListener() {
        return mImageListener;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mLoadPolicy == null) ? 0 : mLoadPolicy.hashCode());
        result = prime * result + mSerialNo;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BitmapRequest other = (BitmapRequest) obj;
        if (mLoadPolicy == null) {
            if (other.mLoadPolicy != null) {
                return false;
            }
        } else if (!mLoadPolicy.equals(other.mLoadPolicy)) {
            return false;
        }
        if (mSerialNo != other.mSerialNo) {
            return false;
        }
        return true;
    }
}
