package ac.at.tuwien.infosys.fakeload.internal;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by martensigwart on 04.07.17.
 */
final class Connection {

    private AtomicInteger cpuLoad;
    private AtomicLong memoryLoad;
    private AtomicLong diskIOLoad;
    private AtomicLong netIOLoad;


    Connection() {
        cpuLoad = new AtomicInteger(0);
        memoryLoad = new AtomicLong(0L);
        diskIOLoad = new AtomicLong(0L);
        netIOLoad = new AtomicLong(0L);
    }

    public int getCpuLoad() {
        return cpuLoad.get();
    }

    public long getMemoryLoad() {
        return memoryLoad.get();
    }

    public long getDiskIOLoad() {
        return diskIOLoad.get();
    }

    public long getNetIOLoad() {
        return netIOLoad.get();
    }

    synchronized void increaseCpu(int cpuLoad) {
        int old = this.cpuLoad.get();
        if (old + cpuLoad > 100) {
            throw new RuntimeException("Increase of CPU to over 100%");     // TODO checked or unchecked exception
        }
        this.cpuLoad.set(old + cpuLoad);
    }

    synchronized void increaseMemory(long memoryLoad) {
        long old = this.memoryLoad.get();
//        if (old + memoryLoad > 100) {
//            throw new RuntimeException("Increase of Memory to over 100%");     // TODO checked or unchecked exception
//        }
        this.memoryLoad.set(old + memoryLoad);
    }

    synchronized void increaseDiskIO(long diskIOLoad) {
        long old = this.diskIOLoad.get();
//        if (old + diskIOLoad > 100) {
//            throw new RuntimeException("Increase of Disk IO to over 100%");     // TODO checked or unchecked exception
//        }
        this.diskIOLoad.set(old + diskIOLoad);
    }

    synchronized void increaseNetIO(long netIOLoad) {
        long old = this.netIOLoad.get();
//        if (old + netIOLoad > 100) {
//            throw new RuntimeException("Increase of Net IO to over 100%");     // TODO checked or unchecked exception
//        }
        this.netIOLoad.set(old + netIOLoad);
    }

    synchronized void decreaseCpu(int cpuLoad) {
        int old = this.cpuLoad.get();
        if (old - cpuLoad < 0) {
            throw new RuntimeException("Decrease of CPU to under 0%");     // TODO checked or unchecked exception
        }
        this.cpuLoad.set(old - cpuLoad);
    }

    synchronized void decreaseMemory(long memoryLoad) {
        long old = this.memoryLoad.get();
//        if (old - netIOLoad < 0) {
//            throw new RuntimeException("Decrease of Memory to under 0%");     // TODO checked or unchecked exception
//        }
        this.memoryLoad.set(old - memoryLoad);
    }

    synchronized void decreaseDiskIO(long diskIOLoad) {
        long old = this.diskIOLoad.get();
//        if (old - netIOLoad < 0) {
//            throw new RuntimeException("Decrease of Disk IO to under 0%");     // TODO checked or unchecked exception
//        }
        this.diskIOLoad.set(old - diskIOLoad);
    }

    synchronized void decreaseNetIO(long netIOLoad) {
        long old = this.netIOLoad.get();
//        if (old - netIOLoad < 0) {
//            throw new RuntimeException("Decrease of Net IO to under 0%");     // TODO checked or unchecked exception
//        }
        this.netIOLoad.set(old - netIOLoad);
    }
}
