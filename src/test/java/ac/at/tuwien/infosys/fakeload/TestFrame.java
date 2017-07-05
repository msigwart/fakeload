package ac.at.tuwien.infosys.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martensigwart on 30.06.17.
 */
public class TestFrame {

    private static final Logger log = LoggerFactory.getLogger(TestFrame.class);

    public static void main(String... args) {
        log.info("Welcome to FakeLoad - TestFrame");

        SomeClass someClass = new SomeClass();

        someClass.someMethod();

        someClass.someOtherMethod();

    }

}
