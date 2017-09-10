package com.martensigwart.fakeload;

/**
 * Enum representing different memory units like bytes, kilobytes, megabytes, gigabytes.
 *
 * The orders of magnitude between bytes, kilobytes, megabytes, and gigabytes are hereby represented as binary.
 * I.e. one kilobyte are 1024, one megabyte are 10242 and one gigabyte are 10243 bytes.
 * These representations are sometimes also known as kibibytes (kiB), mebibytes(MiB) and gibibytes(GiB).
 *
 * @author Marten Sigwart
 * @since 1.8
 */
public enum MemoryUnit {

    /**
     * Represents bytes
     */
    BYTES(1),

    /**
     * Represents kilobytes, 1 kilobyte = 1024 bytes ({@link #BYTES}).
     */
    KB(BYTES.multiplier *1024),

    /**
     * Represents megabytes, 1 megabyte = 1024 kilobytes ({@link #KB}).
     */
    MB(KB.multiplier *1024),

    /**
     * Represents gigabytes, 1 gigabyte = 1024 megabytes ({@link #MB}).
     */
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
