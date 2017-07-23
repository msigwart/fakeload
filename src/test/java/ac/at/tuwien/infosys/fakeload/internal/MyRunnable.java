package ac.at.tuwien.infosys.fakeload.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by martensigwart on 23.07.17.
 */
public class MyRunnable implements Runnable {

    private final Connection connection;
    private final List<Instruction> instructions;

    MyRunnable(Connection connection, List<Instruction> instructions) {
        this.connection = connection;
        this.instructions = instructions;
    }

    @Override
    public void run() {
        System.out.println("Running");
        while(!Thread.currentThread().isInterrupted()) {
            ListIterator<Instruction> iterator = instructions.listIterator();

            while (iterator.hasNext()) {
                Instruction i = iterator.next();
                try {
                    Thread.sleep(i.offSet*1000);
                    switch (i.type) {

                        case INCREASE:
                            System.out.println("Increasing " + i.loadType + " by " + i.change);
                            increase(i.loadType, i.change);
                            break;
                        case DECREASE:
                            System.out.println("Decreasing " + i.loadType + " by " + i.change);
                            decrease(i.loadType, i.change);
                            break;
                    }
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
