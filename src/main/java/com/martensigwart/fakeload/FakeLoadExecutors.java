package com.martensigwart.fakeload;

/**
 * Factory methods that create {@link FakeLoadExecutor} instances.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public class FakeLoadExecutors {

    private static SimulationInfrastructure defaultInfrastructure;

    public static synchronized FakeLoadExecutor newDefaultExecutor() {
        if (defaultInfrastructure == null) {
            try {
                defaultInfrastructure = new DefaultSimulationInfrastructure();
                
            } catch (InstantiationException | IllegalAccessException e) {
                throw new AssertionError(e.getMessage());
            }
        }
        return new DefaultFakeLoadExecutor(new DefaultFakeLoadScheduler(defaultInfrastructure));
    }

    // prevent instantiation by suppressing default constructor
    private FakeLoadExecutors() {
        throw new AssertionError();
    }
}
