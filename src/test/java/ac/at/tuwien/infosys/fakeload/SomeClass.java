package ac.at.tuwien.infosys.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 30.06.17.
 */
public class SomeClass {

    private static final Logger log = LoggerFactory.getLogger(SomeClass.class);

    public void someMethod() {
        log.info("Entered someMethod()");

        // Execute AbstractFakeLoad
        FakeLoad fakeLoad = FakeLoads.createLoad(100, "50%", "1024m");
        fakeLoad.execute();

        // some code
        log.info("Leaving someMethod()...");
    }

    public void someOtherMethod() {
        log.info("Entered someOtherMethod()");

        // Create LoadPattern with Builder pattern
        LoadPattern pattern = new LoadPatternBuilder().build();
        pattern.addLoad(60, TimeUnit.SECONDS, "30%", "2048k");
        pattern.addIntervalLoad(200, TimeUnit.MILLISECONDS, 10, TimeUnit.SECONDS, "50%", "1024m");

        // Execute AbstractFakeLoad
//        AbstractFakeLoad fakeLoad = new AbstractFakeLoad(pattern);
//        fakeLoad.execute();
//
//        pattern = new LoadPatternBuilder(10, TimeUnit.SECONDS)
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

        FakeLoad fakeLoad = new MutableFakeLoad()
                .withCpuLoad(80)
                .withMemoryLoad(300, MemoryUnit.MB);

        fakeLoad.execute();

        log.info("Leaving yetAnotherMethod()");
    }

}
