package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;

import java.util.concurrent.Future;

/**
 * Interface to represent a fakeload dispatcher. A fakeload dispatcher is responsible
 * for scheduling and dispatching load instructions contained in {@link FakeLoad} objects
 * to the simulation infrastructure.
 *
 * <p>
 * Whenever a {@link FakeLoad#execute()} is called, the fake load gets propagated to
 * fake load dispatcher via {@link #submitLoad(FakeLoad)}. The dispatcher is responsible
 * for propagating the load instructions of the {@code FakeLoad} object to an underlying
 * simulation infrastructure.
 *
 *
 * @see FakeLoad
 * @since 1.8
 * @author Marten Sigwart
 */
public interface FakeLoadDispatcher {

    /**
     * Submits a {@code FakeLoad} object to an underlying simulation infrastructure.
     *
     * @param load
     * @return a Future representing the pending completion of the FakeLoad.
     */
    Future<String> submitLoad(FakeLoad load);

}
