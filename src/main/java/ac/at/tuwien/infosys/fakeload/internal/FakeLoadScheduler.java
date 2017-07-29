package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;

import java.util.concurrent.Future;

/**
 * Created by martensigwart on 29.07.17.
 */
public interface FakeLoadScheduler {

    Future<Void> schedule(FakeLoad fakeLoad);

}
