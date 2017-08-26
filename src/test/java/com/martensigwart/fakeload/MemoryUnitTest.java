package com.martensigwart.fakeload;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class to test enum {@link MemoryUnit}
 */
public class MemoryUnitTest {

    @Test
    public void toBytes() throws Exception {
        long bytes;

        // BYTES to bytes
        bytes = MemoryUnit.BYTES.toBytes(1);
        assertEquals(1, bytes);
        bytes = MemoryUnit.BYTES.toBytes(1000);
        assertEquals(1000, bytes);
        bytes = MemoryUnit.BYTES.toBytes(1000000);
        assertEquals(1000000, bytes);
        bytes = MemoryUnit.BYTES.toBytes(1000000000);
        assertEquals(1000000000, bytes);
        bytes = MemoryUnit.BYTES.toBytes(1000000000000L);
        assertEquals(1000000000000L, bytes);
        bytes = MemoryUnit.BYTES.toBytes(1000000000000000L);
        assertEquals(1000000000000000L, bytes);

        // KB to bytes
        bytes = MemoryUnit.KB.toBytes(1);
        assertEquals(1024, bytes);
        bytes = MemoryUnit.KB.toBytes(100);
        assertEquals(102400, bytes);
        bytes = MemoryUnit.KB.toBytes(1000);
        assertEquals(1024000, bytes);
        bytes = MemoryUnit.KB.toBytes(1000000);
        assertEquals(1024000000, bytes);

        // MB to bytes
        bytes = MemoryUnit.MB.toBytes(1);
        assertEquals(1048576, bytes);
        bytes = MemoryUnit.MB.toBytes(10);
        assertEquals(10485760, bytes);
        bytes = MemoryUnit.MB.toBytes(100);
        assertEquals(104857600, bytes);
        bytes = MemoryUnit.MB.toBytes(1000);
        assertEquals(1048576000, bytes);

        // GB to bytes
        bytes = MemoryUnit.GB.toBytes(1);
        assertEquals(1073741824, bytes);
        bytes = MemoryUnit.GB.toBytes(10);
        assertEquals(10737418240L, bytes);
        bytes = MemoryUnit.GB.toBytes(100);
        assertEquals(107374182400L, bytes);

    }

    @Test
    public void toKB() throws Exception {
        double kilobytes;

        // BYTES to kilobytes
        kilobytes = MemoryUnit.BYTES.toKB(1536);
        assertEquals(1.5, kilobytes, 0.0001);
        kilobytes = MemoryUnit.BYTES.toKB(1000);
        assertEquals(0.9765625, kilobytes, 0.0001);
        kilobytes = MemoryUnit.BYTES.toKB(1000000);
        assertEquals(976.5625, kilobytes, 0.0001);

        // KB to kilobytes
        kilobytes = MemoryUnit.KB.toKB(1);
        assertEquals(1, kilobytes, 0);
        kilobytes = MemoryUnit.KB.toKB(1000);
        assertEquals(1000, kilobytes, 0);
        kilobytes = MemoryUnit.KB.toKB(1000000);
        assertEquals(1000000, kilobytes, 0);
        kilobytes = MemoryUnit.KB.toKB(1000000000);
        assertEquals(1000000000, kilobytes, 0);
        kilobytes = MemoryUnit.KB.toKB(1000000000000L);
        assertEquals(1000000000000L, kilobytes, 0);
        kilobytes = MemoryUnit.KB.toKB(1000000000000000L);
        assertEquals(1000000000000000L, kilobytes, 0);

        // MB to kilobytes
        kilobytes = MemoryUnit.MB.toKB(1);
        assertEquals(1024, kilobytes, 0);
        kilobytes = MemoryUnit.MB.toKB(100);
        assertEquals(102400, kilobytes, 0);
        kilobytes = MemoryUnit.MB.toKB(1000);
        assertEquals(1024000, kilobytes, 0);
        kilobytes = MemoryUnit.MB.toKB(1000000);
        assertEquals(1024000000, kilobytes, 0);

        // GB to bytes
        kilobytes = MemoryUnit.GB.toKB(1);
        assertEquals(1048576, kilobytes, 0);
        kilobytes = MemoryUnit.GB.toKB(10);
        assertEquals(10485760, kilobytes, 0);
        kilobytes = MemoryUnit.GB.toKB(100);
        assertEquals(104857600, kilobytes, 0);
        kilobytes = MemoryUnit.GB.toKB(1000);
        assertEquals(1048576000, kilobytes, 0);
    }

