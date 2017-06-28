package com.zuora.domain;

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
            return lastComparison;
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


    public static final void quickSort(PathGraphCounter[] pathGraphCounters, int low, int high){
        if (high > low){
            PathGraphCounter privot = pathGraphCounters[high];
            int mid = partitionIt(pathGraphCounters, low, high, privot);
            quickSort(pathGraphCounters, low, mid-1);
            quickSort(pathGraphCounters, mid+1, high);
        }
    }

    public static final int partitionIt(PathGraphCounter[] pathGraphCounters, int low, int high, PathGraphCounter privot){
        int lowI = low;
        int highI = high-1;

        while (true){
            while (lowI<high && pathGraphCounters[lowI].getCount() >= privot.getCount()){
                lowI++;
            }
            while (highI > 0 && pathGraphCounters[highI].getCount() < privot.getCount()){
                highI--;
            }

            if (lowI >= highI){
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
