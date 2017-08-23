package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.TimeUnit;


public class TestFrame {

    private static final Logger log = LoggerFactory.getLogger(TestFrame.class);

    public static void main(String... args) {
        log.info("Welcome to FakeLoad - TestFrame");

//        int seconds = 30;
//        if (args.length > 0) {
//            int timeArg = Integer.valueOf(args[0]);
//            seconds = (timeArg == 0 ? 10 : timeArg);
//        }
//        long startTime = System.currentTimeMillis();
//
//        try(RandomAccessFile file = new RandomAccessFile("/tmp/test.tmp", "rws")) {
//
//            while (System.currentTimeMillis() - startTime < seconds * 1000) {
//
//                byte bytes[] = new byte[(int) MemoryUnit.MB.toBytes(100)];
//                long startRead = System.nanoTime();
//                file.read(bytes);
//                long endRead = System.nanoTime();
//
//                long duration = endRead - startRead;
//                System.out.printf("Number of bytes: %d, Read duration: %d\n",
//                        bytes.length, TimeUnit.NANOSECONDS.toMillis(duration));
//
//
//                long toSleep = TimeUnit.SECONDS.toMillis(1) - TimeUnit.NANOSECONDS.toMillis(duration);
//
//                if (toSleep > 0) {
//                    System.out.println("Sleeping " + toSleep + "ms");
//                    Thread.sleep(toSleep);
//                }
//
//            }
//        } catch (EOFException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        SomeClass someClass = new SomeClass();

        someClass.someMethod();

        someClass.someOtherMethod();

    }

}
