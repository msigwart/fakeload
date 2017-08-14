package com.martensigwart.fakeload;


import static com.google.common.base.Preconditions.checkArgument;

public abstract class AbstractFakeLoad implements FakeLoad {

    private final int repetitions;


    AbstractFakeLoad(int repetitions) {
        checkArgument(repetitions >= 0, "Repetitions must be non-negative but was %s", repetitions);

        this.repetitions = repetitions;
    }

    @Override
    public int getRepetitions() {
        return repetitions;
    }
}
