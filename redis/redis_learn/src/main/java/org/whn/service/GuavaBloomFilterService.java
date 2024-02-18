package org.whn.service;

/**
 * @Author: WangHn
 * @Date: 2024/2/18 16:53
 * @Description: GuavaBloomFilterService
 */
public abstract class GuavaBloomFilterService {
    /**
     * GuavaBloomFilter 初始化
     */
    protected abstract void guavaInit();

    /**
     * GuavaBloomFilter 清除
     */
    public abstract void guavaClear();

    /**
     * GuavaBloomFilter 重启
     */
    public void reload() {
        guavaClear();
        guavaInit();
    }

    /**
     * 测试GuavaBloomFilter 过滤数据
     * @param dataCount 数据数量（单位：w）
     */
    public abstract void testFilterData(Integer dataCount);

    /**
     * 测试指定数据是否存在
     * @param integer 测试数据
     * @return boolean
     */

    public abstract boolean guavaFilter(Integer integer);
}
