package ac.at.tuwien.infosys.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martensigwart on 30.06.17.
 */
public class SomeClass {

    private static final Logger log = LoggerFactory.getLogger(SomeClass.class);

    public void someMethod() {
        log.info("Entered someMethod()");

        // Execute FakeLoad
        FakeLoad fakeLoad = new FakeLoad("100ms", "50%", "1024m");
        fakeLoad.execute();

        // some code
        log.info("Leaving someMethod()...");
    }

    public void someOtherMethod() {
        log.info("Entered someOtherMethod()");

        // Create LoadPattern
        LoadPattern pattern = new LoadPattern();
        pattern.addLoad("60s", "30%", "2048k");
        pattern.addIntervalLoad("200ms", "10s", "50%", "1024m");

        // Execute FakeLoad
        FakeLoad fakeLoad = new FakeLoad(pattern);
        fakeLoad.execute();

        // some code
        log.info("Leaving someOtherMethod()...");
    }

}
