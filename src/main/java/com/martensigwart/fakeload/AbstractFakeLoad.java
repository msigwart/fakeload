package com.martensigwart.fakeload;


import static com.google.common.base.Preconditions.checkArgument;

public abstract class AbstractFakeLoad implements FakeLoad {

    private final int repetitions;


    protected AbstractFakeLoad(int repetitions) {
        checkArgument(repetitions >= 0, "Repetitions must be nonnegative but was %s", repetitions);

        this.repetitions = repetitions;
    }

    @Override
    public int getRepetitions() {
        return repetitions;
    }
}
