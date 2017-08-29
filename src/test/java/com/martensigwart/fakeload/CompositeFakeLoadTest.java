package com.martensigwart.fakeload;

import java.util.List;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by martensigwart on 27.07.17.
 */
public class CompositeFakeLoadTest extends AbstractFakeLoadTest {


    public CompositeFakeLoadTest() {
        super(CompositeFakeLoad.class);
    }

    //--------------------------------------------------------------------------------------------------
//   TEST CONSTRUCTORS
//--------------------------------------------------------------------------------------------------
    @Test
    public void testNoArgumentConstructor() {
        FakeLoad f1 = new CompositeFakeLoad();
        assertDefault(f1);
    }

    @Test
    public void testSingleArgumentConstructor1() {

        // test with no children
        List<FakeLoad> children = new ArrayList<>();
        FakeLoad f1 = new CompositeFakeLoad(children);

        assertNotNull(f1);
        assertInnerLoadsSize(children.size(), f1);

        // test with two children
        FakeLoad child1 = new SimpleFakeLoad().lasting(10, TimeUnit.SECONDS).withCpu(50);
        FakeLoad child2 = new SimpleFakeLoad().lasting(10, TimeUnit.SECONDS).withCpu(50);
        children.add(child1);
        children.add(child2);

        f1 = new CompositeFakeLoad(children);
        assertNotNull(f1);
        assertInnerLoadsSize(children.size(), f1);      // children size 2 in this case


    }

    @Test(expected = NullPointerException.class)
    public void testSingleArgumentConstructor2() {
        FakeLoad f1 = new CompositeFakeLoad(null);
    }

    @Test
    public void testTwoArgumentConstructor1() {
        // test with no children
        List<FakeLoad> children = new ArrayList<>();
        FakeLoad f1 = new CompositeFakeLoad(children, 2);

        assertNotNull(f1);
        assertRepetitions(2, f1);

        // test with two children
        FakeLoad child1 = new SimpleFakeLoad().lasting(10, TimeUnit.SECONDS).withCpu(50);
        FakeLoad child2 = new SimpleFakeLoad().lasting(10, TimeUnit.SECONDS).withCpu(50);
        FakeLoad child3 = new SimpleFakeLoad().lasting(10, TimeUnit.SECONDS).withCpu(50);
        children.add(child1);
        children.add(child2);
        children.add(child3);

        f1 = new CompositeFakeLoad(children, 5);
        assertNotNull(f1);
        assertEquals(5, f1.getRepetitions());
        assertEquals(0L, f1.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, f1.getTimeUnit());
        assertEquals(0, f1.getCpu());
        assertEquals(0L, f1.getMemory());
        assertEquals(0L, f1.getDiskInput());
        assertEquals(3, f1.getInnerLoads().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwoArgumentConstructor2() {
        // test with no children
        List<FakeLoad> children = new ArrayList<>();
        FakeLoad f1 = new CompositeFakeLoad(children, -2);
    }

}
