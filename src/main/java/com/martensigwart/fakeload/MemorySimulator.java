package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public final class MemorySimulator extends AbstractLoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(MemorySimulator.class);

    private long actualLoad;
    private final List<byte[]> allocatedMemory;

    public MemorySimulator() {
        super(-1L, "MemorySim");
        this.actualLoad = 0L;
        this.allocatedMemory = new ArrayList<>();
    }


    @Override
    protected void simulateLoad(long loadToAllocate) throws InterruptedException {
        allocatedMemory.clear();

        if (loadToAllocate < Integer.MAX_VALUE) {
            allocatedMemory.add(new byte[(int)loadToAllocate]);
        } else {
            int modulo = Math.toIntExact(loadToAllocate % Integer.MAX_VALUE);
            int times = Math.toIntExact((loadToAllocate - modulo) / Integer.MAX_VALUE);
            log.trace("Modulo: {}, times: {}", modulo, times);

            for (int i = 0; i < times; i++) {
                log.trace("Round {} start", i);
                allocatedMemory.add(new byte[Integer.MAX_VALUE]);
                log.trace("Round {} end", i);
            }

            log.debug("Now adding {} bytes", modulo);
            allocatedMemory.add(new byte[modulo]);
        }

        actualLoad = loadToAllocate;
    }

    @Override
    protected boolean waitConditionFulfilled() {
        return (getLoad() == actualLoad);
    }

    @Override
    protected String prettyFormat(long load) {
        return MemoryUnit.mbString(load);
    }

    @Override
    protected void cleanUp() {
        allocatedMemory.clear();
    }

}
