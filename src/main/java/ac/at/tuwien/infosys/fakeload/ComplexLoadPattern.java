package ac.at.tuwien.infosys.fakeload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 06.07.17.
 */
public class ComplexLoadPattern extends AbstractLoadPattern {

    /**
     * The collection of {@link LoadPattern} objects contained in this load pattern.
     */
    private Collection<LoadPattern> loadPatterns;



    ComplexLoadPattern() {
        this.loadPatterns = new ArrayList<>();
    }


    @Override
    public void addLoad(LoadPattern pattern) {

        if (pattern.contains(this)) {
            throw new IllegalArgumentException("The calling LoadPattern is part of the pattern it is trying to add.");
        }

        loadPatterns.add(pattern);

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
        if (this.equals(pattern)) {
            return true;
        }

        // check if pattern is contained in child patterns
        for (LoadPattern l : this.getLoadPatterns()) {
            return l.contains(pattern);
        }

        // if not found anywhere return false
        return false;
    }



    public Collection<LoadPattern> getLoadPatterns() {
        return loadPatterns;
    }
}
