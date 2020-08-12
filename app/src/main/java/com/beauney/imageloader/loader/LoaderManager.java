package com.beauney.imageloader.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zengjiantao
 * @since 2020-08-12
 */
public class LoaderManager {

    private static LoaderManager sInstance = new LoaderManager();

    private Map<String, Loader> mLoaderMap = new HashMap<>();

    private NullLoader mNullLoader = new NullLoader();


    private LoaderManager() {
        register("http", new UrlLoader());
        register("https", new UrlLoader());
        register("file", new LocalLoader());
    }

    public static LoaderManager getInstance() {
        return sInstance;
    }

    public Loader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return mNullLoader;
    }

    private final void register(String key, Loader loader) {
        mLoaderMap.put(key, loader);
    }
}
