package com.martensigwart.fakeload;

/**
 * Created by martensigwart on 19.07.17.
 */
public enum MemoryUnit {
    BYTES, KB, MB, GB;

    public long toBytes() {
        switch (this) {
            case BYTES:
                return 1;
            case KB:
                return 1024;
            case MB:
                return 1024*1024;
            case GB:
                return 1024*1024*1024;
            default:
                return 0;
        }
    }
}
