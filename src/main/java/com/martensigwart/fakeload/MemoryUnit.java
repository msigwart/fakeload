package com.martensigwart.fakeload;

/**
 * Created by martensigwart on 19.07.17.
 */
public enum MemoryUnit {
    BYTES, KB, MB, GB;

    public long toBytes(long amount) {
        switch (this) {
            case BYTES:
                return amount*1;
            case KB:
                return amount*1024;
            case MB:
                return amount*1024*1024;
            case GB:
                return amount*1024*1024*1024;
            default:
                return 0;
        }
    }
}
