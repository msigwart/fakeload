package com.martensigwart.fakeload;

import javax.annotation.Nonnull;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.martensigwart.fakeload.Preconditions.checkNotNull;

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
final class CompositeFakeLoad extends AbstractFakeLoad {

    /**
     * This object's own load instructions
     */
    private final SimpleFakeLoad ownLoad;

    /**
     * This object's inner/children loads
     */
    private final List<FakeLoad> innerLoads;

    CompositeFakeLoad(SimpleFakeLoad ownLoad, List<FakeLoad> innerLoads, int repetitions) {
        super(repetitions);
        this.ownLoad = (SimpleFakeLoad) checkNotNull(ownLoad).repeat(1);
        /*
         * It is okay to just take the argument innerLoads as input to method
         * Collections.unmodifiableList(), because the constructor is not part of
         * the public API and the reference to the argument is not leaked.
         */
        this.innerLoads = Collections.unmodifiableList(innerLoads);
    }

    CompositeFakeLoad(List<FakeLoad> innerLoads, int repetitions) {
        this(new SimpleFakeLoad(), innerLoads, repetitions);
    }

    CompositeFakeLoad(List<FakeLoad> innerLoads) {
        this(innerLoads, 1);
    }

    CompositeFakeLoad() {
        this(new ArrayList<>());
    }

    @Override
    public FakeLoad lasting(long duration, TimeUnit unit) {
        SimpleFakeLoad newOwnLoad = (SimpleFakeLoad) ownLoad.lasting(duration, unit);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad repeat(int noOfRepetitions) {
        return new CompositeFakeLoad(ownLoad, innerLoads, noOfRepetitions);
    }

    @Override
    public FakeLoad withCpu(int cpuLoad) {
        SimpleFakeLoad newOwnLoad = (SimpleFakeLoad) ownLoad.withCpu(cpuLoad);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad withMemory(long amount, MemoryUnit unit) {
        SimpleFakeLoad newOwnLoad = (SimpleFakeLoad) ownLoad.withMemory(amount, unit);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad withDiskInput(long load, MemoryUnit unit) {
        SimpleFakeLoad newOwnLoad = (SimpleFakeLoad) ownLoad.withDiskInput(load, unit);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }

    @Override
    public FakeLoad withDiskOutput(long load, MemoryUnit unit) {
        SimpleFakeLoad newOwnLoad = (SimpleFakeLoad) ownLoad.withDiskOutput(load, unit);
        return new CompositeFakeLoad(newOwnLoad, innerLoads, getRepetitions());
    }


    @Override
    public FakeLoad addLoad(FakeLoad load) {
        checkNotNull(load);

        List<FakeLoad> newLoads;
        if (this.innerLoads.isEmpty()) {
            newLoads = new ArrayList<>();
            newLoads.add(load);
        } else {
            newLoads = new ArrayList<>();
            newLoads.addAll(this.innerLoads);
            newLoads.add(load);
        }
        return new CompositeFakeLoad(ownLoad, newLoads, getRepetitions());
    }

    @Override
    public FakeLoad addLoads(Collection<FakeLoad> loads) {
        checkNotNull(loads);

        List<FakeLoad> newLoads;
        if (this.innerLoads.isEmpty()) {
            newLoads = new ArrayList<>(loads);
        } else {
            newLoads = new ArrayList<>(this.innerLoads);
            newLoads.addAll(loads);
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
    public int getCpu() {
        return ownLoad.getCpu();
    }

    @Override
    public long getMemory() {
        return ownLoad.getMemory();
    }

    @Override
    public long getDiskInput() {
        return ownLoad.getDiskInput();
    }

    @Override
    public long getDiskOutput() {
        return ownLoad.getDiskOutput();
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
    @Nonnull
    public Iterator<FakeLoad> iterator() {
        Iterator<FakeLoad> iterator = ownLoad.iterator();
        for (int i=0; i<getRepetitions(); i++) {
            if (i != 0) {
                iterator = new IteratorOfIterators<>(iterator, ownLoad.iterator());
            }
            for (FakeLoad load : innerLoads) {
                iterator = new IteratorOfIterators<>(iterator, load.iterator());
            }
        }
        return iterator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeFakeLoad fakeload = (CompositeFakeLoad) o;
        return Objects.equals(ownLoad, fakeload.ownLoad) &&
                Objects.equals(innerLoads, fakeload.innerLoads);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownLoad, innerLoads);
    }

    @Override
    public String toString() {
        return "CompositeFakeLoad{" +
                "ownLoad=" + ownLoad +
                ", innerLoads=" + innerLoads +
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
