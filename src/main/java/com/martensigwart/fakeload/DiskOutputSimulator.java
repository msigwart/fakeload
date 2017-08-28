package com.martensigwart.fakeload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class DiskOutputSimulator extends AbstractLoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(DiskOutputSimulator.class);

    private final String filePath;
    private RandomAccessFile file;

    public DiskOutputSimulator(String filePath) throws IOException {
        super(-1, "DiskOutputSim");

        this.filePath = filePath;

        // Create file
        File f = new File(this.filePath);
        if (!f.exists()) {
            //noinspection ResultOfMethodCallIgnored
            f.getParentFile().mkdirs();
            //noinspection ResultOfMethodCallIgnored
            f.createNewFile();
        }

        file = new RandomAccessFile(this.filePath, "rws");
    }

    @Override
    protected void simulateLoad(long load) throws InterruptedException {
        int bytesPerSeconds = (load > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) load;
        long startWrite = System.nanoTime();


        /*
         * A byte array with a length of only a fraction of the bytes to simulate
         * is created because creating a random byte array is expensive.
         * To simulate a certain amount of disk output the same random byte array can
         * be reused, thus no random bytes have to be generated over and over again.
         */
        byte bytes[] = new byte[bytesPerSeconds];
        int iterations = 1;
//        while (bytes.length % 1024 == 0) {
//            bytes = new byte[bytes.length / 1024];
//            iterations *= 1024;
//        }

        // Random byte generation
        long startRandom = System.nanoTime();
        new Random().nextBytes(bytes);      // Create random bytes to write
        long endRandom = System.nanoTime();
        long randomDuration = endRandom - startRandom;

        log.trace("Generated random byte array of length {} for simulation in {} ms",
                bytes.length, TimeUnit.NANOSECONDS.toMillis(randomDuration));


        try {
            file.seek(0);

            for (int i=0; i<iterations; i++) {
                file.write(bytes);
            }
            long endWrite = System.nanoTime();

            long duration = endWrite - startWrite;
            log.trace("Wrote {} bytes in {} ms",
                    iterations*bytes.length, TimeUnit.NANOSECONDS.toMillis(duration));


            long toSleep = TimeUnit.SECONDS.toMillis(1) - TimeUnit.NANOSECONDS.toMillis(duration);

            if (toSleep > 0) {
                System.out.println("Sleeping " + toSleep + "ms");
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
    protected void cleanUp() {
        File f = new File(filePath);
        if (f.delete()) {
            log.trace("File {} successfully deleted", filePath);
        } else {
            log.warn("File {} could not be deleted", filePath);
        }
    }

    @Override
    protected String prettyFormat(long load) {
        return MemoryUnit.mbString(load) + " per second";
    }
}
