package ac.at.tuwien.infosys.fakeload;

import ac.at.tuwien.infosys.fakeload.internal.FakeLoadDispatcher;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public abstract class AbstractFakeLoad implements FakeLoad {

    /**
     *  This method submits the load pattern of the AbstractFakeLoad instance to the {@Link FakeLoadDispatcher} singleton instance.
     *  The method blocks until the requested load simulation completes.
     */
    @Override
    public void execute() {
        try {

            // wait until simulation completes
            Future<String> future = FakeLoadDispatcher.getInstance().submitLoad(this);
            String response = future.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean contains(FakeLoad load) {
        if (this == load) {
            return true;
        }

        // check if pattern is contained in child patterns
        for (FakeLoad l : this.getLoads()) {
            return l.contains(load);
        }

        // if not found anywhere return false
        return false;
    }

    @Override
    public void simulateCpu() {
        // TODO
    }

    @Override
    public void simulateMemory() {
        // TODO
    }

    @Override
    public void simulateNetIO() {
        // TODO
    }

    @Override
    public void simulateDiskIO() {
        // TODO
    }

//    private LoadPattern load;
//
//
//    public AbstractFakeLoad() {
//        load = LoadPatterns.createLoadPattern();
//    }
//
//    public AbstractFakeLoad(LoadPattern pattern) {
//        load = pattern;
//    }
//
//    public AbstractFakeLoad(long duration, String... loads) {
//        load = LoadPatterns.createLoadPattern();
//        load.addLoad(duration, loads);
//    }
//
//    public AbstractFakeLoad(long duration, TimeUnit unit, String... loads) {
//        load = LoadPatterns.createLoadPattern();
//        load.addLoad(duration, unit, loads);
//    }
//
//
//
//
//
//    public void setLoad(LoadPattern pattern) {
//        this.load = pattern;
//    }
//
//    public void setLoad(long duration, String... loads) {
//        this.load = LoadPatterns.createLoadPattern();
//        load.addLoad(duration, loads);
//    }
//
//    public void setLoad(long duration, TimeUnit unit, String... loads) {
//        this.load = LoadPatterns.createLoadPattern();
//        load.addLoad(duration, unit, loads);
//    }




}
