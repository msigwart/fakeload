package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.LoadPattern;
import ac.at.tuwien.infosys.fakeload.SimpleLoadPattern;

import java.util.concurrent.*;

/**
 * This class is responsible for dispatching fake load requests to the simulation infrastructure.
 *
 *
 * @Author Marten Sigwart
 */
public class FakeLoadDispatcher {

//-------------------------------------------------------------
// Singleton Methods
//-------------------------------------------------------------

    /** Singleton instance */
    private static FakeLoadDispatcher instance;

    /**
     * Retrieves this class' singleton instance
     * @return the singleton FakeLoadDispatcher instance
     */
    public static FakeLoadDispatcher getInstance() {
        if (instance == null) {
            instance = new FakeLoadDispatcher();
        }
        return instance;
    }



    /**
     * Represents the connection to the simulation infrastructure through which load requests can be dispatched.
     */
    private Connection connection;

    /** Executor Service for scheduling simulation loads */
    private ScheduledExecutorService scheduler;


    /**
     * Private constructor for use in singleton pattern.
     */
    private FakeLoadDispatcher() {
        this.connection = InfrastructureManager.getInstance().getConnection();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }






    public Future<String> submitLoad(LoadPattern pattern) {
        return handleLoad(pattern);
    }


    private Future<String> handleLoad(LoadPattern pattern) {
        connection.dispatchLoad();

        return scheduler.schedule(() -> {

            connection.dispatchLoad();
            return "";

        }, 2, TimeUnit.SECONDS);
    }



//    private void connectToInfrastructure() {
//        //connection = InfrastructureManager.getInstance().getConnection();
//    }
}