    @Test
    public void toMB() throws Exception {
        double megabytes;

        // BYTES to megabytes
        megabytes = MemoryUnit.BYTES.toMB(100000);
        assertEquals(0.09536743164, megabytes, 0.0001);
        megabytes = MemoryUnit.BYTES.toMB(123456789);
        assertEquals(117.7375689, megabytes, 0.0001);

        // KB to megabytes
        megabytes = MemoryUnit.KB.toMB(1536);
        assertEquals(1.5, megabytes, 0.0001);
        megabytes = MemoryUnit.KB.toMB(1000);
        assertEquals(0.9765625, megabytes, 0.0001);
        megabytes = MemoryUnit.KB.toMB(1000000);
        assertEquals(976.5625, megabytes, 0.0001);

        // MB to megabytes
        megabytes = MemoryUnit.MB.toMB(1);
        assertEquals(1, megabytes, 0);
        megabytes = MemoryUnit.MB.toMB(1000);
        assertEquals(1000, megabytes, 0);
        megabytes = MemoryUnit.MB.toMB(1000000);
        assertEquals(1000000, megabytes, 0);
        megabytes = MemoryUnit.MB.toMB(1000000000);
        assertEquals(1000000000, megabytes, 0);
        megabytes = MemoryUnit.MB.toMB(1000000000000L);
        assertEquals(1000000000000L, megabytes, 0);
        megabytes = MemoryUnit.MB.toMB(1000000000000000L);
        assertEquals(1000000000000000L, megabytes, 0);

        // GB to megabytes
        megabytes = MemoryUnit.GB.toMB(1);
        assertEquals(1024, megabytes, 0);
        megabytes = MemoryUnit.GB.toMB(100);
        assertEquals(102400, megabytes, 0);
        megabytes = MemoryUnit.GB.toMB(1000);
        assertEquals(1024000, megabytes, 0);
        megabytes = MemoryUnit.GB.toMB(1000000);
        assertEquals(1024000000, megabytes, 0);

    }

    @Test
    public void toGB() throws Exception {
        double gigabytes;

        // BYTES to gigabytes
        gigabytes = MemoryUnit.BYTES.toGB(1000000000);
        assertEquals(0.9313225746, gigabytes, 0.0001);
        gigabytes = MemoryUnit.BYTES.toGB(123456789123L);
        assertEquals(114.9780947, gigabytes, 0.0001);

        // KB to gigabytes
        gigabytes = MemoryUnit.KB.toGB(100000);
        assertEquals(0.09536743164, gigabytes, 0.0001);
        gigabytes = MemoryUnit.KB.toGB(123456789);
        assertEquals(117.7375689, gigabytes, 0.0001);

        // MB to gigabytes
        gigabytes = MemoryUnit.MB.toGB(1536);
        assertEquals(1.5, gigabytes, 0.0001);
        gigabytes = MemoryUnit.MB.toGB(1000);
        assertEquals(0.9765625, gigabytes, 0.0001);
        gigabytes = MemoryUnit.MB.toGB(1000000);
        assertEquals(976.5625, gigabytes, 0.0001);

        // GB to gigabytes
        gigabytes = MemoryUnit.GB.toGB(1);
        assertEquals(1, gigabytes, 0);
        gigabytes = MemoryUnit.GB.toGB(1000);
        assertEquals(1000, gigabytes, 0);
        gigabytes = MemoryUnit.GB.toGB(1000000);
        assertEquals(1000000, gigabytes, 0);
        gigabytes = MemoryUnit.GB.toGB(1000000000);
        assertEquals(1000000000, gigabytes, 0);
        gigabytes = MemoryUnit.GB.toGB(1000000000000L);
        assertEquals(1000000000000L, gigabytes, 0);
        gigabytes = MemoryUnit.GB.toGB(1000000000000000L);
        assertEquals(1000000000000000L, gigabytes, 0);
    }

}