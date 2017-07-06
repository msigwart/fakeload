package ac.at.tuwien.infosys.fakeload;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class to test the functionality of the {@Link LoadPattern} interface.
 *
 * @Author Marten Sigwart
 */
public class LoadPatternTest {

    private static final Logger log = LoggerFactory.getLogger(LoadPatternTest.class);

    @Test(expected = IllegalArgumentException.class)
    public void testLevel1Recursion() {
        LoadPattern pattern = LoadPatterns.createLoadPattern();
        pattern.addLoad(pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel2Recursion() {
        LoadPattern patternParent = LoadPatterns.createLoadPattern();
        LoadPattern patternChild = LoadPatterns.createLoadPattern();
        patternChild.addLoad(patternParent);
        patternParent.addLoad(patternChild);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel3Recursion() {
        LoadPattern grandParent = LoadPatterns.createLoadPattern();
        LoadPattern parent = LoadPatterns.createLoadPattern();
        LoadPattern child = LoadPatterns.createLoadPattern();
        child.addLoad(grandParent);
        parent.addLoad(child);
        grandParent.addLoad(parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLevel4Recursion() {
        LoadPattern greatGrandParent = LoadPatterns.createLoadPattern();
        LoadPattern grandParent = LoadPatterns.createLoadPattern();
        LoadPattern parent = LoadPatterns.createLoadPattern();
        LoadPattern child = LoadPatterns.createLoadPattern();
        child.addLoad(greatGrandParent);
        parent.addLoad(child);
        grandParent.addLoad(parent);
        greatGrandParent.addLoad(grandParent);
    }



}
