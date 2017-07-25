package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;

/**
 * Interface to represent a fakeload dispatcher. A fakeload dispatcher is responsible
 * for scheduling and dispatching load instructions contained in {@link FakeLoad} objects
 * to the simulation infrastructure.
 *
 * <p>
 * Whenever a {@link FakeLoad#execute()} is called, the fake load gets propagated to
 * fake load dispatcher. Then, the system load instructions contained within the
 * FakeLoad object are scheduled and dispatched to the simulation infrastructure.
 *
 * <p>
 * The simulation infrastructure consists of multiple threads each with a different simulation task.
 * For more information on the simulation infrastructure see {@link DefaultInfrastructure}.
 *
 * @see FakeLoad
 * @see DefaultInfrastructure
 * @since 1.8
 * @author Marten Sigwart
 */
interface FakeLoadDispatcher {

}
