package com.martensigwart.fakeload;

import com.martensigwart.fakeload.util.Decrease;
import com.martensigwart.fakeload.util.Increase;
import com.martensigwart.fakeload.util.Instruction;

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
        FakeLoad fakeLoad = initFakeLoad(type, increase);

        try {
            systemLoad.increaseBy(fakeLoad);

        } catch (MaximumLoadExceededException e) {
            e.printStackTrace();
        }
    }

    private void decrease(Instruction.LoadType type, long decrease) {
        FakeLoad fakeLoad = initFakeLoad(type, decrease);

        systemLoad.decreaseBy(fakeLoad);
    }

    private FakeLoad initFakeLoad(Instruction.LoadType type, long increase) {
        FakeLoad fakeLoad = FakeLoads.create();
        switch (type) {
            case CPU:
                fakeLoad = fakeLoad.withCpu((int)increase);
                break;
            case MEMORY:
                fakeLoad = fakeLoad.withMemory(increase, MemoryUnit.BYTES);
                break;
            case DISKIO:
                fakeLoad = fakeLoad.withDiskInput(increase, MemoryUnit.BYTES);
                break;

        }
        return fakeLoad;
    }



}
