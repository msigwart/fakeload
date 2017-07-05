package ac.at.tuwien.infosys.fakeload;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test class to test the functionality of the {@Link LoadPattern} class.
 *
 * @Author Marten Sigwart
 */
public class LoadPatternTest {

    private static final Logger log = LoggerFactory.getLogger(LoadPatternTest.class);

    @Test(expected = IllegalArgumentException.class)
    public void testLevel1Recursion() {
        LoadPattern pattern = new LoadPattern();
        pattern.addLoad(pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel2Recursion() {
        LoadPattern patternParent = new LoadPattern();
        LoadPattern patternChild = new LoadPattern();
        patternChild.addLoad(patternParent);
        patternParent.addLoad(patternChild);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel3Recursion() {
        LoadPattern grandParent = new LoadPattern();
        LoadPattern parent = new LoadPattern();
        LoadPattern child = new LoadPattern();
        child.addLoad(grandParent);
        parent.addLoad(child);
        grandParent.addLoad(parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel4Recursion() {
        LoadPattern greatGrandParent = new LoadPattern();
        LoadPattern grandParent = new LoadPattern();
        LoadPattern parent = new LoadPattern();
        LoadPattern child = new LoadPattern();
        child.addLoad(greatGrandParent);
        parent.addLoad(child);
        grandParent.addLoad(parent);
        greatGrandParent.addLoad(grandParent);
    }



}
