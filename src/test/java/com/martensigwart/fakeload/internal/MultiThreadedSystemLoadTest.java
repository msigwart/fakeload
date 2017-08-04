package com.martensigwart.fakeload.internal;

import java.util.LinkedList;
import java.util.List;

import com.martensigwart.fakeload.internal.util.Decrease;
import com.martensigwart.fakeload.internal.util.Increase;
import com.martensigwart.fakeload.internal.util.Instruction;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests to test concurrent behaviour of class {@link SystemLoad}
 */
public class MultiThreadedSystemLoadTest {
    @Test
    public void test() {

        List<Instruction> schedule1 = new LinkedList<>();
        schedule1.add(new Increase(Instruction.LoadType.CPU, 1, 20));
        schedule1.add(new Increase(Instruction.LoadType.CPU, 2, 20));
        schedule1.add(new Increase(Instruction.LoadType.CPU, 2, 20));
        schedule1.add(new Decrease(Instruction.LoadType.CPU, 2, 20));
        schedule1.add(new Decrease(Instruction.LoadType.CPU, 2, 20));
        schedule1.add(new Decrease(Instruction.LoadType.CPU, 2, 20));

        List<Instruction> schedule2 = new LinkedList<>();
        schedule2.add(new Increase(Instruction.LoadType.CPU, 2, 20));
        schedule2.add(new Increase(Instruction.LoadType.CPU, 2, 20));
        schedule2.add(new Decrease(Instruction.LoadType.CPU, 2, 20));
        schedule2.add(new Decrease(Instruction.LoadType.CPU, 2, 20));

        SystemLoad con = new SystemLoad();
        ConnectionRunnable r1 = new ConnectionRunnable(con, schedule1);
        ConnectionRunnable r2 = new ConnectionRunnable(con, schedule2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        try {
            t1.start();
            t2.start();
            // 0 s
            Assert.assertEquals(0, con.getCpu());
            Thread.sleep(1500);
            // 1,5 s
            Assert.assertEquals(20, con.getCpu());
            Thread.sleep(1000);
            // 2,5 s
            Assert.assertEquals(40, con.getCpu());
            Thread.sleep(1000);
            // 3,5 s
            Assert.assertEquals(60, con.getCpu());
            Thread.sleep(1000);
            // 4,5 s
            Assert.assertEquals(80, con.getCpu());
            Thread.sleep(1000);
            // 5,5
            Assert.assertEquals(100, con.getCpu());
            Thread.sleep(1000);
            // 6,5
            Assert.assertEquals(80, con.getCpu());
            Thread.sleep(1000);
            // 7,5
            Assert.assertEquals(60, con.getCpu());
            Thread.sleep(1000);
            // 8,5
            Assert.assertEquals(40, con.getCpu());
            Thread.sleep(1000);
            // 9,5
            Assert.assertEquals(20, con.getCpu());
            Thread.sleep(2000);
            // 11,5
            Assert.assertEquals(0, con.getCpu());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
