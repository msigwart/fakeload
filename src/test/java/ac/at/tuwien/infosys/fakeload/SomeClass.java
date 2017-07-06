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

        // Execute FakeLoad
        FakeLoad fakeLoad = new FakeLoad(100, "50%", "1024m");
        fakeLoad.execute();

        // some code
        log.info("Leaving someMethod()...");
    }

    public void someOtherMethod() {
        log.info("Entered someOtherMethod()");

        // Create SimpleLoadPattern
        SimpleLoadPattern pattern = new SimpleLoadPattern();
        pattern.addLoad(60, TimeUnit.SECONDS, "30%", "2048k");
        pattern.addIntervalLoad(200, TimeUnit.MILLISECONDS, 10, TimeUnit.SECONDS, "50%", "1024m");

        // Execute FakeLoad
        FakeLoad fakeLoad = new FakeLoad(pattern);
        fakeLoad.execute();

        // some code
        log.info("Leaving someOtherMethod()...");
    }

}
