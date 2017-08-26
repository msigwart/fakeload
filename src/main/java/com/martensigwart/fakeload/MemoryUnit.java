package com.martensigwart.fakeload;

/**
 * Enum representing different memory units like bytes, kilobytes, megabytes, gigabytes.
 */
public enum MemoryUnit {
    BYTES(1),
    KB(BYTES.multiplier *1024),
    MB(KB.multiplier *1024),
    GB(MB.multiplier *1024);

    private final long multiplier;

    MemoryUnit(long multiplier) {
        this.multiplier = multiplier;
    }


    public long toBytes(long amount) {
        return amount* multiplier;
    }

    public double toKB(long amount) {
        return convert(amount, KB);
    }

    public double toMB(long amount) {
        return convert(amount, MB);
    }

    public double toGB(long amount) {
        return convert(amount, GB);
    }

    private double convert(long amount, MemoryUnit toUnit) {
        return (double)amount * ((double)this.multiplier/toUnit.multiplier);
    }

    public static String mbString(long bytes) {
        return String.format("%d bytes (%.2f MB)", bytes, (double)bytes / (MB.multiplier));
    }

}
