package ac.at.tuwien.infosys.fakeload.internal;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by martensigwart on 04.07.17.
 */
final class Connection {

    private final AtomicInteger cpuLoad;
    private final AtomicLong memoryLoad;
    private final AtomicLong diskIOLoad;
    private final AtomicLong netIOLoad;


    Connection() {
        cpuLoad = new AtomicInteger(0);
        memoryLoad = new AtomicLong(0L);
        diskIOLoad = new AtomicLong(0L);
        netIOLoad = new AtomicLong(0L);
    }

    int getCpuLoad() {
        return cpuLoad.get();
    }

    long getMemoryLoad() {
        return memoryLoad.get();
    }

    long getDiskIOLoad() {
        return diskIOLoad.get();
    }

    long getNetIOLoad() {
        return netIOLoad.get();
    }

    synchronized int increaseAndGetCpu(int cpuLoad) {
        int old = this.cpuLoad.get();
        if (old + cpuLoad > 100) {
            throw new RuntimeException("Increase of CPU to over 100%");     // TODO checked or unchecked exception
        }
        return this.cpuLoad.addAndGet(cpuLoad);
    }

    synchronized long increaseAndGetMemory(long memoryLoad) {
        long old = this.memoryLoad.get();
//        if (old + memoryLoad > 100) {
//            throw new RuntimeException("Increase of Memory to over 100%");     // TODO checked or unchecked exception
//        }
        return this.memoryLoad.addAndGet(memoryLoad);
    }

    synchronized long increaseAndGetDiskIO(long diskIOLoad) {
        long old = this.diskIOLoad.get();
//        if (old + diskIOLoad > 100) {
//            throw new RuntimeException("Increase of Disk IO to over 100%");     // TODO checked or unchecked exception
//        }
        return this.diskIOLoad.addAndGet(diskIOLoad);
    }

    synchronized long increaseAndGetNetIO(long netIOLoad) {
        long old = this.netIOLoad.get();
//        if (old + netIOLoad > 100) {
//            throw new RuntimeException("Increase of Net IO to over 100%");     // TODO checked or unchecked exception
//        }
        return this.netIOLoad.addAndGet(netIOLoad);
    }

    synchronized int decreaseAndGetCpu(int cpuLoad) {
        int old = this.cpuLoad.get();
        if (old - cpuLoad < 0) {
            throw new RuntimeException("Decrease of CPU to under 0%");     // TODO checked or unchecked exception
        }
        return this.cpuLoad.accumulateAndGet(cpuLoad, (a, b) -> a - b);
    }

    synchronized long decreaseAndGetMemory(long memoryLoad) {
        long old = this.memoryLoad.get();
        if (old - memoryLoad < 0) {
            throw new RuntimeException("Decrease of Memory to under 0%");     // TODO checked or unchecked exception
        }
        return this.memoryLoad.accumulateAndGet(memoryLoad, (a, b) -> a - b);
    }

    synchronized long decreaseAndGetDiskIO(long diskIOLoad) {
        long old = this.diskIOLoad.get();
        if (old - diskIOLoad < 0) {
            throw new RuntimeException("Decrease of Disk IO to under 0%");     // TODO checked or unchecked exception
        }
        return this.diskIOLoad.accumulateAndGet(diskIOLoad, (a, b) -> a - b);
    }

    synchronized long decreaseAndGetNetIO(long netIOLoad) {
        long old = this.netIOLoad.get();
        if (old - netIOLoad < 0) {
            throw new RuntimeException("Decrease of Net IO to under 0%");     // TODO checked or unchecked exception
        }
        return this.netIOLoad.accumulateAndGet(netIOLoad, (a, b) -> a - b);
    }
}
