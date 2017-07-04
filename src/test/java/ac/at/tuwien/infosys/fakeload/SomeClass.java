package ac.at.tuwien.infosys.fakeload;

/**
 * Created by martensigwart on 30.06.17.
 */
public class SomeClass {

    public void someMethod() {

        // Execute FakeLoad
        FakeLoad fakeLoad = new FakeLoad("100ms", "50%", "1024m");
        fakeLoad.execute();

        // some code
    }

    public void someOtherMethod() {

        // Create LoadPattern
        LoadPattern pattern = new LoadPattern();
        pattern.addLoad("60s", "30%", "2048k");
        pattern.addIntervalLoad("200ms", "10s", "50%", "1024m");

        // Execute FakeLoad
        FakeLoad fakeLoad = new FakeLoad(pattern);
        fakeLoad.execute();

        // some code
    }

}
