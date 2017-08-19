package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public final class MemorySimulator extends AbstractLoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(MemorySimulator.class);

    private static final long KB = 1024;
    private static final long MB = KB*1024;
    private static final long GB = MB*1024;


    private long actualLoad;
    private final List<byte[]> allocatedMemory;

    MemorySimulator() {
        super(-1L);
        this.actualLoad = 0L;
        this.allocatedMemory = new ArrayList<>();
    }


    @Override
    void simulateLoad() throws InterruptedException {
        allocatedMemory.clear();

        long loadToAllocate = getLoad();
        if (loadToAllocate < Integer.MAX_VALUE) {
            allocatedMemory.add(new byte[(int)loadToAllocate]);
        } else {
            int modulo = Math.toIntExact(loadToAllocate % Integer.MAX_VALUE);
            int times = Math.toIntExact((loadToAllocate - modulo) / Integer.MAX_VALUE);
            log.trace("modulo: {}, times: {}", modulo, times);

            for (int i = 0; i < times; i++) {
                log.trace("Round {} start", i);
                allocatedMemory.add(new byte[Integer.MAX_VALUE]);
                log.trace("Round {} end", i);
            }

            log.debug("now adding {} bytes", modulo);
            allocatedMemory.add(new byte[modulo]);
        }

        actualLoad = loadToAllocate;
    }

    @Override
    boolean waitConditionFulfilled() {
        return (getLoad() == actualLoad);
    }

    @Override
    String prettyFormat(long load) {
        return mb(load);
    }

    private String mb(long bytes) {
        return String.format("%d (%.2f MB)", bytes, (double)bytes / (MB));
    }

    @Override
    String idString() {
        return "Memory Sim - ";
    }
}
