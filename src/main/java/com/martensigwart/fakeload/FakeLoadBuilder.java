package com.martensigwart.fakeload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A {@code FakeLoad} builder.
 * Can be used as convenient tool to create FakeLoad instances more efficiently.
 * <p>
 * Both implementations of the {@link FakeLoad} interface, {@link SimpleFakeLoad} and {@link CompositeFakeLoad}
 * are immutable classes. Therfore using the provided fluent interface to set load parameters
 * a new instance is created on every call. Using the builder especially bigger {@code FakeLoad}
 * instances can be created more efficiently.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public class FakeLoadBuilder {

        // simple load parameters
        private long duration       = 0L;
        private TimeUnit unit       = TimeUnit.MILLISECONDS;
        private int repetitions     = 0;
        private long cpuLoad        = 0;
        private long memoryLoad     = 0L;
        private long diskIOLoad     = 0L;
        private long netIOLoad      = 0L;

        // inner loads
        private List<FakeLoad> innerLoads = new ArrayList<>();


        public FakeLoadBuilder(long duration, TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
        }

        public FakeLoadBuilder(String json) {
            //TODO parse json
        }

        public FakeLoadBuilder() {
            // empty
        }

        public FakeLoadBuilder lasting(long duration, TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
            return this;
        }

        public FakeLoadBuilder repeat(int repetitions) {
            this.repetitions = repetitions;
            return this;
        }

        public FakeLoadBuilder withCpuLoad(int cpuLoad) {
            this.cpuLoad = cpuLoad;
            return this;
        }

        public FakeLoadBuilder withMemoryLoad(long memoryLoad, MemoryUnit unit) {
            this.memoryLoad = unit.toBytes(memoryLoad);
            return this;
        }

        public FakeLoadBuilder withDiskIO(long diskIOLoad) {
            this.diskIOLoad = diskIOLoad;
            return this;
        }

        public FakeLoadBuilder withNetIO(long netIOLoad) {
            this.netIOLoad = netIOLoad;
            return this;
        }

        public FakeLoadBuilder addLoad(FakeLoad load) {
            innerLoads.add(load);
            return this;
        }

        public FakeLoadBuilder addLoads(Collection<FakeLoad> loads) {
            innerLoads.addAll(loads);
            return this;
        }


        public FakeLoad build() {
            if (innerLoads.isEmpty()) {
                return new SimpleFakeLoad(duration, unit, repetitions, cpuLoad, memoryLoad, diskIOLoad, netIOLoad);
            } else {
                return new CompositeFakeLoad(
                        new SimpleFakeLoad(duration, unit, 0, cpuLoad, memoryLoad, diskIOLoad, netIOLoad),
                        innerLoads, repetitions);
            }
        }

}
