package ac.at.tuwien.infosys.fakeload;

/**
 * An object that executes submitted {@link FakeLoad} objects.
 * This interface provides a way of decoupling FakeLoad submission from the mechanics of how a FakeLoad
 * is actually simulated.
 *
 * <p>
 * Implementations of this interface should take care of scheduling and executing load instructions
 * contained in a FakeLoad as accurate as possible to provide a reliable simulation of system load.
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
     * Executes the specified fake load.
     *
     * <p>
     * This means the 'fake' system load instructions (CPU, memory, other FakeLoad objects, etc.)
     * contained withing the provided {@code FakeLoad} instance are simulated and executed by the executor.
     * For example after calling {@code execute} with a FakeLoad object specifying aCPU load of 80% and
     * a duration of 10 seconds, the executor will generate a CPU load of 80% for the next 10 seconds.
     *
     * <p>
     * Note: This should be a blocking call, meaning within a single thread the method call blocks until
     * the execution of a fake load has finished. This is due to the fact that a fake load represents
     * a spaceholder for some real computation. A real computation would take all the time it needs
     * (and would therefore block) until it has finished, therefore the fake computation should do so as well.
     * However, different threads calling {@code execute()} should be able to do so at the same time.
     *
     * @param load the FakeLoad to be executed
     * @since 1.8
     */
    void execute(FakeLoad load);


}
