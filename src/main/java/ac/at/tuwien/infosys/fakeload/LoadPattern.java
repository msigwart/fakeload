package ac.at.tuwien.infosys.fakeload;


/**
 * Created by martensigwart on 29.06.17.
 */
public class LoadPattern {


    private int repetitions;


    public LoadPattern() {
        this.repetitions = 0;
        // TODO
    }

    public LoadPattern(String json) {
        // TODO
    }



//    public void addLoad(LoadPattern load) {
//        // TODO
//    }

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
}
