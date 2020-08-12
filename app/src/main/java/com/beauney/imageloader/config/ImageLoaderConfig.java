package com.beauney.imageloader.config;

import com.beauney.imageloader.cache.BitmapCache;
import com.beauney.imageloader.cache.NoCache;
import com.beauney.imageloader.policy.LoadPolicy;
import com.beauney.imageloader.policy.SerialPolicy;

/**
 * 图片加载框架全局配置文件
 *
 * @author zengjiantao
 * @since 2020-08-10
 */
public class ImageLoaderConfig {
    //缓存策略
    private BitmapCache mBitmapCache = new NoCache();
    //加载策略
    private LoadPolicy mLoadPolicy = new SerialPolicy();
    //现场个数
    //Java虚拟机可用处理器个数
    private int mThreadCount = Runtime.getRuntime().availableProcessors();
    //图片加载显示配置
    private DisplayConfig mDisplayConfig = new DisplayConfig();

    private ImageLoaderConfig() {
    }

    public static class Builder {

        private BitmapCache mBitmapCache = new NoCache();

        private LoadPolicy mLoadPolicy = new SerialPolicy();

        private int mThreadCount = Runtime.getRuntime().availableProcessors();

        private DisplayConfig mDisplayConfig = new DisplayConfig();

        /**
         * 设置换成策略
         *
         * @param bitmapCache
         * @return
         */
        public Builder setBitmapCache(BitmapCache bitmapCache) {
            mBitmapCache = bitmapCache;
            return this;
        }

        /**
         * 设置加载策略
         *
         * @param loadPolicy
         * @return
         */
        public Builder setLoadPolicy(LoadPolicy loadPolicy) {
            mLoadPolicy = loadPolicy;
            return this;
        }

        /**
         * 设置线程个数
         *
         * @param threadCount
         * @return
         */
        public Builder setThreadCount(int threadCount) {
            mThreadCount = threadCount;
            return this;
        }

        /**
         * 设置图片加载中显示的图片
         *
         * @param resId
         * @return
         */
        public Builder setLoadingPlaceHolder(int resId) {
            mDisplayConfig.mLoadingImage = resId;
            return this;
        }

        /**
         * 设置图片加载失败后显示的图片
         *
         * @param resId
         * @return
         */
        public Builder setFailedPlaceHolder(int resId) {
            mDisplayConfig.mFailedImage = resId;
            return this;
        }

        public ImageLoaderConfig build() {
            ImageLoaderConfig config = new ImageLoaderConfig();
            config.mBitmapCache = mBitmapCache;
            config.mDisplayConfig = mDisplayConfig;
            config.mThreadCount = mThreadCount;
            config.mLoadPolicy = mLoadPolicy;
            return config;
        }
    }

    public BitmapCache getBitmapCache() {
        return mBitmapCache;
    }

    public LoadPolicy getLoadPolicy() {
        return mLoadPolicy;
    }

    public int getThreadCount() {
        return mThreadCount;
    }

    public DisplayConfig getDisplayConfig() {
        return mDisplayConfig;
    }
}
