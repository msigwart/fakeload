package com.martensigwart.fakeload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Skeleton implementation of a {@link LoadSimulator} that simulates disk input.
 *
 * Disk input is simulated by reading an amount of bytes from a file.
 */
public abstract class DiskInputSimulator extends AbstractLoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(DiskInputSimulator.class);


    protected DiskInputSimulator() {
        this("DiskInputSim");
    }

    protected DiskInputSimulator(String name) {
        super(-1, name);
    }

    /**
     * Reads {@code bytes.length} bytes from file.
     * @param bytes bytes to read
     * @throws IOException if an IOException occurs while reading
     */
    protected abstract void read(byte[] bytes) throws IOException;

    @Override
    public void simulateLoad(long load) throws InterruptedException {
        byte bytes[] = new byte[(load > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)load];

        try {
            long startRead = System.nanoTime();
            read(bytes);
            long endRead = System.nanoTime();
            long duration = endRead - startRead;

            log.trace("Read {} in {} ms",
                    MemoryUnit.mbString(bytes.length), TimeUnit.NANOSECONDS.toMillis(duration));

            long toSleep = TimeUnit.SECONDS.toMillis(1) - TimeUnit.NANOSECONDS.toMillis(duration);

            if (toSleep > 0) {
                log.trace("Sleeping {} ms", toSleep);
                Thread.sleep(toSleep);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean waitConditionFulfilled() {
        return isZeroLoad();
    }

    @Override
    protected String prettyFormat(long load) {
        return MemoryUnit.mbString(load) + " per second";
    }



}
