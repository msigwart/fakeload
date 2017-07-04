package ac.at.tuwien.infosys.fakeload;

import ac.at.tuwien.infosys.fakeload.internal.FakeLoadDispatcher;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Represents the main API for the FakeLoad Library.
 * If a client wants to simulate a certain system load he/she should instantiate a FakeLoad object
 * and then, subsequently, call that object's execute method.
 *
 * @Author Marten Sigwart
 * @version 1.0
 */
public final class FakeLoad {


    private LoadPattern load;


    public FakeLoad() {
        load = new LoadPattern();
    }

    public FakeLoad(LoadPattern pattern) {
        load = pattern;
    }

    public FakeLoad(long duration, String... loads) {
        load = new LoadPattern();
        load.addLoad(duration, loads);
    }

    public FakeLoad(String duration, String... loads) {
        load = new LoadPattern();
        load.addLoad(duration, loads);
    }





    public void setLoad(LoadPattern pattern) {
        this.load = pattern;
    }

    public void setLoad(long duration, String... loads) {
        this.load = new LoadPattern();
        load.addLoad(duration, loads);
    }

    public void setLoad(String duration, String... loads) {
        this.load = new LoadPattern();
        load.addLoad(duration, loads);
    }


    /**
     *  This method submits the load pattern of the FakeLoad instance to the {@Link FakeLoadDispatcher} singleton instance.
     *  The method blocks until the requested load simulation completes.
     */
    public void execute() {
        try {

            // wait until simulation completes
            Future<String> future = FakeLoadDispatcher.getInstance().submitLoad(load);
            String response = future.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
