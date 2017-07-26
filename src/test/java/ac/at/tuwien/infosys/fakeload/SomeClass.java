package ac.at.tuwien.infosys.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static ac.at.tuwien.infosys.fakeload.MemoryUnit.MB;

/**
 * Created by martensigwart on 30.06.17.
 */
public class SomeClass {

    private static final Logger log = LoggerFactory.getLogger(SomeClass.class);

    public void someMethod() {
        log.info("Entered someMethod()");

        // Execute FakeLoad
//        FakeLoad fakeLoad = FakeLoads.createLoad(100, "50%", "1024m");
        FakeLoadExecutor executor = FakeLoadExecutors.newDefaultExecutor();
        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(5, TimeUnit.SECONDS)
                .withCpuLoad(50)
                .withMemoryLoad(100, MB);

        executor.execute(fakeLoad);

        // some code
        log.info("Leaving someMethod()...");
    }

    public void someOtherMethod() {
        log.info("Entered someOtherMethod()");

        // Create LoadPattern with Builder pattern
//        FakeLoad fakeload = new FakeLoadBuilder().build();
//        fakeload.addLoad(60, TimeUnit.SECONDS, "30%", "2048k");
//        fakeload.addIntervalLoad(200, TimeUnit.MILLISECONDS, 10, TimeUnit.SECONDS, "50%", "1024m");

        // Execute AbstractFakeLoad
//        AbstractFakeLoad fakeLoad = new AbstractFakeLoad(pattern);
//        fakeLoad.execute();
//
//        pattern = new FakeLoadBuilder(10, TimeUnit.SECONDS)
//                .cpuLoad(50).memoryLoad(600).build();
//
//        fakeLoad.setLoad(pattern);
//        fakeLoad.execute();
//
//
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

        FakeLoadExecutor executor = FakeLoadExecutors.newDefaultExecutor();
        FakeLoad fakeLoad = new MutableFakeLoad()
                .withCpuLoad(80)
                .withMemoryLoad(300, MB);

        executor.execute(fakeLoad);

        log.info("Leaving yetAnotherMethod()");
    }

}
