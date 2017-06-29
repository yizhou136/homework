package com.zuora.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * @author by zy.
 */
public class PathGraphCounterTest{

    private static PathGraphCounter[] pathGraphCounters;

    private static void initArr(){
        Random random = new Random(System.currentTimeMillis());
        pathGraphCounters = new PathGraphCounter[random.nextInt(100)];
        for(int i=0;i<pathGraphCounters.length;i++){
            pathGraphCounters[i] = new PathGraphCounter("aa",random.nextInt(50));
        }
        /*pathGraphCounters = new PathGraphCounter[5];
        pathGraphCounters[0] = new PathGraphCounter("path1", 5);
        pathGraphCounters[1] = new PathGraphCounter("path3", 4);
        pathGraphCounters[2] = new PathGraphCounter("path2", 3);
        pathGraphCounters[3] = new PathGraphCounter("path6", 4);
        pathGraphCounters[4] = new PathGraphCounter("path8", 2);*/
        /*pathGraphCounters[5] = new PathGraphCounter("path4", 1);
        //pathGraphCounters[6] = new PathGraphCounter("path7", 7);
        pathGraphCounters[6] = new PathGraphCounter("path7", 1);
        pathGraphCounters[7] = new PathGraphCounter("path5", 1);
        pathGraphCounters[8] = new PathGraphCounter("path8", 2);*/


        printArr();
    }

    private static void printArr(){
        for (PathGraphCounter pathGraphCounter : pathGraphCounters){
            System.out.print(pathGraphCounter.getCount());
            System.out.print(",");
        }
        System.out.println();
    }

    @Test
    public void testSort(){
        for (int i=0; i<1000; i++) {
            initArr();
            PathGraphCounter.quickSort(pathGraphCounters, 0, pathGraphCounters.length - 1);
            printArr();
            verifySort();

            initArr();
            PathGraphCounter.insertSort(pathGraphCounters, 0, pathGraphCounters.length - 1);
            printArr();
            verifySort();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void verifySort(){
        int mustbe = 0;

        for (int i = 0; i < pathGraphCounters.length-1; i++) {
            PathGraphCounter prevPathGraphCounter = pathGraphCounters[i];
            PathGraphCounter nextPathGraphCounter = pathGraphCounters[i+1];
            int nowCompare = (prevPathGraphCounter.compareTo(nextPathGraphCounter));
            //System.out.println("i:"+i+" mustbe:"+mustbe+" nowCompare:"+nowCompare);
            if (nowCompare == 0) continue;
            if (mustbe == 0)
                mustbe = nowCompare;
            Assert.assertTrue(mustbe == nowCompare);
        }
    }
}
