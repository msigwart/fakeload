package com.martensigwart.fakeload;


import static com.google.common.base.Preconditions.checkArgument;

abstract class AbstractFakeLoad implements FakeLoad {

    private final int repetitions;


    AbstractFakeLoad(int repetitions) {
        checkArgument(repetitions >= 1, "Number of repetitions must be at least 1 but was %s", repetitions);

        this.repetitions = repetitions;
    }

    @Override
    public int getRepetitions() {
        return repetitions;
    }
}
