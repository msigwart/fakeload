package ac.at.tuwien.infosys.fakeload;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 19.07.17.
 */
public class FakeLoadBuilder {

        // Required parameters
        private final long duration;
        private final TimeUnit unit;
        private final String json;

        // Optional parameters
        private int repetitions     = 0;
        private int cpuLoad         = 0;
        private long memoryLoad     = 0L;
        private long diskIOLoad     = 0L;
        private long netIOLoad      = 0L;



        public FakeLoadBuilder(long duration, TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
            this.json = null;
        }

        public FakeLoadBuilder(String json) {
            this.json = json;
            this.duration = -1L;
            this.unit = null;
        }

        public FakeLoadBuilder() {
            this.duration = -1L;
            this.unit = null;
            this.json = null;
        }


        public FakeLoadBuilder repetitions(int repetitions) {
            this.repetitions = repetitions;
            return this;
        }

        public FakeLoadBuilder cpuLoad(int cpuLoad) {
            this.cpuLoad = cpuLoad;
            return this;
        }

        public FakeLoadBuilder memoryLoad(long memoryLoad) {
            this.memoryLoad = memoryLoad;
            return this;
        }

        public FakeLoadBuilder diskIOLoad(long diskIOLoad) {
            this.diskIOLoad = diskIOLoad;
            return this;
        }

        public FakeLoadBuilder netIOLoad(long netIOLoad) {
            this.netIOLoad = netIOLoad;
            return this;
        }

        public FakeLoad build() {

            // TODO: Create FakeLoad from JSON
            if (json != null) {
                Gson g = new Gson();
                FakeLoad pattern = g.fromJson(json, ImmutableFakeLoad.class);
                return new ImmutableFakeLoad();
            }

            // TODO: Create LoadPattern from load parameters
            return new ImmutableFakeLoad();
        }

}
