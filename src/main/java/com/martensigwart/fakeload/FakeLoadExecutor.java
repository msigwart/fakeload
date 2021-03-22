package com.martensigwart.fakeload;

import java.util.concurrent.Future;

/**
 * An object that executes submitted {@link FakeLoad} objects.
 * This interface provides a way of decoupling FakeLoad submission from the mechanics of how a FakeLoad
 * is actually simulated.
 *
 * <p>
 * Implementations of this interface should take care of scheduling and executing load instructions
 * contained in a {@code FakeLoad} as accurate as possible to provide a reliable simulation of system load.
 *
 * <p>
 * A {@code FakeLoadExecutor} can be shut down, which will cause it to reject new submitted {@code FakeLoad}s.
 * An unused {@code FakeLoadExecutor} should be shut down to allow reclamation of its resources.
 *
 * <p>
 * Note: In a 'realistic' scenario multiple {@code FakeLoad}s being executed simultaneously should
 * produce a system load which is an aggregation of all load instructions contained in the
 * {@code FakeLoad} objects. If thread A submits a FakeLoad of 20% CPU and thread B submits a
 * FakeLoad of 30% CPU the resulting system load should be ~ 50%. However, different scenarios
 * of how {@code FakeLoad}s are executed are valid as well. Therefore it is at the discretion of the
 * {@code FakeLoadExecutor} implementation of how execution of {@code FakeLoad}s is handled in
 * detail. To avoid confusion the exact behaviour should be clearly stated in the documentation
 * of the implementing class.
 *
 * <p>
 * The {@link FakeLoadExecutors} class provides convenient factory methods for different executors
 * which should suffice for most simulation scenarios.
 *
 * @see FakeLoad
 * @see FakeLoadExecutors
 * @since 1.8
 * @author Marten Sigwart
 */
public interface FakeLoadExecutor {

    /**
     * Executes the specified fake load in a blocking way.
     *
     * <p>
     * The 'fake' system load instructions (CPU, memory, other FakeLoad objects, etc.)
     * provided by the {@code FakeLoad} instance are simulated and executed by the executor.
     * After calling {@code execute} with a FakeLoad object specifying a CPU load of 80% and
     * a duration of 10 seconds, the executor will generate a CPU load of 80% for the next 10 seconds.
     *
     * <p>
     * Note: This is a blocking call, meaning the method call blocks until
     * the execution of the fake load has finished. This makes sense since a fake load represents
     * a space holder for some real computation which blocks the current thread until it has finished.
     * However, different threads calling {@code execute()} are able to do so at the same time.
     * For a non-blocking call, see {@link FakeLoadExecutor#executeAsync(FakeLoad)}
     *
     * @param load the FakeLoad to be executed
     * @throws NullPointerException if the load is null
     * @throws RuntimeException if the FakeLoad contains illegal loads or loads that
     * would exceed the limit of a specific system load (e.g. a CPU load of more than 100%)
     */
    void execute(FakeLoad load);

    /**
     * Executes the specified fake load in a non-blocking way.
     *
     * <p>
     * The 'fake' system load instructions (CPU, memory, other FakeLoad objects, etc.)
     * provided by the {@code FakeLoad} instance are simulated and executed by the executor.
     * After calling {@code execute} with a FakeLoad object specifying a CPU load of 80% and
     * a duration of 10 seconds, the executor will generate a CPU load of 80% for the next 10 seconds.
     *
     * <p>
     * Note: This is a non-blocking call, meaning the method returns a {@code Future} that completes
     * when the execution of the fake load has finished.
     * The returned Future enables the caller to cancel the fake load execution
     * (contrary to {@link FakeLoadExecutor#execute(FakeLoad)}.
     *
     * @param load the FakeLoad to be executed
     * @throws NullPointerException if the load is null
     * @throws RuntimeException if the FakeLoad contains illegal loads or loads that
     * would exceed the limit of a specific system load (e.g. a CPU load of more than 100%)
     * @return A Future that completes when the fake load execution is done.
     */
    Future<Void> executeAsync(FakeLoad load);

    /**
     * Shuts down the FakeLoad executor after it is no longer needed.
     * <p>
     * Note: Implementing classes should clean up any resources used for the fake load simulation
     * such as threads, executor services, etc.
     */
    void shutdown();

}
