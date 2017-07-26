package ac.at.tuwien.infosys.fakeload.internal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for class {@link Connection}
 */
public class SingleThreadedConnectionTest {


//    // TEST ADDITION AND SUBTRACTION
//    @Test
//    public void testCpu() {
//        Connection con = new Connection();
//
//        Assert.assertEquals(10, con.increaseAndGetCpu(10));
//        Assert.assertEquals(30, con.increaseAndGetCpu(20));
//        Assert.assertEquals(60, con.increaseAndGetCpu(30));
//        Assert.assertEquals(100, con.increaseAndGetCpu(40));
//
//        Assert.assertEquals(90, con.decreaseAndGetCpu(10));
//        Assert.assertEquals(70, con.decreaseAndGetCpu(20));
//        Assert.assertEquals(40, con.decreaseAndGetCpu(30));
//        Assert.assertEquals(0, con.decreaseAndGetCpu(40));
//
//        // test negative value
//        try {
//            con.decreaseAndGetCpu(10);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Decrease of CPU to under 0%", e.getMessage());
//            Assert.assertEquals(0, con.getCpuLoad());
//        }
//
//        // test value too high
//        try {
//            con.increaseAndGetCpu(110);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Increase of CPU to over 100%", e.getMessage());
//            Assert.assertEquals(0, con.getCpuLoad());
//        }
//    }
//
//    @Test
//    public void testMemory() {
//        Connection con = new Connection();
//
//        Assert.assertEquals(10, con.increaseAndGetMemory(10));
//        Assert.assertEquals(30, con.increaseAndGetMemory(20));
//        Assert.assertEquals(60, con.increaseAndGetMemory(30));
//        Assert.assertEquals(100, con.increaseAndGetMemory(40));
//
//        Assert.assertEquals(90, con.decreaseAndGetMemory(10));
//        Assert.assertEquals(70, con.decreaseAndGetMemory(20));
//        Assert.assertEquals(40, con.decreaseAndGetMemory(30));
//        Assert.assertEquals(0, con.decreaseAndGetMemory(40));
//
//        // test negative value
//        try {
//            con.decreaseAndGetMemory(10);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Decrease of Memory to under 0%", e.getMessage());
//            Assert.assertEquals(0, con.getMemoryLoad());
//        }
//
//        // test value too high TODO
////        try {
////            con.increaseAndGetMemory(110);
////        } catch (RuntimeException e) {
////            Assert.assertEquals("Increase of Memory to over 100%", e.getMessage());
////            Assert.assertEquals(0, con.getMemoryLoad());
////        }
//    }
//
//
//    @Test
//    public void testDiskIO() {
//        Connection con = new Connection();
//
//        Assert.assertEquals(10, con.increaseAndGetDiskIO(10));
//        Assert.assertEquals(30, con.increaseAndGetDiskIO(20));
//        Assert.assertEquals(60, con.increaseAndGetDiskIO(30));
//        Assert.assertEquals(100, con.increaseAndGetDiskIO(40));
//
//        Assert.assertEquals(90, con.decreaseAndGetDiskIO(10));
//        Assert.assertEquals(70, con.decreaseAndGetDiskIO(20));
//        Assert.assertEquals(40, con.decreaseAndGetDiskIO(30));
//        Assert.assertEquals(0, con.decreaseAndGetDiskIO(40));
//
//        // test negative value
//        try {
//            con.decreaseAndGetDiskIO(10);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Decrease of Disk IO to under 0%", e.getMessage());
//            Assert.assertEquals(0, con.getDiskIOLoad());
//        }
//
//        // test value too high TODO
////        try {
////            con.increaseAndGetDiskIO(110);
////        } catch (RuntimeException e) {
////            Assert.assertEquals("Increase of DiskIO to over 100%", e.getMessage());
////            Assert.assertEquals(0, con.getDiskIOLoad());
////        }
//    }
//
//    @Test
//    public void testNetIO() {
//        Connection con = new Connection();
//
//        Assert.assertEquals(10, con.increaseAndGetNetIO(10));
//        Assert.assertEquals(30, con.increaseAndGetNetIO(20));
//        Assert.assertEquals(60, con.increaseAndGetNetIO(30));
//        Assert.assertEquals(100, con.increaseAndGetNetIO(40));
//
//        Assert.assertEquals(90, con.decreaseAndGetNetIO(10));
//        Assert.assertEquals(70, con.decreaseAndGetNetIO(20));
//        Assert.assertEquals(40, con.decreaseAndGetNetIO(30));
//        Assert.assertEquals(0, con.decreaseAndGetNetIO(40));
//
//        // test negative value
//        try {
//            con.decreaseAndGetNetIO(10);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Decrease of Net IO to under 0%", e.getMessage());
//            Assert.assertEquals(0, con.getNetIOLoad());
//        }
//
//        // test value too high TODO
////        try {
////            con.increaseAndGetNetIO(110);
////        } catch (RuntimeException e) {
////            Assert.assertEquals("Increase of NetIO to over 100%", e.getMessage());
////            Assert.assertEquals(0, con.getNetIOLoad());
////        }
//    }


}
