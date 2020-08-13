package com.beauney.imageloader.cache;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.beauney.imageloader.disk.DiskLruCache;
import com.beauney.imageloader.disk.IOUtil;
import com.beauney.imageloader.request.BitmapRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 硬盘缓存（支持LRU算法）
 *
 * @author zengjiantao
 * @since 2020-08-10
 */
public class DiskCache implements BitmapCache {
    private static final int MB = 1024 * 1024;

    private static DiskCache sInstance;

    private String mCacheDir = "image_cache";

    private DiskLruCache mDiskLruCache;

    private DiskCache(Context context) {
        initDiskCache(context);
    }

    public static DiskCache getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DiskCache.class) {
                if (sInstance == null) {
                    sInstance = new DiskCache(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        DiskLruCache.Editor editor = null;
        OutputStream os = null;

        try {
            editor = mDiskLruCache.edit(request.getImageUriMD5());
            os = editor.newOutputStream(0);
            if (persistBitmap2disk(bitmap, os)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(os);
        }
        Log.d("Debug", "put in DiskCache:" + request.getImageUri());
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        InputStream is = null;

        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(request.getImageUriMD5());
            if (snapshot != null) {
                is = snapshot.getInputStream(0);
                Log.d("jason", "get from DiskCache:" + request.getImageUri());
                return BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(is);
        }
        return null;
    }

    @Override
    public void remove(BitmapRequest request) {
        try {
            mDiskLruCache.remove(request.getImageUriMD5());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 持久化Bitmap对象到Disk
     *
     * @param bitmap
     * @param os
     * @return
     */
    private boolean persistBitmap2disk(Bitmap bitmap, OutputStream os) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(os);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtil.closeQuietly(bos);
        }
        return true;
    }

    private void initDiskCache(Context context) {
        File directory = getDiskCacheDir(mCacheDir, context);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            //初始化
            //1 每次缓存一个图片
            //50 MB 最大值
            mDiskLruCache = DiskLruCache.open(directory, getAppVersion(context), 1, 50 * MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用的版本号
     *
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取缓存路径
     *
     * @param mCacheDir
     * @param context
     * @return
     */
    private File getDiskCacheDir(String mCacheDir, Context context) {
        //相对路径
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //外部存储
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //内部存储
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separatorChar + mCacheDir);
    }
}
