package com.martensigwart.fakeload;


import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

abstract class AbstractFakeLoad implements FakeLoad {

    private static final long serialVersionUID = 7279757183068731801L;
    private final int repetitions;


    AbstractFakeLoad(int repetitions) {
        checkArgument(repetitions >= 1, "Number of repetitions must be at least 1 but was %s", repetitions);

        this.repetitions = repetitions;
    }

    @Override
    public int getRepetitions() {
        return repetitions;
    }


    static class SerializationProxy implements Serializable {

        private static final long serialVersionUID = 6633428955227710412L;

        private final long duration;
        private final TimeUnit unit;
        private final int repetitions;
        private final int cpu;
        private final long memory;
        private final long diskInput;
        private final long diskOutput;
        private final Collection<FakeLoad> loads;

        SerializationProxy(FakeLoad fakeLoad) {
            duration = fakeLoad.getDuration();
            unit = fakeLoad.getTimeUnit();
            repetitions = fakeLoad.getRepetitions();
            cpu = fakeLoad.getCpu();
            memory = fakeLoad.getMemory();
            diskInput = fakeLoad.getDiskInput();
            diskOutput = fakeLoad.getDiskInput();
            loads = fakeLoad.getInnerLoads();

        }

        private Object readResolve() {
            return  new FakeLoadBuilder(duration, unit)
                    .repeat(repetitions)
                    .withCpu(cpu)
                    .withMemory(memory, MemoryUnit.BYTES)
                    .withDiskInput(diskInput, MemoryUnit.BYTES)
                    .withDiskOutput(diskOutput, MemoryUnit.BYTES)
                    .addLoads(loads)
                    .build();
        }
    }
}
