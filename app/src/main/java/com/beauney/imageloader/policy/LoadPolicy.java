package com.beauney.imageloader.policy;

import com.beauney.imageloader.request.BitmapRequest;

/**
 * 加载策略
 *
 * @author zengjiantao
 * @since 2020-08-10
 */
public interface LoadPolicy {
    /**
     * 两个BitmapRequest进行比较
     *
     * @param request1
     * @param request2
     * @return 小于0，request1 < request2，大于0，request1 > request2，等于
     */
    int compareTo(BitmapRequest request1, BitmapRequest request2);
}
