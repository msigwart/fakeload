package ac.at.tuwien.infosys.fakeload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by martensigwart on 20.07.17.
 */
public class ImmutableFoo extends AbstractFoo {

    private final List<AbstractFoo> list;

    public ImmutableFoo(List<AbstractFoo> list) {
        this.list = list;
    }

    public ImmutableFoo() {
        this(new ArrayList<>());
    }

    @Override
    AbstractFoo add(AbstractFoo foo) {
        List<AbstractFoo> newList;
        if (list.size() > 0) {
            newList = list.subList(0, list.size()-1);
        } else {
            newList = new ArrayList<>();
        }
        newList.add(foo);

        return new ImmutableFoo(newList);
    }

    @Override
    public String toString() {
        return "ImmutableFoo{" +
                "list=" + list +
                '}';
    }
}
