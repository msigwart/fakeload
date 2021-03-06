package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A {@code DiskInputSimulator} that simulates disk input using an underlying {@link RandomAccessFile}.
 */
public final class RandomAccessDiskInputSimulator extends DiskInputSimulator {

    private static final Logger log = LoggerFactory.getLogger(RandomAccessDiskInputSimulator.class);
    private final String filePath;
    private RandomAccessFile file;

    /**
     * @param filePath path to the file for simulating disk input (should be bigger than amount of available RAM)
     */
    public RandomAccessDiskInputSimulator(String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    protected int read(byte[] bytes) throws IOException {
        try {
            if (file == null) {
                file = new RandomAccessFile(filePath, "r");
                log.trace("Opened file {}", filePath);
            }

            int bytesRead = file.read(bytes);
            if (bytesRead == bytes.length) {
                return bytesRead;
            }
            // Could not read fully: reset file pointer and read rest of bytes
            if (bytesRead < bytes.length) {
                log.debug("End of file reached: Resetting file pointer");
                file.seek(0);
                file.read(bytes, bytesRead, bytes.length - bytesRead);
            }
            return bytes.length;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format("File %s used for simulating disk input does not exist. " +
                      "(The file should be at least twice as big as available RAM to prevent caching)", filePath));
        }

    }

    @Override
    protected void cleanUp() {
        try {
            if (file != null) {
                file.close();
            }
        } catch (IOException e) {
            log.error("Failed to close file: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
