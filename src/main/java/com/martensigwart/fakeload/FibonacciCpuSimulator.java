package com.martensigwart.fakeload;

/**
 * A {@link CpuSimulator} that simulates CPU by calculating Fibonacci sequences.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public final class FibonacciCpuSimulator extends CpuSimulator {

    private long fib0 = 0;
    private long fib1 = 1;

    @Override
    public void simulateCpu() {
        long fib2 = fib0 + fib1;
        fib0 = fib1;
        fib1 = fib2;
    }

}
