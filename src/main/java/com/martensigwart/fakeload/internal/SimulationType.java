package com.martensigwart.fakeload.internal;

/**
 * Created by martensigwart on 03.08.17.
 */
enum SimulationType {
    CPU("CPU", "%"),
    MEMORY("Memory", "bytes"),
    DISK_IO("Disk IO", "bla"),
    NET_IO("Net IO", "bla");


    private String value;
    private String unit;

    SimulationType(String value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
