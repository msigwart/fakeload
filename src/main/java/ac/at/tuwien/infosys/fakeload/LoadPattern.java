package ac.at.tuwien.infosys.fakeload;

import java.util.concurrent.TimeUnit;

/**
 * The LoadPattern interface represents load instructions for the simulation infrastructure of the FakeLoad Library.
 * With a LoadPattern instance the user can specify what kind of load (e.g. CPU, RAM, …), how much of it (e.g. 80%, 1024MB, …)
 * and for how long a load (e.g. 10 s, 100 ms, …) is being simulated.
 *
 * <p> Each {@link FakeLoad} object essentially contains a {@code LoadPattern} instance which contains the actual instructions
 * for the load simulation.
 *
 * <p> The name {@code LoadPattern} derives from the fact that other than defining simple load instructions,
 * a {@code LoadPattern} instance can also consist of multiple other {@code LoadPattern} instances,
 * thus forming complex patterns of different loads.
 *
 * <p> Users can create instances of {@code LoadPattern} instances through the static factory methods of class {@link LoadPatterns}.
 *
 *
 * @author Marten Sigwart
 * @since 1.0
 *
 * @see FakeLoad
 * @see LoadPatterns
 *
 *
 */
public interface LoadPattern {

    /**
     * Returns the number of repetitions of the load pattern.
     * The number of repetitions specify how often a pattern is repeated during load simulation.
     *
     * @return the number of repetitions
     */
    int getRepetitions();

    /**
     * Sets the number of repetitions for this load pattern.
     * This means how many times the pattern will be repeated during load simulation.
     *
     * @param noOfRepetitions the number of repetitions
     * @throws IllegalArgumentException if the passed number of repetitions is negative.
     */
    void setRepetitions(int noOfRepetitions);

    /**
     * Adds the specified pattern to the calling {@code LoadPattern} instance.
     *
     * <p> This operation might be handled differently by different {@code LoadPattern} implementations.
     * In particular, some patterns might not permit the adding of internal load patterns.
     * In this case LoadPattern classes should clearly specify in their documentation how they handle this operation.
     *
     * @param pattern LoadPattern to be added to the calling load pattern.
     */
    void addLoad(LoadPattern pattern);

    /**
     * Creates a {@code LoadPattern} object from the specified parameters and adds it to the calling pattern.
     *
     * <p> Parameter <b>loads</b> is a String array containing load instructions for different simulation aspects.
     * <ul>
     *  <li> CPU load is specified as percentage, e.g. for a CPU load of 60 percent specify "60%"</li>
     *  <li> Memory can be specified as Bytes(b), kB(k), MB(m) or GB(g), e.g. for a memory allocation of 1024 MB specify "1024m"</li>
     *  <li>TODO Network IO</li>
     *  <li>TODO File IO</li>
     * </ul>
     * </p>
     *
     * <p> After creation the new pattern is added via {@link #addLoad(LoadPattern)}.
     *
     * @param duration how long the specified loads shall be executed in milliseconds (ms)
     * @param loads strings containing specific load instructions for different aspects.
     */
    void addLoad(long duration, String... loads);

    /**
     * Creates a {@code LoadPattern} object from the specified parameters and adds it to the calling pattern.
     *
     * <p>This operation differs from {@link #addLoad(long, String...)} only in that users can specify a
     * time unit together with the duration of the load pattern.
     *
     * <p> Parameter <b>loads</b> is a String array containing load instructions for different simulation aspects.
     * <ul>
     *  <li> CPU load is specified as percentage, e.g. for a CPU load of 60 percent specify "60%"</li>
     *  <li> Memory can be specified as Bytes(b), kB(k), MB(m) or GB(g), e.g. for a memory allocation of 1024 MB specify "1024m"</li>
     *  <li>TODO Network IO</li>
     *  <li>TODO File IO</li>
     * </ul>
     * </p>
     *
     * <p> After creation the new pattern is added via {@link #addLoad(LoadPattern)}.
     *
     * @param duration how long the specified loads shall be executed in milliseconds
     * @param unit the time unit of the duration
     * @param loads strings containing specific load instructions for different aspects.
     */
    void addLoad(long duration, TimeUnit unit, String... loads);

    //TODO javadoc
    void addIntervalLoad(long duration, TimeUnit unitDuration, long interval, TimeUnit unitInterval, String... loads);

    /**
     * Removes the specified pattern from the calling load pattern.
     *
     * <p>TODO explain more details
     *
     * @param pattern the LoadPattern to remove.
     */
    void removeLoad(LoadPattern pattern);

    /**
     * Checks whether this load pattern contains the specified {@code LoadPattern} instance.
     *
     * @param pattern the pattern to check for
     * @return true if pattern exists, false if not
     */
    boolean contains(LoadPattern pattern);

}
