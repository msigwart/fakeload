package ac.at.tuwien.infosys.fakeload.internal;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import static ac.at.tuwien.infosys.fakeload.internal.Instruction.Type.*;
import static ac.at.tuwien.infosys.fakeload.internal.MyRunnable.LoadType.CPU;

/**
 * Unit tests to test concurrent behaviour of class {@link Connection}
 */
public class MultiThreadedConnectionTest {
    @Test
    public void test() {

        List<Instruction> schedule1 = new LinkedList<>();
        schedule1.add(new Instruction(INCREASE, CPU, 1, 20));
        schedule1.add(new Instruction(INCREASE, CPU, 2, 20));
        schedule1.add(new Instruction(INCREASE, CPU, 2, 20));
        schedule1.add(new Instruction(DECREASE, CPU, 2, 20));
        schedule1.add(new Instruction(DECREASE, CPU, 2, 20));
        schedule1.add(new Instruction(DECREASE, CPU, 2, 20));

        List<Instruction> schedule2 = new LinkedList<>();
        schedule2.add(new Instruction(INCREASE, CPU, 2, 20));
        schedule2.add(new Instruction(INCREASE, CPU, 2, 20));
        schedule2.add(new Instruction(DECREASE, CPU, 2, 20));
        schedule2.add(new Instruction(DECREASE, CPU, 2, 20));

        Connection con = new Connection();
        MyRunnable r1 = new MyRunnable(con, schedule1);
        MyRunnable r2 = new MyRunnable(con, schedule2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        try {
            t1.start();
            t2.start();
            // 0 s
            Assert.assertEquals(0, con.getCpuLoad());
            Thread.sleep(1500);
            // 1,5 s
            Assert.assertEquals(20, con.getCpuLoad());
            Thread.sleep(1000);
            // 2,5 s
            Assert.assertEquals(40, con.getCpuLoad());
            Thread.sleep(1000);
            // 3,5 s
            Assert.assertEquals(60, con.getCpuLoad());
            Thread.sleep(1000);
            // 4,5 s
            Assert.assertEquals(80, con.getCpuLoad());
            Thread.sleep(1000);
            // 5,5
            Assert.assertEquals(100, con.getCpuLoad());
            Thread.sleep(1000);
            // 6,5
            Assert.assertEquals(80, con.getCpuLoad());
            Thread.sleep(1000);
            // 7,5
            Assert.assertEquals(60, con.getCpuLoad());
            Thread.sleep(1000);
            // 8,5
            Assert.assertEquals(40, con.getCpuLoad());
            Thread.sleep(1000);
            // 9,5
            Assert.assertEquals(20, con.getCpuLoad());
            Thread.sleep(2000);
            // 11,5
            Assert.assertEquals(0, con.getCpuLoad());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
