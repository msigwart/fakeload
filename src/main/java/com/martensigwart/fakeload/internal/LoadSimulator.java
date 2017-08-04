package com.martensigwart.fakeload.internal;

/**
 * Created by martensigwart on 03.08.17.
 */
public interface LoadSimulator extends Runnable {

    long getLoad();
    void setLoad(long desiredLoad);
    void increaseLoad(long delta);
    void decreaseLoad(long delta);

}
