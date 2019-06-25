package com.jing.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author RanMoAnRan
 * @ClassName: NewsCacheTest
 * @projectName gossip-parent
 * @description: TODO
 * @date 2019/6/16 21:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-service.xml")
public class NewsCacheTest {
    @Autowired
    private NewsCache newsCache;
    @Test
    public void cacheTest() throws Exception {
        newsCache.cacheNews("林志玲");
    }
}
