package ac.at.tuwien.infosys.fakeload;

import ac.at.tuwien.infosys.fakeload.internal.DefaultFakeLoadDispatcher;
import ac.at.tuwien.infosys.fakeload.internal.DefaultInfrastructure;
import ac.at.tuwien.infosys.fakeload.internal.FakeLoadDispatcher;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public abstract class AbstractFakeLoad implements FakeLoad {

    private final FakeLoadDispatcher dispatcher;

    public AbstractFakeLoad() {
        this.dispatcher = new DefaultFakeLoadDispatcher(DefaultInfrastructure.INSTANCE);
    }


    /**
     *  {@inheritDoc}
     *  This method submits the fake load to a {@Link FakeLoadDispatcher} instance.
     *  The method blocks until the fake load's execution completes or throws an error.
     */
    @Override
    public void execute() {
        try {

            // wait until simulation completes
            Future<String> future = dispatcher.submitLoad(this);
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

}
