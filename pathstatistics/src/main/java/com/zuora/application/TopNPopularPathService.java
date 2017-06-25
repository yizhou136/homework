package com.zuora.application;

import com.zuora.application.command.SetupAccessPathsCommand;

/**
 * @author by zy.
 */
public interface TopNPopularPathService {

    void setup(SetupAccessPathsCommand command);

    void appendAccessPath(String[] accessPath);

    String[] getTopNPopularPathes(int n);
}
