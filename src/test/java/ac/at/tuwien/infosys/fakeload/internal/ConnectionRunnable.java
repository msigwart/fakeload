package ac.at.tuwien.infosys.fakeload.internal.util;

import ac.at.tuwien.infosys.fakeload.internal.Connection;
import ac.at.tuwien.infosys.fakeload.internal.Decrease;
import ac.at.tuwien.infosys.fakeload.internal.Increase;
import ac.at.tuwien.infosys.fakeload.internal.Instruction;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by martensigwart on 23.07.17.
 */
public class ConnectionRunnable implements Runnable {

    private static AtomicInteger threadId = new AtomicInteger(0);

    private final int id;
    private final Connection connection;
    private final List<Instruction> instructions;

    ConnectionRunnable(Connection connection, List<Instruction> instructions) {
        this.id = threadId.getAndIncrement();
        this.connection = connection;
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

    private void increase(LoadType type, long increase) {
        switch (type) {

            case CPU:
                connection.increaseAndGetCpu((int) increase);
                break;
            case MEMORY:
                connection.increaseAndGetMemory(increase);
                break;
            case DISKIO:
                connection.increaseAndGetDiskIO(increase);
                break;
            case NETIO:
                connection.increaseAndGetNetIO(increase);
                break;
        }
    }


    private void decrease(LoadType type, long decrease) {
        switch (type) {

            case CPU:
                connection.decreaseAndGetCpu((int) decrease);
                break;
            case MEMORY:
                connection.decreaseAndGetMemory(decrease);
                break;
            case DISKIO:
                connection.decreaseAndGetDiskIO(decrease);
                break;
            case NETIO:
                connection.decreaseAndGetNetIO(decrease);
                break;
        }
    }

    enum  LoadType {
        CPU, MEMORY, DISKIO, NETIO
    }


}
