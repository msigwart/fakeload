package ac.at.tuwien.infosys.fakeload;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martensigwart on 20.07.17.
 */
public class MutableFakeLoadTest {

    private static final Logger log = LoggerFactory.getLogger(MutableFakeLoadTest.class);

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleRecursion() {
        FakeLoad load = new MutableFakeLoad();
        load.addLoad(load);         //throws exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel2Recursion() {
        FakeLoad patternParent = new MutableFakeLoad();
        FakeLoad patternChild = new MutableFakeLoad();
        patternChild.addLoad(patternParent);
        patternParent.addLoad(patternChild);       //throws exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel3Recursion() {
        FakeLoad grandParent = new MutableFakeLoad();
        FakeLoad parent = new MutableFakeLoad();
        FakeLoad child = new MutableFakeLoad();
        child.addLoad(grandParent);
        parent.addLoad(child);
        grandParent.addLoad(parent);    //throws exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel4Recursion() {
        FakeLoad greatGrandParent = new MutableFakeLoad();
        FakeLoad grandParent = new MutableFakeLoad();
        FakeLoad parent = new MutableFakeLoad();
        FakeLoad child = new MutableFakeLoad();
        child.addLoad(greatGrandParent);
        parent.addLoad(child);
        grandParent.addLoad(parent);
        greatGrandParent.addLoad(grandParent); //throws exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionRecursion() {
        FakeLoad parent = new MutableFakeLoad();
        FakeLoad child1 = new MutableFakeLoad();
        FakeLoad child2 = new MutableFakeLoad();
        FakeLoad child3 = new MutableFakeLoad();
        child1.addLoad(parent);

        List<FakeLoad> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        children.add(child3);

        parent.addLoads(children);  //throws exception
    }

}
