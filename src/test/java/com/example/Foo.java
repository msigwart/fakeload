package com.example;

import com.martensigwart.fakeload.DiskInputSimulator;

import java.io.IOException;

public class Foo extends DiskInputSimulator {

    public Foo() {

    }

    @Override
    protected int read(byte[] bytes) throws IOException {

        return 0;
    }

    @Override
    protected void cleanUp() {

    }
}
