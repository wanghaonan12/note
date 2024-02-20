package org.whn.canal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 10:17
 * @Description:
 */
@SpringBootTest
class RedisCanalClientExampleTest {

    @Autowired
    private RedisCanalClientExample redisCanalClientExample;
    @Test
    void canalExecution() {
        redisCanalClientExample.canalExecution();
    }
}