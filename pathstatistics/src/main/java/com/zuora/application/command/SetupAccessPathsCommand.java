package com.zuora.application.command;

/**
 * @author by zy.
 */
public class SetupAccessPathsCommand {

    private String[][] userAccessPaths;

    public SetupAccessPathsCommand(String[][] userAccessPaths){
        this.userAccessPaths = userAccessPaths;
    }

    public String[][] getUserAccessPaths() {
        return userAccessPaths;
    }

    public void setUserAccessPaths(String[][] userAccessPaths) {
        this.userAccessPaths = userAccessPaths;
    }
}