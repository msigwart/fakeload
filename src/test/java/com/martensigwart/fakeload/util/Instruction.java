package com.martensigwart.fakeload.util;

/**
 * Created by martensigwart on 23.07.17.
 */
public abstract class Instruction {
    private LoadType loadType;
    private int offSet; //in seconds
    private long change;

    Instruction(LoadType loadType, int offSet, long change) {
        if (change <= 0) {
            throw new IllegalArgumentException("change must be a positive value");
        }
        this.loadType = loadType;
        this.offSet = offSet;
        this.change = change;
    }

    public long getChange() {
        return this.change;
    }

    public int getOffSet() {
        return offSet;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public enum  LoadType {
        CPU, MEMORY, DISKIO, NETIO
    }

}

