package ac.at.tuwien.infosys.fakeload;

import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 06.07.17.
 */
public class IntervalLoadPattern extends AbstractLoadPattern {


    IntervalLoadPattern() {

    }

    @Override
    public void addLoad(LoadPattern pattern) {

    }

    @Override
    public void addLoad(long duration, String... loads) {

    }

    @Override
    public void addLoad(long duration, TimeUnit unit, String... loads) {

    }

    @Override
    public void addIntervalLoad(long duration, TimeUnit unitDuration, long interval, TimeUnit unitInterval, String... loads) {

    }

    @Override
    public void removeLoad(LoadPattern pattern) {

    }

    @Override
    public boolean contains(LoadPattern pattern) {
        return false;
    }

}
