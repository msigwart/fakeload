package com.martensigwart.fakeload;

import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;


/**
 * Created by martensigwart on 20.07.17.
 */
public final class SimpleFakeLoad extends AbstractFakeLoad {

    private final long duration;
    private final TimeUnit unit;
    private final long cpuLoad;
    private final long memoryLoad;
    private final long diskIOLoad;
    private final long netIOLoad;


    private SimpleFakeLoad(long duration, TimeUnit unit, int repetitions,
                           long cpuLoad, long memoryLoad, long diskIOLoad, long netIOLoad) {

        super(repetitions);

        checkArgument(duration >= 0, "Duration must be nonnegative but was %s", duration);
        checkArgument(cpuLoad >= 0, "CPU load must be nonnegative but was %s", cpuLoad);
        checkArgument(cpuLoad <= 100, "CPU load must be less than 100 percent but was %s", cpuLoad);
        checkArgument(memoryLoad >= 0, "memory load must be nonnegative but was %s", memoryLoad);
        checkArgument(diskIOLoad >= 0, "Disk IO load must be nonnegative but was %s", diskIOLoad);
        checkArgument(netIOLoad >= 0, "Net IO load must be nonnegative but was %s", netIOLoad);

        this.duration = duration;
        this.unit = checkNotNull(unit);
        this.cpuLoad = cpuLoad;
        this.memoryLoad = memoryLoad;
        this.diskIOLoad = diskIOLoad;
        this.netIOLoad = netIOLoad;

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
        return new SimpleFakeLoad(duration, unit, getRepetitions(), cpuLoad, memoryLoad, diskIOLoad, netIOLoad);
    }

    @Override
    public FakeLoad repeat(int repetitions) {
        return new SimpleFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad);
    }

    @Override
    public FakeLoad withCpuLoad(long cpuLoad) {
        return new SimpleFakeLoad(duration, unit, getRepetitions(), cpuLoad, memoryLoad, diskIOLoad, netIOLoad);
    }

    @Override
    public FakeLoad withMemoryLoad(long amount, MemoryUnit unit) {
        long memoryLoad = amount * unit.toBytes();
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpuLoad, memoryLoad, diskIOLoad, netIOLoad);
    }

    @Override
    public FakeLoad withDiskIOLoad(long diskIOLoad) {
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpuLoad, memoryLoad, diskIOLoad, netIOLoad);
    }

    @Override
    public FakeLoad withNetIOLoad(long netIOLoad) {
        return new SimpleFakeLoad(duration, this.unit, getRepetitions(), cpuLoad, memoryLoad, diskIOLoad, netIOLoad);
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
    public long getDiskIOLoad() {
        return diskIOLoad;
    }

    @Override
    public long getNetIOLoad() {
        return netIOLoad;
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        SimpleFakeLoad that = (SimpleFakeLoad) o;
//
//        if (duration != that.duration) return false;
//        if (getRepetitions() != that.getRepetitions()) return false;
//        if (cpuLoad != that.cpuLoad) return false;
//        if (memoryLoad != that.memoryLoad) return false;
//        if (diskIOLoad != that.diskIOLoad) return false;
//        if (netIOLoad != that.netIOLoad) return false;
//        if (unit != that.unit) return false;
//        return loads.equals(that.loads);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleFakeLoad)) return false;

        SimpleFakeLoad fakeLoad = (SimpleFakeLoad) o;

        if (duration != fakeLoad.duration) return false;
        if (cpuLoad != fakeLoad.cpuLoad) return false;
        if (memoryLoad != fakeLoad.memoryLoad) return false;
        if (diskIOLoad != fakeLoad.diskIOLoad) return false;
        if (netIOLoad != fakeLoad.netIOLoad) return false;
        if (getRepetitions() != fakeLoad.getRepetitions()) return false;
        return unit == fakeLoad.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, getRepetitions(), cpuLoad, memoryLoad, diskIOLoad, netIOLoad, unit);
    }

    @Override
    public String toString() {
        return "SimpleFakeLoad{" +
                "duration=" + duration +
                ", unit=" + unit +
                ", cpuLoad=" + cpuLoad +
                ", memoryLoad=" + memoryLoad +
                ", diskIOLoad=" + diskIOLoad +
                ", netIOLoad=" + netIOLoad +
                ", repetitions=" + getRepetitions() +
                '}';
    }

}
