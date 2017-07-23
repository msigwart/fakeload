package ac.at.tuwien.infosys.fakeload.internal;

/**
 * Created by martensigwart on 23.07.17.
 */
public class Instruction {
    Type type;
    MyRunnable.LoadType loadType;
    int offSet; //in seconds
    long change;

    public Instruction(Type type, MyRunnable.LoadType loadType, int offSet, long change) {
        this.type = type;
        this.loadType = loadType;
        this.offSet = offSet;
        this.change = change;
    }


    enum Type {
        INCREASE, DECREASE
    }

}
