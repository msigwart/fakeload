package ac.at.tuwien.infosys.fakeload.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by martensigwart on 17.05.17.
 */
public class MemorySimulator implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(MemorySimulator.class);

    private static final long KB = 1024;
    private static final long MB = KB*1024;
    private static final long GB = MB*1024;

    private static final long OUT_OF_MEMORY_SAFETYNET = 100*MB;


    private final LoadControl loadControl;
    private final long load;
    private SimulatedMemory memory;


    public MemorySimulator(LoadControl loadControl) {
        this.loadControl = loadControl;
        this.load = this.loadControl.getLoadAdjustment();
        this.memory = new SimulatedMemory();
    }


    @Override
    public void run() {

        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();

        long usedMemory = totalMemory - freeMemory;
        long availableMemory = maxMemory - usedMemory;

        log.info("Max Memory: {} Total Memory: {}, Free Memory: {}, Available Memory {}",
                mb(maxMemory),
                mb(totalMemory),
                mb(freeMemory),
                mb(availableMemory));

        long desiredMemory = load;


        if (desiredMemory >= maxMemory - OUT_OF_MEMORY_SAFETYNET) {
            log.warn("Not enough memory for memory simulation of {} bytes (max: {})", mb(desiredMemory), mb(maxMemory));
            // do something, maybe throw exception? Or simply return?
        }

        long missingMemory = desiredMemory - usedMemory;

        log.info("Desired Memory: {}, Used Memory: {}, Missing Memory: {}", mb(desiredMemory), mb(usedMemory), mb(missingMemory));

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

        log.info("Trying to allocate {} bytes", mb(missingMemory));
        memory.allocateMemory(missingMemory);

        freeMemory = Runtime.getRuntime().freeMemory();
        totalMemory = Runtime.getRuntime().totalMemory();
        maxMemory = Runtime.getRuntime().maxMemory();
        usedMemory = totalMemory - freeMemory;
        availableMemory = maxMemory - usedMemory;


        log.info("Max Memory: {} Total Memory: {}, Free Memory: {}, Used Memory {}, Available Memory {}",
                mb(maxMemory),
                mb(totalMemory),
                mb(freeMemory),
                mb(usedMemory),
                mb(availableMemory));

        while (true) {
            synchronized (loadControl) {
                try {
                    loadControl.wait();
                } catch (InterruptedException e) {
                    System.out.println("INTERRUPTED!!!");
                    memory.removeAll();
                    break;
                }
            }
        }

    }



    private String mb(long s) {
        return String.format("%d (%.2f M)", s, (double)s / (MB));
    }

}
