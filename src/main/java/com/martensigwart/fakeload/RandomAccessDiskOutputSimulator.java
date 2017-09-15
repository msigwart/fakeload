package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A {@code DiskOutputSimulator} that simulates disk output using an underlying {@link RandomAccessFile}.
 */
public final class RandomAccessDiskOutputSimulator extends DiskOutputSimulator {

    private static final Logger log = LoggerFactory.getLogger(RandomAccessDiskOutputSimulator.class);
    private final String filePath;
    private RandomAccessFile file;

    public RandomAccessDiskOutputSimulator(String filePath) throws IOException {
        super();
        this.filePath = filePath;


    }

    @Override
    protected void write(byte[] bytes) throws IOException {

        // Create file if does not exist yet
        if (file == null) {
            File f = new File(this.filePath);
            if (!f.exists()) {
                //noinspection ResultOfMethodCallIgnored
                f.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                f.createNewFile();
                log.trace("Created file {}", this.filePath);
            }
            file = new RandomAccessFile(this.filePath, "rws");
        }

        file.seek(0);
        file.write(bytes);
    }

    @Override
    protected void cleanUp() {
        if (file != null) {
            try {
                file.close();
                File f = new File(filePath);
                if (f.delete()) {
                    log.trace("File {} successfully deleted", filePath);
                } else {
                    log.warn("File {} could not be deleted", filePath);
                }
            } catch (IOException e) {
                log.error("Failed to close file: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
