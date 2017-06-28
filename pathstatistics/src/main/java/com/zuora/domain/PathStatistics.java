package com.zuora.domain;

import com.zuora.ddd.domain.AbstractDomainObject;
import com.zuora.domain.events.SetupAccessPathsDomainEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author by zy.
 */
public class PathStatistics extends AbstractDomainObject {
    private int maxSequentialSize;
    private ConcurrentHashMap<String, Integer> pathFrequency = null;
    private ConcurrentHashMap<String,SizedAccessPaths> userAccessPathMap = null;

    public PathStatistics(String[][] accessPaths, int maxSequentialSize){
        init(accessPaths, maxSequentialSize);
    }

    public void init(String[][] accessPaths, int maxSequentialSize) {
        //pathFrequency = new HashMap<>(accessPaths.length);
        //userAccessPathMap = new HashMap<>();
        pathFrequency = new ConcurrentHashMap<>(accessPaths.length);
        userAccessPathMap = new ConcurrentHashMap<>();
        this.maxSequentialSize = maxSequentialSize;

        Stream.of(accessPaths).forEach((accessPath)->{
            appendAccessPath(accessPath);
        });

        //System.out.println(pathFrequency);

        publishEvent(new SetupAccessPathsDomainEvent(this));
    }

    public void appendAccessPath(String [] accessPath){
        Objects.requireNonNull(pathFrequency, ()->{return "the pathFrequency is null and PathStatistics not be init?";});
        Objects.requireNonNull(userAccessPathMap, ()->{return "the userAccessPathMap is null and PathStatistics not be init?";});

        SizedAccessPaths sizedAccessPathsTmp = userAccessPathMap.compute(accessPath[0],
                (uid, sizedAccessPaths)->{
                    if (sizedAccessPaths == null){
                        sizedAccessPaths = new SizedAccessPaths(uid,maxSequentialSize);
                    }

                    String pathGraphStr = sizedAccessPaths.addAndGenPathGraphStr(accessPath[1]);
                    if (pathGraphStr != null) {
                        pathFrequency.compute(pathGraphStr, (pgStr, pgCnt) -> {
                            if (pgCnt == null)
                                pgCnt = 1;
                            else
                                pgCnt++;
                            return pgCnt;
                        });
                    }

                    return sizedAccessPaths;
                });
    }

    public PriorityQueue<PathGraphCounter> computeTopLevelNPopularPathGraph(int level) {
        Objects.requireNonNull(pathFrequency, ()->{return "the pathFrequency is null and PathStatistics not be init?";});

        PriorityQueue<PathGraphCounter> queue = new PriorityQueue<>(level);
        if (pathFrequency.isEmpty())
            return queue;

        //final int[] frequencyLevel = {level};
        Set<Integer> levelSet = new HashSet<>(level);
        pathFrequency.forEach((pathGraph, pathGraphCnt)->{
            if (levelSet.size() < level){
                queue.add(new PathGraphCounter(pathGraph, pathGraphCnt));
                levelSet.add(pathGraphCnt);
            }else {
                int minCnt = queue.peek().getCount();
                if (levelSet.contains(pathGraphCnt))
                    queue.add(new PathGraphCounter(pathGraph, pathGraphCnt));
                else if (pathGraphCnt > minCnt){
                    queue.add(new PathGraphCounter(pathGraph, pathGraphCnt));
                    levelSet.add(pathGraphCnt);
                    while (queue.peek().getCount() == minCnt)
                        queue.poll();
                    levelSet.remove(minCnt);
                }
            }
        });

        return queue;
    }

    public String[] computeTopLevelNPopularPathGraphArr(int level) {
        PriorityQueue<PathGraphCounter> queue = computeTopLevelNPopularPathGraph(level);
        String res[] = new String[queue.size()];
        for (int i=0; !queue.isEmpty(); i++){
            PathGraphCounter pathGraphCounter = queue.remove();
            //res[i] = pathGraphCounter.getPathGraph();
            /*res[i] = String.format("%s %d",
                    pathGraphCounter.getPathGraph(),
                    pathGraphCounter.getCount());*/
            res[i] = pathGraphCounter.toString();
        }

        return res;
    }

