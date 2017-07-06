package ac.at.tuwien.infosys.fakeload;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by martensigwart on 29.06.17.
 */
public class LoadPattern {

    /**
     * Number of repetitions of the load pattern
     */
    private int repetitions;

    /**
     * Duration of the load pattern
     */
    private long duration;

    /**
     * CPU load in percent
     */
    private int cpuLoad;

    /**
     * Memory load in bytes
     */
    private long memoryLoad;


    /**
     * The list of {@Link LoadPattern} objects which are contained in this LoadPattern instance.
     */
    private List<LoadPattern> loadPatterns;


    public LoadPattern() {
        this.repetitions = 0;
        this.loadPatterns = new ArrayList<>();
        // TODO
    }

    public LoadPattern(String json) {
        // TODO
    }


    public void addLoad(LoadPattern pattern) {

        if (pattern.contains(this)) {
            throw new IllegalArgumentException("The calling LoadPattern is part of the pattern it is trying to add.");
        }

        loadPatterns.add(pattern);

    }

    public void addLoad(long duration, String... loads) {
        // TODO
    }

    public void addLoad(String duration, String... loads) {
        // TODO
    }

    public void addIntervalLoad(long duration, long interval, String... loads) {
        // TODO
    }

    public void addIntervalLoad(String duration, String interval, String... loads) {
        // TODO
    }


    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }


    public List<LoadPattern> getLoadPatterns() {
        return loadPatterns;
    }


    @Override
    public String toString() {
        return "LoadPattern{" +
                "repetitions=" + repetitions +
                ", duration=" + duration +
                ", cpuLoad=" + cpuLoad +
                ", memoryLoad=" + memoryLoad +
                ", loadPatterns=" + loadPatterns +
                '}';
    }



//--------------------------------------------------------------------------------------
//  Private Methods
//--------------------------------------------------------------------------------------

    /**
     * Checks whether this LoadPattern instance contains LoadPattern {@code pattern}.
     * @param pattern the pattern to check for
     * @return true if pattern exists, false if not
     */
    private boolean contains(LoadPattern pattern) {
        if (this.equals(pattern)) {
            return true;
        }

        // check if pattern is contained in child patterns
        for (LoadPattern l: this.getLoadPatterns()) {
            return l.contains(pattern);
        }

        // if not found anywhere return false
        return false;
    }




//    private void checkCircularReference(LoadPattern pattern) {
//        if (pattern == this) {
//            throw new IllegalArgumentException("A LoadPattern cannot add itself to its loads");
//        }
//        pattern.getLoadPatterns().forEach(this::checkCircularReference);
//    }


}
