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
}
