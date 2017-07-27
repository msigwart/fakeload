package ac.at.tuwien.infosys.fakeload;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;


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

        checkArgument(duration >= 0, "Duration must be nonnegative but was %s", duration);
        checkArgument(repetitions >= 0, "Repetitions must be nonnegative but was %s", repetitions);
        checkArgument(cpuLoad >= 0, "CPU load must be nonnegative but was %s", cpuLoad);
        checkArgument(memoryLoad >= 0, "memory load must be nonnegative but was %s", memoryLoad);
        checkArgument(diskIOLoad >= 0, "Disk IO load must be nonnegative but was %s", diskIOLoad);
        checkArgument(netIOLoad >= 0, "Net IO load must be nonnegative but was %s", netIOLoad);

        this.duration = duration;
        this.unit = checkNotNull(unit);
        this.repetitions = repetitions;
        this.cpuLoad = cpuLoad;
        this.memoryLoad = memoryLoad;
        this.diskIOLoad = diskIOLoad;
        this.netIOLoad = netIOLoad;
//        this.loads = Collections.unmodifiableList(loads);
        this.loads = ImmutableList.copyOf(loads);

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
        List<FakeLoad> newLoads;

        if (this.loads.isEmpty()) {
            newLoads = ImmutableList.of(load);
        } else {
            newLoads = ImmutableList.<FakeLoad>builder().addAll(this.loads).add(load).build();
        }
        return new ImmutableFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, newLoads);
    }

    @Override
    public FakeLoad addLoads(Collection<FakeLoad> loads) {
        List<FakeLoad> newLoads;
        if (this.loads.isEmpty()) {
            newLoads = ImmutableList.copyOf(loads);
        } else {
            newLoads = ImmutableList.<FakeLoad>builder().addAll(this.loads).addAll(loads).build();
        }
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

        // recursively check if load is contained in child loads
        for (FakeLoad l : this.getLoads()) {
            if (l.contains(load)) {
                return true;
            }
        }

        // load not found anywhere
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
        return Objects.hash(duration, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad, unit, loads);
    }

    @Override
    public String toString() {
        return "ImmutableFakeLoad{" +
                "duration=" + duration +
                ", unit=" + unit +
                ", repetitions=" + repetitions +
                ", cpuLoad=" + cpuLoad +
                ", memoryLoad=" + memoryLoad +
                ", diskIOLoad=" + diskIOLoad +
                ", netIOLoad=" + netIOLoad +
                ", loads=" + loads +
                '}';
    }
}
