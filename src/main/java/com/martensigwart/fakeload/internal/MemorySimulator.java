package com.martensigwart.fakeload.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martensigwart on 17.05.17.
 */
public class MemorySimulator implements LoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(MemorySimulator.class);

    private static final long KB = 1024;
    private static final long MB = KB*1024;
    private static final long GB = MB*1024;

    private static final long OUT_OF_MEMORY_SAFETYNET = 100*MB;

    private long desiredLoad;
    private long actualLoad;
    private final List<byte[]> allocatedMemory;
    private final Object lock;

    MemorySimulator() {
        this.desiredLoad = 0L;
        this.actualLoad = 0L;
        this.allocatedMemory = new ArrayList<>();
        this.lock = new Object();
    }


    @Override
    public void run() {

        boolean running = true;
        while (running) {
            try {
                synchronized (lock) {
                    while (actualLoad == getLoad()) {
                        lock.wait();
                    }
                }

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

            } catch (InterruptedException e) {
                running = false;
                e.printStackTrace();
            }
        }
//        long freeMemory = Runtime.getRuntime().freeMemory();
//        long totalMemory = Runtime.getRuntime().totalMemory();
//        long maxMemory = Runtime.getRuntime().maxMemory();
//
//        long usedMemory = totalMemory - freeMemory;
//        long availableMemory = maxMemory - usedMemory;
//
//        log.info("Max Memory: {} Total Memory: {}, Free Memory: {}, Available Memory {}",
//                mb(maxMemory),
//                mb(totalMemory),
//                mb(freeMemory),
//                mb(availableMemory));
//
//        long desiredMemory = desiredLoad;
//
//
//        if (desiredMemory >= maxMemory - OUT_OF_MEMORY_SAFETYNET) {
//            log.warn("Not enough memory for memory simulation of {} bytes (max: {})", mb(desiredMemory), mb(maxMemory));
//            // do something, maybe throw exception? Or simply return?
//        }
//
//        long missingMemory = desiredMemory - usedMemory;
//
//        log.info("Desired Memory: {}, Used Memory: {}, Missing Memory: {}", mb(desiredMemory), mb(usedMemory), mb(missingMemory));

//        System.out.println("Runtime max: " + mb(Runtime.getRuntime().maxMemory()));
//        MemoryMXBean m = ManagementFactory.getMemoryMXBean();
//
//        System.out.println("Non-heap: " + mb(m.getNonHeapMemoryUsage().getMax()));
//        System.out.println("Heap: " + mb(m.getHeapMemoryUsage().getMax()));
//
//        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
//            System.out.println("Pool: " + mp.getName() +
//                    " (type " + mp.getType() + ")" +
//                    " = " + mb(mp.getUsage().getMax()));
//        }
//
//        log.info("Trying to allocate {} bytes", mb(missingMemory));
//        memory.allocateMemory(missingMemory);
//
//        freeMemory = Runtime.getRuntime().freeMemory();
//        totalMemory = Runtime.getRuntime().totalMemory();
//        maxMemory = Runtime.getRuntime().maxMemory();
//        usedMemory = totalMemory - freeMemory;
//        availableMemory = maxMemory - usedMemory;
//
//
//        log.info("Max Memory: {} Total Memory: {}, Free Memory: {}, Used Memory {}, Available Memory {}",
//                mb(maxMemory),
//                mb(totalMemory),
//                mb(freeMemory),
//                mb(usedMemory),
//                mb(availableMemory));


    }


    @Override
    public synchronized long getLoad() {
        return desiredLoad;
    }

    @Override
    public synchronized void setLoad(long desiredLoad) {
        synchronized (lock) {
            this.desiredLoad = (desiredLoad < 0) ? 0 : desiredLoad;
            lock.notifyAll();
        }
        log.trace("Set load to {}", mb(this.desiredLoad));
    }

    @Override
    public synchronized void increaseLoad(long delta) {
        setLoad(desiredLoad + delta);
    }

    @Override
    public synchronized void decreaseLoad(long delta) {
        setLoad(desiredLoad - delta);
    }

    private String mb(long bytes) {
        return String.format("%d (%.2f MB)", bytes, (double)bytes / (MB));
    }
}
