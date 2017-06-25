package com.zuora.domain;

import com.zuora.application.command.SetupAccessPathsCommand;

/**
 * @author by zy.
 */
public class PathStatisticsFactory {

    public static PathStatistics createBy(SetupAccessPathsCommand command){
        return createBy(command, 3);
    }

    public static PathStatistics createBy(SetupAccessPathsCommand command, int maxSequentialSize){
        return new PathStatistics(command.getUserAccessPaths(), maxSequentialSize);
    }
}
