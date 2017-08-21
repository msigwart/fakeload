package com.martensigwart.fakeload;

import com.martensigwart.fakeload.util.MoreAsserts;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public abstract class AbstractFakeLoadTest {

    private final Class<? extends FakeLoad> classUnderTest;
    private FakeLoad fakeload = null;


    AbstractFakeLoadTest(Class<? extends FakeLoad> classUnderTest) {
        this.classUnderTest = classUnderTest;
    }

    @Before
    public void setup() {
        try {
            fakeload = classUnderTest.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testLastingMethod1() {
        fakeload = fakeload.lasting(10, TimeUnit.SECONDS);
        assertDuration(10L, TimeUnit.SECONDS, fakeload);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLastingMethod2() {
        fakeload = fakeload.lasting(-10, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testLastingMethod3(){
        fakeload = fakeload.lasting(10, null);
    }

    @Test
    public void testRepeatMethod1() {
        fakeload = fakeload.repeat(20);
        assertRepetitions(20, fakeload);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeatMethod2() {
        fakeload = fakeload.repeat(-20);
    }


    @Test
    public void testCpuLoadMethod1() {
        fakeload = fakeload.withCpu(20);
        assertCpuLoad(20, fakeload);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCpuLoadMethod2() {
        fakeload = fakeload.withCpu(-20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCpuLoadMethod3() {
        fakeload = fakeload.withCpu(120);
    }

    @Test
    public void testMemoryLoadMethod1() {
        // Bytes
        fakeload = fakeload.withMemory(1024, MemoryUnit.BYTES);
        assertMemoryLoad(1024L, fakeload);

        //kB
        fakeload = fakeload.withMemory(300, MemoryUnit.KB);
        assertMemoryLoad(300L*1024, fakeload);

        //MB
        fakeload = fakeload.withMemory(300, MemoryUnit.MB);
        assertMemoryLoad(300L*1024*1024, fakeload);

        //GB
        fakeload = fakeload.withMemory(2, MemoryUnit.GB);
        assertMemoryLoad(2L*1024*1024*1024, fakeload);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoryLoadMethod2() {
        fakeload = fakeload.withMemory(-20, MemoryUnit.MB);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoryLoadMethod3() {
        fakeload = fakeload.withMemory(20, null);
    }


    @Test
    public void testAddLoadMethod() {
        FakeLoad child1 = fakeload.lasting(100, TimeUnit.MILLISECONDS).withCpu(20);
        FakeLoad child2 = fakeload.lasting(10, TimeUnit.SECONDS).withMemory(100, MemoryUnit.MB);

        assertEquals(0, fakeload.getInnerLoads().size());
        fakeload = fakeload.addLoad(child1)
                .addLoad(child2);
        assertEquals(2, fakeload.getInnerLoads().size());
    }

    @Test
    public void testAddLoadsMethod() {
        FakeLoad child1 = fakeload.lasting(100, TimeUnit.MILLISECONDS).withCpu(20);
        FakeLoad child2 = fakeload.lasting(10, TimeUnit.SECONDS).withMemory(100, MemoryUnit.MB);
        FakeLoad child3 = fakeload.lasting(10, TimeUnit.SECONDS).withMemory(300, MemoryUnit.MB);
        List<FakeLoad> children = new ArrayList<>();
        children.add(child2);
        children.add(child3);


        fakeload = fakeload.addLoad(child1);
        assertEquals(1, fakeload.getInnerLoads().size());
        fakeload = fakeload.addLoads(children);
        assertEquals(3, fakeload.getInnerLoads().size());

    }

//--------------------------------------------------------------------------------------------------
//   TEST OTHER METHODS
//--------------------------------------------------------------------------------------------------

    @Test
    public void testEqualsMethodReflexivity() {
        FakeLoad child1 = new SimpleFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2 = new SimpleFakeLoad(500, TimeUnit.MILLISECONDS);
        FakeLoad child3 = new SimpleFakeLoad(1, TimeUnit.MINUTES);

        // test reflexivity
        assertEqualsReflexivity(fakeload);


        fakeload = fakeload.lasting(300, TimeUnit.MILLISECONDS).repeat(3)
                .withCpu(50)
                .withMemory(300, MemoryUnit.MB)
                .withDiskInput(5000);
        assertEqualsReflexivity(fakeload);



        fakeload = fakeload.lasting(30, TimeUnit.SECONDS).repeat(6)
                .withCpu(80)
                .withMemory(300, MemoryUnit.KB)
                .withDiskInput(51200)
                .addLoad(child1).addLoad(child2).addLoad(child3);
        assertEqualsReflexivity(fakeload);

    }


    @Test
    public void testEqualsMethodSymmetry() {
        FakeLoad parent1, parent2;
        FakeLoad child1 = new SimpleFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2 = new SimpleFakeLoad(500, TimeUnit.MILLISECONDS);
        FakeLoad child3 = new SimpleFakeLoad(1, TimeUnit.MINUTES);
        FakeLoad child1Copy = new SimpleFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2Copy = new SimpleFakeLoad(500, TimeUnit.MILLISECONDS);

        // parent load identical, children identical
        parent1 = fakeload.lasting(20, TimeUnit.SECONDS).withCpu(30)
                .addLoad(child1)
                .addLoad(child2);
        parent2 = fakeload.lasting(20, TimeUnit.SECONDS).withCpu(30)
                .addLoad(child1)
                .addLoad(child2);

        assertEquals(parent1, parent2);
        assertEqualsSymmetry(parent1, parent2);


        // parent load identical, children logically equal
        parent2 = fakeload.lasting(20, TimeUnit.SECONDS).withCpu(30)
                .addLoad(child1Copy)
                .addLoad(child2Copy);

        assertEquals(parent1, parent2);
        assertEqualsSymmetry(parent1, parent2);


        // parent load identical, children different
        parent2 = new SimpleFakeLoad().lasting(20, TimeUnit.SECONDS).withCpu(30)
                .addLoad(child1).addLoad(child3);

        assertNotEquals(parent1, parent2);
        assertEqualsSymmetry(parent1, parent2);



        // parent load different, children identical
        parent1 = fakeload.lasting(10, TimeUnit.SECONDS).withCpu(20)
                .addLoad(child1)
                .addLoad(child2);

        parent2 = fakeload.lasting(20, TimeUnit.SECONDS).withCpu(30)
                .addLoad(child1)
                .addLoad(child2);

        assertNotEquals(parent1, parent2);
        assertEqualsSymmetry(parent1, parent2);

        // parent load different, children logically equal
        parent1 = fakeload.lasting(10, TimeUnit.SECONDS).withCpu(20)
                .addLoad(child1)
                .addLoad(child2);

        parent2 = fakeload.lasting(20, TimeUnit.SECONDS).withCpu(30)
                .addLoad(child1Copy)
                .addLoad(child2Copy);

        assertNotEquals(parent1, parent2);
        assertEqualsSymmetry(parent1, parent2);

        // parent load different, children different
        parent1 = fakeload.lasting(10, TimeUnit.SECONDS).withCpu(20)
                .addLoad(child1)
                .addLoad(child2);

        parent2 = fakeload.lasting(20, TimeUnit.SECONDS).withCpu(30)
                .addLoad(child1)
                .addLoad(child3);

        assertNotEquals(parent1, parent2);
        assertEqualsSymmetry(parent1, parent2);
    }

    @Test
    public void testEqualsMethodTransitivity() {
        FakeLoad parent1, parent2, parent3;
        FakeLoad child1 = new SimpleFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2 = new SimpleFakeLoad(500, TimeUnit.MILLISECONDS);
        FakeLoad child1Copy = new SimpleFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2Copy = new SimpleFakeLoad(500, TimeUnit.MILLISECONDS);
        FakeLoad child1Copy2 = new SimpleFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2Copy2 = new SimpleFakeLoad(500, TimeUnit.MILLISECONDS);

        parent1 = fakeload.lasting(10, TimeUnit.SECONDS)
                .addLoad(child1).addLoad(child2);

        parent2 = fakeload.lasting(10, TimeUnit.SECONDS)
                .addLoad(child1Copy).addLoad(child2Copy);

        parent3 = fakeload.lasting(10, TimeUnit.SECONDS)
                .addLoad(child1Copy2).addLoad(child2Copy2);

        assertEqualsTransitivity(parent1, parent2, parent3);
    }

    @Test
    public void testHashCode() {
        FakeLoad child1 = new SimpleFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2 = new SimpleFakeLoad(500, TimeUnit.MILLISECONDS);

        FakeLoad f1 = fakeload.addLoad(child1).addLoad(child2);
        FakeLoad f2 = fakeload.addLoad(child1).addLoad(child2);

        assertEquals(f1.hashCode(), f2.hashCode());

        f1 = fakeload.addLoad(child1).addLoad(child2);
        f2 = fakeload.addLoad(child2).addLoad(child1);

        assertNotEquals(f1.hashCode(), f2.hashCode());

    }

    @Test
    public void testContainsMethod1() {
        FakeLoad twin1 = fakeload.lasting(5, TimeUnit.SECONDS);
        FakeLoad twin2 = fakeload.lasting(5, TimeUnit.SECONDS);
        FakeLoad other = fakeload.lasting(3, TimeUnit.SECONDS);

        assertTrue(twin1.contains(twin2));
        assertTrue(twin2.contains(twin1));
        assertFalse(twin1.contains(other));
        assertFalse(twin2.contains(other));
    }


    @Test
    public void testContainsMethod2() {
        FakeLoad child1 = fakeload.lasting(5, TimeUnit.SECONDS);
        FakeLoad child2 = fakeload.lasting(500, TimeUnit.MILLISECONDS);
        FakeLoad sameAsChild1 = fakeload.lasting(5, TimeUnit.SECONDS);
        FakeLoad other = fakeload.lasting(3, TimeUnit.SECONDS);

        // adding loads creates a new CompositeFakeLoad
        FakeLoad parent = fakeload.lasting(10, TimeUnit.SECONDS)
                .addLoad(child1)
                .addLoad(child2);

        assertTrue(parent.contains(child1));
        assertTrue(parent.contains(child2));
        assertTrue(parent.contains(sameAsChild1));
        assertFalse(parent.contains(other));
    }


    @Test(expected = NoSuchElementException.class)
    public void testIteratorMethod1() {
        FakeLoad simple = new SimpleFakeLoad().lasting(12, TimeUnit.SECONDS)
                .withCpu(80)
                .withMemory(300, MemoryUnit.KB);

        Iterator<FakeLoad> iterator = simple.iterator();
        assertTrue(iterator.hasNext());
        FakeLoad next = iterator.next();
        assertEquals(simple, next);
        assertFalse(iterator.hasNext());
        // now throws an exception
        iterator.next();
    }


    @Test(expected = NoSuchElementException.class)
    public void testIteratorMethod2() {
        FakeLoad simple = new SimpleFakeLoad().lasting(1111, TimeUnit.MILLISECONDS)
                .withCpu(99)
                .withMemory(9999, MemoryUnit.BYTES);

        FakeLoad parent = simple;
        List<FakeLoad> children = new ArrayList<>();
        int noOfChildren = 10;
        long startDuration = 1;
        long startCPU = 10;
        long startMemory = 100;

        for (int i=0; i<noOfChildren; i++) {
            FakeLoad child = new SimpleFakeLoad().lasting(startDuration*i, TimeUnit.SECONDS)
                    .withCpu(startCPU*i)
                    .withMemory(startMemory*i, MemoryUnit.KB);

            children.add(child);
            parent = parent.addLoad(child);
        }
        // check that for loop is correct
        assertEquals(noOfChildren, children.size());


        // now test iterator
        Iterator<FakeLoad> iterator = parent.iterator();
        assertTrue(iterator.hasNext());
        FakeLoad next = iterator.next();
        assertEquals(simple, next);


        for (int i=0; i<noOfChildren; i++) {
            assertTrue(iterator.hasNext());
            next = iterator.next();
            assertEquals(children.get(i), next);
        }

        assertFalse(iterator.hasNext());
        // now throws an exception
        iterator.next();

    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorMethod3() {
        FakeLoad simple = new SimpleFakeLoad().lasting(9999, TimeUnit.MILLISECONDS)
                .withCpu(99)
                .withMemory(9999, MemoryUnit.BYTES);

        int noOfChildren = 10;
        int noOfGrandChildrenPerChild = 9;
        FakeLoad parent = simple;
        List<FakeLoad> children = new ArrayList<>();
        List<FakeLoad> grandChildren = new ArrayList<>();

        long startDuration = 10;
        long startCPU = 1;
        long startMemory = 1000;

        // create children
        for (int i=0; i<noOfChildren; i++) {
            FakeLoad child = new SimpleFakeLoad().lasting(startDuration*i, TimeUnit.SECONDS)
                    .withCpu(startCPU*i)
                    .withMemory(startMemory*i, MemoryUnit.KB);
            children.add(i, child);

            // create grand children
            for (int j=0; j<noOfGrandChildrenPerChild; j++) {
                FakeLoad grandChild = new SimpleFakeLoad().lasting(startDuration*i+j, TimeUnit.SECONDS)
                        .withCpu(startCPU+j)
                        .withMemory(startMemory*i+j, MemoryUnit.KB);

                grandChildren.add(i*noOfGrandChildrenPerChild+j, grandChild);
                child = child.addLoad(grandChild);
            }

            parent = parent.addLoad(child);

        }

        // Check that for-loops are correct
        assertEquals(noOfChildren, children.size());
        assertEquals(noOfGrandChildrenPerChild*noOfChildren, grandChildren.size());

        // Now test iterator
        Iterator<FakeLoad> iterator = parent.iterator();
        assertTrue(iterator.hasNext());
        FakeLoad next = iterator.next();
        assertEquals(simple, next);

        for (int i=0; i<noOfChildren; i++) {
            assertTrue(iterator.hasNext());
            next = iterator.next();
            assertEquals(children.get(i), next);

            for (int j=0; j<noOfGrandChildrenPerChild; j++) {
                assertTrue(iterator.hasNext());
                next = iterator.next();
                assertEquals(grandChildren.get(i*noOfGrandChildrenPerChild+j), next);
            }
        }

        assertFalse(iterator.hasNext());
        // now throws an exception
        iterator.next();

    }






//--------------------------------------------------------------------------------------------------
//   HELPER METHODS
//--------------------------------------------------------------------------------------------------


    void assertDefault(FakeLoad actual) {
        assertEquals(0, actual.getRepetitions());
        assertEquals(0L, actual.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, actual.getTimeUnit());
        assertEquals(0, actual.getCpuLoad());
        assertEquals(0L, actual.getMemoryLoad());
        assertEquals(0L, actual.getDiskIOLoad());
        assertEquals(0, actual.getInnerLoads().size());
    }
    
    void assertDuration(long expectedDuration, TimeUnit expectedUnit, FakeLoad actual) {
        assertEquals(0, actual.getRepetitions());
        assertEquals(expectedDuration, actual.getDuration());
        assertEquals(expectedUnit, actual.getTimeUnit());
        assertEquals(0, actual.getCpuLoad());
        assertEquals(0L, actual.getMemoryLoad());
        assertEquals(0L, actual.getDiskIOLoad());
        assertEquals(0, actual.getInnerLoads().size());
    }

    void assertInnerLoadsSize(int expectedSize, FakeLoad actual) {
        assertEquals(0, actual.getRepetitions());
        assertEquals(0L, actual.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, actual.getTimeUnit());
        assertEquals(0, actual.getCpuLoad());
        assertEquals(0L, actual.getMemoryLoad());
        assertEquals(0L, actual.getDiskIOLoad());
        assertEquals(expectedSize, actual.getInnerLoads().size());
    }
    
    void assertRepetitions(int expectedRepetitions, FakeLoad actual) {
        assertEquals(expectedRepetitions, actual.getRepetitions());
        assertEquals(0L, actual.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, actual.getTimeUnit());
        assertEquals(0, actual.getCpuLoad());
        assertEquals(0L, actual.getMemoryLoad());
        assertEquals(0L, actual.getDiskIOLoad());
        assertEquals(0, actual.getInnerLoads().size());
    }
    
    private void assertCpuLoad(int expectedCpu, FakeLoad actual) {
        assertEquals(0, actual.getRepetitions());
        assertEquals(0L, actual.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, actual.getTimeUnit());
        assertEquals(expectedCpu, actual.getCpuLoad());
        assertEquals(0L, actual.getMemoryLoad());
        assertEquals(0L, actual.getDiskIOLoad());
        assertEquals(0, actual.getInnerLoads().size());
    }
    
    private void assertMemoryLoad(long expectedMemory, FakeLoad actual) {
        assertEquals(0, actual.getRepetitions());
        assertEquals(0L, actual.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, actual.getTimeUnit());
        assertEquals(0, actual.getCpuLoad());
        assertEquals(expectedMemory, actual.getMemoryLoad());
        assertEquals(0L, actual.getDiskIOLoad());
        assertEquals(0, actual.getInnerLoads().size());
    }


    private void assertDiskIOLoad(long expectedDiskIO, FakeLoad actual) {
        // TODO
    }




    public void assertEqualsReflexivity(FakeLoad load) {
        assertEquals(load, load);
        assertNotEquals(load, null);
        assertNotEquals(null, load);
    }

    public void assertEqualsSymmetry(FakeLoad f1, FakeLoad f2) {
        if (f1.equals(f2)) {
            assertEquals(f1, f2);
            assertEquals(f2, f1);
        } else {
            assertNotEquals(f1, f2);
            assertNotEquals(f2, f1);
        }
    }

    public void assertEqualsTransitivity(FakeLoad f1, FakeLoad f2, FakeLoad f3) {
        MoreAsserts.assertEqualsTransitivity(f1, f2, f3);
        MoreAsserts.assertEqualsTransitivity(f1, f3, f2);
        MoreAsserts.assertEqualsTransitivity(f3, f1, f2);
        MoreAsserts.assertEqualsTransitivity(f3, f2, f1);
        MoreAsserts.assertEqualsTransitivity(f2, f3, f1);
        MoreAsserts.assertEqualsTransitivity(f2, f1, f2);


    }




}
