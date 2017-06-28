package com.zuora.domain;

import org.junit.Before;
import org.junit.Test;

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
            //Stream.of(popularPathGraphs).forEach(System.out::println);
            assertTrue(popularPathGraphs.length <= n);
            System.out.println(
                    String.format("testComputeTopNPopularPathGraph expected:%d actual:%d",
                            n, popularPathGraphs.length));
        }
    }

    @Test
    public void testComputeTopNPopularPathGraphByQuickSort(){
        int n = 4;
        String[] popularPathGraphs = pathStatistics.computeTopKPopularPathGraphByQuickSort(n);
        if (popularPathGraphs == null) return;
        Stream.of(popularPathGraphs).forEach(System.out::println);
        assertTrue(popularPathGraphs.length <= n);
        System.out.println(
                String.format("testComputeTopNPopularPathGraph expected:%d actual:%d",
                        n, popularPathGraphs.length));
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
