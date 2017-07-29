package ac.at.tuwien.infosys.fakeload;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;

/**
 * Implementation of interface {@link FakeLoad} representing composite
 * {@code FakeLoad} objects.
 *
 * A {@code CompositeFakeLoad} can contain own load instructions as well as
 * multiple "inner" or "children" fake loads. In detail it is composed of a
 * {@link FakeLoad} object containing its own load instructions and a
 * collection of {@code FakeLoad} objects representing its children.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public final class CompositeFakeLoad extends AbstractFakeLoad {

    /**
     * This object's own load instructions
     */
    private final FakeLoad ownLoad;

    /**
     * This object's inner/children loads
     */
    private final List<FakeLoad> innerLoads;

    CompositeFakeLoad(FakeLoad ownLoad, List<FakeLoad> innerLoads, int repetitions) {
        super(repetitions);
        this.ownLoad = checkNotNull(ownLoad);
        this.innerLoads = ImmutableList.copyOf(checkNotNull(innerLoads));
    }

    CompositeFakeLoad(List<FakeLoad> innerLoads, int repetitions) {
        this(new SimpleFakeLoad(), innerLoads, repetitions);
    }

    CompositeFakeLoad(List<FakeLoad> innerLoads) {
        this(innerLoads, 0);
    }

    CompositeFakeLoad() {
        this(new ArrayList<>());
    }

    @Override
    public FakeLoad lasting(long duration, TimeUnit unit) {
        FakeLoad newOwnLoad = ownLoad.lasting(duration, unit);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad repeat(int noOfRepetitions) {
        return new CompositeFakeLoad(ownLoad, innerLoads, noOfRepetitions);
    }

    @Override
    public FakeLoad withCpuLoad(int cpuLoad) {
        FakeLoad newOwnLoad = ownLoad.withCpuLoad(cpuLoad);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad withMemoryLoad(long amount, MemoryUnit unit) {
        FakeLoad newOwnLoad = ownLoad.withMemoryLoad(amount, unit);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad withDiskIOLoad(long diskIOLoad) {
        FakeLoad newOwnLoad = ownLoad.withDiskIOLoad(diskIOLoad);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad withNetIOLoad(long netIOLoad) {
        FakeLoad newOwnLoad = ownLoad.withNetIOLoad(netIOLoad);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad addLoad(FakeLoad load) {
        checkNotNull(load);

        List<FakeLoad> newLoads;
        if (this.innerLoads.isEmpty()) {
            newLoads = ImmutableList.of(load);
        } else {
            newLoads = ImmutableList.<FakeLoad>builder().addAll(this.innerLoads).add(load).build();
        }
        return new CompositeFakeLoad(ownLoad, newLoads, getRepetitions());
    }

    @Override
    public FakeLoad addLoads(Collection<FakeLoad> loads) {
        checkNotNull(loads);

        List<FakeLoad> newLoads;
        if (this.innerLoads.isEmpty()) {
            newLoads = ImmutableList.copyOf(loads);
        } else {
            newLoads = ImmutableList.<FakeLoad>builder().addAll(this.innerLoads).addAll(loads).build();
        }
        return new CompositeFakeLoad(ownLoad, newLoads, getRepetitions());
    }

    @Override
    public boolean contains(FakeLoad load) {
        if (this.equals(load)) {
            return true;
        }

        // recursively check if load is contained in child innerLoads
        for (FakeLoad l : this.getInnerLoads()) {
            if (l.contains(load)) {
                return true;
            }
        }

        // load not found anywhere
        return false;
    }

    @Override
    public Collection<FakeLoad> getInnerLoads() {
        return innerLoads;
    }

    @Override
    public int getCpuLoad() {
        return ownLoad.getCpuLoad();
    }

    @Override
    public long getMemoryLoad() {
        return ownLoad.getMemoryLoad();
    }

    @Override
    public long getDiskIOLoad() {
        return ownLoad.getDiskIOLoad();
    }

    @Override
    public long getNetIOLoad() {
        return ownLoad.getNetIOLoad();
    }

    @Override
    public long getDuration() {
        return ownLoad.getDuration();
    }

    @Override
    public TimeUnit getTimeUnit() {
        return ownLoad.getTimeUnit();
    }

    @Override
    public Iterator<FakeLoad> iterator() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeFakeLoad fakeLoads = (CompositeFakeLoad) o;
        return Objects.equal(ownLoad, fakeLoads.ownLoad) &&
                Objects.equal(innerLoads, fakeLoads.innerLoads);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ownLoad, innerLoads);
    }
}
