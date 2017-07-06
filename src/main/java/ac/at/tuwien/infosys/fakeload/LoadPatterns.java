package ac.at.tuwien.infosys.fakeload;

/**
 * Created by martensigwart on 29.06.17.
 */
public class LoadPatterns {
    public static LoadPattern createLoadPattern() {
        return new ComplexLoadPattern();
    }
}
