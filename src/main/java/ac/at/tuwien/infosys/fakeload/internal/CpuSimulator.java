package ac.at.tuwien.infosys.fakeload.internal;

import java.util.concurrent.Callable;

/**
 * Created by martensigwart on 17.05.17.
 */
public interface ICpuSimulator extends Runnable {
    void simulateCpu();
}
