package com.martensigwart.fakeload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Skeleton implementation of a {@link LoadSimulator} that simulates disk output
 *
 * Disk output is simulated by writing an amount of bytes to a file.
 *
 */
public abstract class DiskOutputSimulator extends AbstractLoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(DiskOutputSimulator.class);

    protected DiskOutputSimulator() {
        this("DiskOutputSim");
    }

    protected DiskOutputSimulator(String name) {
        super(-1, name);
    }

    /**
     * Writes the specified bytes to a file
     * @param bytes the bytes to write
     * @throws IOException when an IOException occurs while writing the bytes
     */
    protected abstract void write(byte[] bytes) throws IOException;

    @Override
    protected void simulateLoad(long load) throws InterruptedException {

        try {
            long startWrite = System.nanoTime();
            int bytesPerSeconds = (load > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) load;
            byte bytes[] = new byte[bytesPerSeconds];

            write(bytes);
            long endWrite = System.nanoTime();

            long duration = endWrite - startWrite;
            log.trace("Wrote {} bytes in {} ms",
                    bytes.length, TimeUnit.NANOSECONDS.toMillis(duration));


            long toSleep = TimeUnit.SECONDS.toMillis(1) - TimeUnit.NANOSECONDS.toMillis(duration);

            if (toSleep > 0) {
                log.trace("Sleeping " + toSleep + "ms");
                Thread.sleep(toSleep);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean waitConditionFulfilled() {
        return isZeroLoad();
    }



    @Override
    protected String prettyFormat(long load) {
        return MemoryUnit.mbString(load) + " per second";
    }
}
