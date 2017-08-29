package com.martensigwart.fakeload.util;

import com.martensigwart.fakeload.MemoryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class DiskIOBenchmark {

    private static final Logger log = LoggerFactory.getLogger(DiskIOBenchmark.class);

    public static void main(String... args) {
        log.info("Welcome to DiskIO Benchmark - Test Class");

        int seconds = 10;
        if (args.length > 0) {
            int timeArg = Integer.valueOf(args[0]);
            seconds = (timeArg == 0 ? 10 : timeArg);
        }

        System.out.println(MemoryUnit.BYTES.toGB(Integer.MAX_VALUE));

//        String readPath = "/tmp/input.tmp";
//        read(readPath, seconds);


        long start = System.nanoTime();
        long bytes = MemoryUnit.GB.toBytes(1);
        long mod = bytes % (4 * MemoryUnit.MB.toBytes(1));
        long end = System.nanoTime();
        System.out.printf("mod: %d, time: %d\n", mod, end-start);

        // Disk Output test
//        long amount = 200;
//        MemoryUnit unit = MemoryUnit.MB;
//        int iterations = 20;
//
//        String writePath = "/tmp/output.tmp";
//        long[][] data = new long[iterations][2];
//
//        for (int i=0; i<iterations; i++) {
//            data[i][0] = (long) (unit.toBytes(amount)/Math.pow(2, i));
//            data[i][1] = write(writePath, amount, unit, 10, i);
//        }
//
//        try (BufferedWriter br = new BufferedWriter((new FileWriter("disk_output_benchmark_200mb.csv")))) {
//
//            for (int i=0; i<iterations; i++) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(data[i][0]);
//                sb.append(";");
//                sb.append(data[i][1]);
//                br.write(sb.toString());
//                br.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }





    private static long write(String path, long amount, MemoryUnit unit, int iterations, long power) {
        System.out.printf("Now averaging write time of %d %s divided by %d blocks\n", amount, unit, (int)Math.pow(2, power));
        try {
            long fileCreationStart = System.nanoTime();
            File f = new File(path);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            long fileCreationEnd = System.nanoTime();
            System.out.println("File creation time: " + (fileCreationEnd - fileCreationStart));

            long totalDuration = 0;

            for (int i=0; i<iterations; i++) {
                long duration = write(path, unit.toBytes(amount), power);
                totalDuration += duration;
//                long toSleep = TimeUnit.SECONDS.toMillis(1) - TimeUnit.NANOSECONDS.toMillis(duration);
//
//                if (toSleep > 0) {
//                    System.out.println("Sleeping " + toSleep + "ms");
//                    Thread.sleep(toSleep);
//                }
            }

            return totalDuration / iterations;

        } catch (IOException /*| InterruptedException*/ e) {
            throw new RuntimeException(e);
        }

    }


    private static long write(String filePath, long numberOfBytes, long power) {
        int bytesToWrite = (numberOfBytes > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) numberOfBytes;

        try (RandomAccessFile file = new RandomAccessFile(filePath, "rws")) {
//        try (BufferedWriter file = new BufferedWriter(new FileWriter(filePath))) {

            // Seek position
            long seekStart = System.nanoTime();
            file.seek(0);
            long seekEnd = System.nanoTime();
            System.out.println("Time for seeking 0 position: " + (seekEnd-seekStart));

            // Create random bytes to write
            double fraction = Math.pow(2, power);
            bytesToWrite = (int) (bytesToWrite / fraction);
            byte bytes[] = new byte[bytesToWrite];
            long startRandom = System.nanoTime();
            new Random().nextBytes(bytes);
            long endRandom = System.nanoTime();
            long randomDuration = endRandom - startRandom;

            // Write bytes
            long startWrite = System.nanoTime();
            for (int i=0; i<fraction; i++) {
                file.write(bytes);
            }
            long endWrite = System.nanoTime();
            long writeDuration = endWrite - startWrite;

            System.out.printf("Number of bytes: %d, Write duration: %d ms, Random duration: %d ms, Total duration %d ms\n",
                    bytesToWrite*(int)fraction, TimeUnit.NANOSECONDS.toMillis(writeDuration), TimeUnit.NANOSECONDS.toMillis(randomDuration),
                    TimeUnit.NANOSECONDS.toMillis(writeDuration + randomDuration));


            return writeDuration + randomDuration;

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void read(String path, int seconds) {
        long startTime = System.currentTimeMillis();

        try(RandomAccessFile file = new RandomAccessFile(path, "rws")) {

            while (System.currentTimeMillis() - startTime < seconds * 1000) {

                byte bytes[] = new byte[(int) MemoryUnit.MB.toBytes(100)];
                long startRead = System.nanoTime();
                file.read(bytes);
                long endRead = System.nanoTime();

                long duration = endRead - startRead;
                System.out.printf("Number of bytes: %d, Read duration: %d\n",
                        bytes.length, TimeUnit.NANOSECONDS.toMillis(duration));


                long toSleep = TimeUnit.SECONDS.toMillis(1) - TimeUnit.NANOSECONDS.toMillis(duration);

                if (toSleep > 0) {
                    System.out.println("Sleeping " + toSleep + "ms");
                    Thread.sleep(toSleep);
                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
