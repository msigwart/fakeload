package com.martensigwart.fakeload;

import javax.annotation.concurrent.Immutable;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.martensigwart.fakeload.Preconditions.checkArgument;
import static com.martensigwart.fakeload.Preconditions.checkNotNull;

/**
 * A simple representation of a {@link FakeLoad}, which contains only simple
 * load parameters like CPU, memory, etc.
 *
 * <p>
 * This class cannot contain other {@code FakeLoad} objects, any call to
 * {@link #addLoad(FakeLoad)} or {@link #addLoads(Collection)} will return
 * a new {@link CompositeFakeLoad} instance.
 *
 * <p>
 * This class is immutable and therefore threadsafe.
 * The setter methods for load parameters provided as a fluent interface each
 * return a new {@code FakeLoad} object containing the newly specified parameter.
 *
 * <p>
 * Classes {@link FakeLoads} and {@link FakeLoadBuilder} provide convenience methods
 * for simpler and more efficient instantiation.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
@Immutable
final class SimpleFakeLoad extends AbstractFakeLoad {

    private final long duration;
    private final TimeUnit unit;
    private final int cpu;
    private final long memory;
    private final long diskInput;
    private final long diskOutput;


    SimpleFakeLoad(long duration, TimeUnit unit, int repetitions,
                   int cpu, long memory, long diskInput, long diskOutput) {

        super(repetitions);

        checkArgument(duration >= 0, "Duration must be nonnegative but was %s", duration);
        checkArgument(cpu >= 0, "CPU load must be nonnegative but was %s", cpu);
        checkArgument(cpu <= 100, "CPU load must be less than 100 percent but was %s", cpu);
        checkArgument(memory >= 0, "memory load must be nonnegative but was %s", memory);
        checkArgument(diskInput >= 0, "Disk Input load must be nonnegative but was %s", diskInput);
        checkArgument(diskOutput >= 0, "Disk Output load must be nonnegative but was %s", diskOutput);


        this.duration = duration;
        this.unit = checkNotNull(unit);
        this.cpu = cpu;
        this.memory = memory;
        this.diskInput = diskInput;
        this.diskOutput = diskOutput;

    }

    SimpleFakeLoad() {
        this(0L, TimeUnit.MILLISECONDS, 1,
                0, 0L, 0L, 0L);
    }

    SimpleFakeLoad(long duration, TimeUnit unit) {
        this(duration, unit, 1,
                0, 0L, 0L, 0L);
    }

    @Override
    public FakeLoad lasting(long duration, TimeUnit unit) {
        return new SimpleFakeLoad(duration, unit, getRepetitions(), cpu, memory, diskInput, diskOutput);
    }

    @Override
    public FakeLoad repeat(int repetitions) {
        return new SimpleFakeLoad(duration, unit, repetitions, cpu, memory, diskInput, diskOutput);
    }

    @Override
    public FakeLoad withCpu(int cpuLoad) {
        return new SimpleFakeLoad(duration, unit, getRepetitions(), cpuLoad, memory, diskInput, diskOutput);
    }

    @Override
    public FakeLoad withMemory(long amount, MemoryUnit unit) {
        long memoryLoad = unit.toBytes(amount);
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpu, memoryLoad, diskInput, diskOutput);
    }

    @Override
    public FakeLoad withDiskInput(long load, MemoryUnit unit) {
        long diskInputLoad = unit.toBytes(load);
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpu, memory, diskInputLoad, diskOutput);
    }

    @Override
    public FakeLoad withDiskOutput(long load, MemoryUnit unit) {
        long diskOutputLoad = unit.toBytes(load);
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpu, memory, diskInput, diskOutputLoad);
    }

    @Override
    public FakeLoad addLoad(FakeLoad load) {
        checkNotNull(load);
        List<FakeLoad> newLoads = ImmutableList.<FakeLoad>builder().add(load).build();
        return new CompositeFakeLoad(this, newLoads, getRepetitions());

    }

    @Override
    public FakeLoad addLoads(Collection<FakeLoad> loads) {
        checkNotNull(loads);

        List<FakeLoad> newLoads = ImmutableList.copyOf(loads);

        return new CompositeFakeLoad(this, newLoads, getRepetitions());
    }


    @Override
    public Collection<FakeLoad> getInnerLoads() {
        return new ArrayList<>();
    }

    @Override
    public int getCpu() {
        return cpu;
    }

    @Override
    public long getMemory() {
        return memory;
    }

    @Override
    public long getDiskInput() {
        return diskInput;
    }

    @Override
    public long getDiskOutput() {
        return diskOutput;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return unit;
    }


    @Override
    public boolean contains(FakeLoad load) {
        return this.equals(load);
    }

    @Override
    public Iterator<FakeLoad> iterator() {
        return new Iterator<FakeLoad>() {

            private int repetitions = 0;

            @Override
            public boolean hasNext() {
                return (repetitions < SimpleFakeLoad.this.getRepetitions());
            }

            @Override
            public FakeLoad next() {
                if (hasNext()) {
                    repetitions++;
                    return SimpleFakeLoad.this;
                } throw new NoSuchElementException();
            }
        };
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleFakeLoad)) return false;

        SimpleFakeLoad fakeLoad = (SimpleFakeLoad) o;

        if (duration != fakeLoad.duration) return false;
        if (cpu != fakeLoad.cpu) return false;
        if (memory != fakeLoad.memory) return false;
        if (diskInput != fakeLoad.diskInput) return false;
        if (getRepetitions() != fakeLoad.getRepetitions()) return false;
        return unit == fakeLoad.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, getRepetitions(), cpu, memory, diskInput, unit);
    }

    @Override
    public String toString() {
        return "SimpleFakeLoad{" +
                "duration=" + duration +
                ", unit=" + unit +
                ", cpu=" + cpu +
                ", memory=" + memory +
                ", diskInput=" + diskInput +
                ", diskOutput=" + diskOutput +
                ", repetitions=" + getRepetitions() +
                '}';
    }

    // readObject method for the serialization proxy pattern
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

}
