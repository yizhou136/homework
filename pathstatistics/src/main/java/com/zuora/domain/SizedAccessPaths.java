package com.zuora.domain;

import java.util.LinkedList;

/**
 * @author by zy.
 */
public class SizedAccessPaths {
    private String uid;
    private LinkedList<String> paths;
    private int maxSize;

    public SizedAccessPaths(String uid, int maxSize){
        this.uid = uid;
        this.paths = new LinkedList<String>();
        this.maxSize = maxSize;
    }

    public String addAndGenPathGraphStr(String path){
        paths.add(path);
        int newSize = paths.size();
        if (newSize < maxSize) {
            return null;
        }

        if (newSize == maxSize+1){
            paths.removeFirst();
        }
        return String.join("->", paths);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SizedAccessPaths other = (SizedAccessPaths) obj;
        if (uid == null) {
            if (other.uid != null)
                return false;
        } else if (!uid.equals(other.uid))
            return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("uid:").append(uid)
                .append("  maxSize:").append(maxSize);
        return stringBuilder.toString();
    }
}
