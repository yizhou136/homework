package com.zuora.application.impl;

import com.zuora.application.TopNPopularPathService;
import com.zuora.application.command.SetupAccessPathsCommand;
import com.zuora.domain.PathGraphCounter;
import com.zuora.domain.PathStatistics;
import com.zuora.domain.PathStatisticsFactory;
import com.zuora.domain.SizedAccessPaths;

import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Stream;

/**
 * @author by zy.
 */
public class ZgbTopNPopularPathServiceImpl implements TopNPopularPathService{
    private PathStatistics pathStatistics;

    public void setup(SetupAccessPathsCommand command) {
        pathStatistics = PathStatisticsFactory.createBy(command);
    }

    @Override
    public void appendAccessPath(String[] accessPath) {
        pathStatistics.appendAccessPath(accessPath);
        //System.out.println(Thread.currentThread().getName()+" appendAccessPath");
    }

    public String[] getTopNPopularPathes(int n) {
        Objects.requireNonNull(pathStatistics, "pathStatistics is null");

        return pathStatistics.computeTopNPopularPathGraph(n);
    }
}