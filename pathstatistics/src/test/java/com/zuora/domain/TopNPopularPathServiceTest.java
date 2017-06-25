package com.zuora.domain;

import com.zuora.application.TopNPopularPathService;
import com.zuora.application.impl.ZgbTopNPopularPathServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author by zy.
 */
public class TopNPopularPathServiceTest extends BaseTest{
    private TopNPopularPathService topNPopularPathService;
    private ExecutorService executorService;

    @Before
    public void setup(){
        topNPopularPathService = new ZgbTopNPopularPathServiceImpl();
        topNPopularPathService.setup(setupAccessPathsCommand);

        executorService = new ThreadPoolExecutor(1000,1000,60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100));
    }

    /**
     * 测试多线程环境下添加路径及计算TopN频率
     */
    @Test
    public void testGetTopNWithMultiThread(){
        for (int i=100; i>0; i--) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random(System.currentTimeMillis());
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int n = random.nextInt(10) + 1;
                    topNPopularPathService.appendAccessPath(new String[]{"special", "special"});
                    String[] popularPathGraphs = topNPopularPathService.getTopNPopularPathes(n);
                    /*Stream.of(popularPathGraphs).forEach((p)->{System.out.println(
                            Thread.currentThread().getName()+" "+p);});*/
                    System.out.println(
                            String.format("testGetTopNWithMultiThread expected:%d actual:%d",
                                    n, popularPathGraphs.length));
                    assertTrue(popularPathGraphs.length <= n);
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String[] popularPathGraphs = topNPopularPathService.getTopNPopularPathes(1);
        assertArrayEquals(popularPathGraphs, new String[]{
                "{pathGraph:special->special->special count:98}"
        });
    }
}
