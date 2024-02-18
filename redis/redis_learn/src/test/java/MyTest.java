import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

/**
 * @Author: WangHn
 * @Date: 2024/2/18 16:48
 * @Description: 测试类
 */
public class MyTest {
    @Test
    public void testGuavaWithBloomFilter() {
// 创建布隆过滤器对象
        BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), 100);
// 判断指定元素是否存在
        System.out.println(filter.mightContain(1));
        System.out.println(filter.mightContain(2));
// 将元素添加进布隆过滤器
        filter.put(1);
        filter.put(2);
        System.out.println(filter.mightContain(1));
        System.out.println(filter.mightContain(2));
    }
}
