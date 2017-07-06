package ac.at.tuwien.infosys.fakeload;

/**
 * Created by martensigwart on 06.07.17.
 */
public abstract class AbstractLoadPattern implements LoadPattern {

    /**
     * Represents the number of times a load pattern is repeated during actual load simulation.
     * Default value 0.
     */
    private int repetitions;


    AbstractLoadPattern() {
        this.repetitions = 0;
    }

    @Override
    public void setRepetitions(int noOfRepetitions) {
        this.repetitions = noOfRepetitions;
    }

    @Override
    public int getRepetitions() {
        return this.repetitions;
    }

}
