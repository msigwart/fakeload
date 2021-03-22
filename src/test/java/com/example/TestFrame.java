package com.example;

import com.martensigwart.fakeload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


public class TestFrame {

    private static final Logger log = LoggerFactory.getLogger(TestFrame.class);

    public static void main(String... args) {
        log.info("Starting FakeLoad execution...");

        FakeLoadExecutor executor = FakeLoadExecutors.newDefaultExecutor();

        // Create simple FakeLoad
        FakeLoad fakeLoad = FakeLoads.create().lasting(45, TimeUnit.SECONDS)
                .withCpu(75);
//                .withMemory(200, MemoryUnit.MB);
//                .withDiskInput(100, MemoryUnit.MB)
//                .withDiskOutput(100, MemoryUnit.MB);

        // Create FakeLoad with Builder pattern
//        FakeLoad fakeload = new FakeLoadBuilder(30, TimeUnit.SECONDS)
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(10).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(20).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(30).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(40).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(50).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(60).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(70).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(80).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(90).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(100).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(90).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(80).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(70).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(60).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(50).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(40).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(30).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(20).build())
//                .addLoad(new FakeLoadBuilder(4, TimeUnit.SECONDS).withCpu(10).build())
//                .addLoad(new FakeLoadBuilder(20, TimeUnit.SECONDS).build())
//                .build();

        Future<Void> execution = executor.executeAsync(fakeLoad);

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("cancelling fake load execution...");
            execution.cancel(true);
        });

        new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(15));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            execution.get();
            log.info("FakeLoad execution done.");
        } catch (InterruptedException | ExecutionException | CancellationException e) {
            log.info("FakeLoad execution interrupted");
        }
    }



}
