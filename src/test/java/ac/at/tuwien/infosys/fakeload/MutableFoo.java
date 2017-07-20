package ac.at.tuwien.infosys.fakeload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martensigwart on 20.07.17.
 */
public class MutableFoo extends AbstractFoo {

    List<AbstractFoo> list = new ArrayList<>();

    public AbstractFoo add(AbstractFoo obj) {
        list.add(obj);
        return this;
    }

    @Override
    public String toString() {
        return "MutableFoo{" +
                "list=" + list +
                '}';
    }
}
