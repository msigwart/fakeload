package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.LoadPattern;

import java.util.concurrent.Future;

/**
 * Created by martensigwart on 29.06.17.
 */
public class FakeLoadDispatcher {

    /**
     * Singleton instance
     */
    private static FakeLoadDispatcher instance;

    /**
     * Retrieves this class' singleton instance
     * @return the singleton FakeLoadDispatcher instance instance
     */
    public static FakeLoadDispatcher getInstance() {
        if (instance == null) {
            instance = new FakeLoadDispatcher();
        }
        return instance;

    }


    private FakeLoadDispatcher() {
        startSimulationInfrastructure();
    }




    public Future<String> submitLoad(LoadPattern pattern) {
        return handleLoad(pattern);
    }


    private Future<String> handleLoad(LoadPattern pattern) {
        return null;
    }

    private void startSimulationInfrastructure() {

    }
}
