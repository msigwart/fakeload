package ac.at.tuwien.infosys.fakeload.internal;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by martensigwart on 04.07.17.
 */
final class Connection {

    private final AtomicLong cpuLoad;
    private final AtomicLong memoryLoad;
    private final AtomicLong diskIOLoad;
    private final AtomicLong netIOLoad;


    Connection() {
        cpuLoad = new AtomicLong(0L);
        memoryLoad = new AtomicLong(0L);
        diskIOLoad = new AtomicLong(0L);
        netIOLoad = new AtomicLong(0L);
    }

    synchronized long getCpuLoad() {
        return cpuLoad.get();
    }

    synchronized long getMemoryLoad() {
        return memoryLoad.get();
    }

    synchronized long getDiskIOLoad() {
        return diskIOLoad.get();
    }

    synchronized long getNetIOLoad() {
        return netIOLoad.get();
    }

    synchronized void increaseCpu(long cpuLoad) throws MaximumLoadExceededException {
        long old = this.cpuLoad.get();
        if (old + cpuLoad > 100) {
            throw new MaximumLoadExceededException("Increase of CPU to over 100%");
        }
        this.cpuLoad.addAndGet(cpuLoad);
    }

    synchronized void increaseMemory(long memoryLoad) throws MaximumLoadExceededException {
        long old = this.memoryLoad.get();
//        if (old + memoryLoad > 100) {
//            throw new RuntimeException("Increase of Memory to over 100%");
//        }
        this.memoryLoad.addAndGet(memoryLoad);
    }

    synchronized void increaseDiskIO(long diskIOLoad) throws MaximumLoadExceededException {
        long old = this.diskIOLoad.get();
//        if (old + diskIOLoad > 100) {
//            throw new RuntimeException("Increase of Disk IO to over 100%");
//        }
        this.diskIOLoad.addAndGet(diskIOLoad);
    }

    synchronized void increaseNetIO(long netIOLoad) throws MaximumLoadExceededException {
        long old = this.netIOLoad.get();
//        if (old + netIOLoad > 100) {
//            throw new RuntimeException("Increase of Net IO to over 100%");
//        }
        this.netIOLoad.addAndGet(netIOLoad);
    }

    synchronized void decreaseCpu(long cpuLoad) {
        long old = this.cpuLoad.get();
        if (old - cpuLoad < 0) {
            throw new RuntimeException("Decrease of CPU to under 0%");     // TODO checked or unchecked exception
        }
        this.cpuLoad.accumulateAndGet(cpuLoad, (a, b) -> a - b);
    }

    synchronized void decreaseMemory(long memoryLoad) {
        long old = this.memoryLoad.get();
        if (old - memoryLoad < 0) {
            throw new RuntimeException("Decrease of Memory to under 0%");     // TODO checked or unchecked exception
        }
        this.memoryLoad.accumulateAndGet(memoryLoad, (a, b) -> a - b);
    }

    synchronized void decreaseDiskIO(long diskIOLoad) {
        long old = this.diskIOLoad.get();
        if (old - diskIOLoad < 0) {
            throw new RuntimeException("Decrease of Disk IO to under 0%");     // TODO checked or unchecked exception
        }
        this.diskIOLoad.accumulateAndGet(diskIOLoad, (a, b) -> a - b);
    }

    synchronized void decreaseNetIO(long netIOLoad) {
        long old = this.netIOLoad.get();
        if (old - netIOLoad < 0) {
            throw new RuntimeException("Decrease of Net IO to under 0%");     // TODO checked or unchecked exception
        }
        this.netIOLoad.accumulateAndGet(netIOLoad, (a, b) -> a - b);
    }
}
