package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.internal.util.Decrease;
import ac.at.tuwien.infosys.fakeload.internal.util.Increase;
import ac.at.tuwien.infosys.fakeload.internal.util.Instruction;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by martensigwart on 23.07.17.
 */
public class ConnectionRunnable implements Runnable {

    private static AtomicInteger threadId = new AtomicInteger(0);

    private final int id;
    private final SystemLoad systemLoad;
    private final List<Instruction> instructions;

    ConnectionRunnable(SystemLoad systemLoad, List<Instruction> instructions) {
        this.id = threadId.getAndIncrement();
        this.systemLoad = systemLoad;
        this.instructions = instructions;
    }

    @Override
    public void run() {
        System.out.println(id + ": Started");
        while(!Thread.currentThread().isInterrupted()) {
            ListIterator<Instruction> iterator = instructions.listIterator();

            while (iterator.hasNext()) {
                Instruction i = iterator.next();
                try {
                    Thread.sleep(i.getOffSet()*1000);

                    if (i instanceof Increase) {
                        System.out.println(id + ": Increasing " + i.getLoadType() + " by " + i.getChange());
                        increase(i.getLoadType(), i.getChange());

                    } else if (i instanceof Decrease) {
                        System.out.println(id + ": Decreasing " + i.getLoadType() + " by " + i.getChange());
                        decrease(i.getLoadType(), i.getChange());
                    }

                    iterator.remove();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void increase(Instruction.LoadType type, long increase) {
//        try {
//            switch (type) {
//
//                case CPU:
//                    systemLoad.increaseCpu(increase);
//                    break;
//                case MEMORY:
//                    systemLoad.increaseMemory(increase);
//                    break;
//                case DISKIO:
//                    systemLoad.increaseDiskIO(increase);
//                    break;
//                case NETIO:
//                    systemLoad.increaseNetIO(increase);
//                    break;
//            }
//        } catch (MaximumLoadExceededException e) {
//            //TODO
//        }
    }


    private void decrease(Instruction.LoadType type, long decrease) {
//        switch (type) {
//
//            case CPU:
//                systemLoad.decreaseCpu(decrease);
//                break;
//            case MEMORY:
//                systemLoad.decreaseMemory(decrease);
//                break;
//            case DISKIO:
//                systemLoad.decreaseDiskIO(decrease);
//                break;
//            case NETIO:
//                systemLoad.decreaseNetIO(decrease);
//                break;
//        }
    }



}