    public String[] computeTopNPopularPathGraph(int n) {
        Objects.requireNonNull(pathFrequency, ()->{return "the pathFrequency is null and PathStatistics not be init?";});

        if (pathFrequency.isEmpty())
            return new String[0];

        PriorityQueue<PathGraphCounter> queue = new PriorityQueue<>(n);
        pathFrequency.forEach((pathGraph, pathGraphCnt)->{
            if (queue.size() == n){
                int minCnt = queue.peek().getCount();
                if (pathGraphCnt > minCnt){
                    queue.remove();
                    queue.add(new PathGraphCounter(pathGraph, pathGraphCnt));
                }
            }else {
                queue.add(new PathGraphCounter(pathGraph, pathGraphCnt));
            }
        });

        String res[] = new String[queue.size()];
        for (int i=0; !queue.isEmpty(); i++){
            PathGraphCounter pathGraphCounter = queue.remove();
            //res[i] = pathGraphCounter.getPathGraph();
            /*res[i] = String.format("%s %d",
                    pathGraphCounter.getPathGraph(),
                    pathGraphCounter.getCount());*/
            res[i] = pathGraphCounter.toString();
        }

        return res;
    }


    public String[] computeTopNPopularPathGraphArr(int k) {
        /*PathGraphCounter[] pathGraphCounters = new PathGraphCounter[pathFrequency.size()];
        final int[] i = {0};
        pathFrequency.forEach((pathGraph, pathGraphCnt)->{
            pathGraphCounters[i[0]++]=new PathGraphCounter(pathGraph, pathGraphCnt);
        });*/
        PathGraphCounter[]  pathGraphCounters = new PathGraphCounter[8];
        pathGraphCounters[0] = new PathGraphCounter("path1", 1);
        pathGraphCounters[1] = new PathGraphCounter("path3", 3);
        pathGraphCounters[2] = new PathGraphCounter("path2", 2);
        pathGraphCounters[3] = new PathGraphCounter("path6", 6);
        pathGraphCounters[4] = new PathGraphCounter("path8", 8);
        pathGraphCounters[5] = new PathGraphCounter("path4", 4);
        pathGraphCounters[6] = new PathGraphCounter("path7", 7);
        pathGraphCounters[7] = new PathGraphCounter("path5", 5);



        System.out.println(pathGraphCounters.length);

        quickTopK(pathGraphCounters, 0, pathGraphCounters.length, k);

        return null;
    }

    private PathGraphCounter[] quickTopK(PathGraphCounter[] pathGraphCounters, int begin, int end, int k){
        assert begin <= k;
        assert end >= k;

        PathGraphCounter priovit = pathGraphCounters[k];
        int idx = partionit(pathGraphCounters, begin, end, priovit);
        /*while (idx != k){
            idx = partionit(pathGraphCounters, begin, end, priovit);
        }*/

        return null;
    }

    private int partionit(PathGraphCounter[] pathGraphCounters, int low, int high, PathGraphCounter provit){
        int lowI = low;
        int highI = high - 1;

        while (true){
            while (pathGraphCounters[lowI].getCount() < provit.getCount()){
                lowI++;
            }
            while (highI > 0 && pathGraphCounters[highI].getCount() > provit.getCount()){
                highI--;
            }

            if (lowI >= highI){
                break;
            }else {
                swap(pathGraphCounters, lowI, highI);
            }
        }
        swap(pathGraphCounters, lowI, highI);

        return lowI;
    }

    private void swap(PathGraphCounter[] pathGraphCounters, int f, int t){
        PathGraphCounter tmp = pathGraphCounters[f];
        pathGraphCounters[f] = pathGraphCounters[t];
        pathGraphCounters[t] = tmp;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pathFrequency:").append(pathFrequency);
                //.append(" \n userAccessPathMap:").append(userAccessPathMap);
        return stringBuilder.toString();
    }
}