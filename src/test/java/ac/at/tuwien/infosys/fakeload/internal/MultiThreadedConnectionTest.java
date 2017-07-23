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
        schedule1.add(new Instruction(INCREASE, CPU, 2, 10));

        List<Instruction> schedule2 = new LinkedList<>();
        schedule2.add(new Instruction(INCREASE, CPU, 2, 20));

        Connection con = new Connection();
        MyRunnable r1 = new MyRunnable(con, schedule1);
        MyRunnable r2 = new MyRunnable(con, schedule2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        try {
            t1.start();
            t2.start();
            Thread.sleep(3*1000);

            Assert.assertEquals(30, con.getCpuLoad());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
