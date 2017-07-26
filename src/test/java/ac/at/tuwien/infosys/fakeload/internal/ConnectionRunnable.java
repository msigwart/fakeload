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

    private void increase(Instruction.LoadType type, long increase) {
        try {
            switch (type) {

                case CPU:
                    connection.increaseCpu(increase);
                    break;
                case MEMORY:
                    connection.increaseMemory(increase);
                    break;
                case DISKIO:
                    connection.increaseDiskIO(increase);
                    break;
                case NETIO:
                    connection.increaseNetIO(increase);
                    break;
            }
        } catch (MaximumLoadExceededException e) {
            //TODO
        }
    }


    private void decrease(Instruction.LoadType type, long decrease) {
        switch (type) {

            case CPU:
                connection.decreaseCpu(decrease);
                break;
            case MEMORY:
                connection.decreaseMemory(decrease);
                break;
            case DISKIO:
                connection.decreaseDiskIO(decrease);
                break;
            case NETIO:
                connection.decreaseNetIO(decrease);
                break;
        }
    }



}
