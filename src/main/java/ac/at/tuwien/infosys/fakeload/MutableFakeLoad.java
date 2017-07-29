package ac.at.tuwien.infosys.fakeload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 19.07.17.
 */
public final class MutableFakeLoad extends AbstractFakeLoad {

    private long duration;
    private TimeUnit unit;
    private int repetitions;
    private int cpuLoad;
    private long memoryLoad;
    private long diskIOLoad;
    private long netIOLoad;

    private List<FakeLoad> loads;


    MutableFakeLoad() {
        super(0);
        this.loads = new ArrayList<>();
    }

    MutableFakeLoad(long duration, TimeUnit unit) {
        this();
        this.duration = duration;
        this.unit = unit;
    }


    @Override
    public FakeLoad lasting(long duration, TimeUnit unit) {
        this.duration = duration;
        this.unit = unit;
        return this;
    }

    @Override
    public FakeLoad repeat(int noOfRepetitions) {
        return null;
    }

    @Override
    public FakeLoad withCpuLoad(int cpuLoad) {
        this.cpuLoad = cpuLoad;
        return this;
    }

    @Override
    public FakeLoad withMemoryLoad(long amount, MemoryUnit unit) {
        this.memoryLoad = amount;
        return this;
    }

    @Override
    public FakeLoad withDiskIOLoad(long diskIOLoad) {
        this.diskIOLoad = diskIOLoad;
        return this;
    }

    @Override
    public FakeLoad withNetIOLoad(long netIOLoad) {
        this.netIOLoad = netIOLoad;
        return this;
    }

    @Override
    public FakeLoad addLoad(FakeLoad load) {
        if (load.contains(this)) {
            throw new IllegalArgumentException("The calling FakeLoad is part of the load it is trying to add.");
        }
        loads.add(load);
        return this;
    }

    @Override
    public FakeLoad addLoads(Collection<FakeLoad> loads) {
        loads.forEach(l -> {
            this.addLoad(l);
        });
        return this;
    }

    @Override
    public Collection<FakeLoad> getInnerLoads() {
        return loads;
    }

    @Override
    public int getCpuLoad() {
        return 0;
    }

    @Override
    public long getMemoryLoad() {
        return 0;
    }

    @Override
    public long getDiskIOLoad() {
        return 0;
    }

    @Override
    public long getNetIOLoad() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return null;
    }

    @Override
    public boolean contains(FakeLoad load) {
        if (this == load) {
            return true;
        }

        // check if pattern is contained in child patterns
        for (FakeLoad l : this.getInnerLoads()) {
            return l.contains(load);
        }

        // if not found anywhere return false
        return false;
    }

    @Override
    public Iterator<FakeLoad> iterator() {
        return null;
    }
}
