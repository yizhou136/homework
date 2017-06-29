package com.zuora.domain;

import java.util.Objects;

/**
 * @author by zy.
 */
public class PathGraphCounter implements Comparable<PathGraphCounter>{
    private String pathGraph;
    private int count;

    public PathGraphCounter(String pathGraph, int count) {
        this.pathGraph = pathGraph;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPathGraph() {
        return pathGraph;
    }

    public void setPathGraph(String pathGraph) {
        this.pathGraph = pathGraph;
    }


    @Override
    public int compareTo(PathGraphCounter other) {
        int lastComparison = 0;
        lastComparison = Integer.valueOf(getCount()).compareTo(other.getCount());
        if (lastComparison != 0) {
            return lastComparison * -1;
        }

        return 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{pathGraph:").append(pathGraph)
                .append(" count:").append(count)
                .append("}");
        return stringBuilder.toString();
    }

    public static final void insertSort(PathGraphCounter[] pathGraphCounters, int from, int to){
        for (int out = from; out < to; out++){
            int in = out+1;
            while (in > from && pathGraphCounters[in].getCount() > pathGraphCounters[in-1].getCount()){
                swap(pathGraphCounters, in, in-1);
                in--;
            }
        }
    }

    public static final void quickSort(PathGraphCounter[] pathGraphCounters, int low, int high){
        if (high > pathGraphCounters.length){
            throw new IllegalArgumentException("high("+high+") > length("+pathGraphCounters.length+")");
        }

        if (high > low){
            PathGraphCounter privot = pathGraphCounters[high];
            int mid = partitionIt(pathGraphCounters, low, high, privot);
            //if (mid <= low) return;
            quickSort(pathGraphCounters, low, mid-1);
            quickSort(pathGraphCounters, mid+1, high);
        }
    }

    public static final int partitionIt(PathGraphCounter[] pathGraphCounters, int low, int high, PathGraphCounter privot){
        if (low > high){
            throw new IllegalArgumentException("low("+low+") > high("+high+")");
        }
        if (high > pathGraphCounters.length){
            throw new IllegalArgumentException("high("+high+") > length("+pathGraphCounters.length+")");
        }
        //if (low == high) return low;

        int lowI = low - 1;
        int highI = high;

        int sequenceCnt = 0;

        while (true){
            while (pathGraphCounters[++lowI].getCount() > privot.getCount());
            while (highI > lowI && pathGraphCounters[--highI].getCount() < privot.getCount()){
                /*if (pathGraphCounters[highI].getCount() == privot.getCount())
                    sequenceCnt++;
                else
                    sequenceCnt = 0;*/
            }

            if (lowI >= highI){
                /*if (sequenceCnt > 0 && sequenceCnt == (high - 1 - lowI))
                    return -1;*/
                break;
            }else {
                swap(pathGraphCounters, lowI, highI);
            }
        }

        swap(pathGraphCounters, lowI, high);

        return lowI;
    }

    public static final void swap(PathGraphCounter[] pathGraphCounters, int f, int t){
        if (f != t) {
            PathGraphCounter tmp = pathGraphCounters[f];
            pathGraphCounters[f] = pathGraphCounters[t];
            pathGraphCounters[t] = tmp;
        }
    }
}
