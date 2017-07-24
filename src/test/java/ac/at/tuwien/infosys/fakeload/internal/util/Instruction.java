package ac.at.tuwien.infosys.fakeload.internal.util;

/**
 * Created by martensigwart on 23.07.17.
 */
abstract class Instruction {
    private ConnectionRunnable.LoadType loadType;
    private int offSet; //in seconds
    private long change;

    Instruction(ConnectionRunnable.LoadType loadType, int offSet, long change) {
        if (change <= 0) {
            throw new IllegalArgumentException("change must be a positive value");
        }
        this.loadType = loadType;
        this.offSet = offSet;
        this.change = change;
    }

    long getChange() {
        return this.change;
    }

    int getOffSet() {
        return offSet;
    }

    ConnectionRunnable.LoadType getLoadType() {
        return loadType;
    }



}

final class Increase extends Instruction {

    Increase(ConnectionRunnable.LoadType loadType, int offSet, long change) {
        super(loadType, offSet, change);
    }

}

final class Decrease extends Instruction {

    Decrease(ConnectionRunnable.LoadType loadType, int offSet, long change) {
        super(loadType, offSet, change);
    }

}
