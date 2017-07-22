package ac.at.tuwien.infosys.fakeload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 20.07.17.
 */
public final class ImmutableFakeLoad extends AbstractFakeLoad {

    private final long duration;
    private final TimeUnit unit;
    private final int repetitions;
    private final int cpuLoad;
    private final long memoryLoad;
    private final long diskIOLoad;
    private final long netIOLoad;

    private final List<FakeLoad> loads;


    private ImmutableFakeLoad(long duration, TimeUnit unit, int repetitions,
                              int cpuLoad, long memoryLoad, long diskIOLoad, long netIOLoad,
                              List<FakeLoad> loads) {

        this.duration = duration;
        this.unit = unit;
        this.repetitions = repetitions;
        this.cpuLoad = cpuLoad;
        this.memoryLoad = memoryLoad;
        this.diskIOLoad = diskIOLoad;
        this.netIOLoad = netIOLoad;
        this.loads = Collections.unmodifiableList(loads);
    }

    ImmutableFakeLoad() {
        this(0L, TimeUnit.MILLISECONDS, 0,
                0, 0L, 0L, 0L, new ArrayList<>());
    }

    ImmutableFakeLoad(long duration, TimeUnit unit) {
        this(duration, unit, 0,
                0, 0L, 0L, 0L, new ArrayList<>());
    }

    @Override
    public FakeLoad lasting(long duration, TimeUnit unit) {
        return new ImmutableFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, loads);
    }

    @Override
    public FakeLoad repeat(int noOfRepetitions) {
        return null;
    }

    @Override
    public FakeLoad withCpuLoad(int cpuLoad) {
        return new ImmutableFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, loads);
    }

    @Override
    public FakeLoad withMemoryLoad(long amount, MemoryUnit unit) {
        long memoryLoad = amount * unit.toBytes();
        return new ImmutableFakeLoad(duration, this.unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, loads);
    }

    @Override
    public FakeLoad withDiskIOLoad(long diskIOLoad) {
        return new ImmutableFakeLoad(duration, this.unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, loads);
    }

    @Override
    public FakeLoad withNetIOLoad(long netIOLoad) {
        return new ImmutableFakeLoad(duration, this.unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, loads);
    }

    @Override
    public FakeLoad addLoad(FakeLoad load) {
        // add load
        List<FakeLoad> newLoads;
        if (this.loads.size() > 0) {
            newLoads = this.loads.subList(0, this.loads.size()-1);
        } else {
            newLoads = new ArrayList<>();
        }
        newLoads.add(load);
        return new ImmutableFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, newLoads);
    }

    @Override
    public FakeLoad addLoads(Collection<FakeLoad> loads) {
        List<FakeLoad> newLoads;
        if (this.loads.size() > 0) {
            newLoads = this.loads.subList(0, this.loads.size()-1);
        } else {
            newLoads = new ArrayList<>();
        }
        newLoads.addAll(loads);
        return new ImmutableFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, newLoads);
    }


    @Override
    public Collection<FakeLoad> getLoads() {
        return loads;
    }

    @Override
    public int getCpuLoad() {
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
        if (this.equals(load)) {
            return true;
        }

        // check if pattern is contained in child patterns
        for (FakeLoad l : this.getLoads()) {
            return l.contains(load);
        }

        // if not found anywhere return false
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableFakeLoad that = (ImmutableFakeLoad) o;

        if (duration != that.duration) return false;
        if (repetitions != that.repetitions) return false;
        if (cpuLoad != that.cpuLoad) return false;
        if (memoryLoad != that.memoryLoad) return false;
        if (diskIOLoad != that.diskIOLoad) return false;
        if (netIOLoad != that.netIOLoad) return false;
        if (unit != that.unit) return false;
        return loads.equals(that.loads);
    }

    @Override
    public int hashCode() {
        int result = (int) (duration ^ (duration >>> 32));
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + repetitions;
        result = 31 * result + cpuLoad;
        result = 31 * result + (int) (memoryLoad ^ (memoryLoad >>> 32));
        result = 31 * result + (int) (diskIOLoad ^ (diskIOLoad >>> 32));
        result = 31 * result + (int) (netIOLoad ^ (netIOLoad >>> 32));
        result = 31 * result + loads.hashCode();
        return result;
    }
}
