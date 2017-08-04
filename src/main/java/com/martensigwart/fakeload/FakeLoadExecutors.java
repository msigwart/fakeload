package com.martensigwart.fakeload;

/**
 * Created by martensigwart on 26.07.17.
 */
public class FakeLoadExecutors {

    private static SimulationInfrastructure defaultInfrastructure;

    public static synchronized FakeLoadExecutor newDefaultExecutor() {
        if (defaultInfrastructure == null) {
            defaultInfrastructure = new DefaultSimulationInfrastructure();
        }
        return new DefaultFakeLoadExecutor(new DefaultFakeLoadScheduler(defaultInfrastructure));
    }

    // prevent instantiation by suppressing default constructor
    private FakeLoadExecutors() {
        throw new AssertionError();
    }
}
