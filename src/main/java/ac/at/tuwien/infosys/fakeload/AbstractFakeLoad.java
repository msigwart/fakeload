package ac.at.tuwien.infosys.fakeload;


public abstract class AbstractFakeLoad implements FakeLoad {

    @Override
    public boolean contains(FakeLoad load) {
        if (this == load) {
            return true;
        }

        // check if pattern is contained in child patterns
        for (FakeLoad l : this.getLoads()) {
            return l.contains(load);
        }

        // if not found anywhere return false
        return false;
    }

}
