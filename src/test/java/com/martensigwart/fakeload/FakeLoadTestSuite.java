package com.martensigwart.fakeload;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by martensigwart on 28.07.17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SimpleFakeLoadTest.class,
        CompositeFakeLoadTest.class
})
public class FakeLoadTestSuite {
}
