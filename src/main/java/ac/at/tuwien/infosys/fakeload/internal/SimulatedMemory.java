package ac.at.tuwien.infosys.fakeload.internal;

import common.enums.WorkloadUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by martensigwart on 20.06.17.
 */
public class SimulatedMemory {

    private static final Logger log = LoggerFactory.getLogger(SimulatedMemory.class);

    private List<byte[]> usedMemory;


    public SimulatedMemory() {
        this.usedMemory = new LinkedList<>();
    }


    void allocateMemory(long noOfBytes) {

        try {
            if (noOfBytes > Integer.MAX_VALUE) {
                //TODO Implement better way of allocating memory, to avoid out of memory errors
                int modulo = Math.toIntExact(noOfBytes % Integer.MAX_VALUE);
                int times = Math.toIntExact((noOfBytes - modulo) / Integer.MAX_VALUE);
                log.debug("modulo: {}, times: {}", modulo, times);
                for (int i = 0; i < times; i++) {
                    log.debug("Round {} start", i);
                    usedMemory.add(new byte[Integer.MAX_VALUE]);
                    log.debug("Round {} end", i);
                }
                log.debug("now adding {} bytes", modulo);
                usedMemory.add(new byte[modulo]);

            } else {
                usedMemory.add(new byte[(int) noOfBytes]);
            }

        } catch (Exception e) {
            log.error("OH NO WHAT HAPPENED HERE");
            e.printStackTrace();
        }
    }

//    public void dropMemory(Integer noOfMB) {
//        for (int i=0; i<noOfMB; i++) {
//            this.usedMemory.remove(this.usedMemory.size()-1);
//        }
//    }

    void removeAll() {
        usedMemory.clear();
    }

    public void allocateMemory(Integer value, WorkloadUnit unit) {

    }
}
