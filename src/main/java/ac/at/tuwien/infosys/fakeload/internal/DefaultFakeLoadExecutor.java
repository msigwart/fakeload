package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import ac.at.tuwien.infosys.fakeload.FakeLoadExecutor;
import ac.at.tuwien.infosys.fakeload.SimpleFakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A {@link FakeLoadExecutor} that executes each submitted {@link FakeLoad} by passing passing it on to an underlying
 * {@link SimulationInfrastructure}.
 *
 * <p>
 * This class consists of a {@code SimulationInfrastructure} instance and a scheduler.
 * The infrastructure is responsible for actually executing load instructions of {@code FakeLoad} objects
 * submitted via {@code execute(FakeLoad)}. The scheduler is responsible for scheduling the propagation of load
 * instructions to the infrastructure at the right time. Further the scheduler is also responsible for reducing the
 * load again once it has run.
 *
 * <p>
 * Multiple {@code FakeLoad} objects being executed simultaneously produce a system load which is the aggregation of all
 * load instructions contained in the objects. If thread A submits a {@code FakeLoad} of 20% CPU and thread B submits
 * a {@code FakeLoad} of 30% CPU the resulting system load should be ~ 50%.
 *
 * <p>TODO NOT UP TO DATE ANYMORE
 * The {@code DefaultFakeLoadExecutor} is responsible for this aggregation as well as reporting any faults concerning
 * any passing of load limitations of the system. For example executing a CPU load of more than 100% is not possible,
 * therefore if accumulated CPU load of the FakeLoad instances being executed exceeds that an error should be thrown.
 *
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public final class DefaultFakeLoadExecutor implements FakeLoadExecutor {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadExecutor.class);

    private final FakeLoadScheduler scheduler;


    /**
     * Constructor
     */
    public DefaultFakeLoadExecutor(FakeLoadScheduler scheduler) {
        this.scheduler = scheduler;
    }


    @Override
    public void execute(FakeLoad load) {
        try {
            log.debug("Starting FakeLoad execution...");
            log.trace("Executing {}", load);
            Future<Void> future = scheduler.schedule(load);
            future.get();
            log.debug("Finished FakeLoad execution.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }



}
