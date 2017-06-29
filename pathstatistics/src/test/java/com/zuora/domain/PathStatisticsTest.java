package com.zuora.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import static org.junit.Assert.assertTrue;

/**
 * @author by zy.
 */
public class PathStatisticsTest extends BaseTest{

    private PathStatistics pathStatistics;

    @Before
    public void setup(){
        pathStatistics = PathStatisticsFactory.createBy(setupAccessPathsCommand);
    }

    /**
     * 测试计算topN频率路经
     */
    @Test
    public void testComputeTopNPopularPathGraph(){
        Random random = new Random(System.currentTimeMillis());
        int n = 0;
        String[] popularPathGraphs = null;
        for (int i=0; i<10; i++) {
            n = random.nextInt(10)+1;
            popularPathGraphs = pathStatistics.computeTopNPopularPathGraph(n);
            Stream.of(popularPathGraphs).forEach(System.out::println);
            assertTrue(popularPathGraphs.length <= n);
            System.out.println(
                    String.format("testComputeTopNPopularPathGraph expected:%d actual:%d",
                            n, popularPathGraphs.length));
        }
    }

    @Test
    public void testComputeTopNPopularPathGraphByQuickSort(){
        Random random = new Random(System.currentTimeMillis());
        int n = 0;
        String[] popularPathGraphs = null;
        for (int i=0; i<10; i++) {
            n = random.nextInt(10)+1;
            popularPathGraphs = pathStatistics.computeTopKPopularPathGraphByQuickSort(n);
            Stream.of(popularPathGraphs).forEach(System.out::println);
            assertTrue(popularPathGraphs.length <= n);
            System.out.println(
                    String.format("testComputeTopNPopularPathGraphByQuickSort expected:%d actual:%d",
                            n, popularPathGraphs.length));
        }
    }


    /**
     * 当topN >= 45时   computeTopKPopularPathGraphByQuickSort占优
     */
    @Test
    public void testComputeTopNPerformance(){
        for (int out = 0; out < 100; out++) {
            int count = 10000;
            int topN = 50;
            String[] popularPathGraphs = null;

            long begin = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                popularPathGraphs = pathStatistics.computeTopNPopularPathGraph(topN);
            }
            long now = System.currentTimeMillis();
            long escapeTime = (now - begin);
            long averageTime = escapeTime / count;


            System.out.println("computeTopNPopularPathGraph escapeTime:" + escapeTime + "ms"
                    + " averageTime:" + averageTime + "ms  "
                    + popularPathGraphs[0] + "|" + popularPathGraphs[popularPathGraphs.length - 1] + "|" + popularPathGraphs.length);

            begin = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                popularPathGraphs = pathStatistics.computeTopKPopularPathGraphByQuickSort(topN);

            }
            now = System.currentTimeMillis();
            escapeTime = (now - begin);
            averageTime = escapeTime / count;
            System.out.println("computeTopKPopularPathGraphByQuickSort escapeTime:" + escapeTime + "ms"
                    + " averageTime:" + averageTime + "ms  "
                    + popularPathGraphs[0] + "|" + popularPathGraphs[popularPathGraphs.length - 1] + "|" + popularPathGraphs.length);
        }
    }

    /**
     * 测试计算TopN级别的频率路经，同一频率会有多个路经。
     */
    @Test
    public void testComputeTopLevelNPopularPathGraph(){
        int level = 4;
        PriorityQueue<PathGraphCounter>  queue = pathStatistics.computeTopLevelNPopularPathGraph(level);
        Set<Integer> levelSet = new HashSet<>(level);
        queue.forEach((pgc)->{
            levelSet.add(pgc.getCount());
            //System.out.println(pgc);
        });
        System.out.println(
                String.format("testComputeTopLevelNPopularPathGraph expected:%d actual:%d",
                        level, levelSet.size()));
        assertTrue(levelSet.size() <= level);
    }
}
