package ac.at.tuwien.infosys.fakeload;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martensigwart on 30.06.17.
 */
public class TestFrame {

    private static final Logger log = LoggerFactory.getLogger(TestFrame.class);

    public static void main(String... args) {
        log.info("Welcome to FakeLoad - TestFrame");

//        AbstractFoo parent = new ImmutableFoo();
//        AbstractFoo child = new ImmutableFoo();
//        AbstractFoo parent = new MutableFoo();
//        AbstractFoo child = new MutableFoo();

//        AbstractFoo foo = child.add(parent);
//        System.out.println(foo);
//        AbstractFoo foo1 = child.add(foo);
//        System.out.println(foo1);

        SomeClass someClass = new SomeClass();

        someClass.someMethod();

        someClass.someOtherMethod();

    }

}
