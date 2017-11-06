package com.martensigwart.fakeload;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

final class IteratorOfIterators<T> implements Iterator<T> {
    private final List<Iterator<T>> iterators;

    private int activeIterator = 0;

    IteratorOfIterators(List<Iterator<T>> iterators) {
        this.iterators = iterators;
    }

    @SafeVarargs
    IteratorOfIterators(Iterator<T>... iterators) {
        this.iterators = Arrays.asList(iterators);
    }

    @Override
    public boolean hasNext() {
        if (activeIterator >= iterators.size()) {
            return false;
        }
        if (iterators.get(activeIterator).hasNext()) {
            return true;
        } else {
            activeIterator++;
            return hasNext();
        }
    }

    @Override
    public T next() {
        if (hasNext()) {
            return iterators.get(activeIterator).next();
        }
        throw new NoSuchElementException();
    }

}
