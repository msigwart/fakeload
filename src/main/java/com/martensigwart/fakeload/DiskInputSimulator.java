package com.martensigwart.fakeload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

public class DiskInputSimulator extends AbstractLoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(DiskInputSimulator.class);
    private RandomAccessFile file;

    /**
     *
     * @param filePath path to the file for simulating disk input (should be bigger than amount of available RAM)
     * @throws FileNotFoundException if no file with the specified file path can be opened
     */
    DiskInputSimulator(String filePath) throws FileNotFoundException {
        super(-1);
        file = new RandomAccessFile(filePath, "r");
    }

    @Override
    public void simulateLoad(long load) throws InterruptedException {
        byte bytes[] = new byte[(load > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)load];

        try {
            try {

                long startRead = System.nanoTime();
                file.read(bytes);
                long endRead = System.nanoTime();

                long duration = endRead - startRead;

                log.trace("Read {} in {} ms",
                        MemoryUnit.mbString(bytes.length), TimeUnit.NANOSECONDS.toMillis(duration));

                long toSleep = TimeUnit.SECONDS.toMillis(1) - TimeUnit.NANOSECONDS.toMillis(duration);

                if (toSleep > 0) {
                    log.trace("Sleeping {} ms", toSleep);
                    Thread.sleep(toSleep);
                }

            } catch (EOFException e) {
                log.debug("End of file reached: Resetting file pointer");
                file.seek(0);
            }

        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public boolean waitConditionFulfilled() {
        return (getLoad() == 0);
    }

    @Override
    protected String prettyFormat(long load) {
        return MemoryUnit.mbString(load);
    }

    @Override
    protected void cleanUp() {
        try {
            file.close();
        } catch (IOException e) {
            log.error("Failed to close file: {}", e.getMessage());
        }
    }

    @Override
    protected String idString() {
        return "DiskInputSim - ";
    }
}
