package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 30.06.17.
 */
public class SomeClass {

    private static final Logger log = LoggerFactory.getLogger(SomeClass.class);

    private FakeLoadExecutor executor = FakeLoadExecutors.newDefaultExecutor();

    public void someMethod() {
        log.info("Entered someMethod()");

        // Execute FakeLoad
//        FakeLoad fakeLoad = FakeLoads.createLoad(100, "50%", "1024m");
        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(60, TimeUnit.SECONDS)
                .withCpu(50)
                .withMemory(2, MemoryUnit.GB);

        executor.execute(fakeLoad);
        // some code
        log.info("Leaving someMethod()...");
    }

    public void someOtherMethod() {
        log.info("Entered someOtherMethod()");

        // Create FakeLoad with Builder pattern
        FakeLoad fakeload = new FakeLoadBuilder()
                .lasting(60, TimeUnit.SECONDS)
                .withCpu(30)
                .withMemory(1024, MemoryUnit.MB).build();

        // Execute FakeLoad
        executor.execute(fakeload);

        fakeload = new FakeLoadBuilder(20, TimeUnit.SECONDS)
                .withCpu(20).withMemory(100, MemoryUnit.KB)
                .addLoad(new FakeLoadBuilder(20, TimeUnit.SECONDS)
                    .withCpu(40).withMemory(200, MemoryUnit.KB).build())
                .addLoad(new FakeLoadBuilder(20, TimeUnit.SECONDS)
                    .withCpu(60).withMemory(300, MemoryUnit.KB).build())
                .addLoad(new FakeLoadBuilder(20, TimeUnit.SECONDS)
                    .withCpu(80).withMemory(400, MemoryUnit.KB).build())
                .addLoad(new FakeLoadBuilder(20, TimeUnit.SECONDS)
                    .withCpu(100).withMemory(500, MemoryUnit.KB).build())
                .build();

        executor.execute(fakeload);


//        // Create LoadPattern with Factory pattern
//        pattern = LoadPatterns.createLoadPattern();
//        pattern.addLoad(60, TimeUnit.SECONDS, "30%", "2048k");
//        pattern.addIntervalLoad(200, TimeUnit.MILLISECONDS, 10, TimeUnit.SECONDS, "50%", "1024m");
//
//        // Execute AbstractFakeLoad
//        fakeLoad = new AbstractFakeLoad(pattern);
//        fakeLoad.execute();

        // some code
        log.info("Leaving someOtherMethod()...");
    }

    public void yetAnotherMethod() {
        log.info("Entered yetAnotherMethod()");

        FakeLoad fakeLoad = FakeLoads.createLoad()
                .lasting(30, TimeUnit.SECONDS)
                .withCpu(80)
                .withMemory(300, MemoryUnit.MB);

        executor.execute(fakeLoad);

        log.info("Leaving yetAnotherMethod()");
    }

}
