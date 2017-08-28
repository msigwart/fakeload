package com.martensigwart.fakeload;

import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;

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
public final class SimpleFakeLoad extends AbstractFakeLoad {

    private final long duration;
    private final TimeUnit unit;
    private final long cpuLoad;
    private final long memoryLoad;
    private final long diskInputLoad;
    private final long diskOutputLoad;


    SimpleFakeLoad(long duration, TimeUnit unit, int repetitions,
                           long cpuLoad, long memoryLoad, long diskInputLoad, long diskOutputLoad) {

        super(repetitions);

        checkArgument(duration >= 0, "Duration must be nonnegative but was %s", duration);
        checkArgument(cpuLoad >= 0, "CPU load must be nonnegative but was %s", cpuLoad);
        checkArgument(cpuLoad <= 100, "CPU load must be less than 100 percent but was %s", cpuLoad);
        checkArgument(memoryLoad >= 0, "memory load must be nonnegative but was %s", memoryLoad);
        checkArgument(diskInputLoad >= 0, "Disk Input load must be nonnegative but was %s", diskInputLoad);
        checkArgument(diskOutputLoad >= 0, "Disk Output load must be nonnegative but was %s", diskOutputLoad);


        this.duration = duration;
        this.unit = checkNotNull(unit);
        this.cpuLoad = cpuLoad;
        this.memoryLoad = memoryLoad;
        this.diskInputLoad = diskInputLoad;
        this.diskOutputLoad = diskOutputLoad;

    }

    SimpleFakeLoad() {
        this(0L, TimeUnit.MILLISECONDS, 0,
                0, 0L, 0L, 0L);
    }

    SimpleFakeLoad(long duration, TimeUnit unit) {
        this(duration, unit, 0,
                0, 0L, 0L, 0L);
    }

    @Override
    public FakeLoad lasting(long duration, TimeUnit unit) {
        return new SimpleFakeLoad(duration, unit, getRepetitions(), cpuLoad, memoryLoad, diskInputLoad, diskOutputLoad);
    }

    @Override
    public FakeLoad repeat(int repetitions) {
        return new SimpleFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskInputLoad, diskOutputLoad);
    }

    @Override
    public FakeLoad withCpu(long cpuLoad) {
        return new SimpleFakeLoad(duration, unit, getRepetitions(), cpuLoad, memoryLoad, diskInputLoad, diskOutputLoad);
    }

    @Override
    public FakeLoad withMemory(long amount, MemoryUnit unit) {
        long memoryLoad = unit.toBytes(amount);
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpuLoad, memoryLoad, diskInputLoad, diskOutputLoad);
    }

    @Override
    public FakeLoad withDiskInput(long load, MemoryUnit unit) {
        long diskInputLoad = unit.toBytes(load);
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpuLoad, memoryLoad, diskInputLoad, diskOutputLoad);
    }

    @Override
    public FakeLoad withDiskOutput(long load, MemoryUnit unit) {
        long diskOutputLoad = unit.toBytes(load);
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpuLoad, memoryLoad, diskInputLoad, diskOutputLoad);
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
    public long getCpuLoad() {
        return cpuLoad;
    }

    @Override
    public long getMemoryLoad() {
        return memoryLoad;
    }

    @Override
    public long getDiskInputLoad() {
        return diskInputLoad;
    }

    @Override
    public long getDiskOutputLoad() {
        return diskOutputLoad;
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

            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public FakeLoad next() {
                if (hasNext) {
                    this.hasNext = false;
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
        if (cpuLoad != fakeLoad.cpuLoad) return false;
        if (memoryLoad != fakeLoad.memoryLoad) return false;
        if (diskInputLoad != fakeLoad.diskInputLoad) return false;
        if (getRepetitions() != fakeLoad.getRepetitions()) return false;
        return unit == fakeLoad.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, getRepetitions(), cpuLoad, memoryLoad, diskInputLoad, unit);
    }

    @Override
    public String toString() {
        return "SimpleFakeLoad{" +
                "duration=" + duration +
                ", unit=" + unit +
                ", cpuLoad=" + cpuLoad +
                ", memoryLoad=" + memoryLoad +
                ", diskInputLoad=" + diskInputLoad +
                ", diskOutputLoad=" + diskOutputLoad +
                ", repetitions=" + getRepetitions() +
                '}';
    }

}
