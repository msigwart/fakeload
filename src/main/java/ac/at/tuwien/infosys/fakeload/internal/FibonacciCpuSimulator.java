package ac.at.tuwien.infosys.fakeload.internal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martensigwart on 17.05.17.
 */
public class FibonacciCpuSimulator extends AbstractCpuSimulator {

    private static final Logger log = LoggerFactory.getLogger(FibonacciCpuSimulator.class);

    private long fib0 = 0;
    private long fib1 = 1;

    @Override
    public void simulateCpu() {
        long fib2 = fib0 + fib1;
        fib0 = fib1;
        fib1 = fib2;
    }

}
