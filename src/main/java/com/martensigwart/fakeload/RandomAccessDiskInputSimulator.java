package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A {@code DiskInputSimulator} that simulates disk input using an underlying {@link RandomAccessFile}.
 */
public final class RandomAccessDiskInputSimulator extends DiskInputSimulator {

    private static final Logger log = LoggerFactory.getLogger(RandomAccessDiskInputSimulator.class);
    private RandomAccessFile file;

    /**
     * @param filePath path to the file for simulating disk input (should be bigger than amount of available RAM)
     * @throws FileNotFoundException if no file with the specified file path can be opened
     */
    public RandomAccessDiskInputSimulator(String filePath) throws FileNotFoundException {
        super();
        file = new RandomAccessFile(filePath, "r");
    }

    @Override
    protected void read(byte[] bytes) throws IOException {
        try {
            file.read(bytes);
        } catch (EOFException e) {
            log.debug("End of file reached: Resetting file pointer");
            file.seek(0);
        }
    }

    @Override
    protected void cleanUp() {
        try {
            file.close();
        } catch (IOException e) {
            log.error("Failed to close file: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
