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
    protected int maxSequentialSize;
    protected ConcurrentHashMap<String,SizedAccessPaths> userAccessPathMap = null;

    private ConcurrentHashMap<String, Integer> pathFrequency = null;

    public PathStatistics(String[][] accessPaths, int maxSequentialSize){
        init(accessPaths, maxSequentialSize);
    }

    public void init(String[][] accessPaths, int maxSequentialSize) {
        //pathFrequency = new HashMap<>(accessPaths.length);
        //userAccessPathMap = new HashMap<>();
        initPathFrequencyContainer(accessPaths);

        userAccessPathMap = new ConcurrentHashMap<>();
        this.maxSequentialSize = maxSequentialSize;

        Stream.of(accessPaths).forEach((accessPath)->{
            appendAccessPath(accessPath);
        });

        //System.out.println(pathFrequency);

        publishEvent(new SetupAccessPathsDomainEvent(this));
    }

    protected void initPathFrequencyContainer(String[][] accessPaths){
        pathFrequency = new ConcurrentHashMap<>(accessPaths.length);
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
            res[i] = pathGraphCounter.toString();
        }

        return res;
    }

    public String[] computeTopKPopularPathGraphByQuickSort(int k) {
        Objects.requireNonNull(pathFrequency, ()->{return "the pathFrequency is null and PathStatistics not be init?";});

        if (pathFrequency.isEmpty())
            return new String[0];

        PathGraphCounter[] pathGraphCounters = new PathGraphCounter[pathFrequency.size()];
        final int[] i = {0};
        pathFrequency.forEach((pathGraph, pathGraphCnt)->{
            pathGraphCounters[i[0]++] = new PathGraphCounter(pathGraph, pathGraphCnt);
        });


        quickSortForTopK(pathGraphCounters,k);
        //PathGraphCounter.insertSort(pathGraphCounters, 0, pathGraphCounters.length-1);
        //Arrays.sort(pathGraphCounters);
        //Stream.of(pathGraphCounters).forEach((e)->{System.out.println(e.getCount());});

        int len = Math.min(pathGraphCounters.length, k);
        String res[] = new String[len];
        for (int j=0; j < res.length; j++){
            PathGraphCounter pathGraphCounter = pathGraphCounters[j];
            res[j] = pathGraphCounter.toString();
        }

        return res;
    }

    private void quickSortForTopK(PathGraphCounter[] pathGraphCounters, int k){
        int low = 0;
        int high = pathGraphCounters.length - 1;
        int latch = 20;
        int sortLen = high;

        if (k <= (high-latch)) {
            PathGraphCounter privot = pathGraphCounters[high];
            int mid = PathGraphCounter.partitionIt(pathGraphCounters, low, high, privot);
            while (mid != k && mid >= 0) {
                if (mid < k) {
                    low = mid + 1;
                    privot = pathGraphCounters[high];
                    mid = PathGraphCounter.partitionIt(pathGraphCounters, low, high, privot);
                } else {
                    high = mid - 1;
                    privot = pathGraphCounters[high];
                    mid = PathGraphCounter.partitionIt(pathGraphCounters, low, high, privot);
                }
            }
            sortLen = k;
        }
        //the best way maybe used by insert sort
        //Arrays.sort(pathGraphCounters, 0, tmpK);
        PathGraphCounter.quickSort(pathGraphCounters, 0, sortLen);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pathFrequency:").append(pathFrequency);
                //.append(" \n userAccessPathMap:").append(userAccessPathMap);
        return stringBuilder.toString();
    }
}