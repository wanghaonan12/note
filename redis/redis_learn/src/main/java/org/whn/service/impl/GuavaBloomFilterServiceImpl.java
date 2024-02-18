package org.whn.service.impl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.whn.service.GuavaBloomFilterService;

/**
 * @Author: WangHn
 * @Date: 2024/2/18 16:53
 * @Description: GuavaBloomFilterService实现类
 */
@Slf4j
@Service
public class GuavaBloomFilterServiceImpl extends GuavaBloomFilterService {
    public static final int _1W = 10000;
    //布隆过滤器里预计要插入多少数据
    public static int size = 100 * _1W;
    //误判率,它越小误判的个数也就越少(思考，是不是可以设置的无限小，没有误判岂不更好)
    //fpp the desired false positive probability
    public static double fpp = 0.03;
    // 构建布隆过滤器
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size,fpp);

    @Override
    protected void guavaInit() {
        //1 先往布隆过滤器里面插入100万的样本数据
        for (int i = 1; i <=size; i++) {
            bloomFilter.put(i);
        }
    }

    @Override
    public void guavaClear() {
        bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, fpp);
    }

    @Override
    public boolean guavaFilter(Integer integer){
        return bloomFilter.mightContain(integer);
    }

    @Override
    public void testFilterData(Integer dataCount){
        int count = 0;
        for (int i = 0; i < dataCount*_1W; i++) {
            boolean b = bloomFilter.mightContain(i);
            if(!b) {
                count++;
                log.warn("Guava data :{},is don`t exit ",i);
            }
        }
        System.out.println(dataCount*_1W+"数据中共："+count+"不存在！");
    }
}
